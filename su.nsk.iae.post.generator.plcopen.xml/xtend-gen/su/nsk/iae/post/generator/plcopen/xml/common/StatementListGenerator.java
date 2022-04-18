package su.nsk.iae.post.generator.plcopen.xml.common;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.AssignmentStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.CaseStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.ErrorProcessStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.ExitStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.FBInvocationGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.ForStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.IStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.IfStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.RepeatStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.ResetTimerStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.SetStateStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.StartProcessStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.StopProcessStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.SubprogramControlStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.statement.WhileStatementGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.util.GeneratorUtil;
import su.nsk.iae.post.poST.ArrayVariable;
import su.nsk.iae.post.poST.Expression;
import su.nsk.iae.post.poST.ParamAssignmentElements;
import su.nsk.iae.post.poST.ProcessStatusExpression;
import su.nsk.iae.post.poST.Statement;
import su.nsk.iae.post.poST.StatementList;
import su.nsk.iae.post.poST.SymbolicVariable;

@SuppressWarnings("all")
public class StatementListGenerator {
  private ProgramGenerator program;
  
  private ProcessGenerator process;
  
  private StateGenerator state;
  
  private List<IStatementGenerator> statementGenerators;
  
  public StatementListGenerator(final ProgramGenerator program, final ProcessGenerator process, final StateGenerator state) {
    this.program = program;
    this.process = process;
    this.state = state;
    this.statementGenerators = this.initStatementGenerators();
  }
  
  public String generateStatementList(final StatementList statementList) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�FOR s : statementList.statements�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�s.generateStatement�");
    _builder.newLine();
    _builder.append("�ENDFOR�");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String generateStatement(final Statement statement) {
    for (final IStatementGenerator sg : this.statementGenerators) {
      boolean _checkStatement = sg.checkStatement(statement);
      if (_checkStatement) {
        return sg.generateStatement(statement);
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  public String generateExpression(final Expression exp) {
    final Function<SymbolicVariable, String> _function = (SymbolicVariable x) -> {
      return this.generateVar(x);
    };
    final Function<ArrayVariable, String> _function_1 = (ArrayVariable x) -> {
      return this.generateArray(x);
    };
    final Function<ProcessStatusExpression, String> _function_2 = (ProcessStatusExpression x) -> {
      return this.generateProcessStatus(x);
    };
    return GeneratorUtil.generateExpression(exp, _function, _function_1, _function_2);
  }
  
  public String generateParamAssignmentElements(final ParamAssignmentElements elements) {
    final Function<Expression, String> _function = (Expression x) -> {
      return this.generateExpression(x);
    };
    return GeneratorUtil.generateParamAssignmentElements(elements, _function);
  }
  
  public String generateVar(final SymbolicVariable varName) {
    boolean _containsVar = this.process.containsVar(varName.getName());
    if (_containsVar) {
      return GeneratorUtil.generateVarName(this.process, varName.getName());
    }
    return varName.getName();
  }
  
  public String generateArray(final ArrayVariable varDecl) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�varDecl.variable.generateVar�[�varDecl.index.generateExpression�]");
    return _builder.toString();
  }
  
  public String generateProcessStatus(final ProcessStatusExpression exp) {
    boolean _isActive = exp.isActive();
    if (_isActive) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("((�program.generateProcessEnum(exp.process.name)� &lt;&gt; �generateStopConstant�) AND (�program.generateProcessEnum(exp.process.name)� &lt;&gt; �generateErrorConstant�))");
      return _builder.toString();
    } else {
      boolean _isInactive = exp.isInactive();
      if (_isInactive) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("((�program.generateProcessEnum(exp.process.name)� = �generateStopConstant�) OR (�program.generateProcessEnum(exp.process.name)� = �generateErrorConstant�))");
        return _builder_1.toString();
      } else {
        boolean _isStop = exp.isStop();
        if (_isStop) {
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("(�program.generateProcessEnum(exp.process.name)� = �generateStopConstant�)");
          return _builder_2.toString();
        }
      }
    }
    StringConcatenation _builder_3 = new StringConcatenation();
    _builder_3.append("(�program.generateProcessEnum(exp.process.name)� = �generateErrorConstant�)");
    return _builder_3.toString();
  }
  
  private List<IStatementGenerator> initStatementGenerators() {
    AssignmentStatementGenerator _assignmentStatementGenerator = new AssignmentStatementGenerator(this.program, this.process, this.state, this);
    IfStatementGenerator _ifStatementGenerator = new IfStatementGenerator(this.program, this.process, this.state, this);
    CaseStatementGenerator _caseStatementGenerator = new CaseStatementGenerator(this.program, this.process, this.state, this);
    ForStatementGenerator _forStatementGenerator = new ForStatementGenerator(this.program, this.process, this.state, this);
    WhileStatementGenerator _whileStatementGenerator = new WhileStatementGenerator(this.program, this.process, this.state, this);
    RepeatStatementGenerator _repeatStatementGenerator = new RepeatStatementGenerator(this.program, this.process, this.state, this);
    FBInvocationGenerator _fBInvocationGenerator = new FBInvocationGenerator(this.program, this.process, this.state, this);
    StartProcessStatementGenerator _startProcessStatementGenerator = new StartProcessStatementGenerator(this.program, this.process, this.state, this);
    StopProcessStatementGenerator _stopProcessStatementGenerator = new StopProcessStatementGenerator(this.program, this.process, this.state, this);
    ErrorProcessStatementGenerator _errorProcessStatementGenerator = new ErrorProcessStatementGenerator(this.program, this.process, this.state, this);
    SetStateStatementGenerator _setStateStatementGenerator = new SetStateStatementGenerator(this.program, this.process, this.state, this);
    ResetTimerStatementGenerator _resetTimerStatementGenerator = new ResetTimerStatementGenerator(this.program, this.process, this.state, this);
    SubprogramControlStatementGenerator _subprogramControlStatementGenerator = new SubprogramControlStatementGenerator(this.program, this.process, this.state, this);
    ExitStatementGenerator _exitStatementGenerator = new ExitStatementGenerator(this.program, this.process, this.state, this);
    return Arrays.<IStatementGenerator>asList(_assignmentStatementGenerator, _ifStatementGenerator, _caseStatementGenerator, _forStatementGenerator, _whileStatementGenerator, _repeatStatementGenerator, _fBInvocationGenerator, _startProcessStatementGenerator, _stopProcessStatementGenerator, _errorProcessStatementGenerator, _setStateStatementGenerator, _resetTimerStatementGenerator, _subprogramControlStatementGenerator, _exitStatementGenerator);
  }
}
