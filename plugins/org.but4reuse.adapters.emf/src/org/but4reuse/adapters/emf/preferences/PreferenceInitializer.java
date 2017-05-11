package org.but4reuse.adapters.emf.preferences;

import org.but4reuse.adapters.emf.activator.Activator;
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
		store.setDefault(EMFAdapterPreferencePage.XML_ID_HASHING, true);
		store.setDefault(EMFAdapterPreferencePage.MATCH_ID_HASHING, false);
		store.setDefault(EMFAdapterPreferencePage.COMPARISON_METHOD, "");
	}

}
