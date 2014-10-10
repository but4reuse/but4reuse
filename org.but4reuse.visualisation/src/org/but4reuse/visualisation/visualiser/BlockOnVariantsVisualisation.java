package org.but4reuse.visualisation.visualiser;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;

public class BlockOnVariantsVisualisation implements IVisualisation {

	@Override
	public void show(AdaptedModel adaptedModel) {
//		Visualiser view = (Visualiser) WorkbenchUtils
//				.forceShowView("org.eclipse.contribution.visualiser.views.Visualiser");
//		Menu menu = (Menu) WorkbenchUtils
//				.forceShowView("org.eclipse.contribution.visualiser.views.Menu");

		for(ProviderDefinition definition : VisualiserPlugin.getProviderManager().getAllProviderDefinitions()){
			if (definition.getID().equals("org.but4reuse.visualisation.blocks.provider")){
				BlockContentProvider contentProvider = (BlockContentProvider)definition.getContentProvider();
				BlockMarkupProvider markupProvider = (BlockMarkupProvider)definition.getMarkupInstance();
				// creates the blocks on the menu
				markupProvider.update(adaptedModel);
				// fill the variants and add the stripes
				contentProvider.update(adaptedModel);
				
				//VisualiserPlugin.getProviderManager().setCurrent(definition);
				//VisualiserPlugin.refresh();
				break;
			}
		}
	}

}
