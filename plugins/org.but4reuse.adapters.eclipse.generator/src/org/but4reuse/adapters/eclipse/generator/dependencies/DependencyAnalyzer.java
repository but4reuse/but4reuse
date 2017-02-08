package org.but4reuse.adapters.eclipse.generator.dependencies;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.benchmark.FeatureInfosExtractor;
import org.but4reuse.adapters.eclipse.generator.utils.PluginElementGenerator;
import org.but4reuse.utils.files.FileUtils;

/**
 * @author Julien Margarido
 * @author Felix Lima Gorito
 */
public class DependencyAnalyzer {

	private String eclipseInstallationURI;

	private List<ActualFeature> allFeatures;
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithFeaturesDependencies;
	private Map<ActualFeature, String> mapFeatureWithPath;

	private List<PluginElementGenerator> allPlugins;
	private Map<String, List<PluginElementGenerator>> mapSymbNameWithPlugin;
	private Map<ActualFeature, List<String>> mapFeatureWithPluginsDependencies;
	private List<PluginElementGenerator> allPluginsWithoutFeatureDependencies;
	private List<ActualFeature> allMandatoryFeaturesForThisInput;

	public DependencyAnalyzer(List<ActualFeature> allFeatures, List<PluginElementGenerator> allPlugins,
			String eclipseInstallationURI) {
		this.allFeatures = allFeatures;
		this.allPlugins = allPlugins;
		this.eclipseInstallationURI = eclipseInstallationURI;
		initMapsAndLists();
	}

	private void initMapsAndLists() {
		initMapIdWithFeature();
		initMapFeatureWithFeaturesDependencies();
		try {
			initMapFeaturesPath(eclipseInstallationURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		initMapNameWithPlugin();
		initMapFeatureWithPluginsDependencies();
		initAllPluginsWithoutFeaturesDependencies();
		initListFeaturesMandatories();
	}

	/********* Features **********/

	public List<ActualFeature> getFeaturesDependencies(ActualFeature actual) {
		List<String> actuaDependencies = mapFeatureWithFeaturesDependencies.get(actual);
		if (actuaDependencies != null && !actuaDependencies.isEmpty()) {
			return getFeaturesDependenciesFromListIds(actuaDependencies);
		}
		return null;
	}

	public List<String> getListPathFromListFeatures(List<ActualFeature> features) {
		List<String> paths = new ArrayList<>(features.size());
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

		mapIdWithFeature = new HashMap<>();
		for (ActualFeature oneFeature : allFeatures) {
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}
	}

	private void initMapFeatureWithFeaturesDependencies() {
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		mapFeatureWithFeaturesDependencies = new HashMap<>();
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
			mapFeatureWithFeaturesDependencies.put(oneFeature, dependencies);
		}
	}

	private void initMapFeaturesPath(String eclipseInstallationURI) throws URISyntaxException {
		mapFeatureWithPath = new HashMap<>();
		File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
		File featuresFolder = new File(eclipseFile, "features");
		for (File fFolder : featuresFolder.listFiles()) {
			if (FeatureHelper.isAFeature(fFolder)) {
				ActualFeature f = FeatureInfosExtractor.getFeatureInfos(fFolder.getAbsolutePath());
				mapFeatureWithPath.put(f, fFolder.getAbsolutePath());
			}
		}
	}

	private List<ActualFeature> getFeaturesDependenciesFromListIds(List<String> listIds) {
		if (listIds != null && !listIds.isEmpty()) {
			List<ActualFeature> listDependencies = new ArrayList<ActualFeature>();
			for (int i = 0; i < listIds.size(); i++) {
				ActualFeature actFeat = mapIdWithFeature.get(listIds.get(i));
				if (actFeat != null && !listDependencies.contains(actFeat)) {
					listDependencies.add(actFeat);
					List<ActualFeature> list_tmp = getFeaturesDependenciesFromListIds(mapFeatureWithFeaturesDependencies
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

	public List<PluginElementGenerator> getPluginsDependencies(ActualFeature actual) {
		List<String> pluginsDependencies = mapFeatureWithPluginsDependencies.get(actual);
		if (pluginsDependencies != null && !pluginsDependencies.isEmpty()) {
			return getPluginsFromListSymbName(pluginsDependencies);
		}
		return null;
	}

	public List<PluginElementGenerator> getPluginsWithoutAnyFeaturesDependencies() {
		return allPluginsWithoutFeatureDependencies;
	}

	public List<ActualFeature> getMandatoryFeaturesForThisInput() {
		return allMandatoryFeaturesForThisInput;
	}

	private void initMapNameWithPlugin() {
		if (allPlugins == null || allPlugins.isEmpty())
			return;

		mapSymbNameWithPlugin = new HashMap<>();
		for (PluginElementGenerator onePlugin : allPlugins) {
			if (mapSymbNameWithPlugin.containsKey(onePlugin.getSymbName())) {
				mapSymbNameWithPlugin.get(onePlugin.getSymbName()).add(onePlugin);
			} else {
				// Most of times, there is only one version by SymbName
				List<PluginElementGenerator> pluginVersionList = new ArrayList<>(1);
				pluginVersionList.add(onePlugin);
				mapSymbNameWithPlugin.put(onePlugin.getSymbName(), pluginVersionList);
			}
		}
	}

	private void initMapFeatureWithPluginsDependencies() {
		if (allFeatures == null || allFeatures.isEmpty())
			return;

		mapFeatureWithPluginsDependencies = new HashMap<>();
		List<String> dependencies = null;

		for (ActualFeature oneFeature : allFeatures) {
			dependencies = oneFeature.getPlugins();
			List<String> required = oneFeature.getRequiredPlugins();
			if (required != null && !required.isEmpty()) { // y'a un elem dedans
				if (dependencies != null && !dependencies.isEmpty()) {
					for (String req : required) {
						if (!dependencies.contains(req))
							dependencies.add(req);
					}
				} else {
					dependencies = required;
				}
			}
			mapFeatureWithPluginsDependencies.put(oneFeature, dependencies);
		}
	}

	private void initAllPluginsWithoutFeaturesDependencies() {
		allPluginsWithoutFeatureDependencies = new ArrayList<>();
		allPluginsWithoutFeatureDependencies.addAll(allPlugins);
		// eclipseFull Kepler = 2066 plugins
		// all Plugins from Features dependencies = 1947
		for (PluginElementGenerator oneP : getAllPluginDependencies(mapFeatureWithPluginsDependencies)) {
			// We don't use removeAll because it remove all occurrences, but we
			// want to remove just one at each time
			allPluginsWithoutFeatureDependencies.remove(oneP);
		}
	}

	private void initListFeaturesMandatories() {
		allMandatoryFeaturesForThisInput = new ArrayList<>(MandatoryFeatures.list.length);
		for (String manda : MandatoryFeatures.list) {
			List<ActualFeature> feat_tmp = getFeaturesThatStartsWithName(manda);
			if (feat_tmp != null) {
				allMandatoryFeaturesForThisInput.addAll(feat_tmp);
			}
		}
	}

	private List<PluginElementGenerator> getAllPluginDependencies(
			Map<ActualFeature, List<String>> mapFeatureWithPluginDependencies) {
		List<String> plugins = new ArrayList<>();
		for (List<String> list : mapFeatureWithPluginDependencies.values()) {
			for (String s : list) {
				if (!plugins.contains(s)) {
					plugins.add(s);
				}
			}
		}
		return getPluginsFromListSymbName(plugins);
	}

	private List<PluginElementGenerator> getPluginsFromListSymbName(List<String> listSymbName) {
		if (listSymbName != null && !listSymbName.isEmpty()) {
			List<PluginElementGenerator> listPlugins = new ArrayList<PluginElementGenerator>();
			List<PluginElementGenerator> pluginVersions;
			for (int i = 0; i < listSymbName.size(); i++) {
				pluginVersions = mapSymbNameWithPlugin.get(listSymbName.get(i));
				if (pluginVersions != null) {
					for (PluginElementGenerator onePlugin : pluginVersions) {
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
		if (name == null || name.isEmpty())
			return null;

		List<ActualFeature> featuresWithName = new ArrayList<>(10);
		for (ActualFeature feature : allFeatures) {
			if (feature.getId().startsWith(name)) {
				featuresWithName.add(feature);
			}
		}
		return featuresWithName;
	}

}
