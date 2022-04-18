package su.nsk.iae.post.generator.st.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.ProcessGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.StateGenerator;
import su.nsk.iae.post.generator.st.common.StatementListGenerator;
import su.nsk.iae.post.poST.CaseStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class CaseStatementGenerator extends IStatementGenerator {
  public CaseStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof CaseStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final CaseStatement s = ((CaseStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("CASE �context.generateExpression(s.cond)� OF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�FOR e : s.caseElements�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�FOR c : e.caseList.caseListElement�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�c.generateSignedInteger��IF e.caseList.caseListElement.indexOf(c) < e.caseList.caseListElement.size - 1�, �ELSE�:�ENDIF�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�context.generateStatementList(e.statement)�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�IF s.elseStatement !== null�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ELSE");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�context.generateStatementList(s.elseStatement)�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("END_CASE");
    _builder.newLine();
    return _builder.toString();
  }
}
