package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.IOException;
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
	private Map<String, PluginElement> mapSymbNameWithPlugin;
	private Map<ActualFeature, List<String>> mapFeatureWithPluginsDependencies;

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
			mapSymbNameWithPlugin.put(onePlugin.getSymbName(), onePlugin);
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
	
	private List<PluginElement> getPluginsFromListSymbName(List<String> listIds){
		if(listIds != null && !listIds.isEmpty()){
			List<PluginElement> listDependencies = new ArrayList<PluginElement>();
			for(int i=0; i<listIds.size();i++){
				PluginElement plugin = mapSymbNameWithPlugin.get(listIds.get(i));
				if(plugin != null && !listDependencies.contains(plugin)){
					listDependencies.add(plugin);
				}
			}
			return listDependencies;
		}
		return null;
	}
	
	
	public static void main(String[] args) throws IOException{ // Just for tests
	
		List<ActualFeature> allFeatures = null;
		Map<String, String> map = PreferenceUtils.getPreferencesMap();
		File f = new File(map.get("input"));
		java.net.URI u = f.toURI();
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(u.toString());
		} catch (Exception e1) {
			System.out.println("Impossible to recover all the features from : "+u.toString());
			return;
		}
		
		FeaturesAndPluginsDependencies f1 = new FeaturesAndPluginsDependencies(allFeatures, null, u.toString());

		for (int i=0; i<allFeatures.size(); i++){
			ActualFeature oneFeat = allFeatures.get(i);
			List<ActualFeature> feat= f1.getFeaturesDependencies(oneFeat);
			int nbDirectDepend = oneFeat.getIncludedFeatures().size() + oneFeat.getRequiredFeatures().size();
			System.out.println("Number of direct dependencies of \""+oneFeat.getId()+"\""+ " = "+nbDirectDepend);
			
			if(feat==null){
				System.out.println("Number of direct and indirect dependencies  = 0\n");
			}
			else{
				System.out.println("Number of direct and indirect dependencies = "+feat.size());
				for(int j=0; j<feat.size();j++){
					System.out.print("Path du feature : "+feat.get(j).getId()+" = ");
//					System.out.println(f1.linkFeaturesAndPath.get(feat.get(j)));
//					f=new File(f1.linkFeaturesAndPath.get(feat.get(j)));
					FileAndDirectoryUtils.copyDirectory(f, map.get("output"));
				}
				System.out.println("\n");
			}
			
		}
		//System.out.println(FeatureHelper.PATH.get(allFeatures.get(1)));
		System.out.println(allFeatures.size());
		f = new File(f, "features");
		u = f.toURI();

		System.out.println(u);
	}
}
