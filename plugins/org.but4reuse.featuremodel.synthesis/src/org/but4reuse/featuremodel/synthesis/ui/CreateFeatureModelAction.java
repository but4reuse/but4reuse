package org.but4reuse.featuremodel.synthesis.ui;

import java.net.URI;
import java.util.List;

import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.featuremodel.synthesis.IFeatureModelSynthesis;
import org.but4reuse.featuremodel.synthesis.helper.FeatureModelSynthesisHelper;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.NullProgressMonitor;
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
public class CreateFeatureModelAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		try {
			// Get construction uri from user
			String out = "/projectName";
			IContainer output = AdaptedModelManager.getDefaultOutput();
			if (output != null) {
				out = output.getFullPath().toString();
			}
			URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
					"Feature Models container URI", "Insert container URI for feature models",
					"platform:/resource" + out + "/featureModels/");
			if (inputDialog.open() != Dialog.OK) {
				return;
			}
			String constructionString = inputDialog.getValue();
			URI constructionURI = new URI(constructionString);
			// Get the selected fm creators
			List<IFeatureModelSynthesis> featureModelCreators = FeatureModelSynthesisHelper
					.getSelectedFeatureModelCreators();

			// Create fm with each of them
			for (IFeatureModelSynthesis fmc : featureModelCreators) {
				fmc.createFeatureModel(constructionURI, new NullProgressMonitor());
			}

			// Refresh
			if (output != null) {
				WorkbenchUtils.refreshIResource(output);
			} else {
				WorkbenchUtils.refreshAllWorkspace();
			}

		} catch (Exception e) {
			e.printStackTrace();
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
