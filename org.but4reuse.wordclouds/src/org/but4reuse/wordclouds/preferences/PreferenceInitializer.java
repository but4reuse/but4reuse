package org.but4reuse.wordclouds.preferences;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.EXCLUDES, true);
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.REQUIRES, true);
		store.setDefault(TermBasedConstraintsDiscoveryPreferences.HIGHT_SIM, 0.5);
		store.setDefault(WordCloudPreferences.WORDCLOUD_NB_W, 50);
		store.setDefault(WordCloudPreferences.STOP_WORDS, "");
	}

}
