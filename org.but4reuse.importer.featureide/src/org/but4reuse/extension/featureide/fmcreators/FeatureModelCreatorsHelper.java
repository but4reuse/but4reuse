package org.but4reuse.extension.featureide.fmcreators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * Feature Model Creators Helper
 * 
 * @author jabier.martinez
 */
public class FeatureModelCreatorsHelper {

	private static final String FEATUREMODELCREATORS_EXTENSIONPOINT = "org.but4reuse.extensions.featureide.fmcreators";

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

}
