package su.nsk.iae.post.generator.plcopen.xml.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.ProgramGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StateGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StatementListGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.util.GeneratorUtil;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StopProcessStatement;
import su.nsk.iae.post.poST.Variable;

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
    {
      Variable _process = s.getProcess();
      boolean _tripleNotEquals = (_process != null);
      if (_tripleNotEquals) {
        String _generateProcessEnum = this.program.generateProcessEnum(s.getProcess().getName());
        _builder.append(_generateProcessEnum);
      } else {
        String _generateEnumName = GeneratorUtil.generateEnumName(this.process);
        _builder.append(_generateEnumName);
      }
    }
    _builder.append(" := ");
    String _generateStopConstant = GeneratorUtil.generateStopConstant();
    _builder.append(_generateStopConstant);
    _builder.append(";");
    return _builder.toString();
  }
}