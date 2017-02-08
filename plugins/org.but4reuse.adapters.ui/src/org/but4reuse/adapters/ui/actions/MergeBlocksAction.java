package org.but4reuse.adapters.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.helper.ConstraintsDiscoveryHelper;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.impl.FeatureSpecificHeuristicFeatureLocation;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.impl.visualiser.adaptedmodel.BlockMarkupKind;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Merge selected blocks
 * 
 * @author jabier.martinez
 */
public class MergeBlocksAction implements IViewActionDelegate {

	Menu menu;

	@Override
	public void run(IAction action) {
		ProviderDefinition definition = BlockElementsOnArtefactsVisualisation.getBlockElementsOnVariantsProvider();
		BlockElementsMarkupProvider markupProvider = (BlockElementsMarkupProvider) definition.getMarkupInstance();

		Block blockToKeep = null;
		AdaptedModel adaptedModel = null;
		FeatureList featureModel = null;
		for (Object o : markupProvider.getAllMarkupKinds()) {
			IMarkupKind kind = (IMarkupKind) o;
			if (menu.getActive(kind)) {
				// get the text of the Block elements
				if (kind instanceof BlockMarkupKind) {
					BlockMarkupKind markupKind = (BlockMarkupKind) kind;
					Block block = markupKind.getBlock();
					if (adaptedModel == null) {
						adaptedModel = (AdaptedModel) block.eContainer();
					}
					// first one is the block to keep
					if (blockToKeep == null) {
						blockToKeep = block;
					} else {
						List<BlockElement> toBeAddedElements = new ArrayList<BlockElement>();
						// get its elements and then remove it
						for (BlockElement be : block.getOwnedBlockElements()) {
							if (!blockToKeep.getOwnedBlockElements().contains(be)) {
								toBeAddedElements.add(be);
							}
						}
						blockToKeep.getOwnedBlockElements().addAll(toBeAddedElements);

						adaptedModel.getOwnedBlocks().remove(block);

						// Redirect block assigned to feature. Clear and
						// recalculate
						// TODO refactoring code, this is from feature
						// identification and location
						for (Block blockToClear : adaptedModel.getOwnedBlocks()) {
							if (!blockToClear.getCorrespondingFeatures().isEmpty()) {
								// TODO find a better way to get feature model
								if (featureModel == null) {
									featureModel = (FeatureList) blockToClear.getCorrespondingFeatures().get(0)
											.eContainer();
								}
								blockToClear.getCorrespondingFeatures().clear();
							}

						}
						if (featureModel != null) {
							IFeatureLocation featureLocationAlgorithm = new FeatureSpecificHeuristicFeatureLocation();
							featureLocationAlgorithm.locateFeatures(featureModel, adaptedModel,
									new NullProgressMonitor());
						}

						// Constraints! Clear and recalculate
						// TODO refactoring code, this is from feature
						// identification and location
						adaptedModel.setConstraints(null);
						List<IConstraintsDiscovery> constraintsDiscoveryAlgorithms = ConstraintsDiscoveryHelper
								.getSelectedConstraintsDiscoveryAlgorithms();
						List<IConstraint> constraints = new ArrayList<IConstraint>();
						for (IConstraintsDiscovery constraintsDiscovery : constraintsDiscoveryAlgorithms) {
							List<IConstraint> discovered = constraintsDiscovery.discover(null, adaptedModel, null,
									new NullProgressMonitor());
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
					}
				}
			}
		}
		// Changes were made, notify
		if (blockToKeep != null) {
			VisualisationsHelper.notifyVisualisations(featureModel, adaptedModel, null, new NullProgressMonitor());
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
