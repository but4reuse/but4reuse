package org.but4reuse.wordclouds.similarity;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {


	@Override
	public void initializeDefaultPreferences() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.EXCLUDES, true);
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.REQUIRES, true);
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.HIGHT_SIM, (double)0.5);
		

	}

}
