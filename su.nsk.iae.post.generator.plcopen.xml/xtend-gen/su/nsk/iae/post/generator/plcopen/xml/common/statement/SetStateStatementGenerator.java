package su.nsk.iae.post.generator.plcopen.xml.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.ProgramGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StateGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StatementListGenerator;
import su.nsk.iae.post.poST.SetStateStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class SetStateStatementGenerator extends IStatementGenerator {
  public SetStateStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof SetStateStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final SetStateStatement s = ((SetStateStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF s.next��process.generateNextState(state)��ELSE��process.generateSetState(s.state.name)��ENDIF�");
    return _builder.toString();
  }
}
