package org.but4reuse.adapters.ui.preferences;

import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class SimilarityPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public SimilarityPreferencePage() {
		super(GRID);
		setPreferenceStore(PreferencesHelper.getPreferenceStore());
		// setDescription("Similarity");
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		// equal threshold
		IntegerFieldEditor ife = new IntegerFieldEditor(PreferencesHelper.AUTOMATIC_EQUAL_THRESHOLD,
				"Minimum percentage for automatic equal: ", getFieldEditorParent(), 3);
		ife.setValidRange(0, 100);
		addField(ife);
		// threshold to ask users. manual similarity
		BooleanFieldEditor bfe = new BooleanFieldEditor(PreferencesHelper.ASK_USER, "Ask me in case of doubt",
				getFieldEditorParent());
		addField(bfe);
		IntegerFieldEditor ife2 = new IntegerFieldEditor(PreferencesHelper.ASK_USER_THRESHOLD,
				"Minimum percentage for asking: ", getFieldEditorParent(), 3);
		ife2.setValidRange(0, 100);
		addField(ife2);
		// TODO ask a RangeSlider for automatic and "ask the user" or at least
		// add disabled for ife2 and errors for incoherent thresholds
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}