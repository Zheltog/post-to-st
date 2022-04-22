package su.nsk.iae.post.generator.st

import java.io.File
import java.util.LinkedHashMap
import java.util.LinkedList
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.eclipse.xtext.generator.JavaIoFileSystemAccess
import com.google.inject.Injector
import su.nsk.iae.post.IDsmExecutor
import su.nsk.iae.post.PoSTStandaloneSetup
import su.nsk.iae.post.deserialization.ModelDeserializer
import su.nsk.iae.post.generator.IPoSTGenerator
import su.nsk.iae.post.generator.st.common.ProgramGenerator
import su.nsk.iae.post.generator.st.common.vars.GlobalVarHelper
import su.nsk.iae.post.generator.st.common.vars.VarHelper
import su.nsk.iae.post.generator.st.configuration.ConfigurationGenerator
import su.nsk.iae.post.poST.ArrayVariable
import su.nsk.iae.post.poST.AssignmentStatement
import su.nsk.iae.post.poST.AttachVariableConfElement
import su.nsk.iae.post.poST.Constant
import su.nsk.iae.post.poST.ForStatement
import su.nsk.iae.post.poST.Model
import su.nsk.iae.post.poST.PrimaryExpression
import su.nsk.iae.post.poST.ProcessStatements
import su.nsk.iae.post.poST.ProcessStatusExpression
import su.nsk.iae.post.poST.SymbolicVariable
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement
import su.nsk.iae.post.poST.TemplateProcessConfElement
import su.nsk.iae.post.poST.TimeoutStatement
import su.nsk.iae.post.poST.Variable

import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import static extension org.eclipse.xtext.EcoreUtil2.*

class STGenerator implements IPoSTGenerator, IDsmExecutor {

	String DSM_DIRECTORY = "st"
	ConfigurationGenerator configuration = null
	VarHelper globVarList = new GlobalVarHelper
	List<ProgramGenerator> programs = new LinkedList
	
	override String execute(LinkedHashMap<String, Object> request) {
		var id = request.get("id") as String
		var root = request.get("root") as String
		var fileName = request.get("fileName") as String
		var ast = request.get("ast") as String
		
		System.out.println("id = " + id)
		System.out.println("root = " + root)
		System.out.println("fileName = " + fileName)
		System.out.println("ast = " + ast)

		var Injector injector = PoSTStandaloneSetup.getInjector()
		var JavaIoFileSystemAccess fsa = injector.getInstance(typeof(JavaIoFileSystemAccess))
		var Resource resource = ModelDeserializer.deserializeFromXMI(ast)
		var IGeneratorContext context = new NullGeneratorContext()
		var String generated = root + File.separator + DSM_DIRECTORY + File.separator + fileName
		fsa.setOutputPath(generated)

		beforeGenerate(resource, fsa, context)
		doGenerate(resource, fsa, context)
		afterGenerate(resource, fsa, context)
		
		return "Executed for request: " + request
	}

	override setModel(Model model) {
		globVarList.clear()
		programs.clear()
		model.globVars.stream.forEach([v | globVarList.add(v)])
		if (model.conf !== null) {
			configuration = new ConfigurationGenerator(model.conf)
			configuration.resources.stream.map([res | res.resStatement.programConfs]).flatMap([res | res.stream]).forEach([programConf | 
				val program = programConf.program.copy()
				program.name = programConf.name.capitalizeFirst
				programs.add(new ProgramPOUGenerator(program, true))
			])
		} else {
			model.programs.stream.forEach([p | programs.add(new ProgramPOUGenerator(p, false))])
			model.fbs.stream.forEach([fb | programs.add(new FunctionBlockPOUGenerator(fb, false))])
		}
	}

	override beforeGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		preparePrograms()
	}

	override doGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {
		generateSingleFile(fsa, "")
	}

	override afterGenerate(Resource input, IFileSystemAccess2 fsa, IGeneratorContext context) {}

	private def void generateSingleFile(IFileSystemAccess2 fsa, String path) {
		fsa.generateFile('''�path�poST_code.st''', generateSingleFileBody)
	}

//	private def void generateMultipleFiles(IFileSystemAccess2 fsa, String path) {
//		fsa.generateFile('''�path�GVL.st''', ProgramGenerator.generateVar(globVarList))
//		for (c : programs) {
//			c.generate(fsa, path, configuration === null)
//		}
//	}

	private def String generateSingleFileBody() '''
		�globVarList.generateVars�
		�IF configuration !== null�
			�configuration.generateConfiguration�
		�ENDIF�
		�FOR c : programs�
			�c.generateProgram�
			
		�ENDFOR�
	'''

	private def void preparePrograms() {
		if (configuration === null) {
			return
		}
		configuration.resources.stream.map([res | res.resStatement.programConfs]).flatMap([res | res.stream]).forEach([programConf |
			if (programConf.args !== null) {
				val programConfName = programConf.name.capitalizeFirst
				val programGen = programs.stream.filter([x | x.name == programConfName]).findFirst.get
				programConf.args.elements.stream.forEach([confElement |
					if (confElement instanceof TemplateProcessConfElement) {
						val process = confElement.process.copy
						process.name = confElement.name.capitalizeFirst
						confElement.args.elements.stream.forEach([e | e.changeAllVars(process)])
						programGen.addProcess(process, confElement.active)
					} else if (confElement instanceof AttachVariableConfElement) {
						confElement.changeAllVars(programGen.EObject)
					}
				])
			}
		])
	}

	def void changeAllVars(AttachVariableConfElement element, EObject root) {
		changeAllVars(element.programVar, element.attVar, element.const, root)
	}

	def void changeAllVars(TemplateProcessAttachVariableConfElement element, EObject root) {
		changeAllVars(element.programVar, element.attVar, element.const, root)
	}

	def void changeAllVars(Variable programVar, Variable attVar, Constant const, EObject root) {
		root.getAllContentsOfType(PrimaryExpression).stream.filter([v | (v.variable !== null) && (v.variable.name == programVar.name)]).forEach([v |
			if (attVar !== null) {
				v.variable = attVar as SymbolicVariable
			} else {
				v.variable = null
				v.const = const.copy
			}
		])
		root.getAllContentsOfType(AssignmentStatement).stream.filter([v | (v.variable !== null) && (v.variable.name == programVar.name)]).forEach([v |
			v.variable = attVar as SymbolicVariable
		])
		root.getAllContentsOfType(ForStatement).stream.filter([v | v.variable.name == programVar.name]).forEach([v |
			v.variable = attVar as SymbolicVariable
		])
		root.getAllContentsOfType(ArrayVariable).stream.filter([v | v.variable.name == programVar.name]).forEach([v |
			v.variable = attVar as SymbolicVariable
		])
		root.getAllContentsOfType(TimeoutStatement).stream.filter([v | (v.variable !== null) && (v.variable.name == programVar.name)]).forEach([v |
			v.variable = attVar as SymbolicVariable
		])
		root.getAllContentsOfType(ProcessStatements).stream.filter([v | (v.process !== null) && (v.process.name == programVar.name)]).forEach([v |
			v.process.name = attVar.name.capitalizeFirst
		])
		root.getAllContentsOfType(ProcessStatusExpression).stream.filter([v | (v.process !== null) && (v.process.name == programVar.name)]).forEach([v |
			v.process.name = attVar.name.capitalizeFirst
		])
	}

	private def String capitalizeFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1)
	}

}
