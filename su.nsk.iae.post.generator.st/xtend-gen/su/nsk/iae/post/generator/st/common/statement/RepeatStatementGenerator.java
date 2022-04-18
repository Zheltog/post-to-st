package su.nsk.iae.post.generator.st.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.ProcessGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.StateGenerator;
import su.nsk.iae.post.generator.st.common.StatementListGenerator;
import su.nsk.iae.post.poST.RepeatStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class RepeatStatementGenerator extends IStatementGenerator {
  public RepeatStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof RepeatStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final RepeatStatement s = ((RepeatStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("REPEAT");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�context.generateStatementList(s.statement)�");
    _builder.newLine();
    _builder.append("UNTIL �context.generateExpression(s.cond)� END_REPEAT");
    _builder.newLine();
    return _builder.toString();
  }
}
