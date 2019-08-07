package org.but4reuse.feature.location;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.IElement;
import org.but4reuse.featurelist.Feature;

/**
 * Located Features Utils
 * 
 * @author jabier.martinez
 */
public class LocatedFeaturesUtils {
	
	public static List<Block> getBlocksOfFeature(List<LocatedFeature> locatedFeatures, Feature feature) {
		List<Block> blocks = new ArrayList<Block>();
		for (LocatedFeature lf : locatedFeatures) {
			if (lf.getFeature().equals(feature)) {
				blocks.addAll(lf.getBlocks());
			}
		}
		return blocks;
	}

	public static List<IElement> getElementsOfFeature(List<LocatedFeature> locatedFeatures, Feature feature) {
		List<IElement> elements = new ArrayList<IElement>();
		for (LocatedFeature lf : locatedFeatures) {
			if (lf.getFeature().equals(feature)) {
				elements.addAll(lf.getElements());
			}
		}
		return elements;
	}

	public static List<Feature> getFeaturesOfBlock(List<LocatedFeature> locatedFeatures, Block block) {
		List<Feature> features = new ArrayList<Feature>();
		for (LocatedFeature lf : getLocatedFeaturesOfBlock(locatedFeatures, block)) {
			features.add(lf.getFeature());
		}
		return features;
	}
	
	public static List<LocatedFeature> getLocatedFeaturesOfBlock(List<LocatedFeature> locatedFeatures, Block block) {
		List<LocatedFeature> lfeatures = new ArrayList<LocatedFeature>();
		for (LocatedFeature lf : locatedFeatures) {
			if(lf.getBlocks().contains(block)){
				lfeatures.add(lf);
			}
		}
		return lfeatures;
	}
}
