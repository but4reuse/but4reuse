package org.but4reuse.feature.location.spectrum.preferences;

import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.location.spectrum.RankingMetrics;
import org.but4reuse.feature.location.spectrum.activator.Activator;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import fk.stardust.localizer.IFaultLocalizer;

/**
 * Spectrum preference page
 * 
 * @author jabier.martinez
 */
public class SpectrumPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String RANKING_METRIC = "RANKING_METRIC";

	public SpectrumPreferencePage() {
		super();
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		// A combo to select the ranking metric
		List<IFaultLocalizer<Block>> rankingMetrics = RankingMetrics.getAllRankingMetrics();
		String[][] entryNamesAndValues = new String[rankingMetrics.size()][2];
		for (int i = 0; i < rankingMetrics.size(); i++) {
			entryNamesAndValues[i][0] = rankingMetrics.get(i).getName();
			entryNamesAndValues[i][1] = entryNamesAndValues[i][0];
		}
		ComboFieldEditor field = new ComboFieldEditor(RANKING_METRIC, "Ranking metric:", entryNamesAndValues,
				getFieldEditorParent());
		addField(field);
	}

}
