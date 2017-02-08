package org.but4reuse.block.identification.ui.preferences;

import java.util.List;

import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.helper.BlockIdentificationHelper;
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

public class BlockIdentificationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BlockIdentificationPreferencePage() {
		super(GRID);
		setPreferenceStore(BlockIdentificationHelper.getPreferenceStore());
		setDescription("Select only one");
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		List<IBlockIdentification> algos = BlockIdentificationHelper.getAllBlockIdentificationAlgorithms();
		for (IBlockIdentification algo : algos) {
			String algoName = BlockIdentificationHelper.getAlgorithmName(algo);
			BooleanFieldEditor bfe = new BooleanFieldEditor(algoName, algoName, getFieldEditorParent());
			addField(bfe);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}