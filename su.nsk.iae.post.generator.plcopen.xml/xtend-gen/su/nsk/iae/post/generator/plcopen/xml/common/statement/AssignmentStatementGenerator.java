package su.nsk.iae.post.generator.plcopen.xml.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.ProgramGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StateGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StatementListGenerator;
import su.nsk.iae.post.poST.AssignmentStatement;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.SymbolicVariable;

@SuppressWarnings("all")
public class AssignmentStatementGenerator extends IStatementGenerator {
  public AssignmentStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof AssignmentStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final AssignmentStatement s = ((AssignmentStatement) statement);
    SymbolicVariable _variable = s.getVariable();
    boolean _tripleNotEquals = (_variable != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("�context.generateVar(s.variable)� := �context.generateExpression(s.value)�;");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("�context.generateArray(s.array)� := �context.generateExpression(s.value)�;");
    return _builder_1.toString();
  }
}
