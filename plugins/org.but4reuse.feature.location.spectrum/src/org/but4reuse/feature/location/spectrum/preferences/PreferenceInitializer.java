package org.but4reuse.feature.location.spectrum.preferences;

import org.but4reuse.feature.location.spectrum.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import fk.stardust.localizer.sbfl.Wong2;

/**
 * Preference initializer
 * 
 * @author jabier.martinez
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(SpectrumPreferencePage.RANKING_METRIC, new Wong2<>().getName());
	}

}
