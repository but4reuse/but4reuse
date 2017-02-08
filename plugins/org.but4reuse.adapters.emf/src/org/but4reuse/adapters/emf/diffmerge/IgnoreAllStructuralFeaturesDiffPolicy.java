package org.but4reuse.adapters.emf.diffmerge;

import org.eclipse.emf.diffmerge.impl.policies.DefaultDiffPolicy;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * IgnoreAllStructuralFeaturesDiffPolicy
 * 
 * @author jabier.martinez
 * 
 */
public class IgnoreAllStructuralFeaturesDiffPolicy extends DefaultDiffPolicy {

	/**
	 * @see org.eclipse.emf.diffmerge.api.IDiffPolicy#coverFeature(org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public boolean coverFeature(EStructuralFeature feature_p) {
		return false;
	}

}
