package org.but4reuse.adapters.emf.cvl.actions;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.emf.cvl.CVLModelsExtractor;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Create Feature Model action
 * 
 * @author jabier.martinez
 */
public class CreateCVLAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
			// Get construction uri from user
			URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
					"CVL container URI", "Insert container URI for CVL model", "platform:/resource/projectName/cvl/");
			if (inputDialog.open() != Dialog.OK) {
				return;
			}
			String constructionURI = inputDialog.getValue();

			// TODO break the dependency with this visualisation
			// Get the adaptedModel
			ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
			BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();

			AdaptedModel adaptedModel = null;
			for (Object o : markupProvider.getAllMarkupKinds()) {
				IMarkupKind kind = (IMarkupKind) o;
				if (kind instanceof BlockMarkupKind) {
					Block block = ((BlockMarkupKind) kind).getBlock();
					adaptedModel = (AdaptedModel) block.eContainer();
					break;
				}
			}

			// Call the extractor
			CVLModelsExtractor.createCVLModels(constructionURI, adaptedModel);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}

}
