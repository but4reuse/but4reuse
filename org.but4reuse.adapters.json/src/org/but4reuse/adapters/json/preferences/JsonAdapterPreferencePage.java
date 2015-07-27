package org.but4reuse.adapters.json.preferences;

import org.but4reuse.adapters.json.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JsonAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public JsonAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		StringFieldEditor sfe = new StringFieldEditor("DELIMITER",
				"Delimiter for multiple paths\n(choose one which will not be in conflict with paths) : ",
				getFieldEditorParent());
		addField(sfe);
		StringFieldEditor sfe2 = new StringFieldEditor("PATHS", "Paths to ignore : ", getFieldEditorParent());
		addField(sfe2);
		BooleanFieldEditor bfe = new BooleanFieldEditor("OBJECT", "Prevent from object splitting",
				getFieldEditorParent());
		addField(bfe);
		BooleanFieldEditor bfe2 = new BooleanFieldEditor("ARRAY", "Prevent from array splitting",
				getFieldEditorParent());
		addField(bfe2);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
