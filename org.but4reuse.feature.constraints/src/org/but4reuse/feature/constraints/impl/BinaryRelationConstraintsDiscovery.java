package org.but4reuse.feature.constraints.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Binary relations constraints discovery
 * A structural constraints discovery between pairs of blocks
 * @author jabier.martinez
 * 
 */
public class BinaryRelationConstraintsDiscovery implements IConstraintsDiscovery {

	@Override
	public String discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {

		monitor.beginTask("Binary Relation Constraints discovery",
				new Double(Math.pow(adaptedModel.getOwnedBlocks().size(), 2)).intValue());

		String result = "";

		// Block Level
		// TODO feature level
		for (int y = 0; y < adaptedModel.getOwnedBlocks().size(); y++) {
			Block b1 = adaptedModel.getOwnedBlocks().get(y);
			monitor.subTask("Checking relations of " + b1.getName());
			for (int x = 0; x < adaptedModel.getOwnedBlocks().size(); x++) {
				Block b2 = adaptedModel.getOwnedBlocks().get(x);
				if (x != y) {
					// check monitor
					if (monitor.isCanceled()) {
						return result;
					}
					// here we have all binary combinations A and B, B and A
					// etc.
					// requires b1 -> b2
					if (blockRequiresAnotherBlockB(b1, b2)) {
						// TODO provide more info about the origin of the
						// constraint, the involved elements for example
						result = result + b1.getName() + " requires " + b2.getName() + "\n";
					}
					// mutual exclusion, not(b1 and b2), as it is mutual we do not need to check the opposite
					if (y < x) {
						// mutual exclusion
						if (blockExcludesAnotherBlock(b1, b2)) {
							result = result + b1.getName() + " mutually excludes " + b2.getName() + "\n";
						}
					}
				}
				monitor.worked(1);
			}
		}
		monitor.done();
		return result;
	}

	/**
	 * exists e in b1 : exists de in e.dependencies : de containedIn b2
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	public boolean blockRequiresAnotherBlockB(Block b1, Block b2) {
		for (BlockElement e : b1.getOwnedBlockElements()) {
			List<Object> de = getAllDependencies(e);
			for (BlockElement b2e : b2.getOwnedBlockElements()) {
				for (ElementWrapper elementW2 : b2e.getElementWrappers()) {
					if (de.contains(elementW2.getElement())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * exists e1 in b1, exists e2 in b2 : exists de in (e1.dependencies
	 * intersection e2.dependencies) and de.maxDependencies <=1
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	private boolean blockExcludesAnotherBlock(Block b1, Block b2) {
		for (BlockElement e1 : b1.getOwnedBlockElements()) {
			for (BlockElement e2 : b2.getOwnedBlockElements()) {
				// intersection of their dependencies
				Map<String, List<Object>> map1 = getDepedencyTypesAndPointedObjects(e1);
				Map<String, List<Object>> map2 = getDepedencyTypesAndPointedObjects(e2);
				for (String key : map1.keySet()) {
					List<Object> pointed1 = map1.get(key);
					List<Object> pointed2 = map2.get(key);
					if (pointed2 == null) {
						break;
					}
					for (Object o : pointed1) {
						if (pointed2.contains(o)) {
							if (o instanceof IElement) {
								IElement toCheck = (IElement) o;
								if (toCheck.getMaxDependencies(key) <= 1) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	private Map<String, List<Object>> getDepedencyTypesAndPointedObjects(BlockElement blockElement) {
		Map<String, List<Object>> result = new HashMap<String, List<Object>>();
		for (ElementWrapper elementW1 : blockElement.getElementWrappers()) {
			IElement element = (IElement) elementW1.getElement();
			Map<String, List<Object>> map = element.getDependencies();
			for (String key : map.keySet()) {
				List<Object> res = result.get(key);
				if (res == null) {
					res = new ArrayList<Object>();
				}
				List<Object> dependencies = map.get(key);
				for (Object o : dependencies) {
					if (!res.contains(o)) {
						res.add(o);
					}
				}
				result.put(key, res);
			}
		}
		return result;
	}

	public static List<Object> getAllDependencies(BlockElement blockElement) {
		List<Object> result = new ArrayList<Object>();
		for (ElementWrapper elementW1 : blockElement.getElementWrappers()) {
			IElement element = (IElement) elementW1.getElement();
			Map<String, List<Object>> map = element.getDependencies();
			for (String key : map.keySet()) {
				List<Object> dependencies = map.get(key);
				for (Object o : dependencies) {
					if (!result.contains(o)) {
						result.add(o);
					}
				}
			}
		}
		return result;
	}

}
