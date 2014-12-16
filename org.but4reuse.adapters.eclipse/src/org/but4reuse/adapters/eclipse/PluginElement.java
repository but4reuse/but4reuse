package org.but4reuse.adapters.eclipse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;

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
	//Changer le type de require_Bundle en ArrayList<String>
	private String require_Bundle;

	
	public String getPluginSymbName() {
		return pluginSymbName;
	}


	public void setPluginSymbName(String pluginSymbName) {
		this.pluginSymbName = pluginSymbName;
	}


	public String getRequire_bundle() {
		return require_Bundle;
	}


	public void setRequire_bundle(String require_bundle) {
		this.require_Bundle = require_bundle;
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
		return pluginSymbName;
	}


	public String getPluginName() {
		return pluginName;
	}


	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

}
