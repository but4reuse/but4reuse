package org.but4reuse.adapters.ui.preferences;

import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Adapters preference page
 * 
 * @author jabier.martinez
 * 
 */
public class AdaptersPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public AdaptersPreferencePage() {
		super(GRID);
		setPreferenceStore(PreferencesHelper.getPreferenceStore());
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		BooleanFieldEditor bfe = new BooleanFieldEditor(PreferencesHelper.ADAPT_CONCURRENTLY,
				"Adapt concurrently (Known issue in file-system access. Do not use it for filestructure nor eclipse adapters)",
				getFieldEditorParent());
		addField(bfe);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}