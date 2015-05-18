package org.but4reuse.adaptedmodel.manager;

import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adapters.IAdapter;

/**
 * Adapted Model Manager
 * 
 * @author jabier.martinez
 * 
 */
public class AdaptedModelManager {

	private static AdaptedModel adaptedModel;
	private static List<IAdapter> adapters;

	public static AdaptedModel getAdaptedModel() {
		return adaptedModel;
	}

	public static void setAdaptedModel(AdaptedModel adaptedModel) {
		AdaptedModelManager.adaptedModel = adaptedModel;
	}

	public static List<IAdapter> getAdapters() {
		return adapters;
	}

	public static void setAdapters(List<IAdapter> adapters) {
		AdaptedModelManager.adapters = adapters;
	}

}
