package org.but4reuse.extension.featureide.fmcreators.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.extension.featureide.IFeatureModelCreator;
import org.but4reuse.extension.featureide.utils.FeatureIDEUtils;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;

/**
 * TODO not finished
 * @author jabier.martinez
 */
public class SimpleHeuristicFeatureModelCreator implements IFeatureModelCreator {

	@Override
	public FeatureModel createFeatureModel(AdaptedModel adaptedModel) {
		FeatureModel fm = new FeatureModel();
		Feature root = new Feature(fm);
		String rootName = AdaptedModelHelper.getName(adaptedModel);
		if(rootName == null){
			rootName = "FeatureModel";
		} else {
			rootName = FeatureIDEUtils.validFeatureName(rootName);
		}
		root.setName(rootName);
		root.setAND(true);
		
		fm.setRoot(root);
		fm.addFeature(root);
		
		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			f.setAbstract(false);
			f.setMandatory(false);
			f.setParent(root);
			fm.addFeature(f);
		}
		
		// Add constraints, maybe redundant after hierarchical fm creation but it is better to keep them if the user wants to move them
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}
		
		List<List<Feature>> altGroups = new ArrayList<List<Feature>>();
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			if(constraint.getType().equals(IConstraint.EXCLUDES)){
				Feature feature1 = fm.getFeature(FeatureIDEUtils.validFeatureName(constraint.getBlock1().getName()));
				Feature feature2 = fm.getFeature(FeatureIDEUtils.validFeatureName(constraint.getBlock2().getName()));
				// any of them exists in previous?
				
			}
		}
		return fm;
	}

}
