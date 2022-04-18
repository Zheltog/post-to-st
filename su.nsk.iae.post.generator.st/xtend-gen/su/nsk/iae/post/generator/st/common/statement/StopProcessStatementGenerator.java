package su.nsk.iae.post.generator.st.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.ProcessGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.StateGenerator;
import su.nsk.iae.post.generator.st.common.StatementListGenerator;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StopProcessStatement;

@SuppressWarnings("all")
public class StopProcessStatementGenerator extends IStatementGenerator {
  public StopProcessStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof StopProcessStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final StopProcessStatement s = ((StopProcessStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF s.process !== null��program.generateProcessEnum(s.process.name)��ELSE��process.generateEnumName��ENDIF� := �generateStopConstant�;");
    return _builder.toString();
  }
}
