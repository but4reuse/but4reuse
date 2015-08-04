package org.but4reuse.feature.location;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.featurelist.Feature;

/**
 * Located feature
 * 
 * @author jabier.martinez
 * 
 */
public class LocatedFeature {
	// The feature
	private Feature feature;
	// The block
	private Block block;
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
		this.setBlock(block);
		this.confidence = confidence;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
}
