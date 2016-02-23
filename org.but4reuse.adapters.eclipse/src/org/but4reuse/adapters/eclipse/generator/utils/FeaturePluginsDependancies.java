package org.but4reuse.adapters.eclipse.generator.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;

public class FeaturePluginsDependancies {
	public static Map<ActualFeature, String> depFeat;
	public static Map<ActualFeature, String> depPlug;
	
	public static void listDependance(String url){
		
		List<ActualFeature> Allfeatures = null;
		depFeat = new HashMap();
		depPlug = new HashMap();
		try {
			Allfeatures = FeatureHelper.getFeaturesOfEclipse(url);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		for (int i=0; i<100; i++){
			System.out.println("features.get("+i+".getName() = "+Allfeatures.get(i).getName());
			for(int j=0; j<Allfeatures.get(i).getPlugins().size();j++)
				System.out.println("	features.get("+i+").getPlugins().get("+j+") = "+Allfeatures.get(i).getPlugins().get(j));
			
			for(int j=0; j<Allfeatures.get(i).getRequiredFeatures().size();j++){
				List<ActualFeature> features2 = FeatureHelper.getAllRequiredFeatures(Allfeatures, Allfeatures.get(i));
				dependanceFeatureList(features2,Allfeatures);
				System.out.println("	features.get("+i+").getRequiredFeatures().get("+j+") = "+Allfeatures.get(i).getRequiredFeatures().get(j));
			}
			for(int j=0; j<Allfeatures.get(i).getRequiredPlugins().size();j++){
				//TODO Find method getAllRequiredPlugins like getAllrequiredFeatures
				//List<ActualFeature> features2 = FeatureHelper.getAllRequiredPlugins(Allfeatures, Allfeatures.get(i));
				//dependanceFeatureList(features2,Allfeatures);
				System.out.println("	features.get("+i+").getRequiredPlugins().get("+j+") = "+Allfeatures.get(i).getRequiredPlugins().get(j));
			}
			for(int j=0; j<Allfeatures.get(i).getIncludedFeatures().size();j++){
				List<ActualFeature> features2 = FeatureHelper.getAllIncludedFeatures(Allfeatures, Allfeatures.get(i));
				dependanceFeatureList(features2,Allfeatures);
				System.out.println("	features.get("+i+").getIncludedFeatures().get("+j+") = "+Allfeatures.get(i).getIncludedFeatures().get(j));
			}
			System.out.println("features.get("+i+").getDescription() = "+Allfeatures.get(i).getDescription()+"\n");
		}
		parcourMap();
	}
	
	
	
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
		    String valeur = map.getValue();
		    System.out.println("Features Name = "+cle.getName());
		    i++;
		}
		System.out.println(i);
	}
}
