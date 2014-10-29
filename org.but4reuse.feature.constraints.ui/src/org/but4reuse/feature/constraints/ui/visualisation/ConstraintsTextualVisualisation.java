package org.but4reuse.feature.constraints.ui.visualisation;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Constraints textual visualisation
 * @author jabier.martinez
 *
 */
public class ConstraintsTextualVisualisation implements IVisualisation {
	
	public AdaptedModel adaptedModel;
	public FeatureList featureList;
	
	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {	
		this.adaptedModel = adaptedModel;
		this.featureList = featureList;
	}

	@Override
	public void show() {

	}
	
}
