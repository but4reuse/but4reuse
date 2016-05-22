package org.but4reuse.featuremodel.synthesis;

import java.net.URI;

/**
 * Interface for synthesis of feature models
 * 
 * @author jabier.martinez
 */
public interface IFeatureModelSynthesis {

	/**
	 * Create feature model
	 */
	public void createFeatureModel(URI outputContainer);
}
