package su.nsk.iae.post.generator.st.configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import su.nsk.iae.post.generator.st.common.vars.GlobalVarHelper;
import su.nsk.iae.post.generator.st.common.vars.VarHelper;
import su.nsk.iae.post.poST.Configuration;
import su.nsk.iae.post.poST.GlobalVarDeclaration;
import su.nsk.iae.post.poST.Resource;

@SuppressWarnings("all")
public class ConfigurationGenerator {
  private Configuration configuration;
  
  private VarHelper confVarList = new GlobalVarHelper();
  
  private List<ResourceGenerator> resources = new LinkedList<ResourceGenerator>();
  
  public ConfigurationGenerator(final Configuration configuration) {
    this.configuration = configuration;
    final Consumer<GlobalVarDeclaration> _function = (GlobalVarDeclaration v) -> {
      this.confVarList.add(v);
    };
    configuration.getConfGlobVars().stream().forEach(_function);
    final Consumer<Resource> _function_1 = (Resource r) -> {
      ResourceGenerator _resourceGenerator = new ResourceGenerator(r);
      this.resources.add(_resourceGenerator);
    };
    configuration.getResources().stream().forEach(_function_1);
  }
  
  public String generateConfiguration() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("CONFIGURATION �configuration.name�");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�confVarList.generateVars�");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�FOR r : resources�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("�r.generateResource�");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("�ENDFOR�");
    _builder.newLine();
    _builder.append("END_CONFIGURATION");
    _builder.newLine();
    _builder.newLine();
    return _builder.toString();
  }
  
  public EList<Resource> getResources() {
    return this.configuration.getResources();
  }
}
