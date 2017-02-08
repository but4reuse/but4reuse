package org.but4reuse.feature.constraints.helper;

import java.util.List;

import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize Constraints discovery preferences by selecting only the default
 * algorithm
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<IConstraintsDiscovery> algos = ConstraintsDiscoveryHelper.getAllConstraintsDiscoveryAlgorithms();
		for (IConstraintsDiscovery algo : algos) {
			String algoName = ConstraintsDiscoveryHelper.getAlgorithmName(algo);
			boolean isTheDefault = algoName.equals("Structural binary relations constraints");
			store.setDefault(algoName, isTheDefault);
		}
	}

}
