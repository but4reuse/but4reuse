package org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.utils.files.FileUtils;

public class PluginInfosExtractor {
	public static final String BUNDLESINFO_RELATIVEPATH = "configuration/org.eclipse.equinox.simpleconfigurator/bundles.info";
	private static final String BUNDLE_VERSION = "Bundle-Version";
	private static final String BUNDLE_NAME = "Bundle-Name";
	private static final String REQUIRE_BUNDLE = "Require-Bundle";
	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	private static final String BUNDLE_LOCALIZATION = "Bundle-Localization";

	// as used in org.google.guava for example
	private static final String DEFAULT_LOCALIZATION = "OSGI-INF/l10n/bundle";

	private static final String FRAGMENT_HOST = "Fragment-Host";

	private static String currentLocalization = null;

	private static void fillPluginElementInfo(PluginElement plugin, Manifest manifest) {
		Attributes attributes = manifest.getMainAttributes();
		String value = attributes.getValue(BUNDLE_SYMBOLIC_NAME);
		int i = value.indexOf(';');
		if (i != -1)
			value = value.substring(0, i);
		plugin.setSymbName(value);

		// Fragment info
		String fragmentHost = attributes.getValue(FRAGMENT_HOST);
		if (fragmentHost != null) {
			i = fragmentHost.indexOf(';');
			if (i != -1)
				fragmentHost = fragmentHost.substring(0, i);
			plugin.setFragmentHost(fragmentHost);
		}

		String version = attributes.getValue(BUNDLE_VERSION);
		plugin.setVersion(version);
		value = attributes.getValue(REQUIRE_BUNDLE);
		if (value != null) {
			getRequireBundlesSymbNames(value, plugin);
		}

		// Name
		currentLocalization = attributes.getValue(BUNDLE_LOCALIZATION);
		if (currentLocalization == null) {
			currentLocalization = DEFAULT_LOCALIZATION;
		}
		String name = attributes.getValue(BUNDLE_NAME);
		plugin.setName(name);
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
	 */
	public static PluginElement getPluginInfosFromManifest(String manifestFile) {
		PluginElement plugin = new PluginElement();
		plugin.setJar(false);
		File f = new File(manifestFile);
		f = f.getParentFile().getParentFile();
		plugin.setAbsolutePath(f.getAbsolutePath());
		try {
			InputStream ips = new FileInputStream(manifestFile);
			Manifest manifest = new Manifest(ips);
			fillPluginElementInfo(plugin, manifest);
			ips.close();
			manifest = null;
			if (plugin.getName() != null && plugin.getName().contains("%")) {
				File localizationFile = new File(f, currentLocalization + ".properties");
				if (localizationFile.exists()) {
					Properties prop = new Properties();
					InputStream input = new FileInputStream(localizationFile);
					prop.load(input);
					// remove also whitespaces, as the problem with
					// org.eclipse.cdt.dsf.gdb.ui
					String name = prop.getProperty(plugin.getName().substring(1).replaceAll("\\s", ""));
					plugin.setName(name);
					input.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plugin;
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
	 */
	public static PluginElement getPluginInfosFromJar(String jarFile) {
		PluginElement plugin = new PluginElement();
		plugin.setJar(true);
		plugin.setAbsolutePath(jarFile);
		try {
			File f = new File(jarFile);
			JarFile jar = new JarFile(f);
			fillPluginElementInfo(plugin, jar.getManifest());
			if (plugin.getName() != null && plugin.getName().contains("%")) {
				ZipEntry zipEntry = jar.getEntry(currentLocalization + ".properties");
				if (zipEntry != null) {
					Properties prop = new Properties();
					prop.load(jar.getInputStream(zipEntry));
					String name = prop.getProperty(plugin.getName().substring(1).replaceAll("\\s", ""));
					plugin.setName(name);
				}
			}
			jar.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return plugin;
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
		String previous = "";
		for (String val : values) {
			if (!val.matches("\\s*[0-9].*")) {
				int i = val.indexOf(';');
				if (i != -1)
					val = val.substring(0, i);
				val = val.replaceAll("\\s", "");
				previous = val;
				plugin.addRequire_bundle(val);
			} else if (val.contains("resolution:=optional")) {
				plugin.removeRequire_bundle(previous);
			}
		}
	}

	public static Map<String, String> createBundlesInfoMap(URI uri) {
		Map<String, String> map = new HashMap<String, String>();
		File file = FileUtils.getFile(uri);
		File bundlesInfo = new File(file.getAbsolutePath() + "/" + BUNDLESINFO_RELATIVEPATH);
		if (bundlesInfo.exists()) {
			List<String> bundles = FileUtils.getLinesOfFile(bundlesInfo);
			for (String info : bundles) {
				int comma = info.indexOf(",");
				if (comma != -1) {
					map.put(info.substring(0, comma), info);
				}
			}
		}
		return map;
	}

	/**
	 * Check if a file is a plugin
	 * 
	 * @param file
	 * @return true if it is a plugin
	 */
	public static boolean isAPlugin(File file) {
		if (file.getParentFile().getName().equals("plugins") || file.getParentFile().getName().equals("dropins")) {
			if (file.isDirectory()) {
				File manif = new File(file.getAbsolutePath() + "/META-INF/MANIFEST.MF");
				if (manif.exists()) {
					return true;
				}
			} else if (FileUtils.getExtension(file).equalsIgnoreCase("jar")) {
				return true;
			}
		}
		return false;
	}
}
