package org.but4reuse.wordclouds.preferences;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WordCloudPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String WORDCLOUD_NB_W = "WORDCLOUD_NB_WORDS";

	public WordCloudPreferences() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		IntegerFieldEditor intField = new IntegerFieldEditor(WORDCLOUD_NB_W, "Number of words", getFieldEditorParent(),
				3);
		addField(intField);
	}

}
