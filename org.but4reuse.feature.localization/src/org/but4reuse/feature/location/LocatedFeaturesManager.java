package org.but4reuse.feature.location;

import java.util.ArrayList;
import java.util.List;

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

}
