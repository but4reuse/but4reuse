package org.but4reuse.visualisation.ui.preferences;

import java.util.List;

import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.activator.Activator;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize Visualisations, all active
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		List<IVisualisation> algos = VisualisationsHelper.getAllVisualisations();
		for (IVisualisation algo : algos) {
			String algoName = VisualisationsHelper.getVisualisationName(algo);
			store.setDefault(algoName, true);
		}
	}

}
