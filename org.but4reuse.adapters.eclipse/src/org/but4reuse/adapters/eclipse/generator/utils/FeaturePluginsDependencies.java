package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;

public class FeaturePluginsDependencies {

	private static List<ActualFeature> allFeatures;
	private static Map<String, ActualFeature> mapIdWithFeature;
	private static Map<ActualFeature, List<String>> mapFeatureWithDependencies;	
	public static Map<ActualFeature, String> depPlug;
	
	
	public static Map<ActualFeature, String> depFeat;
	
	public FeaturePluginsDependencies(List<ActualFeature> allFeatures){
		FeaturePluginsDependencies.allFeatures = allFeatures;
	}
	
	public static void initMaps(){
		mapIdWithFeature = getMapIdWithFeatureFromListFeatures(allFeatures);
		mapFeatureWithDependencies = getMapFeatureWithDependenciesFromListFeatures(allFeatures);
	}
	
	private static Map<String, ActualFeature> getMapIdWithFeatureFromListFeatures(List<ActualFeature> allFeatures) {
		if(allFeatures == null || allFeatures.isEmpty()) return null;
		
		Map<String, ActualFeature> map = new HashMap<>();
		for(ActualFeature oneFeature : allFeatures){
			map.put(oneFeature.getId(), oneFeature);
		}
		
		return map;
	}
	
	private static Map<ActualFeature, List<String>> getMapFeatureWithDependenciesFromListFeatures(List<ActualFeature> allFeatures) {
		if(allFeatures == null || allFeatures.isEmpty()) return null;
		List<String> dependencies =null;
		
		Map<ActualFeature, List<String>> map = new HashMap<>();
		for(ActualFeature oneFeature : allFeatures){
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
			map.put(oneFeature, dependencies);
		}
		return map;
	}
	
	private static List<ActualFeature> listDependence;
	
	public static List<ActualFeature> getDependence(ActualFeature actual){
		listDependence=new ArrayList<ActualFeature>();
		if(mapFeatureWithDependencies.get(actual) != null && !(mapFeatureWithDependencies.get(actual).isEmpty())){
			getDep(mapFeatureWithDependencies.get(actual));
		}
		return listDependence;
	}
	
	
	public static void getDep(List<String> list){
		if(list != null && !list.isEmpty()){
			for(int i=0; i<list.size();i++){
				ActualFeature a= mapIdWithFeature.get(list.get(i));
				if(a != null)
					if(!listDependence.contains(a))
						listDependence.add(a);
				getDep(mapFeatureWithDependencies.get(a));
			}
		}
	}

	
	// TODO : Faire une methode pour récupérer la liste List<ActualFeatures> des dépendances d'un Feature passé en paramètre
	// Pour cela, utiliser les 2 maps, et les fonctions récursives ci-dessous.
	

	public static void dependanceFeatureList(List<ActualFeature> features, List<ActualFeature> allFeatures){
		if(features.size()>0 && allFeatures.size()>0){
			for(int i=0; i<features.size();i++){
				depFeat.put(features.get(i), features.get(i).getName());
				dependanceFeature(features.get(i), allFeatures);
			}
		}
	}
	
	public static void dependanceFeature(ActualFeature features, List<ActualFeature> allFeatures){
		dependanceFeatureList(FeatureHelper.getAllIncludedFeatures(allFeatures, features),allFeatures);
		dependanceFeatureList(FeatureHelper.getAllRequiredFeatures(allFeatures, features),allFeatures);
	}
	
	public static void dependancePluginList(List<ActualFeature> plugins, List<ActualFeature> allPlugins){
		if(plugins.size()>0 && allPlugins.size()>0){
			for(int i=0; i<plugins.size();i++){
				depPlug.put(plugins.get(i), " ");
				dependanceFeature(plugins.get(i), allPlugins);
			}
		}
	}
	
	public static void dependancePlugin(ActualFeature plugins, List<ActualFeature> allPlugins){
		//TODO Find method getAllRequiredPlugins like getAllrequiredFeatures
		//dependanceFeatureList(FeatureHelper.getAllRequiredPlugins(allPlugins, plugins),allPlugins);
	}
	
	public static void parcourMap(){
		int i=0;
		for(Entry<ActualFeature, String> map : depFeat.entrySet()) {
		    ActualFeature cle = map.getKey();
		    System.out.println("Features Name = "+cle.getName());
		    i++;
		}
		System.out.println(i);
	}

	
	public static void main(String[] args) throws IOException{
	
		List<ActualFeature> allFeatures = null;
		depFeat = new HashMap<>();
		depPlug = new HashMap<>();
		Map<String, String> map = PreferenceUtils.getPreferencesMap();
		File f = new File(map.get("input"));
		java.net.URI u = f.toURI();
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(u.toString());
		} catch (Exception e1) {
			System.out.println("Impossible to recover all the features from : "+u.toString());
			return;
		}
		
		FeaturePluginsDependencies f1 = new FeaturePluginsDependencies(allFeatures);
		initMaps();
		
		for (int i=0; i<allFeatures.size(); i++){
			ActualFeature oneFeat = allFeatures.get(i);
			List<ActualFeature> feat=getDependence(oneFeat);
			int nbDirectDepend = oneFeat.getIncludedFeatures().size() + oneFeat.getRequiredFeatures().size();
			System.out.println("Number of direct dependence of features \""+oneFeat.getId()+"\""
					+ " = "+nbDirectDepend);
			System.out.println("Number of direct and indirect dependence "+feat.size());
		}
	}
}
