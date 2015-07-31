package org.but4reuse.adapters.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.but4reuse.adapters.activator.Activator;
import org.but4reuse.adapters.preferences.PreferencesHelper;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferencesHelper.AUTOMATIC_EQUAL_THRESHOLD, 1.00);
		store.setDefault(PreferencesHelper.ASK_USER, false);
		store.setDefault(PreferencesHelper.ASK_USER_THRESHOLD, 0.90);
		store.setDefault(PreferencesHelper.ASK_USER_DEACTIVATED_FOR_THIS_TIME, false);
		store.setDefault(PreferencesHelper.ADAPT_CONCURRENTLY, false);
	}

}
