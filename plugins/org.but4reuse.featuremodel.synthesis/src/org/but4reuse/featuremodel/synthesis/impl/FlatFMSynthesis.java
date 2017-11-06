package org.but4reuse.featuremodel.synthesis.impl;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featuremodel.synthesis.IFeatureModelSynthesis;
import org.but4reuse.featuremodel.synthesis.utils.FeatureIDEUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.IFeatureModel;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

/**
 * Flat feature model creator
 * 
 * @author jabier.martinez
 */
public class FlatFMSynthesis implements IFeatureModelSynthesis {

	@Override
	public void createFeatureModel(URI outputContainer, IProgressMonitor monitor) {
		// Get current adaptedModel
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();

		// Synthesize fm
		IFeatureModel fm = doCreateFeatureModel(adaptedModel);

		// Remove redundant constraints
		FeatureIDEUtils.removeRedundantConstraints(fm);

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

	/**
	 * Create a flat feature model with the constraints as cross-tree
	 * constraints
	 * 
	 * @param adaptedModel
	 * @return the feature model
	 */
	public FeatureModel doCreateFeatureModel(AdaptedModel adaptedModel) {
		FeatureModel fm = new FeatureModel(DefaultFeatureModelFactory.ID);

		String rootName = AdaptedModelHelper.getName(adaptedModel);
		if (rootName == null) {
			rootName = "FeatureModel";
		} else {
			rootName = FeatureIDEUtils.validFeatureName(rootName);
		}

		IFeature root = new Feature(fm, rootName);
		FeatureUtils.setAnd(root, true);
		FeatureUtils.setRoot(fm, root);
		fm.addFeature(root);
		fm.getStructure().setRoot(root.getStructure());

		LinkedList<IFeature> children = new LinkedList<IFeature>();
		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			FeatureUtils.setAbstract(f, false);
			FeatureUtils.setMandatory(f, false);
			FeatureUtils.setParent(f, root);
			children.add(f);
			fm.addFeature(f);
		}
		FeatureUtils.setChildren(root, children);

		// Add constraints
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}
		return fm;
	}

}
