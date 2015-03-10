package org.but4reuse.extension.featureide.fmcreators;

import org.but4reuse.adaptedmodel.AdaptedModel;

import de.ovgu.featureide.fm.core.FeatureModel;

/**
 * Interface for synthesis of feature models
 * 
 * @author jabier.martinez
 */
public interface IFeatureModelCreator {
	
	/**
	 * Create feature model
	 * 
	 * @param adaptedModel
	 * @return feature model
	 */
	public FeatureModel createFeatureModel(AdaptedModel adaptedModel);
}
