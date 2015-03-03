/**
 * 
 */
package org.but4reuse.adapters.eclipse.plugin_infos_extractor.utils;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.eclipse.PluginElement;

/**
 * @author Diana Malabard
 * 
 */
public class DependenciesBuilder {

	private PluginElement plugin;
	private List<IElement> pluginsSet;

	/**
	 * @param plugin
	 * @param pluginsSet
	 */
	public DependenciesBuilder(PluginElement plugin, List<IElement> pluginsSet) {
		super();
		this.plugin = plugin;
		this.pluginsSet = pluginsSet;
	}

	public void build() {
		for (String dependency_symbName : plugin.getRequire_Bundles()) {
			for (IElement elem : pluginsSet) {
				if (elem instanceof PluginElement) {
					if (((PluginElement) elem).getPluginSymbName().compareToIgnoreCase(dependency_symbName) == 0) {
						// TODO check versions
						plugin.addDependency("requiredBundle", (PluginElement) elem);
						break;
					}
				}
			}
		}
	}
}
