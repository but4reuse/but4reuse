package org.but4reuse.adapters.emf.preferences;

import org.but4reuse.adapters.emf.activator.Activator;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for emf adapter
 * 
 * @author jabier.martinez
 */
public class EMFAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String XML_ID_HASHING = "XML_ID_HASHING";

	public static final String MATCH_ID_HASHING = "MATCH_ID_HASHING";

	public static final String COMPARISON_METHOD = "COMPARISON_METHOD";

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
		BooleanFieldEditor bfe_xml = new BooleanFieldEditor(XML_ID_HASHING, "Hashing by the extrinsic XML ID",
				getFieldEditorParent());
		addField(bfe_xml);
		BooleanFieldEditor bfe_match = new BooleanFieldEditor(MATCH_ID_HASHING,
				"Hashing by the Match ID of the Comparison method", getFieldEditorParent());
		addField(bfe_match);
		StringFieldEditor sfe_comp = new StringFieldEditor(COMPARISON_METHOD,
				"Comparison method name (empty for using the first applicable one)", getFieldEditorParent());
		addField(sfe_comp);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}