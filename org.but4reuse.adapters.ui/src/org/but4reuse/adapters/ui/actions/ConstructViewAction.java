package org.but4reuse.adapters.ui.actions;

import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.visualisation.visualiser.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.BlockElementsOnArtefactsVisualisation;
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
 * Construction action
 * 
 * @author jabier.martinez
 */
public class ConstructViewAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {

		// Get construction uri from user
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
				"Construction URI", "Insert Construction URI", "platform:/resource/projectName/");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}
		String constructionURI = inputDialog.getValue();

		// Construct
		ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();
		// TODO construct
		for (Object o : markupProvider.getAllMarkupKinds()) {
			IMarkupKind kind = (IMarkupKind) o;
			if (menu.getActive(kind)) {
				Object active = kind;
				System.out.println(active + " " + constructionURI);
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {

	}

	@Override
	public void init(IViewPart view) {
		menu = (Menu) view;
	}

}
