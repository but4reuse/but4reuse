package org.but4reuse.adapters.emf.preferences;

import org.but4reuse.adapters.emf.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for emf adapter
 * 
 * @author jabier.martinez
 */
public class EMFAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String XML_ID_HASHING = "XML_ID_HASHING";

	public EMFAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		// This is to be used at your own risk. Use it if you are sure that the
		// matching is based on ids
		BooleanFieldEditor sfe = new BooleanFieldEditor(XML_ID_HASHING, "Hashing by the extrinsic XML ID",
				getFieldEditorParent());
		addField(sfe);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}