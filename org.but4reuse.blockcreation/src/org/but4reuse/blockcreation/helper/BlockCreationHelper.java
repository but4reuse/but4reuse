package org.but4reuse.blockcreation.helper;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.blockcreation.IBlockCreationAlgorithm;
import org.but4reuse.blockcreation.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Block creation helper
 * @author jabier.martinez
 */
public class BlockCreationHelper {

	public static final String BLOCKCREATION_EXTENSIONPOINT = "org.but4reuse.blockcreation";

	private static List<IBlockCreationAlgorithm> cache_blockcreationalgorithms;
	
	/**
	 * Get all the registered block creation algorithms
	 * @return
	 */
	public static List<IBlockCreationAlgorithm> getAllBlockCreationAlgorithms() {
		if(cache_blockcreationalgorithms!=null){
			return cache_blockcreationalgorithms;
		}
		List<IBlockCreationAlgorithm> blockCreationAlgorithms = new ArrayList<IBlockCreationAlgorithm>();
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				BLOCKCREATION_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				blockCreationAlgorithms.add((IBlockCreationAlgorithm) adapterExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_blockcreationalgorithms = blockCreationAlgorithms;
		return blockCreationAlgorithms;
	}

	static IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(Activator.getDefault().getBundle().getSymbolicName());
	
	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getAlgorithmName(IBlockCreationAlgorithm algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				BLOCKCREATION_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IBlockCreationAlgorithm oneAlgo = (IBlockCreationAlgorithm) adapterExtensionPoint.createExecutableExtension("class");
				if(oneAlgo.getClass().equals(algo.getClass())){
					String name = adapterExtensionPoint.getAttribute("name");
					return name;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return "No name";
	}

	public static IBlockCreationAlgorithm getSelectedBlockCreation() {
		for(IBlockCreationAlgorithm algo : getAllBlockCreationAlgorithms()){
			if(isAlgorithmSelected(algo)){
				return algo;
			}
		}
		return null;
	}

	public static boolean isAlgorithmSelected(IBlockCreationAlgorithm algo) {
		String algoName = getAlgorithmName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}
	
}
