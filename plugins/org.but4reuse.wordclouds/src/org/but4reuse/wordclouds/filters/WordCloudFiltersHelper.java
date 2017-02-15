package org.but4reuse.wordclouds.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.wordclouds.activator.Activator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Word cloud filters Helper
 * 
 * @author jabier.martinez
 * 
 */
public class WordCloudFiltersHelper {

	public static final String FILTER_EXTENSIONPOINT = "org.but4reuse.wordclouds.filter";

	static List<IWordsProcessing> cache_wordsprocessing;

	/**
	 * Get all the registered filters
	 * 
	 * @return
	 */
	public static List<IWordsProcessing> getAllFilters() {
		if (cache_wordsprocessing != null) {
			return cache_wordsprocessing;
		}
		List<IWordsProcessing> visualisations = new ArrayList<IWordsProcessing>();
		IConfigurationElement[] visualisationExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FILTER_EXTENSIONPOINT);
		for (IConfigurationElement visualisationExtensionPoint : visualisationExtensionPoints) {
			try {
				visualisations.add((IWordsProcessing) visualisationExtensionPoint.createExecutableExtension("class"));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		cache_wordsprocessing = visualisations;
		return visualisations;
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static String getFilterName(IWordsProcessing algo) {
		IConfigurationElement[] adapterExtensionPoints = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(FILTER_EXTENSIONPOINT);
		for (IConfigurationElement adapterExtensionPoint : adapterExtensionPoints) {
			try {
				IWordsProcessing oneAlgo = (IWordsProcessing) adapterExtensionPoint.createExecutableExtension("class");
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

	public static List<IWordsProcessing> getSortedSelectedFilters() {
		Map<IWordsProcessing, Integer> map = new LinkedHashMap<IWordsProcessing, Integer>();
		for (IWordsProcessing algo : getAllFilters()) {
			String name = getFilterName(algo);
			IPreferenceStore prefs = getPreferenceStore();
			int priority = prefs.getInt(name);
			// 0 means not selected
			if (priority > 0) {
				map.put(algo, priority);
			}
		}

		// sort a hashmap by the values
		List<Map.Entry<IWordsProcessing, Integer>> entries = new ArrayList<Map.Entry<IWordsProcessing, Integer>>(
				map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<IWordsProcessing, Integer>>() {
			public int compare(Map.Entry<IWordsProcessing, Integer> a, Map.Entry<IWordsProcessing, Integer> b) {
				return b.getValue().compareTo(a.getValue());
			}
		});
		LinkedHashMap<IWordsProcessing, Integer> sortedMap = new LinkedHashMap<IWordsProcessing, Integer>();
		for (Map.Entry<IWordsProcessing, Integer> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		// from set to list
		List<IWordsProcessing> toReturn = new ArrayList<IWordsProcessing>();
		for (IWordsProcessing wp : sortedMap.keySet()) {
			toReturn.add(wp);
		}
		return toReturn;
	}

}
