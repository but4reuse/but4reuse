package org.but4reuse.feature.constraints;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Constraints discovery interface
 * 
 * @author jabier.martinez
 */
public interface IConstraintsDiscovery {

	public List<IConstraint> discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor);

}
