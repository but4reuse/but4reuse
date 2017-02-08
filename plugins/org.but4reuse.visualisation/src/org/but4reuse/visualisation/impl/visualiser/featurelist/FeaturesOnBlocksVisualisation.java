package org.but4reuse.visualisation.impl.visualiser.featurelist;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

public class FeaturesOnBlocksVisualisation implements IVisualisation {

	ProviderDefinition definition;
	FeaturesMarkupProvider markupProvider;
	boolean show;

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		// Nothing if feature list is null
		if (featureList == null) {
			show = false;
			return;
		}
		show = true;
		definition = getFeaturesOnBlocksProvider();
		BlocksOnFeaturesContentProvider contentProvider = (BlocksOnFeaturesContentProvider) definition
				.getContentProvider();
		markupProvider = (FeaturesMarkupProvider) definition.getMarkupInstance();
		// reset
		contentProvider.reset();
		markupProvider.reset();
		// creates the blocks on the menu
		markupProvider.update(featureList);
		// fill the blocks and add the stripes
		contentProvider.update(featureList, adaptedModel);

	}

	/**
	 * Get the visualisation provider
	 * 
	 * @return this visualisation provider
	 */
	public static ProviderDefinition getFeaturesOnBlocksProvider() {
		for (ProviderDefinition definition : ProviderManager.getAllProviderDefinitions()) {
			if (definition.getID().equals("org.but4reuse.visualisation.featuresonblocks.provider")) {
				return definition;
			}
		}
		return null;
	}

	@Override
	public void show() {
		if (!show) {
			return;
		}
		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Set only this visualiser as the current one
				for (ProviderDefinition definition2 : ProviderManager.getAllProviderDefinitions()) {
					if (definition2 != definition) {
						definition2.setEnabled(false);
					}
				}
				definition.setEnabled(true);

				// Otherwise Block 0 is not White
				markupProvider.resetColours();

				// Force to show the visualiser views if not shown
				WorkbenchUtils.forceShowView("org.eclipse.contribution.visualiser.views.Visualiser");
				WorkbenchUtils.forceShowView("org.eclipse.contribution.visualiser.views.Menu");

				// Refresh
				VisualiserPlugin.refresh();
			}
		});
	}

}
