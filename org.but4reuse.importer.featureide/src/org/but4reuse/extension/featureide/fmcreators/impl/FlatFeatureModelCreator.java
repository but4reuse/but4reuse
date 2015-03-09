package org.but4reuse.extension.featureide.fmcreators.impl;

import java.util.LinkedList;

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
 * Flat feature model creator
 * @author jabier.martinez
 */
public class FlatFeatureModelCreator implements IFeatureModelCreator {

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
		
		LinkedList<Feature> children = new LinkedList<Feature>();
		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			f.setAbstract(false);
			f.setMandatory(false);
			f.setParent(root);
			children.add(f);
			fm.addFeature(f);
		}
		root.setChildren(children);
		
		// Add constraints
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}
		return fm;
	}

}
