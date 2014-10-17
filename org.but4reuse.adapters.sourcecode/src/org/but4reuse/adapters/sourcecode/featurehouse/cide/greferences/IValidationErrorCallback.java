package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IValidationRule;

public interface IValidationErrorCallback {
	public void validationError(ASTNode node, IValidationRule brokenRule);
}
