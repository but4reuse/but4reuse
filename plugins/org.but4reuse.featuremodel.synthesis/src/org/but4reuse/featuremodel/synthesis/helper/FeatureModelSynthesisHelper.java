package org.but4reuse.featuremodel.synthesis.helper;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.featuremodel.synthesis.IFeatureModelSynthesis;
import org.but4reuse.featuremodel.synthesis.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Feature Model Creators Helper
 * 
 * @author jabier.martinez
 */
public class FeatureModelSynthesisHelper {

	private static final String FEATUREMODELSYNTHESIS_EXTENSIONPOINT = "org.but4reuse.featuremodel.synthesis";

	/**
	 * Get all feature model creators
	 * 
	 * @return get the list
	 */
	public static List<IFeatureModelSynthesis> getAllFeatureModelCreators() {
		List<IFeatureModelSynthesis> fmCreators = new ArrayList<IFeatureModelSynthesis>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FEATUREMODELSYNTHESIS_EXTENSIONPOINT);
		for (IConfigurationElement fmCreatorExtensionPoint : adapterExtensionPoints) {
			try {
				fmCreators.add((IFeatureModelSynthesis) fmCreatorExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return fmCreators;
	}

	/**
	 * Get feature model creator by name
	 * 
	 * @param name
	 * @return null or the feature model creator
	 */
	public static IFeatureModelSynthesis getFeatureModelCreatorByName(String name) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FEATUREMODELSYNTHESIS_EXTENSIONPOINT);
		for (IConfigurationElement fmCreatorExtensionPoint : adapterExtensionPoints) {
			if (fmCreatorExtensionPoint.getAttribute("name").equals(name)) {
				try {
					return (IFeatureModelSynthesis) fmCreatorExtensionPoint.createExecutableExtension("class");
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Get algorithm name
	 * 
	 * @param algo
	 * @return the name
	 */
	public static String getAlgorithmName(IFeatureModelSynthesis algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FEATUREMODELSYNTHESIS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IFeatureModelSynthesis oneAlgo = (IFeatureModelSynthesis) adapterExtensionPoint
						.createExecutableExtension("class");
				if (oneAlgo.getClass().equals(algo.getClass())) {
					String name = adapterExtensionPoint.getAttribute("name");
					return name;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return "No name";
	}

	public static List<IFeatureModelSynthesis> getSelectedFeatureModelCreators() {
		List<IFeatureModelSynthesis> selected = new ArrayList<IFeatureModelSynthesis>();
		for (IFeatureModelSynthesis algo : getAllFeatureModelCreators()) {
			if (isAlgorithmSelected(algo)) {
				selected.add(algo);
			}
		}
		return selected;
	}

	public static boolean isAlgorithmSelected(IFeatureModelSynthesis algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

}
