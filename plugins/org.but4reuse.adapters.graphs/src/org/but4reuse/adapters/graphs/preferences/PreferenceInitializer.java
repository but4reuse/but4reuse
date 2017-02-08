package org.but4reuse.adapters.graphs.preferences;

import org.but4reuse.adapters.graphs.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initialize preferences
 * 
 * @author jabier.martinez
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(GraphsAdapterPreferencePage.NODE_ID, "nodeLabel");
		store.setDefault(GraphsAdapterPreferencePage.EDGE_ID, "edgeLabel");
	}

}
