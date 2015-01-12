package org.but4reuse.adapters.eclipse;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;


/**
 * Plugin Element
 * @author Diana MALABARD
 * @author Jason CHUMMUN
 */
public class PluginElement extends AbstractElement{

	private String pluginSymbName;
	private String pluginName;
	private ArrayList<String> require_Bundles;
	
	public PluginElement() {
		require_Bundles = new ArrayList<String>();
	}

	
	public String getPluginSymbName() {
		return pluginSymbName;
	}


	public void setPluginSymbName(String pluginSymbName) {
		this.pluginSymbName = pluginSymbName;
	}


	public ArrayList<String> getRequire_bundle() {
		return require_Bundles;
	}


	public void addRequire_bundle(String require_bundle) {
		this.require_Bundles.add(require_bundle);
	}


	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		// TODO add a preference option to check file content
		if (anotherElement instanceof PluginElement) {
			if (((PluginElement) anotherElement).getPluginSymbName().equals(getPluginSymbName())) {
				return 1;
			}
		}
		return 0;
	}


	@Override
	public String getText() {
		return pluginName;
	}


	public String getPluginName() {
		return pluginName;
	}


	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

}
