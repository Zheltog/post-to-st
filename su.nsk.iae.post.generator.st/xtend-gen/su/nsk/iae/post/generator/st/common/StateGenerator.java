package su.nsk.iae.post.generator.st.common;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.poST.State;
import su.nsk.iae.post.poST.TimeoutStatement;

@SuppressWarnings("all")
public class StateGenerator {
  private ProcessGenerator process;
  
  private State state;
  
  private StatementListGenerator statementList;
  
  public StateGenerator(final ProgramGenerator program, final ProcessGenerator process, final State state) {
    this.process = process;
    this.state = state;
    StatementListGenerator _statementListGenerator = new StatementListGenerator(program, process, this);
    this.statementList = _statementListGenerator;
  }
  
  public String generateBody() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�statementList.generateStatementList(state.statement)�");
    _builder.newLine();
    _builder.append("�IF state.timeout !== null�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�generateTimeout�");
    _builder.newLine();
    _builder.append("�ENDIF�");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getName() {
    return this.state.getName();
  }
  
  public boolean hasTimeout() {
    TimeoutStatement _timeout = this.state.getTimeout();
    return (_timeout != null);
  }
  
  private String generateTimeout() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("IF (�generateGlobalTime� - �process.generateTimeoutName�) >= �IF state.timeout.variable !== null��statementList.generateVar(state.timeout.variable)��ELSE��state.timeout.const.generateConstant��ENDIF� THEN");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�statementList.generateStatementList(state.timeout.statement)�");
    _builder.newLine();
    _builder.append("END_IF");
    _builder.newLine();
    return _builder.toString();
  }
}
