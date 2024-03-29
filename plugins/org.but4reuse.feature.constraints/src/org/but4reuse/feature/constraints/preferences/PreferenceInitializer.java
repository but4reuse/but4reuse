package org.but4reuse.feature.constraints.preferences;

import org.but4reuse.feature.constraints.activator.Activator;
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
		store.setDefault(BinaryRelationPreferencePage.REQUIRES, true);
		store.setDefault(BinaryRelationPreferencePage.EXCLUDES, true);
		store.setDefault(BinaryRelationPreferencePage.ONLY_ONE_REASON, false);
		store.setDefault(BinaryRelationPreferencePage.IGNORE_DISCOVERY_FROM_COMMON_BLOCK, false);
		store.setDefault(BinaryRelationPreferencePage.IGNORE_REQUIRES_BLOCKS_NEVER_TOGETHER, false);
		store.setDefault(BinaryRelationPreferencePage.IGNORE_EXCLUDES_BLOCKS_TOGETHER_ONCE, false);
	}

}
