package org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.PluginElement;

/**
 * @author Diana Malabard
 * 
 */
public class DependenciesBuilder {

	public static void build(PluginElement plugin, List<IElement> pluginsSet) {

		// Required bundles
		for (String dependency_symbName : plugin.getRequire_Bundles()) {
			for (IElement elem : pluginsSet) {
				if (elem instanceof PluginElement) {
					PluginElement pe = (PluginElement) elem;
					if (pe.getSymbName().equalsIgnoreCase(dependency_symbName)) {
						// TODO check versions
						plugin.addDependency("requiredBundle", pe);
						break;
					}
				}
			}
		}

		// In case of a fragment
		if (plugin.isFragment()) {
			String hostSymbName = plugin.getFragmentHost();
			// check if they already included, it is not needed for fragments
			// but who knows. We put this to avoid duplicate dependencies
			if (!plugin.getRequire_Bundles().contains(hostSymbName)) {
				for (IElement elem : pluginsSet) {
					if (elem instanceof PluginElement) {
						PluginElement pe = (PluginElement) elem;
						if (hostSymbName != null && hostSymbName.equalsIgnoreCase(pe.getSymbName())) {
							// TODO check versions
							// TODO maybe use another name for the dependency
							plugin.addDependency("requiredBundle", pe);
							break;
						}
					}
				}
			}
		}
	}
}
