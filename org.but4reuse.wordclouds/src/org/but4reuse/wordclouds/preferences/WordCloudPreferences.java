package org.but4reuse.wordclouds.preferences;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WordCloudPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String WORDCLOUD_NB_W = "WORDCLOUD_NB_WORDS";
	public static final String STOP_WORDS = "STOP_WORDS";
	public static final String MULTI_WORDS = "MULTI_WORDS";
	public static final String SYNONYM_WORDS = "SYNONYM_WORDS";
	
	public WordCloudPreferences() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		// max of words
		IntegerFieldEditor intField = new IntegerFieldEditor(WORDCLOUD_NB_W, "Number of words", getFieldEditorParent(),
				3);
		addField(intField);
		
		// stop words
		StringFieldEditor sfe = new StringFieldEditor(STOP_WORDS, "Stop words (comma separated): ", getFieldEditorParent());
		addField(sfe);
		
		// multi words
		StringFieldEditor sfe2 = new StringFieldEditor(MULTI_WORDS, "Multi words (comma separated): ", getFieldEditorParent());
		addField(sfe2);
		
		// synonym words
		StringFieldEditor sfe3 = new StringFieldEditor(SYNONYM_WORDS, "Synonyms (pairs of words comma separated, first will be kept): ", getFieldEditorParent());
		addField(sfe3);
	}

}
