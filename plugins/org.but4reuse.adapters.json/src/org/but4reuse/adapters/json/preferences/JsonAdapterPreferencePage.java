package org.but4reuse.adapters.json.preferences;

import org.but4reuse.adapters.json.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JsonAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String ONLY_VALUES = "ONLY_VALUES";
	public static final String DELIMITER = "DELIMITER";
	public static final String ABSOLUTE_PATHS_TO_INCLUDE = "ABSOLUTE_PATHS_TO_INCLUDE";
	public static final String ABSOLUTE_PATHS_TO_OMIT = "ABSOLUTE_PATHS_TO_OMIT";
	public static final String RELATIVE_PATHS_TO_OMIT = "RELATIVE_PATHS_TO_OMIT";
	public static final String ABSOLUTE_PATHS_TO_IGNORE = "ABSOLUTE_PATHS_TO_IGNORE";
	public static final String RELATIVE_PATHS_TO_IGNORE = "RELATIVE_PATHS_TO_IGNORE";
	public static final String ABSOLUTE_PATHS_UNSPLITTABLE = "ABSOLUTE_PATHS_UNSPLITTABLE";
	public static final String RELATIVE_PATHS_UNSPLITTABLE = "RELATIVE_PATHS_UNSPLITTABLE ";

	public JsonAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		BooleanFieldEditor bfe = new BooleanFieldEditor(ONLY_VALUES, "Keep only Value elements",
				getFieldEditorParent());
		addField(bfe);
		StringFieldEditor sfe = new StringFieldEditor(DELIMITER,
				"Delimiter for multiple paths in these preferences\n(use one which will not be in conflict with paths): ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_TO_INCLUDE, "Root keys to exclusively include: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_TO_OMIT, "Absolute paths to completely omit: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(RELATIVE_PATHS_TO_OMIT, "Relative paths to completely omit: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_TO_IGNORE, "Absolute paths to ignore content: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(RELATIVE_PATHS_TO_IGNORE, "Relative paths to ignore content: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_UNSPLITTABLE, "Absolute paths unsplittable: ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(RELATIVE_PATHS_UNSPLITTABLE, "Relative paths to unsplittable: ",
				getFieldEditorParent());
		addField(sfe);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
