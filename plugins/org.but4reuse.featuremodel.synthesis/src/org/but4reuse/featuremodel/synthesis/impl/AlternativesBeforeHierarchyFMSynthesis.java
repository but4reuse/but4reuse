package org.but4reuse.featuremodel.synthesis.impl;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.impl.ConstraintsHelper;
import org.but4reuse.featuremodel.synthesis.IFeatureModelSynthesis;
import org.but4reuse.featuremodel.synthesis.utils.FeatureIDEUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;

/**
 * Feature Model synthesis: First we identify alternative groups, then we create
 * the hierarchy. From the possible parent candidates, we discard those that are
 * already containing other parent candidates. The parent candidates of an
 * alternative group are the intersection of the parent candidates of the
 * features integrating the group. In case of several parent candidates, we
 * select parent candidates belonging to alternative groups and in the case of
 * any, we select the parent candidate with the higher number of reasons in the
 * requires constraint description. Finally, the features without parent are
 * added to the root. The common features are set as mandatory and redundant
 * constraints are removed.
 * 
 * @author jabier.martinez
 */
public class AlternativesBeforeHierarchyFMSynthesis implements IFeatureModelSynthesis {

	@Override
	public void createFeatureModel(URI outputContainer, IProgressMonitor monitor) {
		AdaptedModel adaptedModel = AdaptedModelManager.getAdaptedModel();
		// TODO Check for loops in the Requires graph.
		// Assumption is that there are no loops in the Requires constraints
		// between blocks as it happens with the default block identification
		// algorithm.
		FeatureModel fm = new FeatureModel(DefaultFeatureModelFactory.ID);
		// fm.getFeatures returns a collection with random ordering...
		// let's keep our own list of features
		List<IFeature> fmFeatures = new ArrayList<IFeature>();

		String rootName = AdaptedModelHelper.getName(adaptedModel);
		if (rootName == null) {
			rootName = "FeatureModel";
		} else {
			rootName = FeatureIDEUtils.validFeatureName(rootName);
		}
		IFeature root = new Feature(fm, rootName);

		FeatureUtils.setAnd(root, true);

		List<IFeature> parentAssigned = new ArrayList<IFeature>();

		FeatureUtils.setRoot(fm, root);
		FeatureUtils.addFeature(fm, root);

		fm.addFeature(root);
		fmFeatures.add(root);

		// Common blocks (probably mandatory)
		List<Block> common = AdaptedModelHelper.getCommonBlocks(adaptedModel);

		// Add blocks as features
		for (Block block : adaptedModel.getOwnedBlocks()) {
			Feature f = new Feature(fm, FeatureIDEUtils.validFeatureName(block.getName()));
			FeatureUtils.setAbstract(f, false);
			if (common.contains(block)) {
				FeatureUtils.setMandatory(f, true);
			} else {
				FeatureUtils.setMandatory(f, false);
			}
			fm.addFeature(f);
			fmFeatures.add(f);
		}

		// Add constraints
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}

		// Identify alt groups
		AltGroupList altGroupList = new AltGroupList();
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		for (IConstraint constraint : constraints) {
			if (constraint instanceof BasicExcludesConstraint) {
				BasicExcludesConstraint c = (BasicExcludesConstraint)constraint;
				IFeature feature1 = fm.getFeature(FeatureIDEUtils.validFeatureName(c.getBlock1().getName()));
				IFeature feature2 = fm.getFeature(FeatureIDEUtils.validFeatureName(c.getBlock2().getName()));
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
					for (IFeature f : altF2.features) {
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
				// feature1 already was in an alt group
				else if (altF2 == null) {
					boolean allFound = true;
					for (IFeature f : altF1.features) {
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
			IFeature fakeAltFeature = new Feature(fm, "Alternative_" + altGroup.id);
			altGroup.altRoot = fakeAltFeature;
			FeatureUtils.setAlternative(fakeAltFeature);
			FeatureUtils.setAbstract(fakeAltFeature, true);
			FeatureUtils.setMandatory(fakeAltFeature, false);
			FeatureUtils.setChildren(fakeAltFeature, altGroup.features);
			FeatureUtils.addFeature(fm, fakeAltFeature);
			fm.addFeature(fakeAltFeature);
			fmFeatures.add(fakeAltFeature);
		}

		// Create hierarchy with the Requires
		for (IFeature f : fmFeatures) {

			// check if the feature belongs to an alternative group
			AltGroup altGroup = altGroupList.getAltGroupOfFeature(f);

			List<IFeature> parentCandidates;
			if (altGroup == null) {
				// normal feature
				parentCandidates = FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, f);
			} else {
				// feature inside an alt group
				// the parent candidates will be those that are shared parent
				// candidates for all the alt group
				parentCandidates = FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, f);
				for (IFeature altf : altGroup.features) {
					parentCandidates.retainAll(FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, altf));
				}
			}
			List<IFeature> definitiveList = new ArrayList<IFeature>();
			for (IFeature pc : parentCandidates) {
				definitiveList.add(pc);
			}

			// Reduce the parent candidates, remove ancestors
			for (IFeature pc1 : parentCandidates) {
				for (IFeature pc2 : parentCandidates) {
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
				IFeature parent = null;

				// Preference to parents in alternative groups
				// TODO for the moment get the first alternative group
				for (IFeature dp : definitiveList) {
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
					for (IFeature dp : definitiveList) {
						int reasons = FeatureIDEUtils.getNumberOfReasonsOfRequiresConstraint(constraints, f, dp);
						if (reasons > maximumReasons) {
							parent = dp;
						}
					}
				}

				// And add it
				FeatureUtils.setAnd(parent, true);
				if (altGroup == null) {
					FeatureUtils.addChild(parent, f);
					FeatureUtils.setParent(f, parent);
					parentAssigned.add(f);
				} else {
					// Only once for the whole alt group
					if (!parentAssigned.contains(altGroup.altRoot)) {
						FeatureUtils.addChild(parent, altGroup.altRoot);
						parentAssigned.add(altGroup.altRoot);
						FeatureUtils.setParent(altGroup.altRoot, parent);
					}
				}
			}
		}

		// Features without parent are added to the root
		LinkedList<IFeature> toTheRoot = new LinkedList<IFeature>();
		for (IFeature f : fmFeatures) {
			if (!f.equals(root)) {
				AltGroup altGroup = altGroupList.getAltGroupOfFeature(f);
				if (altGroup != null) {
					f = altGroup.altRoot;
				}
				if (!parentAssigned.contains(f)) {
					toTheRoot.add(f);
					FeatureUtils.setParent(f, root);
					FeatureUtils.addChild(root, f);
					parentAssigned.add(f);
				}
			}
		}
		FeatureUtils.setChildren(root, toTheRoot);

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
	 * Auxiliary classes
	 */
	public class AltGroup {
		LinkedList<IFeature> features = new LinkedList<IFeature>();
		int id;
		IFeature altRoot;
	}

	public class AltGroupList {
		List<AltGroup> altGroups = new ArrayList<AltGroup>();

		public AltGroup getAltGroupOfFeature(IFeature feature) {
			for (AltGroup altGroup : altGroups) {
				if (altGroup.features.contains(feature)) {
					return altGroup;
				}
			}
			return null;
		}

		public void addAltGroup(IFeature... features) {
			AltGroup altGroup = new AltGroup();
			// Automatically set the id
			altGroup.id = altGroups.size() + 1;
			for (IFeature feature : features) {
				altGroup.features.add(feature);
			}
			altGroups.add(altGroup);
		}
	}
}
