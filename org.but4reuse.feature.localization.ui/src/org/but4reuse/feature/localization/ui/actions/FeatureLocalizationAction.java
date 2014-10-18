package org.but4reuse.feature.localization.ui.actions;

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
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.helpers.FeatureListHelper;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.visualiser.featurelist.FeaturesOnBlocksVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class FeatureLocalizationAction implements IObjectActionDelegate {

	FeatureList featureList;
	List<IAdapter> adapters;
	ArtefactModel artefactModel;
	
	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object featureListObject = ((IStructuredSelection) selection).getFirstElement();
			if (featureListObject instanceof FeatureList) {
				featureList = ((FeatureList) featureListObject);
				
				artefactModel = FeatureListHelper.getArtefactModel(featureList);
				
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

								// Adapting each active artefact + prepare visualisation
								int totalWork = AdaptersHelper.getActiveArtefacts(artefactModel).size() + 1;
								monitor.beginTask("Feature Identification", totalWork);
								
								AdaptedModel adaptedModel = AdaptedModelHelper.adapt(artefactModel, adapters, monitor);
								
								monitor.subTask("Calculating existing blocks");
								// TODO selection of block creation algorithm
								IBlockCreationAlgorithm a = new IntersectionsAlgorithm();
								List<Block> blocks = a.createBlocks(adaptedModel.getOwnedAdaptedArtefacts(), monitor);
								blocks = AdaptedModelHelper.checkBlockNames(blocks);
								adaptedModel.getOwnedBlocks().addAll(blocks);
								
								monitor.subTask("Preparing visualisations");
								// TODO selection of visualisations
								IVisualisation s = new BlockElementsOnArtefactsVisualisation();
								s.prepare(null, adaptedModel, null, monitor);
								s.show();
								
								IVisualisation s2 = new FeaturesOnBlocksVisualisation();
								s2.prepare(featureList, adaptedModel, null, monitor);
								s2.show();
								
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
