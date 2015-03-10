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

}
