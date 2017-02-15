package org.but4reuse.wordclouds.preferences;

import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.filters.IWordsProcessing;
import org.but4reuse.wordclouds.filters.WordCloudFiltersHelper;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Word cloud preferences
 * 
 * @author jabier.martinez
 */
public class WordCloudPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String WORDCLOUD_NB_W = "WORDCLOUD_NB_WORDS";
	public static final String STOP_WORDS = "STOP_WORDS";
	public static final String MULTI_WORDS = "MULTI_WORDS";
	public static final String SYNONYM_WORDS = "SYNONYM_WORDS";
	public static final String AUTORENAME_NB_WORDS = "AUTORENAME_NB_WORDS";
	public static final String AUTORENAME_KEEP_PREVIOUS = "AUTORENAME_KEEP_PREVIOUS";

	public WordCloudPreferences() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		setDescription(
				"Filters: 0 means that they are not activated. Order is important, put bigger numbers for the ones that you want to be executed first. We agree that this user interface should be improved.");
	}

	@Override
	protected void createFieldEditors() {

		// priority for filters
		for (IWordsProcessing filter : WordCloudFiltersHelper.getAllFilters()) {
			String name = WordCloudFiltersHelper.getFilterName(filter);
			IntegerFieldEditor prio = new IntegerFieldEditor(name, name, getFieldEditorParent());
			addField(prio);
		}

		Label label = new Label(getFieldEditorParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));

		// max of words
		IntegerFieldEditor intField = new IntegerFieldEditor(WORDCLOUD_NB_W, "Number of words in the word cloud",
				getFieldEditorParent(), 3);
		addField(intField);

		// words for automatic renaming
		IntegerFieldEditor intAutoField = new IntegerFieldEditor(AUTORENAME_NB_WORDS, "Words for automatic renaming",
				getFieldEditorParent(), 3);
		addField(intAutoField);

		// Keep previous block name
		BooleanFieldEditor keepPreviousName = new BooleanFieldEditor(AUTORENAME_KEEP_PREVIOUS,
				"Keep previous name for automatic renaming", getFieldEditorParent());
		addField(keepPreviousName);

		// stop words
		StringFieldEditor sfe = new StringFieldEditor(STOP_WORDS, "Stop words (comma separated): ",
				getFieldEditorParent());
		addField(sfe);

		// multi words
		StringFieldEditor sfe2 = new StringFieldEditor(MULTI_WORDS, "Multi words (comma separated): ",
				getFieldEditorParent());
		addField(sfe2);

		// synonym words
		StringFieldEditor sfe3 = new StringFieldEditor(SYNONYM_WORDS,
				"Synonyms (pairs of words 'house home' comma separated, first will be kept): ", getFieldEditorParent());
		addField(sfe3);
	}

}
