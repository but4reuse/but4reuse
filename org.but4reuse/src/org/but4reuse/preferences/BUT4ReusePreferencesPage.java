package org.but4reuse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class BUT4ReusePreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BUT4ReusePreferencesPage() {
		super(GRID);
		setDescription("General settings.\nMore info at www.but4reuse.org");
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {

	}

}
