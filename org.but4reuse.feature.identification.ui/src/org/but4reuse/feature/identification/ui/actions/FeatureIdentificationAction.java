package org.but4reuse.feature.identification.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.blockcreation.IBlockCreationAlgorithm;
import org.but4reuse.adaptedmodel.blockcreation.IntersectionsAlgorithm;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class FeatureIdentificationAction implements IObjectActionDelegate {

	ArtefactModel artefactModel;
	List<IAdapter> adapters;
	
	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				artefactModel = ((ArtefactModel) art);
				
				// Adapter selection by user
				adapters = AdaptersSelectionDialog.show("Adapters selection", artefactModel);

				if (!adapters.isEmpty()) {
					// Launch Progress dialog
					ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent()
							.getActiveShell());
					
					try {
						progressDialog.run(true, true, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
									InterruptedException {
								
								List<IVisualisation> visualisations = VisualisationsHelper.getAllVisualisations();
								
								// Adapting each active artefact + calculating blocks + prepare visualisation
								int totalWork = AdaptersHelper.getActiveArtefacts(artefactModel).size() + 2;
								monitor.beginTask("Feature Identification", totalWork);
								
								AdaptedModel adaptedModel = AdaptedModelHelper.adapt(artefactModel, adapters, monitor);
								
								monitor.subTask("Calculating existing blocks");
								// TODO selection of block creation algorithm
								IBlockCreationAlgorithm a = new IntersectionsAlgorithm();
								List<Block> blocks = a.createBlocks(adaptedModel.getOwnedAdaptedArtefacts(), monitor);
								blocks = AdaptedModelHelper.checkBlockNames(blocks);
								
								adaptedModel.getOwnedBlocks().addAll(blocks);
								monitor.worked(1);
								
								monitor.subTask("Preparing visualisations");
								
								for(IVisualisation visualisation : visualisations){
									visualisation.prepare(null, adaptedModel, null, monitor);
									visualisation.show();
								}
								
								monitor.worked(1);
								monitor.done();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}

	ISelection selection;

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
