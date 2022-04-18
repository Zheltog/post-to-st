package su.nsk.iae.post.generator.plcopen.xml.configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.plcopen.xml.XMLGenerator;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.GlobalVarHelper;
import su.nsk.iae.post.generator.plcopen.xml.common.vars.VarHelper;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Resource;
import su.nsk.iae.post.poST.Task;

@SuppressWarnings("all")
public class ResourceGenerator {
  private Resource resource;
  
  private VarHelper resVarList = new GlobalVarHelper();
  
  private List<TaskGenerator> tasks = new LinkedList<TaskGenerator>();
  
  private List<ProgramConfigurationGenerator> programConfigurationGenerators = new LinkedList<ProgramConfigurationGenerator>();
  
  public ResourceGenerator(final Resource resource, final XMLGenerator xmlGenerator) {
    this.resource = resource;
    final Consumer<GlobalVarDeclaration> _function = (GlobalVarDeclaration v) -> {
      this.resVarList.add(v);
      xmlGenerator.addGlobalVar(v);
    };
    resource.getResGlobVars().stream().forEach(_function);
    final Consumer<Task> _function_1 = (Task t) -> {
      TaskGenerator _taskGenerator = new TaskGenerator(t);
      this.tasks.add(_taskGenerator);
    };
    resource.getResStatement().getTasks().stream().forEach(_function_1);
    final Consumer<ProgramConfiguration> _function_2 = (ProgramConfiguration p) -> {
      ProgramConfigurationGenerator _programConfigurationGenerator = new ProgramConfigurationGenerator(p);
      this.programConfigurationGenerators.add(_programConfigurationGenerator);
    };
    resource.getResStatement().getProgramConfs().stream().forEach(_function_2);
  }
  
  public CharSequence generateResource() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<resource name=\"�resource.name�\">");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�FOR t : tasks�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�t.generateTask�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�FOR p : programConfigurationGenerators�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�p.generateProgramConfiguration�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("</resource>");
    _builder.newLine();
    return _builder;
  }
}
