package org.but4reuse.feature.constraints;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IConstraintsDiscovery {
	
	// TODO this will change to return IConstraints list, but we do not know if adding it as a part of the adaptedModel or not
	public String discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor);
	
}
