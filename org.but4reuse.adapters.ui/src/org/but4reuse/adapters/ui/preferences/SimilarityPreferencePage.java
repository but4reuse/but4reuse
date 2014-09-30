package org.but4reuse.adapters.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.but4reuse.adapters.ui.activator.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class SimilarityPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public static final String AUTOMATIC_EQUAL_THRESHOLD = "automatic_threshold";
	public static final String ASK_USER_THRESHOLD = "ask_user_threshold";
	public static final String ASK_USER = "ask_user";

	public SimilarityPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		// setDescription("Similarity");
	}
	
	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		IntegerFieldEditor ife = new IntegerFieldEditor(AUTOMATIC_EQUAL_THRESHOLD, "Minimum percentage for automatic equal: ",  getFieldEditorParent(), 3);
		ife.setValidRange(0, 100);
		addField(ife);
		BooleanFieldEditor bfe = new BooleanFieldEditor(ASK_USER,"Ask me in case of doubt",getFieldEditorParent());
		addField(bfe);
		IntegerFieldEditor ife2 = new IntegerFieldEditor(ASK_USER_THRESHOLD, "Minimum percentage for asking: ",  getFieldEditorParent(), 3);
		ife2.setValidRange(0, 100);
		addField(ife2);
		// TODO ask a RangeSlider for automatic and "ask the user" or at least add disabled for ife2 and errors for incoherent thresholds
	}

	@Override
	public void init(IWorkbench workbench) {
	}
	
}