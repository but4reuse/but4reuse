package org.but4reuse.visualisation.helpers;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

/**
 * VisualisationHelpers
 * @author jabier.martinez
 *
 */
public class VisualisationsHelper {

	public static final String VISUALISATIONS_EXTENSIONPOINT = "org.but4reuse.visualisation";
	
	public static List<IVisualisation> getAllVisualisations(){
		List<IVisualisation> visualisations = new ArrayList<IVisualisation>();
		IConfigurationElement[] visualisationExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				VISUALISATIONS_EXTENSIONPOINT);
		for (IConfigurationElement visualisationExtensionPoint : visualisationExtensionPoints) {
			try {
				visualisations.add((IVisualisation) visualisationExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return visualisations;
	}
}
