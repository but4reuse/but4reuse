package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.EclipseAdapter;
import org.but4reuse.adapters.eclipse.FileElement;
import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.generator.dependencies.DependenciesAnalyzer;
import org.eclipse.core.runtime.NullProgressMonitor;

import pledge.core.ModelPLEDGE;
import pledge.core.ModelPLEDGE.FeatureModelFormat;
import pledge.core.Product;

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
	
	
	public static void generate(){

		int nbVariants=5;
		int time=60;
		
		EclipseAdapter adapter = new EclipseAdapter();
		try {
			prefMap = PreferenceUtils.getPreferences();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		String input = prefMap.get(PreferenceUtils.PREF_INPUT);
		File eclipse = new File(input);

		if (!eclipse.exists()) {
			return;
		}
		
		String output = prefMap.get(PreferenceUtils.PREF_OUTPUT);
		
		// if the eclipse dir is inside the input
		if(eclipse.list().length==1 && eclipse.listFiles()[0].getName().equals("eclipse")){
			if(input.endsWith(File.separator)) input += "eclipse"+File.separator;
			else input += File.separator+"eclipse"+File.separator;
			eclipse = new File(input);
			eclipse.getParentFile().getName();
		} else {
			eclipse.getName();
		}
		// check if it's an eclipse directory
		if(!Arrays.asList(eclipse.list()).containsAll(Arrays.asList(new String[]{"plugins", "features"}))) {
			System.out.println(input + " is not an eclipse !");
			return;
		}

		URI inputURI = new File(input).toURI();
		List<ActualFeature> allFeatures;
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());
		} catch (Exception e) {
			System.out.println("Error in generator : Impossible to get all features.");
			e.printStackTrace();
			return;
		}

		List<PluginElement> allPlugins = new ArrayList<PluginElement>();
		List<FileElement> allFileElements = new ArrayList<FileElement>();
		{
			List<IElement> iElems = adapter.adapt(inputURI, new NullProgressMonitor());
			for (IElement elem : iElems) {
				if (elem instanceof PluginElement)
					allPlugins.add((PluginElement) elem);
				else if (elem instanceof FileElement)
					allFileElements.add((FileElement) elem);
			}
		}
		System.out.println("Total features number in the input = " + allFeatures.size());
		System.out.println("Total plugins number in the input = " + allPlugins.size() + "\n");

		List<PluginElementGenerator> allPlugs = PluginElementGenerator.transformInto(allPlugins);
		
		DependenciesAnalyzer depAnalyzer = new DependenciesAnalyzer(allFeatures, allPlugs,
				inputURI.toString());

		//Generation fichier format Splot
		File f = SplotUtils.exportToSPLOT(allFeatures);
		
		ModelPLEDGE mp= new ModelPLEDGE();
		try {
			mp.loadFeatureModel(f.getAbsolutePath(), FeatureModelFormat.SPLOT);
		} catch (Exception e1) {
			System.out.println("Error in generator : Errot to load FeatureModel.");
			e1.printStackTrace();
			return;
		}
		
		mp.setNbProductsToGenerate(nbVariants);
		mp.setGenerationTimeMSAllowed(time*1000L);
		mp.SetPrioritizationTechniqueByName("SimilarityGreedy");
		try {
			mp.generateProducts();
		} catch (Exception e1) {
			System.out.println("Error in generator : Error to generate the product.");
			e1.printStackTrace();
			return;
		}
		
		for (int i = 1; i <= nbVariants; i++) {
			String output_variant = output + File.separator + VariantsUtils.VARIANT + i;
			int nbSelectedFeatures = 0;

			List<PluginElement> pluginsList = new ArrayList<PluginElement>();
			List<ActualFeature> chosenFeatures = new ArrayList<ActualFeature>();
			
			
			Product p = mp.getProducts().get(i-1);
			
			for (Integer j : p) {
	           if (j > 0) {
	        	   String id=mp.getFeaturesList().get(j-1);
	        	   id = id.replace("555555", "(");
	        	   id = id.replace("°°°°°°", "(");
	        	   id = id.replace("111111", ".");
	        	   id = id.replace("666666", "-");
	        	   for(ActualFeature oneFeat: allFeatures){
	        		   if(oneFeat.getId().equals(id)){
	        			   chosenFeatures.add(oneFeat);
	        			   nbSelectedFeatures++;
	        			   break;
	            		}
	            	}
	            }
			 }// end of iterate through allFeatures

			// Get all plugins from chosen features
			for (ActualFeature chosenFeature : chosenFeatures) {
				List<PluginElementGenerator> allPluginsDependencies = depAnalyzer.getPluginsDependencies(chosenFeature);
				if (allPluginsDependencies != null) {
					for (PluginElementGenerator depPlugin : allPluginsDependencies) {
						// Avoid duplicates dependencies in the plugins list
						if (!pluginsList.contains(depPlugin)) {
							pluginsList.add(depPlugin);
						}
					}
				}
			}

			pluginsList.addAll(depAnalyzer.getPluginsWithoutAnyFeaturesDependencies());
			for(PluginElementGenerator one_manda : depAnalyzer.getPluginsMandatoriesByInput()){
				if(!pluginsList.contains(one_manda)) pluginsList.add(one_manda);
			}
			
				
			try {
				// Create all dirs and copy features and plugins
				File output_variantFile = new File(output_variant);
				FileUtils.forceMkdir(output_variantFile);

				for (File file_eclipse : eclipse.listFiles()) {
					// Copy eclipse files & dirs (except features & plugins)
					if (!file_eclipse.getName().equals(VariantsUtils.FEATURES)
							&& !file_eclipse.getName().equals(VariantsUtils.PLUGINS)) {
						FileAndDirectoryUtils.copyFilesAndDirectories(output_variant, file_eclipse);
					}
				}

				// features copy
				File[] allFilesFeatures = new File[chosenFeatures.size()];
				for (int j = 0; j < chosenFeatures.size(); j++) {
					allFilesFeatures[j] = new File(depAnalyzer.getPathFromFeature(chosenFeatures.get(j)));
				}
				FileAndDirectoryUtils.copyFilesAndDirectories(output_variant + File.separator + VariantsUtils.FEATURES,
						allFilesFeatures);

				// plugins copy
				File[] allFilesPlugins = new File[pluginsList.size()];
				for (int j = 0; j < pluginsList.size(); j++) {
					allFilesPlugins[j] = new File(pluginsList.get(j).getAbsolutePath());
				}
				FileAndDirectoryUtils.copyFilesAndDirectories(output_variant + File.separator + VariantsUtils.PLUGINS,
						allFilesPlugins);

				System.out.println("(Variant " + i + ") features created.");
			} catch (Exception e) {
				System.out.println("(Variant " + i + ") features error : " + e);
			}

			System.out.println("Total of features selected from Pledge for Variant " + i + " = " + nbSelectedFeatures
					+ "\nPlugins number for Variant " + i + "= " + pluginsList.size() + "\n");

			List<IElement> allElements = new ArrayList<IElement>();
			allElements.addAll(allFileElements);
			allElements.addAll(pluginsList);
			adapter.construct(inputURI, allElements, new NullProgressMonitor());

		} // end of variants loop

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
		
		for(int i=0;i<allFeatures.size();i++){
			
			ActualFeature oneFeat = allFeatures.get(i);
			String name = oneFeat.getName().replace("(", "");
			name = name.replace(")", "");

			
			String id = oneFeat.getId().replace("(", "555555");
			id = id.replace(")", "°°°°°°");
			id = id.replace(".", "111111");
			id = id.replace("-", "666666");
			
			splotARetourner+="\t:o "+name+"("+id+")";
	
			splotARetourner+="\n";
		}
		splotARetourner+="</feature_tree>\n";
		splotARetourner+="<constraints>";
		int nbContrainte=1;
		for(int i=0;i<allFeatures.size();i++){
			ActualFeature oneFeat = allFeatures.get(i);
			
			String id = oneFeat.getId().replace("(", "555555");
			id = id.replace(")", "°°°°°°");
			id = id.replace(".", "111111");
			id = id.replace("-", "666666");
			
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
								+mapIdWithFeature.get(allDependence.get(j)).getId().replace(".","111111").replace("-", "666666").replace("(", "555555").replace(")", "°°°°°°");
						nbContrainte++;
					}
				}
			}
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
		
		generate();
		
	}
}
