package su.nsk.iae.post.generator.st.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.ProcessGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.StateGenerator;
import su.nsk.iae.post.generator.st.common.StatementListGenerator;
import su.nsk.iae.post.poST.IfStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class IfStatementGenerator extends IStatementGenerator {
  public IfStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof IfStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final IfStatement s = ((IfStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("IF �context.generateExpression(s.mainCond)� THEN");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�context.generateStatementList(s.mainStatement)�");
    _builder.newLine();
    _builder.append("�IF !s.elseIfCond.empty�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�FOR i : 0..(s.elseIfCond.size - 1)�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ELSIF �context.generateExpression(s.elseIfCond.get(i))� THEN");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("�context.generateStatementList(s.elseIfStatements.get(i))�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("�IF s.elseStatement !== null�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ELSE");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�context.generateStatementList(s.elseStatement)�");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("END_IF");
    _builder.newLine();
    return _builder.toString();
  }
}
