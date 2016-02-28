package org.but4reuse.adapters.filestructure.preferences;

import org.but4reuse.adapters.filestructure.activator.Activator;
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
		store.setDefault(FileStructureAdapterPreferencePage.CONTENT_CHECK, false);
		store.setDefault(FileStructureAdapterPreferencePage.IGNORE_FOLDERS, false);
	}

}
