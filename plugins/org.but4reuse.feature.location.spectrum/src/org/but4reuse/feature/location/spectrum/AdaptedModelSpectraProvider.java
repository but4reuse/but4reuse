package org.but4reuse.feature.location.spectrum;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.helpers.FeatureListHelper;

import fk.stardust.provider.ISpectraProvider;
import fk.stardust.traces.IMutableTrace;
import fk.stardust.traces.ISpectra;
import fk.stardust.traces.ITrace;
import fk.stardust.traces.Spectra;
import fk.stardust.traces.Trace;

/**
 * Spectra provider
 * 
 * @author jabier.martinez
 */
public class AdaptedModelSpectraProvider implements ISpectraProvider<Block> {

	Spectra<Block> s = null;
	AdaptedModel adaptedModel = null;

	public AdaptedModelSpectraProvider(AdaptedModel adaptedModel) {
		this.adaptedModel = adaptedModel;
		s = new Spectra<>();

		// one trace per artefact
		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {

			// create trace
			// for the moment all false
			final IMutableTrace<Block> trace = s.addTrace(true);

			// add blocks to the trace
			List<Block> relevantBlocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(adaptedArtefact);
			for (Block block : relevantBlocks) {
				trace.setInvolvement(block, true);
			}
		}
	}

	@Override
	public ISpectra<Block> loadSpectra() throws Exception {
		return s;
	}

	public void updateSpectraForFeature(FeatureList featureList, Feature feature) {
		// artefacts have the same index as traces
		int i = 0;
		for (AdaptedArtefact adaptedArtefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			// is an artefact of currentFeature?
			// Successful is false if it is the target feature (this is because the library
			// is related to testing so if we want to locate a feature we need to say that
			// the test is failing in this trace)
			boolean currentFeat = false;
			List<Feature> features = FeatureListHelper.getArtefactFeatures(featureList, adaptedArtefact.getArtefact());
			if (features.contains(feature)) {
				currentFeat = true;
			}
			ITrace<Block> trace = s.getTraces().get(i);
			((Trace<Block>)trace).setSuccessful(!currentFeat);
			i++;
		}
	}

}
