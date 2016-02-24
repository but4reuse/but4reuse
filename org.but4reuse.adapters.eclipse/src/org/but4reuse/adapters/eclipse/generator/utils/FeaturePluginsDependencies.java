package org.but4reuse.adapters.eclipse.generator.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;

public class FeaturePluginsDependencies {

	private List<ActualFeature> allFeatures;
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithDependencies;
	
	public FeaturePluginsDependencies(List<ActualFeature> allFeatures){
		this.allFeatures = allFeatures;
	}
	
	public void initMaps(){
		mapIdWithFeature = getMapIdWithFeatureFromListFeatures(allFeatures);
		mapFeatureWithDependencies = getMapFeatureWithDependenciesFromListFeatures(allFeatures);
	}
	
	private Map<String, ActualFeature> getMapIdWithFeatureFromListFeatures(List<ActualFeature> allFeatures) {
		if(allFeatures == null || allFeatures.isEmpty()) return null;
		
		Map<String, ActualFeature> map = new HashMap<>();
		for(ActualFeature oneFeature : allFeatures){
			map.put(oneFeature.getId(), oneFeature);
		}
		
		return map;
	}
	
	private Map<ActualFeature, List<String>> getMapFeatureWithDependenciesFromListFeatures(List<ActualFeature> allFeatures) {
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
	
	// TODO : Faire une methode pour récupérer la liste List<ActualFeatures> des dépendances d'un Feature passé en paramètre
	// Pour cela, utiliser les 2 maps, et les fonctions récursives ci-dessous.
	
	
//	public static Map<ActualFeature, String> depFeat;
//	public static Map<ActualFeature, String> depPlug;
//	
//	public static void dependanceFeatureList(List<ActualFeature> features, List<ActualFeature> allFeatures){
//		if(features.size()>0 && allFeatures.size()>0){
//			for(int i=0; i<features.size();i++){
//				depFeat.put(features.get(i), features.get(i).getName());
//				dependanceFeature(features.get(i), allFeatures);
//			}
//		}
//	}
//	
//	public static void dependanceFeature(ActualFeature features, List<ActualFeature> allFeatures){
//		dependanceFeatureList(FeatureHelper.getAllIncludedFeatures(allFeatures, features),allFeatures);
//		dependanceFeatureList(FeatureHelper.getAllRequiredFeatures(allFeatures, features),allFeatures);
//	}
//	
//	public static void dependancePluginList(List<ActualFeature> plugins, List<ActualFeature> allPlugins){
//		if(plugins.size()>0 && allPlugins.size()>0){
//			for(int i=0; i<plugins.size();i++){
//				depPlug.put(plugins.get(i), " ");
//				dependanceFeature(plugins.get(i), allPlugins);
//			}
//		}
//	}
//	
//	public static void dependancePlugin(ActualFeature plugins, List<ActualFeature> allPlugins){
//		//TODO Find method getAllRequiredPlugins like getAllrequiredFeatures
//		//dependanceFeatureList(FeatureHelper.getAllRequiredPlugins(allPlugins, plugins),allPlugins);
//	}
//	
//	public static void parcourMap(){
//		int i=0;
//		for(Entry<ActualFeature, String> map : depFeat.entrySet()) {
//		    ActualFeature cle = map.getKey();
//		    String valeur = map.getValue();
//		    System.out.println("Features Name = "+cle.getName());
//		    i++;
//		}
//		System.out.println(i);
//	}
}
