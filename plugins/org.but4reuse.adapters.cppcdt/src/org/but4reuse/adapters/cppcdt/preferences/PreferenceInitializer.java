package org.but4reuse.adapters.cppcdt.preferences;

import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Initializer for the C++ Adapter preferences
 * 
 * @author sandu.postaru
 *
 */

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(CppAdapterPreferencePage.USE_FUNCTION_CALL_HIERARCHY, false);
		store.setDefault(CppAdapterPreferencePage.DOXYGEN_PATH, "/usr/bin/doxygen");
	}

}
