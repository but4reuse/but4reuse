package org.but4reuse.block.identification.helper;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Block identification helper
 * 
 * @author jabier.martinez
 */
public class BlockIdentificationHelper {

	public static final String BLOCKIDENTIFICATION_EXTENSIONPOINT = "org.but4reuse.block.identification";

	private static List<IBlockIdentification> cache_blockidentificationalgorithms;

	/**
	 * Get all the registered block creation algorithms
	 * 
	 * @return
	 */
	public static List<IBlockIdentification> getAllBlockIdentificationAlgorithms() {
		if (cache_blockidentificationalgorithms != null) {
			return cache_blockidentificationalgorithms;
		}
		List<IBlockIdentification> algorithms = new ArrayList<IBlockIdentification>();
		IConfigurationElement[] extensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				BLOCKIDENTIFICATION_EXTENSIONPOINT);
		for (IConfigurationElement extensionPoint : extensionPoints) {
			try {
				algorithms.add((IBlockIdentification) extensionPoint
						.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_blockidentificationalgorithms = algorithms;
		return algorithms;
	}

	static IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.getDefault().getBundle()
			.getSymbolicName());

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getAlgorithmName(IBlockIdentification algo) {
		IConfigurationElement[] extensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				BLOCKIDENTIFICATION_EXTENSIONPOINT);
		for (IConfigurationElement extensionPoint : extensionPoints) {
			try {
				IBlockIdentification oneAlgo = (IBlockIdentification) extensionPoint
						.createExecutableExtension("class");
				if (oneAlgo.getClass().equals(algo.getClass())) {
					String name = extensionPoint.getAttribute("name");
					return name;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return "No name";
	}

	public static IBlockIdentification getSelectedBlockIdentification() {
		for (IBlockIdentification algo : getAllBlockIdentificationAlgorithms()) {
			if (isAlgorithmSelected(algo)) {
				return algo;
			}
		}
		return null;
	}

	public static boolean isAlgorithmSelected(IBlockIdentification algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}

}
