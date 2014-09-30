package org.but4reuse.adapters.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import org.but4reuse.adapters.ui.activator.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(SimilarityPreferencePage.AUTOMATIC_EQUAL_THRESHOLD, 100);
		store.setDefault(SimilarityPreferencePage.ASK_USER, false);
		store.setDefault(SimilarityPreferencePage.ASK_USER_THRESHOLD, 100);
	}

}
