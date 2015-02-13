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
	//each pluginElement in require_Bundles, the list of require_Bundle will be empty.
	//because we do not know the dependencies
	//The same thing for absoluthPath
	private ArrayList<String> require_Bundles;
	
	public ArrayList<String> getRequire_Bundles() {
		return require_Bundles;
	}


	private String absolutePath;
	private boolean isJar; 
	
	public PluginElement() {
		require_Bundles = new ArrayList<String>();
	}

	
	public String getPluginSymbName() {
		return pluginSymbName;
	}


	public void setPluginSymbName(String pluginSymbName) {
		this.pluginSymbName = pluginSymbName;
	}



	public void addRequire_bundle(String require_bundle) {
		this.require_Bundles.add(require_bundle);
	}


	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same symbolic name
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


	public boolean isJar() {
		return isJar;
	}


	public void setJar(boolean isJar) {
		this.isJar = isJar;
	}


	public String getAbsolutePath() {
		return absolutePath;
	}


	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((pluginSymbName == null) ? 0 : pluginSymbName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PluginElement other = (PluginElement) obj;
		if (pluginSymbName == null) {
			if (other.pluginSymbName != null)
				return false;
		} else if (!pluginSymbName.equals(other.pluginSymbName))
			return false;
		return true;
	}
	
	
	

}
