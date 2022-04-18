package su.nsk.iae.post.generator.st.common.statement;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.ProcessGenerator;
import su.nsk.iae.post.generator.st.common.ProgramGenerator;
import su.nsk.iae.post.generator.st.common.StateGenerator;
import su.nsk.iae.post.generator.st.common.StatementListGenerator;
import su.nsk.iae.post.poST.FBInvocation;
import su.nsk.iae.post.poST.Statement;

@SuppressWarnings("all")
public class FBInvocationGenerator extends IStatementGenerator {
  public FBInvocationGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state, final StatementListGenerator context) {
    super(program, process, state, context);
  }
  
  @Override
  public boolean checkStatement(final Statement statement) {
    return (statement instanceof FBInvocation);
  }
  
  @Override
  public String generateStatement(final Statement statement) {
    final FBInvocation s = ((FBInvocation) statement);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�s.fb.name�(�context.generateParamAssignmentElements(s.args)�)");
    return _builder.toString();
  }
}
