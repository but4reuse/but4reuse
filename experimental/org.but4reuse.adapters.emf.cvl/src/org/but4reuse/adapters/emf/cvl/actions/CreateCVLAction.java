package org.but4reuse.adapters.emf.cvl.actions;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.emf.cvl.CVLModelsExtractor;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.core.resources.IContainer;
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
		String out = "/projectName";
		IContainer output = AdaptedModelManager.getDefaultOutput();
		if (output != null) {
			out = output.getFullPath().toString();
		}
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
				"CVL container URI", "Insert container URI for CVL model", "platform:/resource" + out + "/cvl/");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}
		String constructionURI = inputDialog.getValue();
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

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
