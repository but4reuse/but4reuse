package org.but4reuse.feature.location;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adapters.IElement;
import org.but4reuse.featurelist.Feature;

/**
 * A manager to store the located features that were higher than the threshold
 * 
 * @author jabier.martinez
 * 
 */
public class LocatedFeaturesManager {

	static List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();

	public static List<LocatedFeature> getLocatedFeatures() {
		return locatedFeatures;
	}

	public static void setLocatedFeatures(List<LocatedFeature> list) {
		LocatedFeaturesManager.locatedFeatures = list;
	}

	public static void addLocatedFeature(LocatedFeature locatedFeature) {
		LocatedFeaturesManager.locatedFeatures.add(locatedFeature);
	}
	
	public static List<Block> getBlocksOfFeature(Feature feature){
		List<Block> blocks = new ArrayList<Block>();
		for(LocatedFeature lf : locatedFeatures){
			if(lf.getFeature().equals(feature)){
				blocks.addAll(lf.getBlocks());
			}
		}
		return blocks;
	}
	
	public static List<IElement> getElementsOfFeature(Feature feature){
		List<IElement> elements = new ArrayList<IElement>();
		for(LocatedFeature lf : locatedFeatures){
			if(lf.getFeature().equals(feature)){
				elements.addAll(lf.getElements());
			}
		}
		return elements;
	}

}
