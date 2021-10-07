package org.but4reuse.feature.location.spectrum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.spectrum.activator.Activator;
import org.but4reuse.feature.location.spectrum.preferences.SpectrumPreferencePage;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import fk.stardust.localizer.IFaultLocalizer;
import fk.stardust.localizer.NormalizedRanking;
import fk.stardust.localizer.NormalizedRanking.NormalizationStrategy;
import fk.stardust.localizer.Ranking;
import fk.stardust.traces.INode;
import fk.stardust.traces.ISpectra;

/**
 * Spectrum-based feature localization
 * 
 * @author jabier.martinez
 */
public class SpectrumBasedLocalization implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		// Get algorithm/ranking metric from preferences
		String rankingMetricName = Activator.getDefault().getPreferenceStore().getString(SpectrumPreferencePage.RANKING_METRIC);
		IFaultLocalizer<Block> rankingMetric = RankingMetrics.getRankingMetricByName(rankingMetricName);

		// Call the location
		List<LocatedFeature> locatedFeatures = locateFeatures(featureList, adaptedModel, rankingMetric);
		return locatedFeatures;
	}

	/**
	 * Locate features
	 * 
	 * @param featureList
	 * @param adaptedModel
	 * @param algo
	 * @return located features
	 */
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IFaultLocalizer<Block> algo) {
		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

		// for each feature
		for (Feature feature : featureList.getOwnedFeatures()) {
			try {
				// create the spectra
				AdaptedModelSpectraProvider provider = new AdaptedModelSpectraProvider(featureList, adaptedModel, feature);
				ISpectra<Block> spectra = provider.loadSpectra();
				
				// launch the ranking algorithm
				Ranking<Block> ranking = algo.localize(spectra);
				
				// normalize the ranking
				NormalizedRanking<Block> normalizedRanking = new NormalizedRanking<Block>(ranking,
						NormalizationStrategy.ZeroOne);

				// add all locations with rankings greater than 0
				// the threshold will be later applied as defined by the user
				Iterator<INode<Block>> i = normalizedRanking.iterator();
				while (i.hasNext()) {
					INode<Block> node = i.next();
					double suspiciousness = normalizedRanking.getSuspiciousness(node);
					if (suspiciousness > 0) {
						LocatedFeature located = new LocatedFeature(feature, node.getIdentifier(), suspiciousness);
						locatedFeatures.add(located);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return locatedFeatures;
	}
	
}
