package org.but4reuse.adapters.ui.preferences;

import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


public class AdaptersPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public AdaptersPreferencePage() {
		super(GRID);
		setPreferenceStore(PreferencesHelper.getPreferenceStore());
	}
	
	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
	}

	@Override
	public void init(IWorkbench workbench) {
	}
	
}