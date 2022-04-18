package su.nsk.iae.post.generator.plcopen.xml.common;

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
import su.nsk.iae.post.generator.plcopen.xml.common.util.GeneratorUtil;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.ExternalVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.InputOutputVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.InputVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.OutputVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.SimpleVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.TempVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.VarHelper;

@SuppressWarnings("all")
public class ProgramGenerator {
  protected EObject object;
  
  protected String programName;
  
  protected String type;
  
  private boolean templateProcess;
  
  protected VarHelper inVarList = new InputVarHelper();
  
  protected VarHelper outVarList = new OutputVarHelper();
  
  protected VarHelper inOutVarList = new InputOutputVarHelper();
  
  protected VarHelper externalVarList = new ExternalVarHelper();
  
  protected VarHelper varList = new SimpleVarHelper();
  
  protected VarHelper tempVarList = new TempVarHelper();
  
  protected List<ProcessGenerator> processList = new LinkedList<ProcessGenerator>();
  
  public ProgramGenerator(final boolean templateProcess) {
    this.templateProcess = templateProcess;
  }
  
  public void generate(final IFileSystemAccess2 fsa, final String path) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�path��programName.toLowerCase�.st");
    fsa.generateFile(_builder.toString(), this.generateFullProgram());
  }
  
  public String generateProgram() {
    this.prepareProgramVars();
    return this.generateXMLBody();
  }
  
  public String generateFullProgram() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("�generateXMLStart�");
    _builder.newLine();
    _builder.append("�generateXMLBody�");
    _builder.newLine();
    _builder.append("�generateXMLEnd�");
    _builder.newLine();
    return _builder.toString();
  }
  
  private String generateXMLBody() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t\t\t");
    _builder.append("<pou name=\"�programName�\" pouType=\"�type.toLowerCase�\">");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<interface>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�IF !templateProcess�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("�inVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("�outVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("�inOutVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�ENDIF�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�externalVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�varList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("�tempVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</interface>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("<body>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("<ST>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t\t");
    _builder.append("<xhtml xmlns=\"http://www.w3.org/1999/xhtml\">�generateGlobalTime� := TIME();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("�FOR p : processList�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�p.generateBody�");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("</xhtml>");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("</ST>");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("</body>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</pou>");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String getName() {
    return this.programName;
  }
  
  public EObject getEObject() {
    return this.object;
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
      x.prepareStateVars(this.templateProcess);
    };
    this.processList.stream().forEach(_function_2);
    this.addVar(GeneratorUtil.generateStopConstant(), "INT", "254", true);
    this.addVar(GeneratorUtil.generateErrorConstant(), "INT", "255", true);
  }
  
  public void addProcess(final su.nsk.iae.post.poST.Process process) {
    ProcessGenerator _processGenerator = new ProcessGenerator(this, process);
    this.processList.add(_processGenerator);
  }
  
  public void addProcess(final su.nsk.iae.post.poST.Process process, final boolean active) {
    ProcessGenerator _processGenerator = new ProcessGenerator(this, process, active);
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
