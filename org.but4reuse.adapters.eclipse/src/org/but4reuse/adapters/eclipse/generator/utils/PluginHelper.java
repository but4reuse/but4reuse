package org.but4reuse.adapters.eclipse.generator.utils;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.eclipse.PluginElement;
import org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils.PluginInfosExtractor;
import org.but4reuse.utils.files.FileUtils;

public class PluginHelper {

	public static final String PLUGINS_FOLDER = "plugins";

	public static List<PluginElement> getPluginsOfEclipse(String eclipseInstallationURI) throws Exception {
		List<PluginElement> allPlugins = new ArrayList<PluginElement>();
		File eclipseFile = FileUtils.getFile(new URI(eclipseInstallationURI));
		File pluginsFolder = new File(eclipseFile, PLUGINS_FOLDER);
		for (File folder : pluginsFolder.listFiles()) {
			if (isAPlugin(folder)) {

				PluginElement plugin = null;
				File manif = new File(folder.getAbsolutePath() + "/META-INF/MANIFEST.MF");
				if (manif.exists()) {
					plugin = PluginInfosExtractor.getPluginInfosFromManifest(manif.getAbsolutePath());
				} else if (isJar(folder)) {
					plugin = PluginInfosExtractor.getPluginInfosFromJar(folder.getAbsolutePath());
				}

				if (plugin != null)
					allPlugins.add(plugin);
			}
		}
		return allPlugins;
	}

	public static boolean isAPlugin(File file) {
		return PluginInfosExtractor.isAPlugin(file);
	}

	public static boolean isJar(File f) {
		return FileUtils.getExtension(f).equalsIgnoreCase("jar");
	}

}
