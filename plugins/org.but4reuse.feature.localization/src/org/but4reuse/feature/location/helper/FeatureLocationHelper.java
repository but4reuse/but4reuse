package org.but4reuse.feature.location.helper;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Feature location helper
 * 
 * @author jabier.martinez
 */
public class FeatureLocationHelper {

	public static final String FEATURELOCATION_EXTENSIONPOINT = "org.but4reuse.feature.location";

	public static final String LOCATION_THRESHOLD_PREFERENCE = "LOCATION_THRESHOLD_PREFERENCE";

	private static List<IFeatureLocation> cache_featurelocation;

	/**
	 * Get all the registered feature location algorithms
	 * 
	 * @return
	 */
	public static List<IFeatureLocation> getAllFeatureLocation() {
		if (cache_featurelocation != null) {
			return cache_featurelocation;
		}
		List<IFeatureLocation> blockCreationAlgorithms = new ArrayList<IFeatureLocation>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FEATURELOCATION_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				blockCreationAlgorithms
						.add((IFeatureLocation) adapterExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_featurelocation = blockCreationAlgorithms;
		return blockCreationAlgorithms;
	}

	static IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.getDefault().getBundle()
			.getSymbolicName());

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getAlgorithmName(IFeatureLocation algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				FEATURELOCATION_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IFeatureLocation oneAlgo = (IFeatureLocation) adapterExtensionPoint.createExecutableExtension("class");
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

	public static IFeatureLocation getSelectedFeatureLocation() {
		for (IFeatureLocation algo : getAllFeatureLocation()) {
			if (isAlgorithmSelected(algo)) {
				return algo;
			}
		}
		return null;
	}

	public static boolean isAlgorithmSelected(IFeatureLocation algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}

}
