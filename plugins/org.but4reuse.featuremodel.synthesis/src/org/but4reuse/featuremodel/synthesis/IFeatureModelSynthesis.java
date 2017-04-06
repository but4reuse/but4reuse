package org.but4reuse.featuremodel.synthesis;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Interface for synthesis of feature models
 * 
 * @author jabier.martinez
 */
public interface IFeatureModelSynthesis {

	/**
	 * Create feature model
	 */
	public void createFeatureModel(URI outputContainer, IProgressMonitor monitor);
}
