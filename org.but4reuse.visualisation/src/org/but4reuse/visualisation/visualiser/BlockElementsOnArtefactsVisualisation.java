package org.but4reuse.visualisation.visualiser;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.swt.widgets.Display;

public class BlockElementsOnArtefactsVisualisation implements IVisualisation {

	@Override
	public void show(AdaptedModel adaptedModel) {

		final ProviderDefinition definition = getBlockElementsOnVariantsProvider();
		BlockElementsContentProvider contentProvider = (BlockElementsContentProvider) definition.getContentProvider();
		final BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();
		// reset
		contentProvider.reset();
		markupProvider.reset();
		// creates the blocks on the menu
		markupProvider.update(adaptedModel);
		// fill the variants and add the stripes
		contentProvider.update(adaptedModel);

		// asyncExec to avoid SWT invalid thread access
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// Set only this visualiser as the current one
				for (ProviderDefinition definition2 : ProviderManager.getAllProviderDefinitions()) {
					if(definition2!=definition){
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

	/**
	 * Get the visualisation provider
	 * 
	 * @return this visualisation provider
	 */
	public static ProviderDefinition getBlockElementsOnVariantsProvider() {
		for (ProviderDefinition definition : ProviderManager.getAllProviderDefinitions()) {
			if (definition.getID().equals("org.but4reuse.visualisation.blockelementsonartefacts.provider")) {
				return definition;
			}
		}
		return null;
	}

}
