package su.nsk.iae.post.generator.st;

import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.util.CancelIndicator;

@SuppressWarnings("all")
public class NullGeneratorContext implements IGeneratorContext {
  @Override
  public CancelIndicator getCancelIndicator() {
    return null;
  }
}
