package org.but4reuse.visualisation;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Visualisation interface
 * 
 * @author jabier.martinez
 */
public interface IVisualisation {

	/**
	 * Prepare the visualisation
	 * 
	 * @param featureList
	 *            , it can be null
	 * @param adaptedModel
	 *            , it can be null
	 * @param extra
	 *            parameter that can be needed
	 * @param monitor
	 */
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor);

	/**
	 * Show the visualisation.
	 */
	public void show();
}
