package org.but4reuse.extension.featureide.fmcreators.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.extension.featureide.fmcreators.IFeatureModelCreator;
import org.but4reuse.extension.featureide.utils.FeatureIDEUtils;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;

import de.ovgu.featureide.fm.core.Feature;
import de.ovgu.featureide.fm.core.FeatureModel;

/**
 * TODO not finished, for the moment only alternative groups
 * 
 * @author jabier.martinez
 */
public class AlternativesBeforeHierarchyFMCreator implements IFeatureModelCreator {

	@Override
	public FeatureModel createFeatureModel(AdaptedModel adaptedModel) {
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

		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			f.setAbstract(false);
			f.setMandatory(false);
			fm.addFeature(f);
		}

		// Add constraints, maybe redundant after hierarchical fm creation but
		// it is better to keep them if the user wants to move them
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}

		// Identify alt groups
		AltGroupList altGroupList = new AltGroupList();
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		for (IConstraint constraint : constraints) {
			if (constraint.getType().equals(IConstraint.EXCLUDES)) {
				Feature feature1 = fm.getFeature(FeatureIDEUtils.validFeatureName(constraint.getBlock1().getName()));
				Feature feature2 = fm.getFeature(FeatureIDEUtils.validFeatureName(constraint.getBlock2().getName()));
				// any of them exists in previous?
				AltGroup altF1 = altGroupList.getAltGroupOfFeature(feature1);
				AltGroup altF2 = altGroupList.getAltGroupOfFeature(feature2);
				// Completely new alt group
				if (altF1 == null && altF2 == null) {
					altGroupList.addAltGroup(feature1, feature2);
				}
				// feature2 already was in a alt group so check if for all the
				// features of this alt group feature1 is also excluded
				else if (altF1 == null) {
					boolean allFound = true;
					for (Feature f : altF2.features) {
						if (!f.equals(feature2)) {
							if (!existsExcludeConstraint(constraints, f, feature1)) {
								allFound = false;
								break;
							}
						}
					}
					if (allFound) {
						altF2.features.add(feature1);
					}
				}
				// feature1 already was in a alt group
				else if (altF2 == null) {
					boolean allFound = true;
					for (Feature f : altF1.features) {
						if (!f.equals(feature1)) {
							if (!existsExcludeConstraint(constraints, f, feature2)) {
								allFound = false;
								break;
							}
						}
					}
					if (allFound) {
						altF1.features.add(feature2);
					}
				}
			}
		}

		// Create alt groups in the fm
		for (AltGroup altGroup : altGroupList.altGroups) {
			Feature fakeAltFeature = new Feature(fm);
			fakeAltFeature.setName("Alternative_" + altGroup.id);
			fakeAltFeature.setAlternative();
			fakeAltFeature.setAbstract(true);
			fakeAltFeature.setMandatory(false);
			fakeAltFeature.setChildren(altGroup.features);
			fm.addFeature(fakeAltFeature);
			fakeAltFeature.setParent(root);
		}

		// TODO Improve with Requires hierarchy. Feature not belonging to an
		// alternative group we just put them in the root
		LinkedList<Feature> toTheRoot = new LinkedList<Feature>();
		for (Feature f : fm.getFeatures()) {
			if (!f.equals(root)) {
				if (altGroupList.getAltGroupOfFeature(f) == null) {
					toTheRoot.addFirst(f);
					f.setParent(root);
				}
			}
		}
		root.setChildren(toTheRoot);

		return fm;
	}

	public static boolean existsExcludeConstraint(List<IConstraint> constraints, Feature f1, Feature f2) {
		for (IConstraint constraint : constraints) {
			if (constraint.getType().equals(IConstraint.EXCLUDES)) {
				// check f1 excludes f2 and viceversa
				if (f1.getName().equals(FeatureIDEUtils.validFeatureName(constraint.getBlock1().getName()))
						&& f2.getName().equals(FeatureIDEUtils.validFeatureName(constraint.getBlock2().getName()))) {
					return true;
				} else if (f2.getName().equals(FeatureIDEUtils.validFeatureName(constraint.getBlock1().getName()))
						&& f1.getName().equals(FeatureIDEUtils.validFeatureName(constraint.getBlock2().getName()))) {
					return true;
				}
			}
		}
		return false;
	}

	public class AltGroup {
		LinkedList<Feature> features = new LinkedList<Feature>();
		int id;
	}

	public class AltGroupList {
		List<AltGroup> altGroups = new ArrayList<AltGroup>();

		public AltGroup getAltGroupOfFeature(Feature feature) {
			for (AltGroup altGroup : altGroups) {
				if (altGroup.features.contains(feature)) {
					return altGroup;
				}
			}
			return null;
		}

		public void addAltGroup(Feature... features) {
			AltGroup altGroup = new AltGroup();
			// Automatically set the id
			altGroup.id = altGroups.size() + 1;
			for (Feature feature : features) {
				altGroup.features.add(feature);
			}
			altGroups.add(altGroup);
		}
	}
}
