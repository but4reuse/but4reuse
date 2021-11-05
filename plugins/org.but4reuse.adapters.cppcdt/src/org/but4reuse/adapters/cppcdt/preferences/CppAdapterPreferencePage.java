package org.but4reuse.adapters.cppcdt.preferences;

import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for the C++ adapter
 * 
 * @author sandu.postaru
 *
 */

public class CppAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String USE_FUNCTION_CALL_HIERARCHY = "USE FUNCTION CALL HIERARCHY";
	public static final String DOXYGEN_PATH = "DOXYGEN PATH";

	public CppAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		BooleanFieldEditor useFunctionCallHierarchy = new BooleanFieldEditor(USE_FUNCTION_CALL_HIERARCHY,
				"Enable function call hierarchy analysis", getFieldEditorParent());
		addField(useFunctionCallHierarchy);

		StringFieldEditor doxygenPath = new StringFieldEditor(DOXYGEN_PATH, "Doxygen path: ", getFieldEditorParent());
		addField(doxygenPath);

	}

	@Override
	public void init(IWorkbench workbench) {

	}

}
