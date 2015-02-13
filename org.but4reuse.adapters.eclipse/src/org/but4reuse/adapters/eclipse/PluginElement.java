package org.but4reuse.adapters.eclipse;

import java.util.ArrayList;

/**
 * Plugin Element
 * 
 * @author Diana MALABARD
 * @author Jason CHUMMUN
 */
public class PluginElement extends FileElement {

	private String pluginSymbName;
	private String pluginName;
	// each pluginElement in require_Bundles, the list of require_Bundle will be
	// empty.
	// because we do not know the dependencies
	// The same thing for absoluthPath
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
	public String getText() {
		return " Plugin :" + pluginSymbName + " -> " + super.getText();
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

}
