package org.but4reuse.featuremodel.synthesis.fmcreators;

import java.net.URI;


/**
 * Interface for synthesis of feature models
 * 
 * @author jabier.martinez
 */
public interface IFeatureModelCreator {
	
	/**
	 * Create feature model
	 */
	public void createFeatureModel(URI outputContainer);
}
