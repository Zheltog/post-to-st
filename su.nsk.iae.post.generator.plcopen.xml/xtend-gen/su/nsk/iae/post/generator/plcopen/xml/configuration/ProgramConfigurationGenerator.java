package su.nsk.iae.post.generator.plcopen.xml.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.poST.ProgramConfiguration;
import su.nsk.iae.post.poST.Task;

@SuppressWarnings("all")
public class ProgramConfigurationGenerator {
  private ProgramConfiguration programConf;
  
  public ProgramConfigurationGenerator(final ProgramConfiguration programConfiguration) {
    this.programConf = programConfiguration;
  }
  
  public String generateProgramConfiguration() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("PROGRAM �programConf.name��generateTask�;");
    return _builder.toString();
  }
  
  private String generateTask() {
    Task _task = this.programConf.getTask();
    boolean _tripleNotEquals = (_task != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(" ");
      _builder.append("WITH �programConf.task.name�");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    return _builder_1.toString();
  }
}
