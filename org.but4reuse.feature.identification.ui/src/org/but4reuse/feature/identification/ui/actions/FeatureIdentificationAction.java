package org.but4reuse.feature.identification.ui.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.but4reuse.adapters.ui.AdaptersSelectionDialog;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.but4reuse.blockcreation.IBlockCreationAlgorithm;
import org.but4reuse.blockcreation.helper.BlockCreationHelper;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.helper.ConstraintsDiscoveryHelper;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Feature identification action
 * 
 * @author jabier.martinez
 */
public class FeatureIdentificationAction implements IObjectActionDelegate {

	ArtefactModel artefactModel;
	List<IAdapter> adapters;

	@Override
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Object art = ((IStructuredSelection) selection).getFirstElement();
			if (art instanceof ArtefactModel) {
				artefactModel = ((ArtefactModel) art);

				List<IAdapter> defaultAdapters = AdaptersHelper.getAdaptersByIds(artefactModel.getAdapters());

				// Adapter selection by user
				adapters = AdaptersSelectionDialog.show("Adapters selection", artefactModel, defaultAdapters);

				if (!adapters.isEmpty()) {
					// Launch Progress dialog
					ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent()
							.getActiveShell());

					try {
						progressDialog.run(true, true, new IRunnableWithProgress() {
							@Override
							public void run(IProgressMonitor monitor) throws InvocationTargetException,
									InterruptedException {

								// Adapting each active artefact + calculating
								// blocks + constraints discovery + prepare
								// visualisation
								int totalWork = AdaptersHelper.getActiveArtefacts(artefactModel).size() + 1 + 1
										+ VisualisationsHelper.getSelectedVisualisations().size();
								monitor.beginTask("Feature Identification", totalWork);
								AdaptedModel adaptedModel = AdaptedModelHelper.adapt(artefactModel, adapters, monitor);

								monitor.subTask("Calculating existing blocks");
								PreferencesHelper.setDeactivateManualEqualOnlyForThisTime(false);

								IBlockCreationAlgorithm a = BlockCreationHelper.getSelectedBlockCreation();
								long startTime = System.currentTimeMillis();
								List<Block> blocks = a.createBlocks(adaptedModel.getOwnedAdaptedArtefacts(), monitor);
								long stopTime = System.currentTimeMillis();
								long elapsedTime = stopTime - startTime;
								AdaptedModelManager.registerTime(
										"Block identification " + a.getClass().getSimpleName(), elapsedTime);

								blocks = AdaptedModelHelper.checkBlockNames(blocks);

								adaptedModel.getOwnedBlocks().addAll(blocks);
								monitor.worked(1);

								monitor.subTask("Constraints discovery");
								List<IConstraintsDiscovery> constraintsDiscoveryAlgorithms = ConstraintsDiscoveryHelper
										.getSelectedConstraintsDiscoveryAlgorithms();
								List<IConstraint> constraints = new ArrayList<IConstraint>();
								for (IConstraintsDiscovery constraintsDiscovery : constraintsDiscoveryAlgorithms) {
									startTime = System.currentTimeMillis();
									List<IConstraint> discovered = constraintsDiscovery.discover(null, adaptedModel,
											null, monitor);
									stopTime = System.currentTimeMillis();
									elapsedTime = stopTime - startTime;
									AdaptedModelManager.registerTime("Constraints discovery "
											+ constraintsDiscovery.getClass().getSimpleName(), elapsedTime);
									// System.out.println(elapsedTime / 1000.0);
									if (constraints.isEmpty()) {
										constraints.addAll(discovered);
									} else {
										// Only add the ones that are not
										// already there
										List<IConstraint> toBeAdded = new ArrayList<IConstraint>();
										for (IConstraint d : discovered) {
											boolean found = false;
											for (IConstraint c : constraints) {
												if (ConstraintsHelper.equalsConstraint(d, c)) {
													found = true;
													break;
												}
											}
											if (!found) {
												toBeAdded.add(d);
											}
										}
										constraints.addAll(toBeAdded);
									}
								}
								adaptedModel.setConstraints(constraints);
								monitor.worked(1);

								monitor.subTask("Preparing visualisations");
								VisualisationsHelper.notifyVisualisations(null, adaptedModel, null, monitor);

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
