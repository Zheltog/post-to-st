package su.nsk.iae.post.generator.st.common.statement

import su.nsk.iae.post.generator.st.common.ProcessGenerator
import su.nsk.iae.post.generator.st.common.ProgramGenerator
import su.nsk.iae.post.generator.st.common.StateGenerator
import su.nsk.iae.post.generator.st.common.StatementListGenerator
import su.nsk.iae.post.poST.CaseStatement
import su.nsk.iae.post.poST.Statement

import static extension su.nsk.iae.post.generator.st.common.util.GeneratorUtil.*

class CaseStatementGenerator extends IStatementGenerator {
	
	new(ProgramGenerator program, ProcessGenerator process, StateGenerator state, StatementListGenerator context) {
		super(program, process, state, context)
	}
	
	override checkStatement(Statement statement) {
		return statement instanceof CaseStatement
	}
	
	override generateStatement(Statement statement) {
		val s = statement as CaseStatement
		return '''
			CASE �context.generateExpression(s.cond)� OF
				�FOR e : s.caseElements�
					�FOR c : e.caseList.caseListElement�
						�c.generateSignedInteger��IF e.caseList.caseListElement.indexOf(c) < e.caseList.caseListElement.size - 1�, �ELSE�:�ENDIF�
					�ENDFOR�
						�context.generateStatementList(e.statement)�
				�ENDFOR�
				�IF s.elseStatement !== null�
					ELSE
						�context.generateStatementList(s.elseStatement)�
				�ENDIF�
			END_CASE
		'''
	}
	
}