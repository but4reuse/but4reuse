package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.Constraint;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.activator.Activator;
import org.but4reuse.feature.constraints.preferences.BinaryRelationPreferencePage;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Binary relations constraints discovery A structural constraints discovery
 * between pairs of blocks
 * 
 * @author jabier.martinez
 * 
 */
public class BinaryRelationConstraintsDiscovery implements IConstraintsDiscovery {

	public static boolean onlyOneReason = false;

	@Override
	public List<IConstraint> discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {

		List<IConstraint> constraintList = new ArrayList<IConstraint>();

		// for binary relations we explore the matrix n*n where n is the number
		// of blocks. We ignore the matrix diagonal so it is n*n - n for
		// requires and (n*n-n)/2 for mutual exclusion
		int n = adaptedModel.getOwnedBlocks().size();
		// TODO monitor is not used, only for reporting messages
		// monitor.beginTask("Binary Relation Constraints discovery", (n * n -
		// n) + ((n * n - n) / 2));

		onlyOneReason = Activator.getDefault().getPreferenceStore()
				.getBoolean(BinaryRelationPreferencePage.ONLY_ONE_REASON);

		// REQUIRES
		// Block Level
		// TODO feature level
		boolean requires = Activator.getDefault().getPreferenceStore()
				.getBoolean(BinaryRelationPreferencePage.REQUIRES);
		if (requires) {
			long start = System.currentTimeMillis();
			for (Block b1 : adaptedModel.getOwnedBlocks()) {
				for (Block b2 : adaptedModel.getOwnedBlocks()) {
					if (b1 != b2) {
						monitor.subTask("Checking Requires relations of " + b1.getName() + " with " + b2.getName());
						// check monitor
						if (monitor.isCanceled()) {
							return constraintList;
						}
						// here we have all binary combinations A and B, B and A
						// etc.
						// requires b1 -> b2
						List<String> messages = blockRequiresAnotherBlockB(b1, b2);
						if (messages.size() > 0) {
							Constraint constraint = new BasicRequiresConstraint(b1, b2);
							constraint.setExplanations(messages);
							constraint.setNumberOfReasons(messages.size());
							constraintList.add(constraint);
						}
						// monitor.worked(1);
					}
				}
			}
			AdaptedModelManager.registerTime("Constraints discovery [Requires]", System.currentTimeMillis() - start);
		}
		// EXCLUDES
		boolean excludes = Activator.getDefault().getPreferenceStore()
				.getBoolean(BinaryRelationPreferencePage.EXCLUDES);
		if (excludes) {
			long start = System.currentTimeMillis();
			for (int y = 0; y < n; y++) {
				Block b1 = adaptedModel.getOwnedBlocks().get(y);
				for (int x = 0; x < n; x++) {
					// mutual exclusion, not(b1 and b2), as it is mutual we do
					// not need to check the opposite
					if (x != y && y < x) {
						Block b2 = adaptedModel.getOwnedBlocks().get(x);
						monitor.subTask(
								"Checking Mutual Exclusion relations of " + b1.getName() + " with " + b2.getName());
						// check monitor
						if (monitor.isCanceled()) {
							return constraintList;
						}
						// mutual exclusion
						List<String> messages = blockExcludesAnotherBlock(b1, b2);
						if (messages.size() > 0) {
							Constraint constraint = new BasicExcludesConstraint(b1, b2);
							constraint.setExplanations(messages);
							constraint.setNumberOfReasons(messages.size());
							constraintList.add(constraint);
						}

						// monitor.worked(1);
					}
				}
			}
			AdaptedModelManager.registerTime("Constraints discovery [Mutual exclusion]",
					System.currentTimeMillis() - start);
		}
		// monitor.done();
		return constraintList;
	}

	/**
	 * exists e in b1 : exists de in e.dependencies : de containedIn b2
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static List<String> blockRequiresAnotherBlockB(Block b1, Block b2) {
		List<String> messages = new ArrayList<String>();
		// Get the elements of B1
		HashSet<IElement> elementsOfB1 = AdaptedModelHelper.getElementsOfBlockHashSet(b1);
		for (BlockElement e : b1.getOwnedBlockElements()) {
			// TODO getAllDependencies is the bottleneck here
			List<IDependencyObject> de = getAllDependencies(e);
			// Actually check
			for (IDependencyObject deo : de) {
				// Check if the dependency object is already in b1
				boolean deoAlreadyInB1 = elementsOfB1.contains(deo);
				if (!deoAlreadyInB1) {
					for (BlockElement b2e : b2.getOwnedBlockElements()) {
						for (ElementWrapper elementW2 : b2e.getElementWrappers()) {
							if (deo.equals(elementW2.getElement())) {
								String message = ((IElement) e.getElementWrappers().get(0).getElement()).getText()
										+ "->" + ((IElement) elementW2.getElement()).getText();
								messages.add(message);
								if (onlyOneReason) {
									return messages;
								}
								// it is enough for all the element wrappers of
								// b2e
								// TODO continue with all the element wrappers
								// but keep track of already added dependencies
								break;
							}
						}
					}
				}
			}
		}
		return messages;
	}

	/**
	 * exists e1 in b1, exists e2 in b2 : exists de in (e1.dependencies
	 * intersection e2.dependencies) and de.maxDependencies <=1
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static List<String> blockExcludesAnotherBlock(Block b1, Block b2) {
		List<String> messages = new ArrayList<String>();
		// Create the global maps of dependency ids and dependency objects
		Map<String, List<IDependencyObject>> map1 = new HashMap<String, List<IDependencyObject>>();
		Map<String, List<IDependencyObject>> map2 = new HashMap<String, List<IDependencyObject>>();
		for (BlockElement e1 : b1.getOwnedBlockElements()) {
			map1 = getDepedencyTypesAndPointedObjects(map1, e1);
		}
		for (BlockElement e2 : b2.getOwnedBlockElements()) {
			map2 = getDepedencyTypesAndPointedObjects(map2, e2);
		}
		for (String key : map1.keySet()) {
			List<IDependencyObject> pointed1 = map1.get(key);
			List<IDependencyObject> pointed2 = map2.get(key);
			if (pointed2 == null) {
				continue;
			}
			for (IDependencyObject o : pointed1) {
				if (pointed2.contains(o)) {
					if (o.getMaxDependencies(key) < Collections.frequency(pointed1, o)
							+ Collections.frequency(pointed2, o)) {
						messages.add(o.getDependencyObjectText());
						if (onlyOneReason) {
							return messages;
						}
					}
				}
			}
		}
		return messages;
	}

	public static Map<String, List<IDependencyObject>> getDepedencyTypesAndPointedObjects(
			Map<String, List<IDependencyObject>> result, BlockElement blockElement) {
		// we allow duplicates in result but not those that belong to the same
		// BlockElement
		List<IDependencyObject> visitedForThisBlockElement = new ArrayList<IDependencyObject>();
		for (ElementWrapper elementW1 : blockElement.getElementWrappers()) {
			IElement element = (IElement) elementW1.getElement();
			Map<String, List<IDependencyObject>> map = element.getDependencies();
			for (String key : map.keySet()) {
				List<IDependencyObject> res = result.get(key);
				if (res == null) {
					res = new ArrayList<IDependencyObject>();
				}
				List<IDependencyObject> dependencies = map.get(key);
				for (IDependencyObject o : dependencies) {
					if (!visitedForThisBlockElement.contains(o)) {
						res.add(o);
						visitedForThisBlockElement.add(o);
					}
				}
				result.put(key, res);
			}
		}
		return result;
	}

	/**
	 * Get all dependency objects independently of the dependency id
	 * 
	 * @param blockElement
	 * @return non empty list of dependency objects
	 */
	public static List<IDependencyObject> getAllDependencies(BlockElement blockElement) {
		List<IDependencyObject> result = new ArrayList<IDependencyObject>();
		for (ElementWrapper elementW1 : blockElement.getElementWrappers()) {
			IElement element = (IElement) elementW1.getElement();
			Map<String, List<IDependencyObject>> map = element.getDependencies();
			for (List<IDependencyObject> dependencies : map.values()) {
				for (IDependencyObject o : dependencies) {
					if (!result.contains(o)) {
						result.add(o);
					}
				}
			}
		}
		return result;
	}

}
