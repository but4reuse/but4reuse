package org.but4reuse.adapters.eclipse.generator.utils;

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

public class FeaturesAndPluginsDependencies {

	private String eclipseInstallationURI;
	
	private List<ActualFeature> allFeatures;
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithFeaturesDependencies;	
	private Map<ActualFeature, String> mapFeatureWithPath;
	
	private List<PluginElement> allPlugins;
	private Map<String, List<PluginElement>> mapSymbNameWithPlugin;
	private Map<ActualFeature, List<String>> mapFeatureWithPluginsDependencies;
	private List<PluginElement> allPluginsWithoutFeaturesDependencies;

	public FeaturesAndPluginsDependencies(List<ActualFeature> allFeatures, List<PluginElement> allPlugins, String eclipseInstallationURI){
		this.allFeatures = allFeatures;
		this.allPlugins = allPlugins;
		this.eclipseInstallationURI = eclipseInstallationURI;
		initMaps();
	}
	
	private void initMaps(){
		initMapIdWithFeature(allFeatures);
		initMapFeatureWithFeaturesDependencies(allFeatures);
		try {
			initMapFeaturesPath(eclipseInstallationURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		initMapNameWithPlugin(allPlugins);
		initMapFeatureWithPluginsDependencies(allFeatures);
		initAllPluginsWithoutFeaturesDependencies(mapFeatureWithPluginsDependencies);
	}
	
	
	/********* Features **********/
	
	private void initMapIdWithFeature(List<ActualFeature> listAllFeatures) {
		if(listAllFeatures == null || listAllFeatures.isEmpty()) return;
		
		mapIdWithFeature = new HashMap<>();
		for(ActualFeature oneFeature : listAllFeatures){
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}
	}
	
	private void initMapFeatureWithFeaturesDependencies(List<ActualFeature> listAllFeatures) {
		if(listAllFeatures == null || listAllFeatures.isEmpty()) return;

		mapFeatureWithFeaturesDependencies = new HashMap<>();
		List<String> dependencies =null;
		
		for(ActualFeature oneFeature : listAllFeatures){
			dependencies=oneFeature.getRequiredFeatures();
			List<String> included = oneFeature.getIncludedFeatures();
			if(included != null && !included.isEmpty()){
				if(dependencies != null && !dependencies.isEmpty()){
					for(int i=0; i<included.size();i++){
						if(!dependencies.contains(included.get(i))){
							dependencies.add(included.get(i));
						}
					}
				} else {
					dependencies=included;
				}
			}
			mapFeatureWithFeaturesDependencies.put(oneFeature, dependencies);
		}
	}
	
	private void initMapFeaturesPath(String eclipseInstallationURI) throws URISyntaxException{
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
	
	public List<ActualFeature> getFeaturesDependencies(ActualFeature actual){
		List<String> actuaDependencies = mapFeatureWithFeaturesDependencies.get(actual);
		if( actuaDependencies != null && !actuaDependencies.isEmpty()){
			return getFeaturesDependenciesFromListIds(actuaDependencies);
		}
		return null;
	}
	
	private List<ActualFeature> getFeaturesDependenciesFromListIds(List<String> listIds){
		if(listIds != null && !listIds.isEmpty()){
			List<ActualFeature> listDependencies = new ArrayList<ActualFeature>();
			for(int i=0; i<listIds.size();i++){
				ActualFeature actFeat = mapIdWithFeature.get(listIds.get(i));
				if(actFeat != null && !listDependencies.contains(actFeat)){
					listDependencies.add(actFeat);
					List<ActualFeature> list_tmp = getFeaturesDependenciesFromListIds(mapFeatureWithFeaturesDependencies.get(actFeat));
					if(list_tmp != null && !list_tmp.isEmpty()) listDependencies.addAll(list_tmp);
				}
			}
			return listDependencies;
		}
		return null;
	}

	public List<String> getListPathFromListFeatures(List<ActualFeature> features){
		List<String> paths = new ArrayList<>(features.size());
		for(ActualFeature feature : features){
			if(feature!=null) paths.add(mapFeatureWithPath.get(feature));
		}
		return paths;
	}
	
	public String getPathFromFeature(ActualFeature feature){
		if(feature==null) return null;
		else return mapFeatureWithPath.get(feature);
	}
	
	
	/*********** Plugins ************/
	
	private void initMapNameWithPlugin(List<PluginElement> listPlugins) {
		if(listPlugins == null || listPlugins.isEmpty()) return;
		
		mapSymbNameWithPlugin = new HashMap<>();
		for(PluginElement onePlugin : listPlugins){
			if(mapSymbNameWithPlugin.containsKey(onePlugin.getSymbName())){
				mapSymbNameWithPlugin.get(onePlugin.getSymbName()).add(onePlugin);
			} else {
				List<PluginElement> pluginVersionList = new ArrayList<>(1); // Most of times, there is only one version by SymbName
				pluginVersionList.add(onePlugin);
				mapSymbNameWithPlugin.put(onePlugin.getSymbName(), pluginVersionList );
			}
		}
	}
	
	private void initMapFeatureWithPluginsDependencies(List<ActualFeature> listAllFeatures) {
		if(listAllFeatures == null || listAllFeatures.isEmpty()) return;

		mapFeatureWithPluginsDependencies = new HashMap<>();
		List<String> dependencies =null;
		
		for(ActualFeature oneFeature : listAllFeatures){
			dependencies = oneFeature.getPlugins();
			List<String> required = oneFeature.getRequiredPlugins();
			if(required != null && !required.isEmpty()){
				if(dependencies != null && !dependencies.isEmpty()){
					for(int i=0; i<required.size();i++){
						if(!dependencies.contains(required.get(i))){
							dependencies.add(required.get(i));
						}
					}
				} else {
					dependencies=required;
				}
			}
			mapFeatureWithPluginsDependencies.put(oneFeature, dependencies);
		}
	}

	
	public List<PluginElement> getPluginsDependencies(ActualFeature actual){
		List<String> pluginsDependencies = mapFeatureWithPluginsDependencies.get(actual);
		if( pluginsDependencies != null && !pluginsDependencies.isEmpty()){
			return getPluginsFromListSymbName(pluginsDependencies);
		}
		return null;
	}
	
	private void initAllPluginsWithoutFeaturesDependencies(Map<ActualFeature, List<String>> mapFeatureWithPluginsDependencies){
		allPluginsWithoutFeaturesDependencies = new ArrayList<>();
		allPluginsWithoutFeaturesDependencies.addAll(allPlugins);  // eclipseFull Kepler = 2066 plugins
		for(PluginElement oneP : getAllPluginsDependencies(mapFeatureWithPluginsDependencies)){ // all Plugins from Features dependencies = 1947
			allPluginsWithoutFeaturesDependencies.remove(oneP);  // We don't use removeAll because it remove all occurences, but we want to remove just one at each time
		}
	}
	
	private List<PluginElement> getAllPluginsDependencies(Map<ActualFeature, List<String>> mapFeatureWithPluginsDependencies) {
		List<String> plugins = new ArrayList<>();
		for(List<String> list : mapFeatureWithPluginsDependencies.values()){
			for(String s : list){
				if(!plugins.contains(s)) plugins.add(s);
			}
		}
		return getPluginsFromListSymbName(plugins);
	}

	private List<PluginElement> getPluginsFromListSymbName(List<String> listSymbName){
		if(listSymbName != null && !listSymbName.isEmpty()){
			List<PluginElement> listPlugins = new ArrayList<PluginElement>();
			for(int i=0; i<listSymbName.size();i++){
				List<PluginElement> pluginVersions = mapSymbNameWithPlugin.get(listSymbName.get(i));
				for(PluginElement onePlugin : pluginVersions){
					if(onePlugin != null && !listPlugins.contains(onePlugin)){
						listPlugins.add(onePlugin);
					}
				}
			}
			return listPlugins;
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	private List<String> listPluginToString(List<PluginElement> list){
		List<String> toList = new ArrayList<>(list.size());
		for(PluginElement elem : list) toList.add(elem.getSymbName());
		return toList;
	}

	public List<PluginElement> getPluginsWithoutAnyFeaturesDependencies() {
		return allPluginsWithoutFeaturesDependencies;
	}
	
}
