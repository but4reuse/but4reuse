package org.but4reuse.feature.constraints;

import org.but4reuse.featurelist.Feature;
import org.but4reuse.utils.strings.StringUtils;

/**
 * Feature constraint part
 * 
 * @author jabier.martinez
 */
public class FeatureConstraintPart implements IConstraintPart {

	private Feature feature;
	
	public FeatureConstraintPart(Feature feature) {
		setFeature(feature);
	}

	@Override
	public String getText() {
		return StringUtils.validName(getFeature().getName());
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}
