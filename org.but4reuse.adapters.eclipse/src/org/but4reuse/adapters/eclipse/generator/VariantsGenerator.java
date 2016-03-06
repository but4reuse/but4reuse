package org.but4reuse.adapters.eclipse.generator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.but4reuse.adapters.eclipse.benchmark.ActualFeature;
import org.but4reuse.adapters.eclipse.benchmark.FeatureHelper;
import org.but4reuse.adapters.eclipse.generator.utils.FeaturesAndPluginsDependencies;
import org.but4reuse.adapters.eclipse.generator.utils.FileAndDirectoryUtils;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.ISender;
import org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils;
import org.eclipse.emf.common.util.URI;


public class VariantsGenerator implements IVariantsGenerator, ISender{
	
	private String input;
	private String output;
	private int nbVariants;
	private int percentage;
	private boolean saveOnlyMetadata;
	
	String generatorSummary;
	List<IListener> listeners;
	
	public VariantsGenerator(String input, String output, int nbVariants, int percentage, boolean saveOnlyMetadata) {
		this.input = input;
		this.output = output;
		this.nbVariants = nbVariants;
		this.percentage = percentage;
		this.saveOnlyMetadata = saveOnlyMetadata;
	}
	
	public void generate() {
		sendToAll("Starting generate with :");
		sendToAll("-input = "+input);
		sendToAll("-output = "+output);
		sendToAll("-variants number = "+nbVariants);
		sendToAll("-percentage = "+percentage+" %");
		sendToAll("-save only metadata = "+saveOnlyMetadata+"\n");
		
		File eclipse = new File(input);
		File outputFile = new File(output);
		
		if(!eclipse.exists()){
			sendToAll(input+" not exists !");
			return; 
		}
		
 		try {  // Clear the output
			FileAndDirectoryUtils.deleteFile(outputFile);
			sendToAll("output clear");
		} catch (Exception e) {
			sendToAll("output not clear");
		}
		
		String output_variant;
		
		URI inputURI = URI.createFileURI(input);
		List<ActualFeature> allFeatures;
		try {
			allFeatures = FeatureHelper.getFeaturesOfEclipse(inputURI.toString());
		} catch (Exception e) {
			sendToAll("Error in generator : Impossible to get all features.");
			return;
		}
		
		sendToAll("Total features number at the input = "+allFeatures.size()+"\n");
//		sendToAll("Total plugins number at the input = "+allPlugins.size()+"\n");
		
		FeaturesAndPluginsDependencies depOperator = new FeaturesAndPluginsDependencies(allFeatures, inputURI.toString());
		for(int i=1; i<=nbVariants ; i++){
			output_variant = output+File.separator+VariantsUtils.VARIANT+i;
			
			List<String> pluginsList = new ArrayList<String>();
			List<ActualFeature> chosenFeatures = new ArrayList<>();
			
			int nbSelectedFeatures = 0;
			for(int cptFeature=0; cptFeature<allFeatures.size(); cptFeature++){
				ActualFeature oneFeature = allFeatures.get(cptFeature);
				boolean wasChosen = wasChosen(oneFeature);
				if(wasChosen) nbSelectedFeatures++;
				
				if(chosenFeatures.contains(oneFeature) || !wasChosen) continue; // Check if not exists and apply the random choice
				chosenFeatures.add(oneFeature);
				
				List<ActualFeature> allDependencies = depOperator.getDependencies(oneFeature);
				if(allDependencies != null){
					for(ActualFeature depFeat : allDependencies){
						if(!chosenFeatures.contains(depFeat)) chosenFeatures.add(depFeat); // Avoid duplicates dependencies in the chosenFeatures list
					}
				}
				
//				for(String plugin : oneFeature.getPlugins()){
//					if( !pluginsList.contains(plugin) ) pluginsList.add(plugin);
//				}
				
			} // end of iterate through allFeatures
			
			
			try { // Create all dirs and copy features and plugins
				FileUtils.forceMkdir( new File(output_variant) ); 
				
				for(File file_eclipse : eclipse.listFiles()){
					if(!file_eclipse.getName().equals(VariantsUtils.FEATURES) && !file_eclipse.getName().equals(VariantsUtils.PLUGINS)){
						FileAndDirectoryUtils.copyFilesAndDirectories(output_variant, file_eclipse);
					}
				}
				
				File[] allFilesFeatures = new File[chosenFeatures.size()];
				for(int j=0; j<chosenFeatures.size(); j++){
					allFilesFeatures[j] = new File(depOperator.getPathFromFeature(chosenFeatures.get(j)));
				}
					
				FileAndDirectoryUtils.copyFilesAndDirectories(output_variant+File.separator+VariantsUtils.FEATURES, allFilesFeatures);
				System.out.println("(Variant "+i+") features created.");
			} catch (Exception e) {
				System.out.println("(Variant "+i+") features error : "+e);
			}
			
			
			sendToAll("Total of features selected with the random for variant n°"+i+" = "+nbSelectedFeatures);
			sendToAll("Total of features (all selected, including required and \n"
					+ "included dependencies, direct or indirect) for variant n°"+i+" = "+chosenFeatures.size()+"\n");
//			sendToAll("Plugins number for variant n0"+i+"= "+pluginsList.size());
				
		} // end of variantes loop
		
		
//		==== Old Part for Files copy (we keep this just in case of...)
//			for(File dir : eclipse.listFiles()){  // Iterate over the contains
//				name_tmp = dir.getName();
//				
//				if(dir.isDirectory()){
//					
//					if(name_tmp.equals(VariantsUtils.FEATURES)){
//						try {
//							File[] someFeatures = FileAndDirectoryUtils.getSomeFiles(dir.listFiles(), percentage);
//							FileUtils.forceMkdir( new File(output_variant+File.separator+name_tmp) ); 
//							FileAndDirectoryUtils.copyFilesAndDirectories(output_variant+File.separator+name_tmp, someFeatures);
//							sendToAll("(Variant "+i+") features created.");
//						} catch (Exception e) {
//							sendToAll("(Variant "+i+") features error : "+e);
//						}
//					} else if( name_tmp.equals(VariantsUtils.PLUGINS) ){
//						if(saveOnlyMetadata){
//							try {
//								copyProcessForPluginsDirectories(FileAndDirectoryUtils.getAllSubDirectories(dir), output_variant+File.separator+name_tmp);
//								sendToAll("(Variant "+i+") plugins (in dir) created.");
//								copyProcessForPluginsJar(FileAndDirectoryUtils.getAllJarsInDirectory(dir), output_variant+File.separator+name_tmp);
//								sendToAll("(Variant "+i+") plugins (in jar) created.");
//							} catch (Exception e) {
//								sendToAll("(Variant "+i+") plugins error : "+e);
//							}
//						} else {
//							try {
//								FileAndDirectoryUtils.copyDirectory(dir, output);
//								sendToAll("(Variant "+i+") plugins created.");
//							} catch (Exception e) {
//								sendToAll("(Variant "+i+") plugins error : "+e);
//							}
//						}
//					}
//					
//				}
//					
//			}
//			sendToAll(output_variant+" fully successed !");

		
		sendToAll("\nGeneration finished !");
	}
	
//	private void copyProcessForPluginsDirectories(File[] allPlugin, String output) throws Exception {
//		IOFileFilter propertiesFileFilter = FileFilterUtils.suffixFileFilter(VariantsUtils.PROPERTIES, IOCase.INSENSITIVE);
//		IOFileFilter metaInfFileFilter = FileFilterUtils.nameFileFilter(VariantsUtils.META_INF, IOCase.INSENSITIVE);
//
//		FileFilter filter = FileFilterUtils.or(metaInfFileFilter, propertiesFileFilter);
//
//		for(File onePlugin : allPlugin){
//			File[] contentPlugin = onePlugin.listFiles(filter);
//			FileAndDirectoryUtils.copyFilesAndDirectories(output+File.separator+onePlugin.getName(), contentPlugin);
//		}
//	}
//
//	/**
//	 * Extract and copy the content from all the ".jar" files, and paste into the output.
//	 * @param allJarsInDirectory : All the ".jar" files
//	 * @param output : The destination
//	 */
//	private void copyProcessForPluginsJar(File[] allJarsInDirectory, String output) {
//		String jarName;
//		String newOutputForEachJar;
//		File onePluginDir;
//
//		for(File oneJar : allJarsInDirectory){
//			jarName = oneJar.getName().replace(VariantsUtils.JAR, "");
//			newOutputForEachJar = output+File.separator+jarName;
//			onePluginDir = new File(newOutputForEachJar);
//			onePluginDir.mkdirs();
//			
//			try {
//				extractJar(oneJar, onePluginDir);
//			} catch (Exception e) {}
//		}
//	}
//	
//	private void extractJar(File jarFile, File destDir) throws Exception {
//		JarFile jar = new JarFile(jarFile);
//		Enumeration<JarEntry> entries = jar.entries();
//		InputStream inputStream;
//		FileOutputStream fileOutputStream;
//		
//		while (entries.hasMoreElements()) {
//			JarEntry file = (JarEntry) entries.nextElement();
//			File f = new File(destDir + File.separator + file.getName());
//			inputStream = jar.getInputStream(file);
//			boolean isDir = f.isDirectory();
//			String path = f.getPath();
//			boolean containsMeta = path.contains(VariantsUtils.META_INF);
//			boolean endsWithPluginProp = path.endsWith(VariantsUtils.PROPERTIES);
//			try {
//				if(isDir || (!containsMeta && !endsWithPluginProp) ){
//					continue;
//				}
//				fileOutputStream = new FileOutputStream(f);
//			}
//			catch (FileNotFoundException e) {
//				f.getParentFile().mkdirs();
//				fileOutputStream = new FileOutputStream(f);
//			}
//			byte[] b = new byte[16384];
//			int bytes;
//			while ((bytes = inputStream.read(b)) > 0) {
//				fileOutputStream.write(b, 0, bytes);
//			}
//			
//			fileOutputStream.close();
//			inputStream.close();
//		}
//		jar.close();
//	}
	

	@Override
	public void addListener(IListener listener) {
		if(listeners==null) listeners = new ArrayList<IListener>();
		listeners.add(listener);
	}

	@Override
	public void sendToAll(String msg) {
		if(msg!= null && listeners!= null && !listeners.isEmpty()){
			for(IListener oneListener : listeners){
				oneListener.receive(msg);
			}
		}
	}

	@Override
	public void sendToOne(IListener listener, String msg) {
		if(listeners!= null && !listeners.isEmpty()){
			int index = listeners.indexOf(listener);
			listeners.get(index).receive(msg);
		}
	}

	private boolean wasChosen(ActualFeature feature){
		if(feature==null || percentage==0) return false;
		else if (percentage == 100 || Math.random()*100 < percentage) return true;
		else return false;
	}
	
}
