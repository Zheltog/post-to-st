package su.nsk.iae.post.generator.st;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import su.nsk.iae.post.IDsmExecutor;
import su.nsk.iae.post.PoSTStandaloneSetup;
import su.nsk.iae.post.deserialization.ModelDeserializer;
import su.nsk.iae.post.generator.IPoSTGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.vars.GlobalVarHelper;
import su.nsk.iae.post.generator.st.common.vars.VarHelper;
import su.nsk.iae.post.generator.st.configuration.ConfigurationGenerator;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.AttachVariableConfElement;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.ForStatement;
import su.nsk.iae.post.poST.FunctionBlock;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.Model;
import su.nsk.iae.post.poST.PrimaryExpression;
import su.nsk.iae.post.poST.ProcessStatements;
import su.nsk.iae.post.poST.ProcessStatusExpression;
import su.nsk.iae.post.poST.Program;
import su.nsk.iae.post.poST.ProgramConfElement;
import su.nsk.iae.post.poST.ProgramConfElements;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.SymbolicVariable;
import su.nsk.iae.post.poST.TemplateProcessAttachVariableConfElement;
import su.nsk.iae.post.poST.TemplateProcessConfElement;
import su.nsk.iae.post.poST.TimeoutStatement;
import su.nsk.iae.post.poST.Variable;

@SuppressWarnings("all")
public class STGenerator implements IPoSTGenerator, IDsmExecutor {
  private String DSM_DIRECTORY = "st";
  
  private ConfigurationGenerator configuration = null;
  
  private VarHelper globVarList = new GlobalVarHelper();
  
  private List<ProgramGenerator> programs = new LinkedList<ProgramGenerator>();
  
  @Override
  public String execute(final LinkedHashMap<String, Object> request) {
    Object _get = request.get("id");
    String id = ((String) _get);
    Object _get_1 = request.get("root");
    String root = ((String) _get_1);
    Object _get_2 = request.get("fileName");
    String fileName = ((String) _get_2);
    Object _get_3 = request.get("ast");
    String ast = ((String) _get_3);
    System.out.println(("id = " + id));
    System.out.println(("root = " + root));
    System.out.println(("fileName = " + fileName));
    System.out.println(("ast = " + ast));
    Injector injector = PoSTStandaloneSetup.getInjector();
    JavaIoFileSystemAccess fsa = injector.<JavaIoFileSystemAccess>getInstance(JavaIoFileSystemAccess.class);
    Resource resource = ModelDeserializer.deserializeFromXMI(ast);
    IGeneratorContext context = new NullGeneratorContext();
    String generated = ((((root + File.separator) + this.DSM_DIRECTORY) + File.separator) + fileName);
    fsa.setOutputPath(generated);
    this.beforeGenerate(resource, fsa, context);
    this.doGenerate(resource, fsa, context);
    this.afterGenerate(resource, fsa, context);
    return ("Executed for request: " + request);
  }
  
  @Override
  public void setModel(final Model model) {
    this.globVarList.clear();
    this.programs.clear();
    final Consumer<GlobalVarDeclaration> _function = (GlobalVarDeclaration v) -> {
      this.globVarList.add(v);
    };
    model.getGlobVars().stream().forEach(_function);
    Configuration _conf = model.getConf();
    boolean _tripleNotEquals = (_conf != null);
    if (_tripleNotEquals) {
      Configuration _conf_1 = model.getConf();
      ConfigurationGenerator _configurationGenerator = new ConfigurationGenerator(_conf_1);
      this.configuration = _configurationGenerator;
      final Function<su.nsk.iae.post.poST.Resource, EList<ProgramConfiguration>> _function_1 = (su.nsk.iae.post.poST.Resource res) -> {
        return res.getResStatement().getProgramConfs();
      };
      final Function<EList<ProgramConfiguration>, Stream<ProgramConfiguration>> _function_2 = (EList<ProgramConfiguration> res) -> {
        return res.stream();
      };
      final Consumer<ProgramConfiguration> _function_3 = (ProgramConfiguration programConf) -> {
        final Program program = EcoreUtil.<Program>copy(programConf.getProgram());
        program.setName(this.capitalizeFirst(programConf.getName()));
        ProgramPOUGenerator _programPOUGenerator = new ProgramPOUGenerator(program, true);
        this.programs.add(_programPOUGenerator);
      };
      this.configuration.getResources().stream().<EList<ProgramConfiguration>>map(_function_1).<ProgramConfiguration>flatMap(_function_2).forEach(_function_3);
    } else {
      final Consumer<Program> _function_4 = (Program p) -> {
        ProgramPOUGenerator _programPOUGenerator = new ProgramPOUGenerator(p, false);
        this.programs.add(_programPOUGenerator);
      };
      model.getPrograms().stream().forEach(_function_4);
      final Consumer<FunctionBlock> _function_5 = (FunctionBlock fb) -> {
        FunctionBlockPOUGenerator _functionBlockPOUGenerator = new FunctionBlockPOUGenerator(fb, false);
        this.programs.add(_functionBlockPOUGenerator);
      };
      model.getFbs().stream().forEach(_function_5);
    }
  }
  
  @Override
  public void beforeGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    this.preparePrograms();
  }
  
  @Override
  public void doGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    this.generateSingleFile(fsa, "");
  }
  
  @Override
  public void afterGenerate(final Resource input, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
  }
  
  private void generateSingleFile(final IFileSystemAccess2 fsa, final String path) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�path�poST_code.st");
    fsa.generateFile(_builder.toString(), this.generateSingleFileBody());
  }
  
  private String generateSingleFileBody() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�globVarList.generateVars�");
    _builder.newLine();
    _builder.append("�IF configuration !== null�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�configuration.generateConfiguration�");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("�FOR c : programs�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�c.generateProgram�");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("�ENDFOR�");
    _builder.newLine();
    return _builder.toString();
  }
  
  private void preparePrograms() {
    if ((this.configuration == null)) {
      return;
    }
    final Function<su.nsk.iae.post.poST.Resource, EList<ProgramConfiguration>> _function = (su.nsk.iae.post.poST.Resource res) -> {
      return res.getResStatement().getProgramConfs();
    };
    final Function<EList<ProgramConfiguration>, Stream<ProgramConfiguration>> _function_1 = (EList<ProgramConfiguration> res) -> {
      return res.stream();
    };
    final Consumer<ProgramConfiguration> _function_2 = (ProgramConfiguration programConf) -> {
      ProgramConfElements _args = programConf.getArgs();
      boolean _tripleNotEquals = (_args != null);
      if (_tripleNotEquals) {
        final String programConfName = this.capitalizeFirst(programConf.getName());
        final Predicate<ProgramGenerator> _function_3 = (ProgramGenerator x) -> {
          String _name = x.getName();
          return Objects.equal(_name, programConfName);
        };
        final ProgramGenerator programGen = this.programs.stream().filter(_function_3).findFirst().get();
        final Consumer<ProgramConfElement> _function_4 = (ProgramConfElement confElement) -> {
          if ((confElement instanceof TemplateProcessConfElement)) {
            final su.nsk.iae.post.poST.Process process = EcoreUtil.<su.nsk.iae.post.poST.Process>copy(((TemplateProcessConfElement)confElement).getProcess());
            process.setName(this.capitalizeFirst(((TemplateProcessConfElement)confElement).getName()));
            final Consumer<TemplateProcessAttachVariableConfElement> _function_5 = (TemplateProcessAttachVariableConfElement e) -> {
              this.changeAllVars(e, process);
            };
            ((TemplateProcessConfElement)confElement).getArgs().getElements().stream().forEach(_function_5);
            programGen.addProcess(process, ((TemplateProcessConfElement)confElement).isActive());
          } else {
            if ((confElement instanceof AttachVariableConfElement)) {
              this.changeAllVars(((AttachVariableConfElement)confElement), programGen.getEObject());
            }
          }
        };
        programConf.getArgs().getElements().stream().forEach(_function_4);
      }
    };
    this.configuration.getResources().stream().<EList<ProgramConfiguration>>map(_function).<ProgramConfiguration>flatMap(_function_1).forEach(_function_2);
  }
  
  public void changeAllVars(final AttachVariableConfElement element, final EObject root) {
    this.changeAllVars(element.getProgramVar(), element.getAttVar(), element.getConst(), root);
  }
  
  public void changeAllVars(final TemplateProcessAttachVariableConfElement element, final EObject root) {
    this.changeAllVars(element.getProgramVar(), element.getAttVar(), element.getConst(), root);
  }
  
  public void changeAllVars(final Variable programVar, final Variable attVar, final Constant const_, final EObject root) {
    final Predicate<PrimaryExpression> _function = (PrimaryExpression v) -> {
      return ((v.getVariable() != null) && Objects.equal(v.getVariable().getName(), programVar.getName()));
    };
    final Consumer<PrimaryExpression> _function_1 = (PrimaryExpression v) -> {
      if ((attVar != null)) {
        v.setVariable(((SymbolicVariable) attVar));
      } else {
        v.setVariable(null);
        v.setConst(EcoreUtil.<Constant>copy(const_));
      }
    };
    EcoreUtil2.<PrimaryExpression>getAllContentsOfType(root, PrimaryExpression.class).stream().filter(_function).forEach(_function_1);
    final Predicate<AssignmentStatement> _function_2 = (AssignmentStatement v) -> {
      return ((v.getVariable() != null) && Objects.equal(v.getVariable().getName(), programVar.getName()));
    };
    final Consumer<AssignmentStatement> _function_3 = (AssignmentStatement v) -> {
      v.setVariable(((SymbolicVariable) attVar));
    };
    EcoreUtil2.<AssignmentStatement>getAllContentsOfType(root, AssignmentStatement.class).stream().filter(_function_2).forEach(_function_3);
    final Predicate<ForStatement> _function_4 = (ForStatement v) -> {
      String _name = v.getVariable().getName();
      String _name_1 = programVar.getName();
      return Objects.equal(_name, _name_1);
    };
    final Consumer<ForStatement> _function_5 = (ForStatement v) -> {
      v.setVariable(((SymbolicVariable) attVar));
    };
    EcoreUtil2.<ForStatement>getAllContentsOfType(root, ForStatement.class).stream().filter(_function_4).forEach(_function_5);
    final Predicate<ArrayVariable> _function_6 = (ArrayVariable v) -> {
      String _name = v.getVariable().getName();
      String _name_1 = programVar.getName();
      return Objects.equal(_name, _name_1);
    };
    final Consumer<ArrayVariable> _function_7 = (ArrayVariable v) -> {
      v.setVariable(((SymbolicVariable) attVar));
    };
    EcoreUtil2.<ArrayVariable>getAllContentsOfType(root, ArrayVariable.class).stream().filter(_function_6).forEach(_function_7);
    final Predicate<TimeoutStatement> _function_8 = (TimeoutStatement v) -> {
      return ((v.getVariable() != null) && Objects.equal(v.getVariable().getName(), programVar.getName()));
    };
    final Consumer<TimeoutStatement> _function_9 = (TimeoutStatement v) -> {
      v.setVariable(((SymbolicVariable) attVar));
    };
    EcoreUtil2.<TimeoutStatement>getAllContentsOfType(root, TimeoutStatement.class).stream().filter(_function_8).forEach(_function_9);
    final Predicate<ProcessStatements> _function_10 = (ProcessStatements v) -> {
      return ((v.getProcess() != null) && Objects.equal(v.getProcess().getName(), programVar.getName()));
    };
    final Consumer<ProcessStatements> _function_11 = (ProcessStatements v) -> {
      Variable _process = v.getProcess();
      _process.setName(this.capitalizeFirst(attVar.getName()));
    };
    EcoreUtil2.<ProcessStatements>getAllContentsOfType(root, ProcessStatements.class).stream().filter(_function_10).forEach(_function_11);
    final Predicate<ProcessStatusExpression> _function_12 = (ProcessStatusExpression v) -> {
      return ((v.getProcess() != null) && Objects.equal(v.getProcess().getName(), programVar.getName()));
    };
    final Consumer<ProcessStatusExpression> _function_13 = (ProcessStatusExpression v) -> {
      Variable _process = v.getProcess();
      _process.setName(this.capitalizeFirst(attVar.getName()));
    };
    EcoreUtil2.<ProcessStatusExpression>getAllContentsOfType(root, ProcessStatusExpression.class).stream().filter(_function_12).forEach(_function_13);
  }
  
  private String capitalizeFirst(final String str) {
    String _upperCase = str.substring(0, 1).toUpperCase();
    String _substring = str.substring(1);
    return (_upperCase + _substring);
  }
}
