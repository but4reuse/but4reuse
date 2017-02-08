package org.but4reuse.feature.location.helper;

import java.util.List;

import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Intialize Feature location preferences by selecting only the default
 * algorithm
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(FeatureLocationHelper.LOCATION_THRESHOLD_PREFERENCE, 0.8);
		List<IFeatureLocation> algos = FeatureLocationHelper.getAllFeatureLocation();
		for (IFeatureLocation algo : algos) {
			String algoName = FeatureLocationHelper.getAlgorithmName(algo);
			boolean isTheDefault = algoName.equals("Feature-Specific Heuristic");
			store.setDefault(FeatureLocationHelper.getAlgorithmName(algo), isTheDefault);
		}
	}

}
