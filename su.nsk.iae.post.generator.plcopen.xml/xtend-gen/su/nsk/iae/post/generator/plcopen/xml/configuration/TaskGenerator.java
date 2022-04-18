package su.nsk.iae.post.generator.plcopen.xml.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.poST.Task;

@SuppressWarnings("all")
public class TaskGenerator {
  private Task task;
  
  public TaskGenerator(final Task task) {
    this.task = task;
  }
  
  public String generateTask() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<task name=\"�task.name�\" interval=\"PT0.01S\" priority=\"�task.init.priority�\">");
    _builder.newLine();
    return _builder.toString();
  }
}
