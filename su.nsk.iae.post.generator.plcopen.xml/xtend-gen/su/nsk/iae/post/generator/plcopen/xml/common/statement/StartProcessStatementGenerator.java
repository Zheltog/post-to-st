package su.nsk.iae.post.generator.plcopen.xml.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.ProgramGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StateGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StatementListGenerator;
import su.nsk.iae.post.poST.StartProcessStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class StartProcessStatementGenerator extends IStatementGenerator {
  public StartProcessStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof StartProcessStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final StartProcessStatement s = ((StartProcessStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�IF s.process !== null��program.generateProcessStart(s.process.name)��ELSE��process.generateStart��ENDIF�");
    return _builder.toString();
  }
}
