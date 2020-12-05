package su.nsk.iae.post.generator.xml.vars

import org.eclipse.emf.ecore.EObject
import su.nsk.iae.post.poST.InputOutputVarDeclaration

class InputOutputVarHelper extends VarHelper {
	
	new() {
		varType = "VAR_IN_OUT"
	}
	
	override add(EObject varDecl) {
		if (varDecl instanceof InputOutputVarDeclaration) {
			parseSimpleVar(varDecl.vars)
		}
	}
	
}