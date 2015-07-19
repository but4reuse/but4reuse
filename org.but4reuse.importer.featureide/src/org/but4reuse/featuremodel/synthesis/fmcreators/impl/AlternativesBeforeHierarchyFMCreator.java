package org.but4reuse.featuremodel.synthesis.fmcreators.impl;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * Feature Model synthesis: First we identify alternative groups, then we create
 * the hierarchy. From the possible parent candidates, we discard those that are
 * already containing other parent candidates. The parent candidates of an
 * alternative group are the intersection of the parent candidates of the
 * features integrating the group. In case of several parent candidates, we
 * select parent candidates belonging to alternative groups and in the case of
 * any, we select the parent candidate with the higher number of reasons in the
 * requires constraint description. Finally, the features without parent are
 * added to the root.
 * 
 * @author jabier.martinez
 */
public class AlternativesBeforeHierarchyFMCreator implements IFeatureModelCreator {

	@Override
	public void createFeatureModel(URI outputContainer) {
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		// TODO Check for loops in the Requires graph.
		// Assumption is that there is no loops in the Requires constraints
		// between blocks as it happens with the default block identification
		// algorithm.
		FeatureModel fm = new FeatureModel();
		// fm.getFeatures returns a collection with random ordering...
		// let's keep our own list of features
		List<Feature> fmFeatures = new ArrayList<Feature>();

		Feature root = new Feature(fm);
		String rootName = AdaptedModelHelper.getName(adaptedModel);
		if (rootName == null) {
			rootName = "FeatureModel";
		} else {
			rootName = FeatureIDEUtils.validFeatureName(rootName);
		}
		root.setName(rootName);
		root.setAND(true);

		List<Feature> parentAssigned = new ArrayList<Feature>();

		fm.setRoot(root);
		fm.addFeature(root);
		fmFeatures.add(root);

		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			f.setAbstract(false);
			f.setMandatory(false);
			fm.addFeature(f);
			fmFeatures.add(f);
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
			if (constraint.getType().equals(IConstraint.MUTUALLY_EXCLUDES)) {
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
							if (!FeatureIDEUtils.existsExcludeConstraint(constraints, f, feature1)) {
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
							if (!FeatureIDEUtils.existsExcludeConstraint(constraints, f, feature2)) {
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
			altGroup.altRoot = fakeAltFeature;
			fakeAltFeature.setAlternative();
			fakeAltFeature.setAbstract(true);
			fakeAltFeature.setMandatory(false);
			fakeAltFeature.setChildren(altGroup.features);
			fm.addFeature(fakeAltFeature);
			fmFeatures.add(fakeAltFeature);
		}

		// Create hierarchy with the Requires
		for (Feature f : fmFeatures) {

			// check if the feature belongs to an alternative group
			AltGroup altGroup = altGroupList.getAltGroupOfFeature(f);

			List<Feature> parentCandidates;
			if (altGroup == null) {
				// normal feature
				parentCandidates = FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, f);
			} else {
				// feature inside an alt group
				// the parent candidates will be those that are shared parent
				// candidates for all the alt group
				parentCandidates = FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, f);
				for (Feature altf : altGroup.features) {
					parentCandidates.retainAll(FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, altf));
				}
			}
			List<Feature> definitiveList = new ArrayList<Feature>();
			for (Feature pc : parentCandidates) {
				definitiveList.add(pc);
			}

			// Reduce the parent candidates, remove ancestors
			for (Feature pc1 : parentCandidates) {
				for (Feature pc2 : parentCandidates) {
					if (pc1 != pc2) {
						if (FeatureIDEUtils.isAncestorFeature1ofFeature2(fm, constraints, pc1, pc2)) {
							definitiveList.remove(pc1);
						} else if (FeatureIDEUtils.isAncestorFeature1ofFeature2(fm, constraints, pc2, pc1)) {
							definitiveList.remove(pc2);
						}
					}
				}
			}

			// Select one
			if (!definitiveList.isEmpty()) {
				Feature parent = null;

				// Preference to parents in alternative groups
				// TODO for the moment get the first alternative group
				for (Feature dp : definitiveList) {
					if (altGroupList.getAltGroupOfFeature(dp) != null) {
						parent = dp;
						break;
					}
				}

				// If no parent in alternative group
				// Get the one with higher number of reasons in the requires
				// constraint
				if (parent == null) {
					int maximumReasons = Integer.MIN_VALUE;
					for (Feature dp : definitiveList) {
						int reasons = FeatureIDEUtils.getNumberOfReasonsOfRequiresConstraint(constraints, f, dp);
						if (reasons > maximumReasons) {
							parent = dp;
						}
					}
				}

				// And add it
				parent.setAND(true);
				LinkedList<Feature> childs = parent.getChildren();
				if (altGroup == null) {
					childs.add(f);
					parentAssigned.add(f);
					parent.setChildren(childs);
					f.setParent(parent);
				} else {
					// Only once for the whole alt group
					if (!parentAssigned.contains(altGroup.altRoot)) {
						childs.add(altGroup.altRoot);
						parentAssigned.add(altGroup.altRoot);
						parent.setChildren(childs);
						altGroup.altRoot.setParent(parent);
					}
				}
			}
		}

		// Features without parent are added to the root
		LinkedList<Feature> toTheRoot = new LinkedList<Feature>();
		for (Feature f : fmFeatures) {
			if (!f.equals(root)) {
				AltGroup altGroup = altGroupList.getAltGroupOfFeature(f);
				if (altGroup != null) {
					f = altGroup.altRoot;
				}
				if (!parentAssigned.contains(f)) {
					toTheRoot.add(f);
					f.setParent(root);
					parentAssigned.add(f);
				}
			}
		}
		root.setChildren(toTheRoot);

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
	 * Auxiliary classes
	 */
	public class AltGroup {
		LinkedList<Feature> features = new LinkedList<Feature>();
		int id;
		Feature altRoot;
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
