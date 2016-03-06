package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
	private Map<String, ActualFeature> mapIdWithFeature;
	private Map<ActualFeature, List<String>> mapFeatureWithDependencies;	
	public Map<ActualFeature, String> linkFeaturesAndPath;

	public FeaturesAndPluginsDependencies(List<ActualFeature> allFeatures){
		this.allFeatures = allFeatures;
		initMaps();
	}
	
	private void initMaps(){
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

	
	
	public void initLinkFeaturesPath(String eclipseInstallationURI) throws Exception{
		linkFeaturesAndPath = new HashMap<>();
		File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
		File featuresFolder = new File(eclipseFile, "features");
		for (File fFolder : featuresFolder.listFiles()) {
			if (FeatureHelper.isAFeature(fFolder)) {
				ActualFeature f = FeatureInfosExtractor.getFeatureInfos(fFolder.getAbsolutePath());
				linkFeaturesAndPath.put(f, fFolder.getAbsolutePath());
			}
		}
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
		
		FeaturesAndPluginsDependencies f1 = new FeaturesAndPluginsDependencies(allFeatures);
		try {
			f1.initLinkFeaturesPath(u.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
					System.out.println(f1.linkFeaturesAndPath.get(feat.get(j)));
					f=new File(f1.linkFeaturesAndPath.get(feat.get(j)));
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
