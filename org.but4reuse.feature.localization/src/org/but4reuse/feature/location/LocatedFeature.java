package org.but4reuse.feature.location;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.IElement;
import org.but4reuse.featurelist.Feature;

/**
 * Located feature. It can be a list of blocks and/or a list of elements.
 * 
 * @author jabier.martinez
 * 
 */
public class LocatedFeature {
	// The feature
	private Feature feature;
	// The block
	private List<Block> blocks = new ArrayList<Block>();
	// The elements
	private List<IElement> elements = new ArrayList<IElement>();
	// A confidence value between 0 to 1
	private double confidence;

	/**
	 * Constructor
	 * 
	 * @param feature
	 * @param block
	 * @param confidence
	 *            between 0 and 1
	 */
	public LocatedFeature(Feature feature, Block block, double confidence) {
		this.feature = feature;
		addBlock(block);
		this.confidence = confidence;
	}

	public LocatedFeature(Feature feature, IElement element, double confidence) {
		this.feature = feature;
		addElement(element);
		this.confidence = confidence;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public void addBlock(Block block) {
		blocks.add(block);
	}

	public List<IElement> getElements() {
		return elements;
	}

	public void setElements(List<IElement> elements) {
		this.elements = elements;
	}
	
	private void addElement(IElement element) {
		elements.add(element);
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

}
