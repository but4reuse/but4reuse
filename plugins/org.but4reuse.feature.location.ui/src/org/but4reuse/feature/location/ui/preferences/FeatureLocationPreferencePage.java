package org.but4reuse.feature.location.ui.preferences;

import java.util.List;

import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.helper.FeatureLocationHelper;
import org.but4reuse.utils.ui.preferences.DoubleScaleFieldEditor;
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

public class FeatureLocationPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public FeatureLocationPreferencePage() {
		super(GRID);
		setPreferenceStore(FeatureLocationHelper.getPreferenceStore());
		setDescription("Select only one");
	}

	/**
	 * Creates the field editors.
	 */
	public void createFieldEditors() {
		// Location threshold
		DoubleScaleFieldEditor scale = new DoubleScaleFieldEditor(FeatureLocationHelper.LOCATION_THRESHOLD_PREFERENCE,
				"Located feature threshold", getFieldEditorParent());
		addField(scale);
		// Selection of algorithm
		List<IFeatureLocation> algos = FeatureLocationHelper.getAllFeatureLocation();
		for (IFeatureLocation algo : algos) {
			String algoName = FeatureLocationHelper.getAlgorithmName(algo);
			BooleanFieldEditor bfe = new BooleanFieldEditor(algoName, algoName, getFieldEditorParent());
			addField(bfe);
		}
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}