package org.but4reuse.adapters.ui.preferences;

import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.but4reuse.utils.ui.preferences.DoubleScaleFieldEditor;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Similarity Preference Page
 * 
 * @author jabier.martinez
 * 
 */
public class SimilarityPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public SimilarityPreferencePage() {
		setPreferenceStore(PreferencesHelper.getPreferenceStore());
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		// equal threshold
		DoubleScaleFieldEditor autoEqualFieldEditor = new DoubleScaleFieldEditor(
				PreferencesHelper.AUTOMATIC_EQUAL_THRESHOLD, "Minimum percentage for automatic equal: ",
				getFieldEditorParent());
		addField(autoEqualFieldEditor);
		// threshold to ask users. manual similarity
		BooleanFieldEditor bfe = new BooleanFieldEditor(PreferencesHelper.ASK_USER, "Ask me in case of doubt",
				getFieldEditorParent());
		addField(bfe);
		DoubleScaleFieldEditor askingFieldEditor = new DoubleScaleFieldEditor(PreferencesHelper.ASK_USER_THRESHOLD,
				"Minimum percentage for asking: ", getFieldEditorParent());
		addField(askingFieldEditor);
		// TODO add disabled for ife2 and errors for incoherent thresholds
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}