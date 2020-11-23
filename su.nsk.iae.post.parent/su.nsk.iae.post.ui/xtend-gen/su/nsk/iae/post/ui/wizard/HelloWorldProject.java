/**
 * generated by Xtext 2.23.0
 */
package su.nsk.iae.post.ui.wizard;

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.util.PluginProjectFactory;
import org.eclipse.xtext.ui.wizard.template.AbstractProjectTemplate;
import org.eclipse.xtext.ui.wizard.template.BooleanTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.GroupTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.IProjectGenerator;
import org.eclipse.xtext.ui.wizard.template.ProjectTemplate;
import org.eclipse.xtext.ui.wizard.template.StringSelectionTemplateVariable;
import org.eclipse.xtext.ui.wizard.template.StringTemplateVariable;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ProjectTemplate(label = "Hello World", icon = "project_template.png", description = "<p><b>Hello World</b></p>\r\n<p>This is a parameterized hello world for PoST. You can set a parameter to modify the content in the generated file\r\nand a parameter to set the package the file is created in.</p>")
@SuppressWarnings("all")
public final class HelloWorldProject extends AbstractProjectTemplate {
  private final BooleanTemplateVariable advanced = this.check("Advanced:", false);
  
  private final GroupTemplateVariable advancedGroup = this.group("Properties");
  
  private final StringSelectionTemplateVariable name = this.combo("Name:", new String[] { "Xtext", "World", "Foo", "Bar" }, "The name to say \'Hello\' to", this.advancedGroup);
  
  private final StringTemplateVariable path = this.text("Package:", "mydsl", "The package path to place the files in", this.advancedGroup);
  
  @Override
  protected void updateVariables() {
    this.name.setEnabled(this.advanced.getValue());
    this.path.setEnabled(this.advanced.getValue());
    boolean _value = this.advanced.getValue();
    boolean _not = (!_value);
    if (_not) {
      this.name.setValue("Xtext");
      this.path.setValue("post");
    }
  }
  
  @Override
  protected IStatus validate() {
    Status _xifexpression = null;
    boolean _matches = this.path.getValue().matches("[a-z][a-z0-9_]*(/[a-z][a-z0-9_]*)*");
    if (_matches) {
      _xifexpression = null;
    } else {
      _xifexpression = new Status(IStatus.ERROR, "Wizard", (("\'" + this.path) + "\' is not a valid package name"));
    }
    return _xifexpression;
  }
  
  @Override
  public void generateProjects(final IProjectGenerator generator) {
    PluginProjectFactory _pluginProjectFactory = new PluginProjectFactory();
    final Procedure1<PluginProjectFactory> _function = (PluginProjectFactory it) -> {
      it.setProjectName(this.getProjectInfo().getProjectName());
      it.setLocation(this.getProjectInfo().getLocationPath());
      List<String> _projectNatures = it.getProjectNatures();
      Iterables.<String>addAll(_projectNatures, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(JavaCore.NATURE_ID, "org.eclipse.pde.PluginNature", XtextProjectHelper.NATURE_ID)));
      List<String> _builderIds = it.getBuilderIds();
      Iterables.<String>addAll(_builderIds, Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList(JavaCore.BUILDER_ID, XtextProjectHelper.BUILDER_ID)));
      List<String> _folders = it.getFolders();
      _folders.add("src");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("src/");
      _builder.append(this.path);
      _builder.append("/Model.post");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("/*");
      _builder_1.newLine();
      _builder_1.append(" ");
      _builder_1.append("* This is an example model");
      _builder_1.newLine();
      _builder_1.append(" ");
      _builder_1.append("*/");
      _builder_1.newLine();
      _builder_1.append("Hello ");
      _builder_1.append(this.name);
      _builder_1.append("!");
      _builder_1.newLineIfNotEmpty();
      this.addFile(it, _builder, _builder_1);
    };
    PluginProjectFactory _doubleArrow = ObjectExtensions.<PluginProjectFactory>operator_doubleArrow(_pluginProjectFactory, _function);
    generator.generate(_doubleArrow);
  }
}
