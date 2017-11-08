package org.but4reuse.feature.constraints;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.utils.strings.StringUtils;

/**
 * Block constraint part
 * 
 * @author jabier.martinez
 */
public class BlockConstraintPart implements IConstraintPart {

	private Block block;
	
	public BlockConstraintPart(Block block) {
		setBlock(block);
	}

	@Override
	public String getText() {
		return StringUtils.validName(getBlock().getName());
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

}
