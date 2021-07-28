package su.nsk.iae.post.generator.st.common;

import com.google.common.base.Objects;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import su.nsk.iae.post.generator.st.common.util.GeneratorUtil;
import su.nsk.iae.post.generator.st.common.vars.ExternalVarHelper;
import su.nsk.iae.post.generator.st.common.vars.InputOutputVarHelper;
import su.nsk.iae.post.generator.st.common.vars.InputVarHelper;
import su.nsk.iae.post.generator.st.common.vars.OutputVarHelper;
import su.nsk.iae.post.generator.st.common.vars.SimpleVarHelper;
import su.nsk.iae.post.generator.st.common.vars.TempVarHelper;
import su.nsk.iae.post.generator.st.common.vars.VarHelper;

@SuppressWarnings("all")
public class ProgramGenerator {
  protected String programName;
  
  protected String type;
  
  protected VarHelper inVarList = new InputVarHelper();
  
  protected VarHelper outVarList = new OutputVarHelper();
  
  protected VarHelper inOutVarList = new InputOutputVarHelper();
  
  protected VarHelper externalVarList = new ExternalVarHelper();
  
  protected VarHelper varList = new SimpleVarHelper();
  
  protected VarHelper tempVarList = new TempVarHelper();
  
  protected List<ProcessGenerator> processList = new LinkedList<ProcessGenerator>();
  
  public void generate(final IFileSystemAccess2 fsa, final String path) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(path);
    String _lowerCase = this.programName.toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(".st");
    fsa.generateFile(_builder.toString(), this.generateProgram());
  }
  
  public String generateProgram() {
    this.prepareProgramVars();
    return this.generateBody();
  }
  
  public String generateBody() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.type);
    _builder.append(" ");
    _builder.append(this.programName);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String _generateVars = GeneratorUtil.generateVars(this.inVarList);
    _builder.append(_generateVars);
    _builder.newLineIfNotEmpty();
    String _generateVars_1 = GeneratorUtil.generateVars(this.outVarList);
    _builder.append(_generateVars_1);
    _builder.newLineIfNotEmpty();
    String _generateVars_2 = GeneratorUtil.generateVars(this.inOutVarList);
    _builder.append(_generateVars_2);
    _builder.newLineIfNotEmpty();
    String _generateVars_3 = GeneratorUtil.generateVars(this.externalVarList);
    _builder.append(_generateVars_3);
    _builder.newLineIfNotEmpty();
    String _generateVars_4 = GeneratorUtil.generateVars(this.varList);
    _builder.append(_generateVars_4);
    _builder.newLineIfNotEmpty();
    String _generateVars_5 = GeneratorUtil.generateVars(this.tempVarList);
    _builder.append(_generateVars_5);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String _generateGlobalTime = GeneratorUtil.generateGlobalTime();
    _builder.append(_generateGlobalTime);
    _builder.append(" := TIME();");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      for(final ProcessGenerator p : this.processList) {
        String _generateBody = p.generateBody();
        _builder.append(_generateBody);
        _builder.newLineIfNotEmpty();
        _builder.newLine();
      }
    }
    _builder.append("END_");
    _builder.append(this.type);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public String getName() {
    return this.programName;
  }
  
  protected void parseProcesses(final EList<su.nsk.iae.post.poST.Process> processes) {
    final Consumer<su.nsk.iae.post.poST.Process> _function = (su.nsk.iae.post.poST.Process p) -> {
      final ProcessGenerator process = new ProcessGenerator(this, p);
      boolean _isTemplate = process.isTemplate();
      boolean _not = (!_isTemplate);
      if (_not) {
        this.processList.add(process);
      }
    };
    processes.stream().forEach(_function);
  }
  
  public void prepareProgramVars() {
    final Consumer<ProcessGenerator> _function = (ProcessGenerator x) -> {
      x.prepareProcessVars();
    };
    this.processList.stream().forEach(_function);
    this.addVar(GeneratorUtil.generateGlobalTime(), "TIME");
    final Consumer<ProcessGenerator> _function_1 = (ProcessGenerator x) -> {
      x.prepareTimeVars();
    };
    this.processList.stream().forEach(_function_1);
    final Consumer<ProcessGenerator> _function_2 = (ProcessGenerator x) -> {
      x.prepareStateVars();
    };
    this.processList.stream().forEach(_function_2);
    this.addVar(GeneratorUtil.generateStopConstant(), "INT", "254", true);
    this.addVar(GeneratorUtil.generateErrorConstant(), "INT", "255", true);
  }
  
  public void addProcess(final su.nsk.iae.post.poST.Process process) {
    ProcessGenerator _processGenerator = new ProcessGenerator(this, process);
    this.processList.add(_processGenerator);
  }
  
  public void addVar(final EObject varDecl) {
    this.varList.add(varDecl);
  }
  
  public void addVar(final EObject varDecl, final String pref) {
    this.varList.add(varDecl, pref);
  }
  
  public void addVar(final String name, final String type) {
    this.varList.add(name, type);
  }
  
  public void addVar(final String name, final String type, final String value) {
    this.varList.add(name, type, value);
  }
  
  public void addVar(final String name, final String type, final String value, final boolean isConstant) {
    this.varList.add(name, type, value, isConstant);
  }
  
  public void addTempVar(final EObject varDecl) {
    this.tempVarList.add(varDecl);
  }
  
  public void addTempVar(final EObject varDecl, final String pref) {
    this.tempVarList.add(varDecl, pref);
  }
  
  public void addTempVar(final String name, final String type, final String value) {
    this.tempVarList.add(name, type, value);
  }
  
  public boolean isFirstProcess(final ProcessGenerator process) {
    ProcessGenerator _get = this.processList.get(0);
    return Objects.equal(_get, process);
  }
  
  public void addInVar(final EObject varDecl) {
    this.inVarList.add(varDecl);
  }
  
  public void addInVar(final EObject varDecl, final String pref) {
    this.inVarList.add(varDecl, pref);
  }
  
  public void addOutVar(final EObject varDecl) {
    this.outVarList.add(varDecl);
  }
  
  public void addOutVar(final EObject varDecl, final String pref) {
    this.outVarList.add(varDecl, pref);
  }
  
  public void addInOutVar(final EObject varDecl) {
    this.inOutVarList.add(varDecl);
  }
  
  public void addInOutVar(final EObject varDecl, final String pref) {
    this.inOutVarList.add(varDecl, pref);
  }
  
  public String generateProcessEnum(final String processName) {
    final Function1<ProcessGenerator, Boolean> _function = (ProcessGenerator it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, processName));
    };
    return GeneratorUtil.generateEnumName(IterableExtensions.<ProcessGenerator>findFirst(this.processList, _function));
  }
  
  public String generateProcessStart(final String processName) {
    final Function1<ProcessGenerator, Boolean> _function = (ProcessGenerator it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, processName));
    };
    return IterableExtensions.<ProcessGenerator>findFirst(this.processList, _function).generateStart();
  }
}
