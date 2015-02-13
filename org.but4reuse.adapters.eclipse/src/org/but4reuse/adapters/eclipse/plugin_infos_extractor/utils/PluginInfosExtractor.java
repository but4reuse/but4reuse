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
	private static final String REQUIRE_BUNDLE2 = "Require-Bundle";
	private static final String BUNDLE_NAME2 = "Bundle-Name";
	private static final String BUNDLE_SYMBOLIC_NAME2 = "Bundle-SymbolicName";
	private static final String BUNDLE_NAME = "Bundle-Name: ";
	private static final String REQUIRE_BUNDLE = "Require-Bundle: ";
	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName: ";

	public PluginInfosExtractor() {
	}

	/*
	 * FOLDERS
	 */
	/**
	 * Extracts the plugin infos from its MANIFEST.MF file
	 * 
	 * @param manifestFile
	 *            the absolute path to the manifest
	 * @return a PluginElement containing all the required informations
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static PluginElement getPluginInfosFromManifest(String manifestFile) throws FileNotFoundException,
			IOException {
		try {
			PluginElement plugin = new PluginElement();
			plugin.setJar(false);
			File f = new File(manifestFile);
			f = f.getParentFile().getParentFile();
			plugin.setAbsolutePath(f.getAbsolutePath());
			InputStream ips = new FileInputStream(manifestFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;

			while ((ligne = br.readLine()) != null) {
				if (ligne.contains(BUNDLE_SYMBOLIC_NAME)) {
					getSymbolicName(ligne, plugin);
				}
				if (ligne.contains(BUNDLE_NAME)) {
					getPluginName(ligne, manifestFile, plugin);
				}
				if (ligne.contains(REQUIRE_BUNDLE)) {
					getRequireBundles(ligne, br, plugin);
				}
			}
			br.close();
			return plugin;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("The file " + manifestFile + " doesn't exist !");
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Error during the reading of the file !");
		}
	}

	/**
	 * Gets the plugin's symbolic name from its manifest file
	 * 
	 * @param line
	 *            the line containing the Bundle-SymbolicName field
	 * @param plugin
	 *            the PluginElement for which we are searching the symbolic name
	 */
	private static void getSymbolicName(String line, PluginElement plugin) {
		int i = line.indexOf(';');
		if (i == -1)
			line = line.substring(line.indexOf(BUNDLE_SYMBOLIC_NAME) + BUNDLE_SYMBOLIC_NAME.length());
		else
			line = line.substring(line.indexOf(BUNDLE_SYMBOLIC_NAME) + BUNDLE_SYMBOLIC_NAME.length(), i);
		line.replaceAll("[\\s]", "");
		plugin.setPluginSymbName(line);
	}

	/**
	 * Gets the plugin's name from the manifest, or searches in any properties
	 * file if the name is not in the manifest
	 * 
	 * @param line
	 *            the line of the manifest containing the Bundle-Name field
	 * @param manifestFile
	 *            absolute path to the manifest file
	 * @param plugin
	 *            the PluginElement for which we are searching the name
	 */
	private static void getPluginName(String line, String manifestFile, PluginElement plugin) {
		line = line.substring(line.indexOf(BUNDLE_NAME) + BUNDLE_NAME.length());
		// Test if the name is there or it is on properties file
		if (line.startsWith("%")) {
			try {
				File manifest = new File(manifestFile);
				// First try: plugin.properties at the root of the folder
				String pathToProperties = manifest.getParentFile().getParentFile().getAbsolutePath()
						+ "/plugin.properties";
				File file = new File(pathToProperties);
				if (file.exists()) {
					plugin.setPluginName(getNameFromPropertiesFile(pathToProperties, line.substring(1)));
				} else {
					// Second try: bundle.properties, OSGI
					pathToProperties = manifest.getParentFile().getParentFile().getAbsolutePath()
							+ "/OSGI-INF/l10n/bundle.properties";
					file = new File(pathToProperties);
					if (file.exists()) {
						plugin.setPluginName(getNameFromPropertiesFile(pathToProperties, line.substring(1)));
					} else {
						// Nothing => we take the symbolic name
						plugin.setPluginName(plugin.getPluginSymbName());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			plugin.setPluginName(line);
		}
	}

	/**
	 * Extracts the required plugins from the manifest
	 * 
	 * @param line
	 *            the line containing the Require-Bundle field
	 * @param br
	 *            the manifest's BufferedReader
	 * @param plugin
	 *            the PluginElement for which we are searching the required
	 *            plugins
	 * @throws IOException
	 */
	private static void getRequireBundles(String line, BufferedReader br, PluginElement plugin) throws IOException {
		String value = line.substring(line.indexOf(REQUIRE_BUNDLE) + REQUIRE_BUNDLE.length());
		// Read all the lines containing require-bundle
		boolean isMarked = false;
		while ((line = br.readLine()) != null && !line.matches("\\s*[A-Z].*")) {
			value += line;
			br.mark(1024);
			isMarked = true;
		}
		// reset of reader
		if (isMarked)
			br.reset();
		getRequireBundlesSymbNames(value, plugin);
	}

	/**
	 * Extracts the name of a plugin from its properties file
	 * 
	 * @param pathToProperties
	 *            the absolute path to the properties file
	 * @param variableName
	 *            the name of the variable of the plugin name
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
				return ligne.substring(ligne.indexOf("=") + 2);
			}
		}
		br.close();
		return null;
	}

	/*
	 * JARS
	 */

	/**
	 * Extracts the plugin infos, considering that it is a jar file
	 * 
	 * @param jarFile
	 *            the absolute path to the jar file
	 * @return the plugin element
	 * @throws IOException
	 */
	public static PluginElement getPluginInfosFromJar(String jarFile) throws IOException {
		File f = new File(jarFile);
		JarFile jar = new JarFile(f);
		PluginElement plugin = new PluginElement();
		plugin.setJar(true);
		plugin.setAbsolutePath(jarFile);
		try {
			String value = jar.getManifest().getMainAttributes().getValue(BUNDLE_SYMBOLIC_NAME2);
			int i = value.indexOf(';');
			if (i != -1)
				value = value.substring(0, i);
			plugin.setPluginSymbName(value);
			plugin.setPluginName(jar.getManifest().getMainAttributes().getValue(BUNDLE_NAME2));
			value = jar.getManifest().getMainAttributes().getValue(REQUIRE_BUNDLE2);
			if (value != null) {
				getRequireBundlesSymbNames(value, plugin);
			}
			jar.close();
			return plugin;
		} catch (IOException e) {
			throw new IOException("Read exception for file " + jarFile + " !");
		}
	}

	/*
	 * BOTH
	 */

	/**
	 * Extracts all the required plugins' symbolic names from the Require-Bundle
	 * field's value
	 * 
	 * @param value
	 *            a String containing the whole Require-Bundle field's value
	 * @param plugin
	 *            the PluginElement for which we are searching the required
	 *            plugins
	 */
	private static void getRequireBundlesSymbNames(String value, PluginElement plugin) {
		String[] values = value.split(",");
		for (String val : values) {
			if (!val.matches("\\s*[0-9].*")) {
				int i = val.indexOf(';');
				if (i != -1)
					val = val.substring(0, i);
				val = val.replaceAll("\\s", "");
				plugin.addRequire_bundle(val);
			}
		}
	}
}
