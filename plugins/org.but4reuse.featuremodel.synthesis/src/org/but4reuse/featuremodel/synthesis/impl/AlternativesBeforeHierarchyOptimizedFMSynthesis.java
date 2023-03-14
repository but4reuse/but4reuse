package org.but4reuse.featuremodel.synthesis.impl;

import de.ovgu.featureide.fm.core.base.FeatureUtils;
import de.ovgu.featureide.fm.core.base.IFeature;
import de.ovgu.featureide.fm.core.base.impl.DefaultFeatureModelFactory;
import de.ovgu.featureide.fm.core.base.impl.Feature;
import de.ovgu.featureide.fm.core.base.impl.FeatureModel;
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

import java.io.File;
import java.net.URI;
import java.util.*;

/**
 *  This is the optimized HashSet-variant written by Lukas Selvaggio.
 *
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
public class AlternativesBeforeHierarchyOptimizedFMSynthesis implements IFeatureModelSynthesis {

	@Override
	public void createFeatureModel(URI outputContainer, IProgressMonitor monitor) {
		System.out.print("Time 1 = " + System.currentTimeMillis());
		System.out.println("Creating Feature Model.");
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

		Set<String> parentAssigned = new HashSet<>();

		FeatureUtils.setRoot(fm, root);
		FeatureUtils.addFeature(fm, root);

		fm.addFeature(root);
		fmFeatures.add(root);

		// Common blocks (probably mandatory)
		List<Block> common = AdaptedModelHelper.getCommonBlocks(adaptedModel);

		System.out.println("Creating Feature Model. Adding Blocks.");
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

		System.out.println("Creating Feature Model. Adding Constraints.");
		// Add constraints
		for (IConstraint constraint : ConstraintsHelper.getCalculatedConstraints(adaptedModel)) {
			FeatureIDEUtils.addConstraint(fm, FeatureIDEUtils.getConstraintString(constraint));
		}

		System.out.println("Creating Feature Model. Identifying Alt Groups.");
		// Identify alt groups
		AltGroupList altGroupList = new AltGroupList();
		List<IConstraint> constraints = ConstraintsHelper.getCalculatedConstraints(adaptedModel);
		int i = 0;
		int si = constraints.size();
		for (IConstraint constraint : constraints) {
			System.out.println("Creating Feature Model. Identifying Alt Groups. Constraints Loop " + i + "/" + si);
			++i;
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

		System.out.println("Creating Feature Model. Create alt groups in the fm.");

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

		System.out.println("Creating Feature Model. Create hierarchy with the Requires.");

		Map<String, List<String>> requiredFeaturesMap = new HashMap<>();
		Map<String, AltGroup> altGroupsMap = new HashMap<>();
		Map<String, Map<String, Boolean>> isAncestorMap = new HashMap<>();

		i = 0;
		for(final IFeature feature : fmFeatures) {
			System.out.println("Creating Feature Model. Prepare Loop 1 | " + i + "/" + fmFeatures.size());
			++i;
			final String name = feature.getName();
			if(requiredFeaturesMap.containsKey(name)) {
				System.out.println("Something went wrong 1");
			}
			else {
				final List<IFeature> requiredFeatures = FeatureIDEUtils.getFeatureRequiredFeatures(fm, constraints, feature);
				final List<String> names = new ArrayList<>();
				for(final IFeature requiredFeature : requiredFeatures) {
					names.add(requiredFeature.getName());
				}
				requiredFeaturesMap.put(name, names);
			}
			if(altGroupsMap.containsKey(name)) {
				System.out.println("Something went wrong 2");
			}

			else {
				altGroupsMap.put(name, altGroupList.getAltGroupOfFeature(feature));
			}
		}

		i = 0;
		for(final IFeature feature : fmFeatures) {
			System.out.println("Creating Feature Model. Prepare Loop 2 | " + i + "/" + fmFeatures.size());
			++i;
			final String name = feature.getName();
			if(isAncestorMap.containsKey(name)) {
				System.out.println("Something went wrong 3");
			}
			else {
				final Map<String, Boolean> isAncestor = new HashMap<>();
				for(final IFeature feature2 : fmFeatures) {
					final String name2 = feature2.getName();
					if(isAncestor.containsKey(name2)) {
						System.out.println("Something went wrong 4");
					}
					else {
						isAncestor.put(name2, isAncestorFeature1ofFeature2(fm, name, name2, requiredFeaturesMap));
					}
				}
				isAncestorMap.put(name, isAncestor);
			}
		}

		// Create hierarchy with the Requires
		i = 0;
		si = fmFeatures.size();
		for (IFeature f : fmFeatures) {
			System.out.println("Creating Feature Model. Create hierarchy with the Requires. Looping Features " + i + "/" + si);
			++i;
			// check if the feature belongs to an alternative group
			AltGroup altGroup = altGroupsMap.get(f.getName());

			List<String> parentCandidates;
			if (altGroup == null) {
				// normal feature
				parentCandidates = requiredFeaturesMap.get(f.getName());
			} else {
				// feature inside an alt group
				// the parent candidates will be those that are shared parent
				// candidates for all the alt group
				parentCandidates = requiredFeaturesMap.get(f.getName());
				for (IFeature altf : altGroup.features) {
					parentCandidates.retainAll(requiredFeaturesMap.get(altf.getName()));
				}
			}
			List<IFeature> definitiveList = new ArrayList<IFeature>();
			for (String s : parentCandidates) {
				definitiveList.add(fm.getFeature(s));
			}

			// Reduce the parent candidates, remove ancestors
			for (String s1 : parentCandidates) {
				final IFeature pc1 = fm.getFeature(s1);
				for (String s2 : parentCandidates) {
					final IFeature pc2 = fm.getFeature(s2);
					if (pc1 != pc2) {
						if (isAncestorMap.get(s1).get(s2)) {
							definitiveList.remove(pc1);
						} else if (isAncestorMap.get(s2).get(s1)) {
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
					if (altGroupsMap.get(dp.getName()) != null) {
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
					parentAssigned.add(f.getName());
				} else {
					// Only once for the whole alt group
					if (!parentAssigned.contains(altGroup.altRoot.getName())) {
						FeatureUtils.addChild(parent, altGroup.altRoot);
						parentAssigned.add(altGroup.altRoot.getName());
						FeatureUtils.setParent(altGroup.altRoot, parent);
					}
				}
			}
		}

		// Features without parent are added to the root
		LinkedList<IFeature> toTheRoot = new LinkedList<IFeature>();
		for (IFeature f : fmFeatures) {
			if (!f.equals(root)) {
				AltGroup altGroup = altGroupsMap.get(f.getName());
				if (altGroup != null) {
					f = altGroup.altRoot;
				}
				if (!parentAssigned.contains(f.getName())) {
					toTheRoot.add(f);
					FeatureUtils.setParent(f, root);
					FeatureUtils.addChild(root, f);
					parentAssigned.add(f.getName());
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
		System.out.print("Time 1 = " + System.currentTimeMillis());
	}

	/**
	 * For example 3 requires 2 (3 is child of 2). Then 2 requires 1.
	 * isAncestor 1 of 3 is true.
	 * (s. isAncestorFeature1ofFeature2 in FeatureIDEUtils.java)
	 *
	 * @param fm exactly the same as in FeatureIDEUtils.java
	 * @param f1 The name of IFeature f1 (s. FeatureIDEUtils.java)
	 * @param f2 The name of IFeature f2 (s. FeatureIDEUtils.java)
	 * @param requiredFeatures is a map, that replaces getFeatureRequiredFeatures (s. FeatureIDEUtils.java).
	 *                         It could be analogously called getFeatureNameRequiredFeatureName, but that's too long...
	 * @return
	 */
	public static boolean isAncestorFeature1ofFeature2(FeatureModel fm, String f1,
													   String f2, Map<String, List<String>> requiredFeatures) {
		final List<String> directRequired = requiredFeatures.get(f2);
		if (directRequired.contains(f1)) {
			return true;
		}
		for (final String direct : directRequired) {
			if (isAncestorFeature1ofFeature2(fm, f1, direct, requiredFeatures)) {
				return true;
			}
		}
		return false;
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