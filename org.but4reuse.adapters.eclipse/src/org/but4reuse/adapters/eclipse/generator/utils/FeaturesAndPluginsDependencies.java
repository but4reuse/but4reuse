package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.benchmark.FeatureInfosExtractor;
import org.but4reuse.utils.files.FileUtils;

public class FeaturesAndPluginsDependencies {

	private List<ActualFeature> allFeatures;
	private String eclipseInstallationURI;
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithDependencies;	
	private Map<ActualFeature, String> mapFeatureWithPath;

	public FeaturesAndPluginsDependencies(List<ActualFeature> allFeatures, String eclipseInstallationURI){
		this.allFeatures = allFeatures;
		this.eclipseInstallationURI = eclipseInstallationURI;
		initMaps();
	}
	
	private void initMaps(){
		initMapIdWithFeatureFromListFeatures(allFeatures);
		initMapFeatureWithDependenciesFromListFeatures(allFeatures);
		try {
			initMapFeaturesPath(eclipseInstallationURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void initMapIdWithFeatureFromListFeatures(List<ActualFeature> allFeatures) {
		if(allFeatures == null || allFeatures.isEmpty()) return;
		
		mapIdWithFeature = new HashMap<>();
		for(ActualFeature oneFeature : allFeatures){
			mapIdWithFeature.put(oneFeature.getId(), oneFeature);
		}
	}
	
	private void initMapFeatureWithDependenciesFromListFeatures(List<ActualFeature> allFeatures) {
		if(allFeatures == null || allFeatures.isEmpty()) return;

		mapFeatureWithDependencies = new HashMap<>();
		List<String> dependencies =null;
		
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
			mapFeatureWithDependencies.put(oneFeature, dependencies);
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
	
	public List<ActualFeature> getDependencies(ActualFeature actual){
		List<String> actuaDependencies = mapFeatureWithDependencies.get(actual);
		if( actuaDependencies != null && !actuaDependencies.isEmpty()){
			return getDependenciesFromListIds(actuaDependencies);
		}
		return null;
	}
	
	
	private List<ActualFeature> getDependenciesFromListIds(List<String> listIds){
		if(listIds != null && !listIds.isEmpty()){
			List<ActualFeature> listDependencies = new ArrayList<ActualFeature>();
			for(int i=0; i<listIds.size();i++){
				ActualFeature actFeat = mapIdWithFeature.get(listIds.get(i));
				if(actFeat != null && !listDependencies.contains(actFeat)){
					listDependencies.add(actFeat);
					List<ActualFeature> list_tmp = getDependenciesFromListIds(mapFeatureWithDependencies.get(actFeat));
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
		
		FeaturesAndPluginsDependencies f1 = new FeaturesAndPluginsDependencies(allFeatures, u.toString());

		for (int i=0; i<allFeatures.size(); i++){
			ActualFeature oneFeat = allFeatures.get(i);
			List<ActualFeature> feat= f1.getDependencies(oneFeat);
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
