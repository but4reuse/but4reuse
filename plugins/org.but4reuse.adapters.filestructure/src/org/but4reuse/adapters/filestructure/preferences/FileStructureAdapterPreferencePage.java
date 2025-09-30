package org.but4reuse.adapters.filestructure.preferences;

import org.but4reuse.adapters.filestructure.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for file structure adapter
 * 
 * @author jabier.martinez
 */
public class FileStructureAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String CONTENT_CHECK = "CONTENT_CHECK";
	public static final String IGNORE_FOLDERS = "IGNORE_FOLDERS";
	public static final String INCLUDE_ONLY_EXTENSIONS = "INCLUDE_ONLY_EXTENSIONS";

	public FileStructureAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		BooleanFieldEditor bfe = new BooleanFieldEditor(CONTENT_CHECK,
				"Similarity: Apart from relative paths, use also checksums with MD5", getFieldEditorParent());
		addField(bfe);
		BooleanFieldEditor bfe2 = new BooleanFieldEditor(IGNORE_FOLDERS, "Ignore folders", getFieldEditorParent());
		addField(bfe2);
		StringFieldEditor sfe = new StringFieldEditor(INCLUDE_ONLY_EXTENSIONS,
				"Include only extensions (keep empty for all, use comma as separator): ",
				getFieldEditorParent());
		addField(sfe);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}