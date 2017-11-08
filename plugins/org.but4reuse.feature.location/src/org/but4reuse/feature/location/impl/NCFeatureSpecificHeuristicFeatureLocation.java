package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.IConstraint;

/**
 * Feature-Specific feature location heuristic Only those Blocks that are ALWAYS
 * for a given Feature are interesting. From this set of Blocks we create a
 * Requires Graph and we remove the ones with in-degree > 0
 * 
 * @author jabier.martinez
 */
public class NCFeatureSpecificHeuristicFeatureLocation extends FeatureSpecificHeuristicFeatureLocation {

	@Override
	public void post(List<IConstraint> constraints, List<Block> blocks) {
		// Calculate reduced list
		List<Block> toBeRemoved = new ArrayList<Block>();
		for (Block block : blocks) {
			for (IConstraint constraint : constraints) {
				if (constraint instanceof BasicRequiresConstraint) {
					BasicRequiresConstraint c = (BasicRequiresConstraint) constraint;
					if (c.getBlock2().equals(block) && blocks.contains(c.getBlock1())) {
						toBeRemoved.add(block);
						break;
					}
				}
			}
		}
		blocks.removeAll(toBeRemoved);
	}
}
