package org.but4reuse.feature.constraints;

import org.but4reuse.adaptedmodel.Block;

public class BasicRequiresConstraint extends Constraint {
	
	public BasicRequiresConstraint(Block b1, Block b2) {
		addConstraintPart(new BlockConstraintPart(b1));
		addConstraintPart(new ImpliesConstraintPart());
		addConstraintPart(new BlockConstraintPart(b2));
	}

	public Block getBlock1() {
		return ((BlockConstraintPart) getConstraintParts().get(0)).getBlock();
	}
	
	public Block getBlock2() {
		return ((BlockConstraintPart) getConstraintParts().get(2)).getBlock();
	}

}
