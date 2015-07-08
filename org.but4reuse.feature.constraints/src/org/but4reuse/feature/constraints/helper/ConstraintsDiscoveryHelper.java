package org.but4reuse.feature.constraints.helper;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Constraints discovery helper
 * 
 * @author jabier.martinez
 */
public class ConstraintsDiscoveryHelper {

	public static final String CONSTRAINTSDISCOVERY_EXTENSIONPOINT = "org.but4reuse.constraints.discovery";

	private static List<IConstraintsDiscovery> cache_constraintsdiscoveryalgorithms;

	/**
	 * Get all the registered constraints discovery algorithms
	 * 
	 * @return
	 */
	public static List<IConstraintsDiscovery> getAllConstraintsDiscoveryAlgorithms() {
		if (cache_constraintsdiscoveryalgorithms != null) {
			return cache_constraintsdiscoveryalgorithms;
		}
		List<IConstraintsDiscovery> constraintsDiscoveryAlgorithms = new ArrayList<IConstraintsDiscovery>();
		IConfigurationElement[] constraintsDiscoveryExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(CONSTRAINTSDISCOVERY_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : constraintsDiscoveryExtensionPoints) {
			try {
				constraintsDiscoveryAlgorithms.add((IConstraintsDiscovery) adapterExtensionPoint
						.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_constraintsdiscoveryalgorithms = constraintsDiscoveryAlgorithms;
		return constraintsDiscoveryAlgorithms;
	}

	static IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.getDefault().getBundle()
			.getSymbolicName());

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getAlgorithmName(IConstraintsDiscovery algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				CONSTRAINTSDISCOVERY_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IConstraintsDiscovery oneAlgo = (IConstraintsDiscovery) adapterExtensionPoint
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

	public static List<IConstraintsDiscovery> getSelectedConstraintsDiscoveryAlgorithms() {
		List<IConstraintsDiscovery> selected = new ArrayList<IConstraintsDiscovery>();
		for (IConstraintsDiscovery algo : getAllConstraintsDiscoveryAlgorithms()) {
			if (isAlgorithmSelected(algo)) {
				selected.add(algo);
			}
		}
		return selected;
	}

	public static boolean isAlgorithmSelected(IConstraintsDiscovery algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}

}
