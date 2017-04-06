package org.but4reuse.feature.location.ui.actions;

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
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.helper.BlockIdentificationHelper;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.helper.ConstraintsDiscoveryHelper;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.LocatedFeaturesManager;
import org.but4reuse.feature.location.helper.FeatureLocationHelper;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.helpers.FeatureListHelper;
import org.but4reuse.utils.emf.EMFUtils;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Feature Location Action
 * 
 * @author jabier.martinez
 * 
 */
public class FeatureLocationAction implements IObjectActionDelegate {

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

				if (artefactModel == null) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "",
							"No artefact model is associated to this feature list");
					return;
				}

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

								// Set default output
								AdaptedModelManager.setDefaultOutput(EMFUtils.getIResource(artefactModel.eResource())
										.getParent());

								// Adapting each active artefact + calculating
								// blocks + constraints discovery + feature
								// location + prepare visualisations
								int totalWork = AdaptersHelper.getActiveArtefacts(artefactModel).size() + 1 + 1 + 1
										+ VisualisationsHelper.getSelectedVisualisations().size();
								monitor.beginTask("Feature Location", totalWork);

								AdaptedModel adaptedModel = AdaptedModelHelper.adapt(artefactModel, adapters, monitor);
								AdaptedModelManager.setFeatureList(featureList);

								monitor.subTask("Calculating existing blocks");
								PreferencesHelper.setDeactivateManualEqualOnlyForThisTime(false);
								IBlockIdentification a = BlockIdentificationHelper.getSelectedBlockIdentification();
								long startTime = System.currentTimeMillis();
								List<Block> blocks = a.identifyBlocks(adaptedModel.getOwnedAdaptedArtefacts(), monitor);
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
									List<IConstraint> discovered = constraintsDiscovery.discover(null, adaptedModel,
											null, monitor);
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

								monitor.subTask("Feature location");
								IFeatureLocation featureLocationAlgorithm = FeatureLocationHelper
										.getSelectedFeatureLocation();
								startTime = System.currentTimeMillis();
								List<LocatedFeature> locatedFeatures = featureLocationAlgorithm.locateFeatures(
										featureList, adaptedModel, monitor);
								stopTime = System.currentTimeMillis();
								elapsedTime = stopTime - startTime;
								AdaptedModelManager.registerTime("Feature location "
										+ featureLocationAlgorithm.getClass().getSimpleName(), elapsedTime);

								// get the location threshold
								double threshold = FeatureLocationHelper.getPreferenceStore().getDouble(
										FeatureLocationHelper.LOCATION_THRESHOLD_PREFERENCE);
								// realize the feature location that are greater
								// or equal to the threshold

								// initialize the located features manager
								LocatedFeaturesManager.setLocatedFeatures(new ArrayList<LocatedFeature>());
								for (LocatedFeature locatedFeature : locatedFeatures) {
									if (locatedFeature.getConfidence() >= threshold) {
										// greater than threshold so feed the
										// located features manager
										LocatedFeaturesManager.addLocatedFeature(locatedFeature);

										// TODO maybe remove and just keep the
										// manager
										// change adapted model...
										for (Block block : locatedFeature.getBlocks()) {
											if (!block.getCorrespondingFeatures().contains(locatedFeature.getFeature())) {
												block.getCorrespondingFeatures().add(locatedFeature.getFeature());
											}
										}
									}
								}
								monitor.worked(1);

								monitor.subTask("Preparing visualisations");
								VisualisationsHelper.notifyVisualisations(featureList, adaptedModel, null, monitor);

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
