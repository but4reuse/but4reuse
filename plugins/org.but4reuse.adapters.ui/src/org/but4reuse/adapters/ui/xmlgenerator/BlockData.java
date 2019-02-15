package org.but4reuse.adapters.ui.xmlgenerator;

import java.util.List;

import org.but4reuse.adapters.IElement;
/**
 * The contents of a block 
 * @author lin_chao
 *
 */
public class BlockData {
	// block name
	private String name;
	// list of elements of the block
	private List<IElement> list;
	
	public BlockData(String name, List<IElement> list) {
		this.name = name;
		this.list = list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IElement> getList() {
		return list;
	}

	public void setList(List<IElement> list) {
		this.list = list;
	}
	
	public int getNbElem() {
		return list.size();
	}
}
