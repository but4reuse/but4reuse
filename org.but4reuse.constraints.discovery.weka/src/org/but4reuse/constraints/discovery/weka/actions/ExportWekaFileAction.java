package org.but4reuse.constraints.discovery.weka.actions;

import java.io.File;
import java.net.URI;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.constraints.discovery.weka.utils.WekaUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import weka.core.Instances;

/**
 * Build weka file
 * 
 * @author jabier.martinez
 */
public class ExportWekaFileAction implements IViewActionDelegate {

	@Override
	public void run(IAction action) {
		// Get output uri from user
		String out = "/projectName";
		IContainer output = AdaptedModelManager.getDefaultOutput();
		if (output != null) {
			out = output.getFullPath().toString();
		}
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
				"Weka arff file URI", "Insert folder URI for the weka file", "platform:/resource" + out + "/weka.arff");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}

		String constructionURI = inputDialog.getValue();
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

		// Create instances
		Instances instances = WekaUtils.createInstances(adaptedModel);

		// Save it and refresh
		try {
			URI uri = new URI(constructionURI);
			File file = FileUtils.getFile(uri);
			WekaUtils.save(file, instances);
			IResource res = WorkbenchUtils.getIResourceFromURI(uri);
			if (res != null) {
				WorkbenchUtils.refreshIResource(res);
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

	}

}
