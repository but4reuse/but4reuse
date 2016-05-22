package org.but4reuse.featuremodel.synthesis.ui.preferences;

import java.util.List;

import org.but4reuse.featuremodel.synthesis.IFeatureModelSynthesis;
import org.but4reuse.featuremodel.synthesis.activator.Activator;
import org.but4reuse.featuremodel.synthesis.helper.FeatureModelSynthesisHelper;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize feature model synthesis preferences by selecting only the default
 * algorithm
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<IFeatureModelSynthesis> algos = FeatureModelSynthesisHelper.getAllFeatureModelCreators();
		for (IFeatureModelSynthesis algo : algos) {
			String algoName = FeatureModelSynthesisHelper.getAlgorithmName(algo);
			boolean isTheDefault = algoName.equals("Alternatives Before Hierarchy");
			store.setDefault(algoName, isTheDefault);
		}
	}

}
