package org.but4reuse.adapters.graphs.preferences;

import org.but4reuse.adapters.graphs.activator.Activator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page for graphs adapter
 * 
 * @author jabier.martinez
 */
public class GraphsAdapterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String NODE_ID = "NODE ID";
	public static final String EDGE_ID = "EDGE ID";

	public GraphsAdapterPreferencePage() {
		super(GRID);
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		StringFieldEditor sfe = new StringFieldEditor(NODE_ID, "Node id (keep it empty to use default id):",
				getFieldEditorParent());
		addField(sfe);
		StringFieldEditor sfe2 = new StringFieldEditor(EDGE_ID, "Edge id (keep it empty to use default id):",
				getFieldEditorParent());
		addField(sfe2);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}