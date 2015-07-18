package org.but4reuse.extension.featureide.ui;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.extension.featureide.fmcreators.FeatureModelCreatorsHelper;
import org.but4reuse.extension.featureide.fmcreators.IFeatureModelCreator;
import org.but4reuse.extension.featureide.utils.FeatureIDEUtils;
import org.but4reuse.utils.files.FileUtils;
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
					"Feature Models container URI", "Insert container URI for feature models", "platform:/resource"
							+ out + "/featureModels/");
			if (inputDialog.open() != Dialog.OK) {
				return;
			}
			String constructionURI = inputDialog.getValue();
			AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

			// Get the registered fm creators
			List<IFeatureModelCreator> featureModelCreators = FeatureModelCreatorsHelper.getAllFeatureModelCreators();

			// Create fm with each of them
			for (IFeatureModelCreator fmc : featureModelCreators) {
				URI fmURI = new URI(constructionURI + fmc.getClass().getSimpleName() + ".xml");
				File fmFile = FileUtils.getFile(fmURI);
				FileUtils.createFile(fmFile);
				FeatureIDEUtils.exportFeatureModel(fmURI, adaptedModel, fmc);
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
