package su.nsk.iae.post.generator.plcopen.xml.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.ProcessGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.ProgramGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StateGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.StatementListGenerator;
import su.nsk.iae.post.poST.ForStatement;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class ForStatementGenerator extends IStatementGenerator {
  public ForStatementGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof ForStatement);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final ForStatement s = ((ForStatement) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("FOR �context.generateVar(s.variable)� := �context.generateExpression(s.forList.start)� TO �context.generateExpression(s.forList.end)��IF s.forList.step !== null� BY �context.generateExpression(s.forList.step)��ENDIF� DO");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�context.generateStatementList(s.statement)�");
    _builder.newLine();
    _builder.append("END_FOR");
    _builder.newLine();
    return _builder.toString();
  }
}
