package org.but4reuse.fca.feature.location;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.fca.utils.FCAUtils;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.erca.clf.ConceptLatticeFamily;

public class RelationalContextsFeatureLocation implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {
		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		ConceptLatticeFamily clf = FCAUtils
				.createArtefactsBlocksFeaturesConceptLatticeFamily(featureList, adaptedModel);
		
		// TODO implement
		
		// TODO ongoing work

		return locatedFeatures;
	}

}
