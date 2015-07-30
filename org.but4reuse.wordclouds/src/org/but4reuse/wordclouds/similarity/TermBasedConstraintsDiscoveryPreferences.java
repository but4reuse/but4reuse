package org.but4reuse.wordclouds.similarity;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TermBasedConstraintsDiscoveryPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String REQUIRES = "REQUIRES_WC";
	public static final String EXCLUDES = "EXCLUDES_WC";
	public static final String HIGHT_SIM = "HIGHT_SIMILARITY_WC";
	
	public TermBasedConstraintsDiscoveryPreferences()
	{
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createFieldEditors() {
		// TODO Auto-generated method stub
		BooleanFieldEditor bfeR = new BooleanFieldEditor(REQUIRES, "Requires", getFieldEditorParent());
		addField(bfeR);
		BooleanFieldEditor bfeE = new BooleanFieldEditor(EXCLUDES, "Mutual exclusion", getFieldEditorParent());
		addField(bfeE);
		DoubleFieldEditor dfe = new DoubleFieldEditor(HIGHT_SIM,"Location Similarity",getFieldEditorParent(),Activator.getDefault());
		addField(dfe);
	}

}
