package org.but4reuse.wordclouds.feature.location;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(FeaturesLocationPreferences.HIGHT_SIM, 0.5);

	}

}
