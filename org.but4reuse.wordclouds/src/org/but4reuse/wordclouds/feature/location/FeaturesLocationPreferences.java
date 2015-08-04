package org.but4reuse.wordclouds.feature.location;

import org.but4reuse.utils.ui.preferences.DoubleScaleFieldEditor;
import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class FeaturesLocationPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String HIGHT_SIM = "SIM_LOCATION";

	public FeaturesLocationPreferences() {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub

		DoubleScaleFieldEditor dfe = new DoubleScaleFieldEditor(HIGHT_SIM, "Location Similarity",
				getFieldEditorParent());
		addField(dfe);
	}

}
