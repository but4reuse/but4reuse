package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;

public class SplotUtils {

	private static Map<String, String> prefMap;
	private static Map<String, ActualFeature> mapIdWithFeature;
	
	
	public static String getDependencieTree(String r, String id){
		
		ActualFeature oneFeat = mapIdWithFeature.get(id);
		if(oneFeat==null) return "";
		String sousSplot="";
		
		List<String> allDependence= new ArrayList<String>();
		allDependence=oneFeat.getIncludedFeatures();
		
		sousSplot+="\n";
		for(int k=0;k<r.length();k+=2){
			sousSplot+="\t";
		}
		sousSplot +=" :m "+oneFeat.getId()+"("+oneFeat.getName()+")";
		
		for(int j=0;j<allDependence.size();j++){
			sousSplot+=getDependencieTree(r+"_"+j,allDependence.get(j));
		}	

		return sousSplot;
	}
	
	public static String getDependencieContraint(String r, String id){
		
		ActualFeature oneFeat = mapIdWithFeature.get(id);
		if(oneFeat==null) return "";
		String sousSplot="";
		
		List<String> allDependence= new ArrayList<String>();
		if(oneFeat.getRequiredFeatures()!=null){
			allDependence=oneFeat.getRequiredFeatures();
		}
		if(oneFeat.getIncludedFeatures()!=null){
			for(String s : oneFeat.getIncludedFeatures()){
				if(! allDependence.contains(s)) allDependence.add(s);
			}
		}
		
		sousSplot+="\n";
		
		for(int j=0;j<allDependence.size();j++){
			sousSplot+=getDependencieContraint(r+"_"+j,allDependence.get(j));
		}	

		return sousSplot;
	}
	
	
	public static File exportToSPLOT(List<ActualFeature> allFeatures){

		if (allFeatures == null || allFeatures.isEmpty())
			return null;

		String splotARetourner="";

		mapIdWithFeature = new HashMap<>();
		for (ActualFeature oneFeature : allFeatures) mapIdWithFeature.put(oneFeature.getId(), oneFeature);

		splotARetourner+="<feature_model name=\"Eclipse Feature\">\n";
		splotARetourner+="<meta>\n";

		splotARetourner+="</meta>\n";
		splotARetourner+="<feature_tree>\n";
		splotARetourner+="  :r Eclipse Feature(EclipseFeature)\n";
		
		System.out.println(allFeatures.size());
		for(int i=0;i<allFeatures.size();i++){
			
			ActualFeature oneFeat = allFeatures.get(i);
			String name = oneFeat.getName().replace("(", "");
			name = name.replace(")", "");

			
			String id = oneFeat.getId().replace("(", "");
			id = id.replace(")", "");
			id = id.replace(".", "_");
			id = id.replace("-", "666666");
			
			splotARetourner+="\t:o "+name+"("+id+")";
	
			splotARetourner+="\n";
			System.out.println(i);
		}
		splotARetourner+="</feature_tree>\n";
		splotARetourner+="<constraints>";
		int nbContrainte=1;
		for(int i=0;i<allFeatures.size();i++){
			ActualFeature oneFeat = allFeatures.get(i);
			
			String id = oneFeat.getId().replace("(", "");
			id = id.replace(")", "");
			id = id.replace(".", "_");
			
			List<String> allDependence=new ArrayList<String>();
			if(oneFeat.getRequiredFeatures()!=null){
				allDependence=oneFeat.getRequiredFeatures();
			}
			if(oneFeat.getIncludedFeatures()!=null){
				for(String s : oneFeat.getIncludedFeatures()){
					if(! allDependence.contains(s)) allDependence.add(s);
				}
			}
			for(int j=0;j<allDependence.size();j++){
				if(allDependence.get(j)!=null){
					
					if(mapIdWithFeature.get(allDependence.get(j))!=null){
						splotARetourner+="\n";
						splotARetourner+="constraint_"+nbContrainte+":";
						splotARetourner+="~"+id+" or "
								+mapIdWithFeature.get(allDependence.get(j)).getId().replace(".","_").replace("-", "666666").replace("(", "").replace(")", "");
						nbContrainte++;
					}
				}
			}
			System.out.println(i);
		}
		splotARetourner+= "\n</constraints>\n";
		
		splotARetourner+="</feature_model>\n";
		
		
		String filename="EclipseFeature.xml";
		
		BufferedWriter bufferedWriter=null;
		
		try{
			FileWriter fileWriter=new FileWriter(filename);
			
			bufferedWriter = new BufferedWriter(fileWriter);
			
			bufferedWriter.write(splotARetourner);
			bufferedWriter.close();
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		String path = PreferenceUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if (path.endsWith("bin/")) {
			// On some OS, it gives "bin/" in more, but we don't want
			path = path.substring(0, path.length() - 4);
		}
		path=path+filename;
		System.out.println(path);
		
		File file = new File(path);
		
		return file;
		
	}
	
	public static void main(String[] args) throws Exception{
		
		prefMap = PreferenceUtils.getPreferences();
		
		String input = prefMap.get(PreferenceUtils.PREF_INPUT);
		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());

		if (allFeatures == null || allFeatures.isEmpty())
			return;

		exportToSPLOT(allFeatures);
		
	}
}
