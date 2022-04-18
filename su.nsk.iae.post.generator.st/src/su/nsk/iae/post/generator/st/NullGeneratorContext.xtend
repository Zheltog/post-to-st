package su.nsk.iae.post.generator.st

import org.eclipse.xtext.generator.IGeneratorContext
import org.eclipse.xtext.util.CancelIndicator

class NullGeneratorContext implements IGeneratorContext {
	
    override CancelIndicator getCancelIndicator() {
        return null;
    }
}