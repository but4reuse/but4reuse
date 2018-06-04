package org.but4reuse.adapters.eclipse.benchmark.generator.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.benchmark.FeatureInfosExtractor;
import org.but4reuse.utils.files.FileUtils;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class DependencyAnalyzer {

	private String eclipseInstallationURI;

	private List<ActualFeature> allFeatures;
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithFeatureDependencies;
	private Map<ActualFeature, String> mapFeatureWithPath;

	private List<PluginElement> allPlugins;
	private Map<String, List<PluginElement>> mapSymbNameWithPlugin;
	private Map<ActualFeature, List<String>> mapFeatureWithPluginDependencies;
	private List<PluginElement> allPluginsWithoutFeatureDependencies;
	private List<ActualFeature> allMandatoryFeaturesForThisInput;

	public DependencyAnalyzer(List<ActualFeature> allFeatures, List<PluginElement> allPlugins,
			String eclipseInstallationURI) {
		this.allFeatures = allFeatures;
		this.allPlugins = allPlugins;
		this.eclipseInstallationURI = eclipseInstallationURI;
		initMapsAndLists();
	}

	private void initMapsAndLists() {
		initMapIdWithFeature();
		initMapFeatureWithFeatureDependencies();
		try {
			initMapFeaturesPath(eclipseInstallationURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		initMapNameWithPlugin();
		initMapFeatureWithPluginDependencies();
		initAllPluginsWithoutFeatureDependencies();
		initListMandatoryFeatures();
	}

	/********* Features **********/

	public List<ActualFeature> getFeatureDependencies(ActualFeature actual) {
		List<String> actualDependencies = mapFeatureWithFeatureDependencies.get(actual);
		if (actualDependencies != null && !actualDependencies.isEmpty()) {
			return getFeatureDependenciesFromListIds(actualDependencies);
		}
		return null;
	}

	public List<String> getListPathFromListFeatures(List<ActualFeature> features) {
		List<String> paths = new ArrayList<String>();
		for (ActualFeature feature : features) {
			if (feature != null) {
				paths.add(mapFeatureWithPath.get(feature));
			}
		}
		return paths;
	}

	public String getPathFromFeature(ActualFeature feature) {
		if (feature == null) {
			return null;
		} else {
			return mapFeatureWithPath.get(feature);
		}
	}

	private void initMapIdWithFeature() {
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		mapIdWithFeature = new HashMap<String, ActualFeature>();
		for (ActualFeature oneFeature : allFeatures) {
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}
	}

	private void initMapFeatureWithFeatureDependencies() {
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		mapFeatureWithFeatureDependencies = new HashMap<ActualFeature, List<String>>();
		List<String> dependencies = null;

		for (ActualFeature oneFeature : allFeatures) {
			dependencies = oneFeature.getRequiredFeatures();
			List<String> included = oneFeature.getIncludedFeatures();
			if (included != null && !included.isEmpty()) {
				if (dependencies != null && !dependencies.isEmpty()) {
					for (int i = 0; i < included.size(); i++) {
						if (!dependencies.contains(included.get(i))) {
							dependencies.add(included.get(i));
						}
					}
				} else {
					dependencies = included;
				}
			}
			mapFeatureWithFeatureDependencies.put(oneFeature, dependencies);
		}
	}

	private void initMapFeaturesPath(String eclipseInstallationURI) throws URISyntaxException {
		mapFeatureWithPath = new HashMap<ActualFeature, String>();
		File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
		File featuresFolder = new File(eclipseFile, "features");
		for (File fFolder : featuresFolder.listFiles()) {
			if (FeatureHelper.isAFeature(fFolder)) {
				ActualFeature f = FeatureInfosExtractor.getFeatureInfos(fFolder.getAbsolutePath());
				mapFeatureWithPath.put(f, fFolder.getAbsolutePath());
			}
		}
	}

	private List<ActualFeature> getFeatureDependenciesFromListIds(List<String> listIds) {
		if (listIds != null && !listIds.isEmpty()) {
			List<ActualFeature> listDependencies = new ArrayList<ActualFeature>();
			for (int i = 0; i < listIds.size(); i++) {
				ActualFeature actFeat = mapIdWithFeature.get(listIds.get(i));
				if (actFeat != null && !listDependencies.contains(actFeat)) {
					listDependencies.add(actFeat);
					List<ActualFeature> list_tmp = getFeatureDependenciesFromListIds(mapFeatureWithFeatureDependencies
							.get(actFeat));
					if (list_tmp != null && !list_tmp.isEmpty())
						listDependencies.addAll(list_tmp);
				}
			}
			return listDependencies;
		}
		return null;
	}

	/*********** Plugins ************/

	public List<PluginElement> getPluginDependencies(ActualFeature actual) {
		List<String> pluginsDependencies = mapFeatureWithPluginDependencies.get(actual);
		if (pluginsDependencies != null && !pluginsDependencies.isEmpty()) {
			return getPluginsFromListSymbName(pluginsDependencies);
		}
		return null;
	}

	public List<PluginElement> getPluginsWithoutAnyFeatureDependencies() {
		return allPluginsWithoutFeatureDependencies;
	}

	public List<ActualFeature> getMandatoryFeaturesForThisInput() {
		return allMandatoryFeaturesForThisInput;
	}

	private void initMapNameWithPlugin() {
		if (allPlugins == null || allPlugins.isEmpty())
			return;

		mapSymbNameWithPlugin = new HashMap<String, List<PluginElement>>();
		for (PluginElement onePlugin : allPlugins) {
			if (mapSymbNameWithPlugin.containsKey(onePlugin.getSymbName())) {
				mapSymbNameWithPlugin.get(onePlugin.getSymbName()).add(onePlugin);
			} else {
				// Most of times, there is only one version by SymbName
				List<PluginElement> pluginVersionList = new ArrayList<PluginElement>();
				pluginVersionList.add(onePlugin);
				mapSymbNameWithPlugin.put(onePlugin.getSymbName(), pluginVersionList);
			}
		}
	}

	private void initMapFeatureWithPluginDependencies() {
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		mapFeatureWithPluginDependencies = new HashMap<ActualFeature, List<String>>();
		List<String> dependencies = null;

		for (ActualFeature oneFeature : allFeatures) {
			dependencies = oneFeature.getPlugins();
			List<String> required = oneFeature.getRequiredPlugins();
			if (required != null && !required.isEmpty()) {
				// there is at least one
				if (dependencies != null && !dependencies.isEmpty()) {
					for (String req : required) {
						if (!dependencies.contains(req))
							dependencies.add(req);
					}
				} else {
					dependencies = required;
				}
			}
			mapFeatureWithPluginDependencies.put(oneFeature, dependencies);
		}
	}

	private void initAllPluginsWithoutFeatureDependencies() {
		allPluginsWithoutFeatureDependencies = new ArrayList<PluginElement>();
		allPluginsWithoutFeatureDependencies.addAll(allPlugins);
		for (PluginElement oneP : getAllPluginDependencies(mapFeatureWithPluginDependencies)) {
			// We don't use removeAll because it remove all occurrences, but we
			// want to remove just one at each time
			allPluginsWithoutFeatureDependencies.remove(oneP);
		}
	}

	private void initListMandatoryFeatures() {
		allMandatoryFeaturesForThisInput = new ArrayList<ActualFeature>(MandatoryFeatures.list.length);
		for (String manda : MandatoryFeatures.list) {
			List<ActualFeature> feat_tmp = getFeaturesThatStartsWithName(manda);
			if (feat_tmp != null) {
				allMandatoryFeaturesForThisInput.addAll(feat_tmp);
			}
		}
	}

	private List<PluginElement> getAllPluginDependencies(
			Map<ActualFeature, List<String>> mapFeatureWithPluginDependencies) {
		List<String> plugins = new ArrayList<String>();
		for (List<String> list : mapFeatureWithPluginDependencies.values()) {
			for (String s : list) {
				if (!plugins.contains(s)) {
					plugins.add(s);
				}
			}
		}
		return getPluginsFromListSymbName(plugins);
	}

	private List<PluginElement> getPluginsFromListSymbName(List<String> listSymbName) {
		if (listSymbName != null && !listSymbName.isEmpty()) {
			List<PluginElement> listPlugins = new ArrayList<PluginElement>();
			List<PluginElement> pluginVersions;
			for (int i = 0; i < listSymbName.size(); i++) {
				pluginVersions = mapSymbNameWithPlugin.get(listSymbName.get(i));
				if (pluginVersions != null) {
					for (PluginElement onePlugin : pluginVersions) {
						if (onePlugin != null && !listPlugins.contains(onePlugin)) {
							listPlugins.add(onePlugin);
						}
					}
				}
			}
			return listPlugins;
		}
		return null;
	}

	private List<ActualFeature> getFeaturesThatStartsWithName(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		List<ActualFeature> featuresWithName = new ArrayList<ActualFeature>();
		for (ActualFeature feature : allFeatures) {
			if (feature.getId().startsWith(name)) {
				featuresWithName.add(feature);
			}
		}
		return featuresWithName;
	}

}
