package org.but4reuse.feature.location.lsi.location.preferences;

import org.but4reuse.feature.location.lsi.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LSIPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	
	static final public String DIM = "DIMENSION";
	static final public String FIXED = "FIXED";
	private BooleanFieldEditor bfe;
	private ScaleFieldEditor dfe; 
	
	public LSIPreferencePage() {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createFieldEditors() {
		
		ParameterLSIFieldEditor fiel = new ParameterLSIFieldEditor(DIM, "", getFieldEditorParent(),FIXED);
		
	}

}
