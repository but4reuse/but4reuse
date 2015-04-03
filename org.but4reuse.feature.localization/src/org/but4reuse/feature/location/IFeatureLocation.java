package org.but4reuse.feature.location;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature location
 * TODO provisional API
 * @author jabier.martinez
 */
public interface IFeatureLocation {
	/**
	 * Update the Blocks information to set their assigned Features
	 * @param featureList
	 * @param adaptedModel
	 */
	public void locateFeatures(FeatureList featureList, AdaptedModel adaptedModel, IProgressMonitor monitor);
}
