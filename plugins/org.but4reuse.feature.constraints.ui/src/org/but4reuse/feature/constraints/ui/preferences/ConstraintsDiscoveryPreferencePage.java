package org.but4reuse.feature.constraints.ui.preferences;

import java.util.List;

import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.helper.ConstraintsDiscoveryHelper;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class ConstraintsDiscoveryPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public ConstraintsDiscoveryPreferencePage() {
		super(GRID);
		setPreferenceStore(ConstraintsDiscoveryHelper.getPreferenceStore());
		setDescription("Select");
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		List<IConstraintsDiscovery> algos = ConstraintsDiscoveryHelper.getAllConstraintsDiscoveryAlgorithms();
		for (IConstraintsDiscovery algo : algos) {
			String algoName = ConstraintsDiscoveryHelper.getAlgorithmName(algo);
			BooleanFieldEditor bfe = new BooleanFieldEditor(algoName, algoName, getFieldEditorParent());
			addField(bfe);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}