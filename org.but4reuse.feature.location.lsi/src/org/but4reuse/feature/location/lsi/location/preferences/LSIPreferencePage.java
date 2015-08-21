package org.but4reuse.feature.location.lsi.location.preferences;

import org.but4reuse.feature.location.lsi.activator.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LSIPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	static final public String DIM = "NB_DIMENSION";
	static final public String FIXED = "IS_FIXED";
	private ParameterLSIFieldEditor field;

	public LSIPreferencePage() {
		super();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

		field = new ParameterLSIFieldEditor(DIM, " ", getFieldEditorParent(), FIXED);
		addField(field);
	}

}
