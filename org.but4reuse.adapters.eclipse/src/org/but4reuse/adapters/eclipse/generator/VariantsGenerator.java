package org.but4reuse.adapters.eclipse.generator;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.but4reuse.adapters.eclipse.generator.utils.FileAndDirectoryUtils;
import org.but4reuse.adapters.eclipse.generator.utils.IListener;
import org.but4reuse.adapters.eclipse.generator.utils.ISender;
import static org.but4reuse.adapters.eclipse.generator.utils.VariantsUtils.*;


public class VariantsGenerator implements IVariantsGenerator, ISender{
	
	private String input;
	private String output;
	private int nbVariants;
	private int percentage;
	private boolean saveOnlyMetadata;
	
	String generatorSummary;
	List<IListener> listeners;
	
	public VariantsGenerator(String input, String output, int nbVariants,
			int percentage, boolean saveOnlyMetadata) {
		this.input = input;
		this.output = output;
		this.nbVariants = nbVariants;
		this.percentage = percentage;
		this.saveOnlyMetadata = saveOnlyMetadata;
	}
	
	public void generate() {
		sendToAll("Starting generate !");
		File eclipse = new File(input);
		File outputFile = new File(output);
		
		if(!eclipse.exists()){
			sendToAll(input+" not exists !");
			return; 
		}
		
 		try {
			FileAndDirectoryUtils.deleteFile(outputFile);
			sendToAll(output + " deleted");
		} catch (Exception e) {
			sendToAll(output +" not deleted because : "+e+"");
		}
		
		String name_tmp;
		String output_variant;
		for(int i=1; i<=nbVariants ; i++){
			output_variant = output+File.separator+VARIANT+i;
			
			for(File dir : eclipse.listFiles()){  // Parcours tous le contenu
				name_tmp = dir.getName();
				
				if(dir.isDirectory()){
					
					if(name_tmp.equals(FEATURES)){
						try {
							File[] someFeatures = FileAndDirectoryUtils.getSomeFiles(dir.listFiles(), percentage);
							FileUtils.forceMkdir( new File(output_variant+File.separator+name_tmp) ); 
							FileAndDirectoryUtils.copyFilesAndDirectories(output_variant+File.separator+name_tmp, someFeatures);
							sendToAll("(Variant "+i+") features created.");
						} catch (Exception e) {
							sendToAll("(Variant "+i+") features error : "+e);
						}
					} else if( name_tmp.equals(PLUGINS) ){
						if(saveOnlyMetadata){
							try {
								copyProcessForPluginsDirectories(FileAndDirectoryUtils.getAllSubDirectories(dir), output_variant+File.separator+name_tmp);
								sendToAll("(Variant "+i+") plugins (in dir) created.");
								copyProcessForPluginsJar(FileAndDirectoryUtils.getAllJarsInDirectory(dir), output_variant+File.separator+name_tmp);
								sendToAll("(Variant "+i+") plugins (in jar) created.");
							} catch (Exception e) {
								sendToAll("(Variant "+i+") plugins error : "+e);
							}
						} else {
							try {
								FileAndDirectoryUtils.copyDirectory(dir, output);
								sendToAll("(Variant "+i+") plugins created.");
							} catch (Exception e) {
								sendToAll("(Variant "+i+") plugins error : "+e);
							}
						}
					}
					
				}
					
			}
			sendToAll(output_variant+" fully successed !");
		}
		
		sendToAll("Generation finished !");
	}
	
	private void copyProcessForPluginsDirectories(File[] allPlugin, String output) throws Exception {
		IOFileFilter propertiesFileFilter = FileFilterUtils.nameFileFilter(PLUGINS_PROPERTIES);
		IOFileFilter metaInfFileFilter = FileFilterUtils.nameFileFilter(META_INF);

		FileFilter filter = FileFilterUtils.or(metaInfFileFilter, propertiesFileFilter);

		for(File onePlugin : allPlugin){
			File[] contentPlugin = onePlugin.listFiles(filter);
			FileAndDirectoryUtils.copyFilesAndDirectories(output+File.separator+onePlugin.getName(), contentPlugin);
		}
	}

	/**
	 * Extract and copy the content from all the ".jar" files, and paste into the output.
	 * @param allJarsInDirectory : All the ".jar" files
	 * @param output : The destination
	 */
	private void copyProcessForPluginsJar(File[] allJarsInDirectory, String output) {
		String jarName;
		String newOutputForEachJar;
		File onePluginDir;

		for(File oneJar : allJarsInDirectory){
			jarName = oneJar.getName().replace(JAR, "");
			newOutputForEachJar = output+File.separator+jarName;
			onePluginDir = new File(newOutputForEachJar);
			onePluginDir.mkdirs();
			
			try {
				extractJar(oneJar, onePluginDir);
			} catch (Exception e) {}
		}
	}
	
	private void extractJar(File jarFile, File destDir) throws Exception {
		JarFile jar = new JarFile(jarFile);
		Enumeration<JarEntry> entries = jar.entries();
		InputStream inputStream;
		FileOutputStream fileOutputStream;
		
		while (entries.hasMoreElements()) {
			JarEntry file = (JarEntry) entries.nextElement();
			File f = new File(destDir + File.separator + file.getName());
			inputStream = jar.getInputStream(file);
			boolean isDir = f.isDirectory();
			String path = f.getPath();
			boolean containsMeta = path.contains(META_INF);
			boolean endsWithPluginProp = path.endsWith(PLUGINS_PROPERTIES);
			try {
				if(isDir || (!containsMeta && !endsWithPluginProp) ){
					continue;
				}
				fileOutputStream = new FileOutputStream(f);
			}
			catch (FileNotFoundException e) {
				f.getParentFile().mkdirs();
				fileOutputStream = new FileOutputStream(f);
			}
			byte[] b = new byte[16384];
			int bytes;
			while ((bytes = inputStream.read(b)) > 0) {
				fileOutputStream.write(b, 0, bytes);
			}
			
			fileOutputStream.close();
			inputStream.close();
		}
		jar.close();
	}
	

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

	
	

}
