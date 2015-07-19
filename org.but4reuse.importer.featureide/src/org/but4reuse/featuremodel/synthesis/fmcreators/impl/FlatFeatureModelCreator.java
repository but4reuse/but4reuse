package org.but4reuse.featuremodel.synthesis.fmcreators.impl;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featuremodel.synthesis.fmcreators.IFeatureModelCreator;
import org.but4reuse.featuremodel.synthesis.utils.FeatureIDEUtils;
import org.but4reuse.utils.files.FileUtils;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;

/**
 * Flat feature model creator
 * 
 * @author jabier.martinez
 */
public class FlatFeatureModelCreator implements IFeatureModelCreator {

	@Override
	public void createFeatureModel(URI outputContainer) {
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		FeatureModel fm = new FeatureModel();
		Feature root = new Feature(fm);

		String rootName = AdaptedModelHelper.getName(adaptedModel);
		if (rootName == null) {
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

		// Save
		try {
			URI fmURI = new URI(outputContainer + this.getClass().getSimpleName() + ".xml");
			File fmFile = FileUtils.getFile(fmURI);
			FileUtils.createFile(fmFile);
			FeatureIDEUtils.save(fm, fmFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
