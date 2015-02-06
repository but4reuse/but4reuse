package org.but4reuse.adapters.ui.views;

import java.util.ArrayList;
import java.util.List;

/**
 * PluginBouchon a remplacer par PluginElement
 * @author Selma Sadouk
 * @author Julia Wisniewski
 */
public class PluginBouchon {

	String name;
	
	public PluginBouchon(String name) {
		this.name = name;
	}
	
	public List<PluginBouchon> getDependencies() {
		ArrayList<PluginBouchon> list = new ArrayList<PluginBouchon>();
		if(name.equals("plugin1"))
			list.add(new PluginBouchon("plugin2"));
		return list;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
