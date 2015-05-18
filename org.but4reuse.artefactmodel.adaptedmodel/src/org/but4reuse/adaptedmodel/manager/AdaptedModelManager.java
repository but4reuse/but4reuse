package org.but4reuse.adaptedmodel.manager;

import org.but4reuse.adaptedmodel.AdaptedModel;

/**
 * Adapted Model Manager
 * 
 * @author jabier.martinez
 * 
 */
public class AdaptedModelManager {

	private static AdaptedModel adaptedModel;

	public static AdaptedModel getAdaptedModel() {
		return adaptedModel;
	}

	public static void setAdaptedModel(AdaptedModel adaptedModel) {
		AdaptedModelManager.adaptedModel = adaptedModel;
	}

}
