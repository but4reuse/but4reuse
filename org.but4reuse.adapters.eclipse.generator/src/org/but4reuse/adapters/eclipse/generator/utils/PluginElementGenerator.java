package org.but4reuse.adapters.eclipse.generator.utils;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.eclipse.PluginElement;

/**
 * Class for redefine equals method of PluginElement (equals not function out of
 * a Eclipse application context)
 */
public class PluginElementGenerator extends PluginElement {

	public PluginElementGenerator(PluginElement one_elem) {
		super();
		VariantsUtils.copy(one_elem, this);
	}

	@Override
	public boolean equals(Object plugin_elem) {
		if (plugin_elem instanceof PluginElement) {
			boolean sameStateJar = (this.isJar() && ((PluginElement) plugin_elem).isJar())
					|| (!this.isJar() && !((PluginElement) plugin_elem).isJar());
			boolean sameSymbName = this.getSymbName().equals(((PluginElement) plugin_elem).getSymbName());
			boolean sameVersion = this.getVersion().equals(((PluginElement) plugin_elem).getVersion());
			return (sameStateJar && sameSymbName && sameVersion);
		} else {
			return super.equals(plugin_elem);
		}
	}

	public static List<PluginElementGenerator> transformInto(List<PluginElement> elems) {
		if (elems == null)
			return null;

		List<PluginElementGenerator> generators = new ArrayList<>(elems.size());
		for (PluginElement one_elem : elems) {
			generators.add(new PluginElementGenerator(one_elem));
		}
		return generators;
	}

}
