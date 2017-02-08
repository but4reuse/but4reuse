package org.but4reuse.visualisation.impl.visualiser.featurelist;

import org.but4reuse.featurelist.Feature;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;

/**
 * Feature Markup Kind
 * 
 * @author jabier.martinez
 */
public class FeatureMarkupKind extends SimpleMarkupKind {

	private Feature feature;

	public FeatureMarkupKind(String name) {
		super(name);
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}
