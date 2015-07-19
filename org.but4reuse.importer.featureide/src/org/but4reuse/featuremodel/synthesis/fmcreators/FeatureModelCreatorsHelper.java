package org.but4reuse.featuremodel.synthesis.fmcreators;

import java.util.ArrayList;
import java.util.List;

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
public class FeatureModelCreatorsHelper {

	private static final String FEATUREMODELCREATORS_EXTENSIONPOINT = "org.but4reuse.featuremodel.synthesis.fmcreators";

	/**
	 * Get all feature model creators
	 * 
	 * @return get the list
	 */
	public static List<IFeatureModelCreator> getAllFeatureModelCreators() {
		List<IFeatureModelCreator> fmCreators = new ArrayList<IFeatureModelCreator>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FEATUREMODELCREATORS_EXTENSIONPOINT);
		for (IConfigurationElement fmCreatorExtensionPoint : adapterExtensionPoints) {
			try {
				fmCreators.add((IFeatureModelCreator) fmCreatorExtensionPoint.createExecutableExtension("class"));
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
	public static IFeatureModelCreator getFeatureModelCreatorByName(String name) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FEATUREMODELCREATORS_EXTENSIONPOINT);
		for (IConfigurationElement fmCreatorExtensionPoint : adapterExtensionPoints) {
			if (fmCreatorExtensionPoint.getAttribute("name").equals(name)) {
				try {
					return (IFeatureModelCreator) fmCreatorExtensionPoint.createExecutableExtension("class");
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
	public static String getAlgorithmName(IFeatureModelCreator algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FEATUREMODELCREATORS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IFeatureModelCreator oneAlgo = (IFeatureModelCreator) adapterExtensionPoint
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

	public static List<IFeatureModelCreator> getSelectedFeatureModelCreators() {
		List<IFeatureModelCreator> selected = new ArrayList<IFeatureModelCreator>();
		for (IFeatureModelCreator algo : getAllFeatureModelCreators()) {
			if (isAlgorithmSelected(algo)) {
				selected.add(algo);
			}
		}
		return selected;
	}

	public static boolean isAlgorithmSelected(IFeatureModelCreator algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

}
