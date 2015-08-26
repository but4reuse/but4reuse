package org.but4reuse.adapters.json.preferences;

import org.but4reuse.adapters.json.activator.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JsonAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DELIMITER = "DELIMITER";
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
		StringFieldEditor sfe = new StringFieldEditor(DELIMITER,
				"Delimiter for multiple paths\n(choose one which will not be in conflict with paths) : ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_TO_IGNORE, "Absolute paths to ignore : ", getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(RELATIVE_PATHS_TO_IGNORE, "Relative paths to ignore : ", getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(ABSOLUTE_PATHS_UNSPLITTABLE, "Absolute paths unsplittable : ",
				getFieldEditorParent());
		addField(sfe);
		sfe = new StringFieldEditor(RELATIVE_PATHS_UNSPLITTABLE, "Relative paths to unsplittable : ",
				getFieldEditorParent());
		addField(sfe);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
