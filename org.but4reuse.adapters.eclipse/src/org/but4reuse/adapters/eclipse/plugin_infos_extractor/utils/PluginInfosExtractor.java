package org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

import org.but4reuse.adapters.eclipse.PluginElement;

public class PluginInfosExtractor {
	
	public PluginInfosExtractor(){}
	
	/**
	 * Extracts the plugin infos from its MANIFEST.MF file
	 * @param fichierManifest the absolute path to the manifest
	 * @return a PluginElement containing all the required informations
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static PluginElement getPluginInfosFromManifest(String fichierManifest) 
			throws FileNotFoundException, IOException {
		try {
			PluginElement plugin = new PluginElement();
			InputStream ips = new FileInputStream(fichierManifest);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			
			while ((ligne = br.readLine()) != null) {
				if (ligne.contains("Bundle-SymbolicName: ")) {
					int i = ligne.indexOf(';');
					if (i==-1)
						ligne = ligne.substring(ligne.indexOf("Bundle-SymbolicName: ") + 21);
					else
						ligne = ligne.substring(ligne.indexOf("Bundle-SymbolicName: ") + 21, i);
					ligne.replaceAll("[\\s]", "");
					plugin.setPluginSymbName(ligne);
				}
				if (ligne.contains("Bundle-Name: ")) {
					ligne = ligne.substring(ligne.indexOf("Bundle-Name: ") + 13);
					//Tester si le nom est présent ou s'il s'agit d'une variable
					//faisant référence à un fichier properties
					if (ligne.startsWith("%")) {
						File manifest = new File(fichierManifest);
						try {
							//Premier essai : plugin.properties, à la racine du dossier
							String pathToProperties = manifest.getParentFile().getParentFile().getAbsolutePath()+
									"/plugin.properties";
							plugin.setPluginName(getNameFromPropertiesFile(pathToProperties, ligne.substring(1)));
						} catch (Exception e) {
							try {
								//Deuxième essai, bundle.properties, OSGI
								String pathToProperties = manifest.getParentFile().getParentFile().getAbsolutePath()+
										"/OSGI-INF/l10n/bundle.properties";
								plugin.setPluginName(getNameFromPropertiesFile(pathToProperties, ligne.substring(1)));
							} catch (Exception e2) {
								//Echec => On prend le symbolic name à la place.
								plugin.setPluginName(plugin.getPluginSymbName()); //on le trouve avant le name, donc non null
							}
						}
					} else {
						plugin.setPluginName(ligne);
					}
				}
				if (ligne.contains("Require-Bundle: ")) {
					//TODO à traiter. Pas de convention pour la présentation de la liste,
					//ormi la séparation par des virgules
//					ligne = ligne.substring(ligne.indexOf("Require-Bundle: ") + 16);
				}
			}
			br.close();
			return plugin;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("The file " + fichierManifest + " doesn't exist !");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Error during the reading of the file !");
		}
	}
	
	/**
	 * Extracts the name of a plugin from its properties file
	 * @param pathToProperties the absolute path to the properties file
	 * @param variableName the name of the variable of the plugin name
	 * @return the name of the plugin
	 * @throws IOException 
	 */
	private static String getNameFromPropertiesFile(String pathToProperties, String variableName) throws IOException {
		
		InputStream ips = new FileInputStream(pathToProperties);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String ligne;
		
		while ((ligne = br.readLine()) != null) {
			if (ligne.startsWith(variableName)) {
				br.close();
				return ligne.substring(ligne.indexOf("=")+2);
			}
		}
		br.close();
		return null;
	}

	/**
	 * Extracts the plugin infos, considering that it is a jar file
	 * @param fichierJar the absolute path to the jar file
	 * @return the plugin element
	 * @throws IOException
	 */
	public static PluginElement getPluginInfosFromJar(String fichierJar) throws IOException{
		File f = new File(fichierJar);
		JarFile jar = new JarFile(f);
		PluginElement plugin = new PluginElement();
		try {
			String value = jar.getManifest().getMainAttributes().getValue("Bundle-SymbolicName");
			int i = value.indexOf(';');
			if (i!=-1)
				value = value.substring(0, i);
//			System.out.println(value);
			plugin.setPluginSymbName(value);
			plugin.setPluginName(jar.getManifest().getMainAttributes().getValue("Bundle-Name"));
			value = jar.getManifest().getMainAttributes().getValue("Require-Bundle");
			if (value != null) {
				String[] values = value.split(",");
				for (String val : values) {
					if (!val.matches("[0-9]+.*")) {
						i = val.indexOf(';');
						if (i!=-1)
							val = val.substring(0, i);
						val = val.replaceAll("\\s", "");
						plugin.addRequire_bundle(val);
//						System.err.println(val);
					}
				}
//				System.err.println("$$$$$$$$$$$$$$$$$$$$$");
			}
			jar.close();
			return plugin;
		} catch (IOException e) {
			throw new IOException("Read exception for file " + fichierJar + " !");
		}
	}
}
