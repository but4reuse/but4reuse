package org.but4reuse.constraints.discovery.weka.actions;

import java.io.File;
import java.net.URI;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.constraints.discovery.weka.utils.WekaUtils;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.ui.dialogs.URISelectionDialog;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
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
		URISelectionDialog inputDialog = new URISelectionDialog(Display.getCurrent().getActiveShell(),
				"Weka arff file URI", "Insert folder URI for the weka file",
				"platform:/resource/projectName/weka.arff");
		if (inputDialog.open() != Dialog.OK) {
			return;
		}

		String constructionURI = inputDialog.getValue();

		ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();

		// get the adaptedModel
		AdaptedModel adaptedModel = null;
		for (Object o : markupProvider.getAllMarkupKinds()) {
			IMarkupKind kind = (IMarkupKind) o;
			Object active = kind;
			if (active instanceof BlockMarkupKind) {
				Block block = ((BlockMarkupKind) active).getBlock();
				adaptedModel = (AdaptedModel) block.eContainer();
				break;
			}
		}

		// Create instances
		Instances instances = WekaUtils.createInstances(adaptedModel);
		
		// Save it and refresh
		try {
			URI uri = new URI(constructionURI);
			File file = FileUtils.getFile(uri);
			WekaUtils.save(file,instances);
			IResource res = WorkbenchUtils.getIResourceFromURI(uri);
			if(res!=null){
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
