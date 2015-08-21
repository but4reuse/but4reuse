package org.but4reuse.feature.location.lsi.location.preferences;

import org.but4reuse.feature.location.lsi.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(LSIPreferencePage.DIM, 0.5);
		store.setDefault(LSIPreferencePage.FIXED, true);
	}

}
