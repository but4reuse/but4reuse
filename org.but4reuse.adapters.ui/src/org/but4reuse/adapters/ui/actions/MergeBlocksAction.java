package org.but4reuse.adapters.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.helpers.VisualisationsHelper;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsMarkupProvider;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockElementsOnArtefactsVisualisation;
import org.but4reuse.visualisation.visualiser.adaptedmodel.BlockMarkupKind;
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
						List<BlockElement> toBeAdded = new ArrayList<BlockElement>();
						// get its elements and then remove it
						for (BlockElement be : block.getOwnedBlockElements()) {
							if (!blockToKeep.getOwnedBlockElements().contains(be)) {
								toBeAdded .add(be);
							}
						}
						blockToKeep.getOwnedBlockElements().addAll(toBeAdded);
						
						// TODO redirect block assigned to feature. Maybe the thing to do is to recalculate!
						List<Feature> toBeAddedFeatures = new ArrayList<Feature>();
						List<Feature> features = block.getCorrespondingFeatures();
						for(Feature f : features){
							if(!blockToKeep.getCorrespondingFeatures().contains(f)){
								toBeAddedFeatures.add(f);
							}
						}
						blockToKeep.getCorrespondingFeatures().addAll(toBeAddedFeatures);
						
						// TODO redirect constraints. Maybe the thing to do is to recalculate!
						List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
						for(IConstraint c : constraints){
							if(c.getBlock1().equals(block)){
								c.setBlock1(blockToKeep);
							}
							if(c.getBlock2().equals(block)){
								c.setBlock2(blockToKeep);
							}
						}
						
						// Remove the block
						adaptedModel.getOwnedBlocks().remove(block);
					}
				}
			}
		}
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
