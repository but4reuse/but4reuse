package org.but4reuse.visualisation.impl.visualiser.adaptedmodel;

import org.but4reuse.adaptedmodel.Block;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;

/**
 * Block Markup Kind
 * 
 * @author jabier.martinez
 */
public class BlockMarkupKind extends SimpleMarkupKind {

	private Block block;

	public BlockMarkupKind(String name) {
		super(name);
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

}
