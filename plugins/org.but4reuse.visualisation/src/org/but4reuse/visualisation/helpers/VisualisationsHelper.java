package org.but4reuse.visualisation.helpers;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.visualisation.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * VisualisationHelpers
 * 
 * @author jabier.martinez
 * 
 */
public class VisualisationsHelper {

	public static final String VISUALISATIONS_EXTENSIONPOINT = "org.but4reuse.visualisation";

	static List<IVisualisation> cache_visualisations;

	/**
	 * Get all the registered visualisations
	 * 
	 * @return
	 */
	public static List<IVisualisation> getAllVisualisations() {
		if (cache_visualisations != null) {
			return cache_visualisations;
		}
		List<IVisualisation> visualisations = new ArrayList<IVisualisation>();
		IConfigurationElement[] visualisationExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(VISUALISATIONS_EXTENSIONPOINT);
		for (IConfigurationElement visualisationExtensionPoint : visualisationExtensionPoints) {
			try {
				visualisations.add((IVisualisation) visualisationExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_visualisations = visualisations;
		return visualisations;
	}

	/**
	 * Notify changes and show them
	 * 
	 * @param featureList
	 * @param adaptedModel
	 * @param extra
	 * @param monitor
	 */
	public static void notifyVisualisations(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		List<IVisualisation> all = getSelectedVisualisations();
		for (IVisualisation v : all) {
			v.prepare(featureList, adaptedModel, extra, monitor);
			v.show();
		}
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getVisualisationName(IVisualisation algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry().getConfigurationElementsFor(
				VISUALISATIONS_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IVisualisation oneAlgo = (IVisualisation) adapterExtensionPoint.createExecutableExtension("class");
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

	public static List<IVisualisation> getSelectedVisualisations() {
		List<IVisualisation> selected = new ArrayList<IVisualisation>();
		for (IVisualisation algo : getAllVisualisations()) {
			if (isVisualisationSelected(algo)) {
				selected.add(algo);
			}
		}
		return selected;
	}

	public static boolean isVisualisationSelected(IVisualisation algo) {
		String algoName = getVisualisationName(algo);
		IPreferenceStore prefs = getPreferenceStore();
		return prefs.getBoolean(algoName);
	}
}
