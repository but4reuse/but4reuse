package org.but4reuse.adapters.eclipse;

import java.util.ArrayList;

import org.but4reuse.adapters.IElement;

/**
 * Plugin Element
 * 
 * @author Diana MALABARD
 * @author Jason CHUMMUN
 */
public class PluginElement extends FileElement {

	private String pluginSymbName;
	private String pluginVersion;
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

	public void removeRequire_bundle(String require_bundle) {
		this.require_Bundles.remove(require_bundle);
	}

	@Override
	public String getText() {
		return pluginSymbName + " " + pluginVersion;
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
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		// TODO URIs can reference to the same file... check this
		if (anotherElement instanceof PluginElement) {
			PluginElement anotherPluginElement = ((PluginElement) anotherElement);

			// Same symbolic name
			if (this.getPluginSymbName().equals(anotherPluginElement.getPluginSymbName())) {
				// TODO no versioning supported
				return 1;
			}
		}
		return 0;
	}

	public String getPluginVersion() {
		return pluginVersion;
	}

	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getPluginSymbName() == null) ? 0 : getPluginSymbName().hashCode());
		return result;
	}

}
