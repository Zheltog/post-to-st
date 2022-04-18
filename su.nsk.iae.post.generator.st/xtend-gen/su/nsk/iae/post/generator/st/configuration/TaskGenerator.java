package su.nsk.iae.post.generator.st.configuration;

import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.poST.Constant;
import su.nsk.iae.post.poST.Task;
import su.nsk.iae.post.poST.TaskInitialization;

@SuppressWarnings("all")
public class TaskGenerator {
  private Task task;
  
  public TaskGenerator(final Task task) {
    this.task = task;
  }
  
  public String generateTask() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("TASK �task.name� (�generateFirstArg�, PRIORITY := �task.init.priority�);");
    return _builder.toString();
  }
  
  private String generateFirstArg() {
    final TaskInitialization init = this.task.getInit();
    Constant _interval = init.getInterval();
    boolean _tripleNotEquals = (_interval != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("INTERVAL := �init.interval.generateConstant�");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("SINGLE := �init.single.generateConstant�");
    return _builder_1.toString();
  }
}
