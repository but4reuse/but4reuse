package org.but4reuse.feature.location;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature location
 * 
 * @author jabier.martinez
 */
public interface IFeatureLocation {
	/**
	 * Update the Blocks information to set their assigned Features
	 * 
	 * @param featureList
	 * @param adaptedModel
	 * @return list of located features
	 */
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor);
}
