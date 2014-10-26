package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import java.util.Set;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.features.IASTColorProvider;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.features.IFeature;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IValidationRule;

public abstract class AValidationRule implements IValidationRule {

	public int getErrorSeverity() {
		return Error;
	}

	protected boolean isColorSubset(IASTColorProvider colorProvider,
			ASTNode subset, ASTNode superset) {
		Set<IFeature> subsetColors = colorProvider.getColorsI(subset);
		Set<IFeature> supersetColors = colorProvider.getColorsI(superset);
		return supersetColors.containsAll(subsetColors);
	}

}
