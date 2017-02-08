package org.but4reuse.feature.constraints.impl;

import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.feature.constraints.IConstraint;

/**
 * Provisional binary operator constraint implementation
 * 
 * @author jabier.martinez
 */
public class ConstraintImpl implements IConstraint {

	Block block1;
	Block block2;
	String type;
	String text;
	List<String> explanations;
	int numberOfReasons;

	@Override
	public Block getBlock1() {
		return block1;
	}

	@Override
	public Block getBlock2() {
		return block2;
	}

	@Override
	public void setBlock1(Block block) {
		block1 = block;
	}

	@Override
	public void setBlock2(Block block) {
		block2 = block;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public List<String> getExplanations() {
		return explanations;
	}

	@Override
	public void setExplanations(List<String> explanations) {
		this.explanations = explanations;
	}

	@Override
	public int getNumberOfReasons() {
		return numberOfReasons;
	}

	@Override
	public void setNumberOfReasons(int numberOfReasons) {
		this.numberOfReasons = numberOfReasons;
	}

	@Override
	public String getText() {
		if (getType().equals(IConstraint.FREETEXT)) {
			return text;
		}
		return getBlock1().getName() + " " + getType() + " " + getBlock2().getName();
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

}
