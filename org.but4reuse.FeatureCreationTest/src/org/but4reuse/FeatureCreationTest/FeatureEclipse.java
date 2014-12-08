package org.but4reuse.FeatureCreationTest;

import java.util.ArrayList;

public class FeatureEclipse {
	private String name;
	private ArrayList<String> distributions;
	private ArrayList<String> plugins;
	
	public FeatureEclipse(String name, ArrayList<String> plugins, ArrayList<String> distribs){
		this.name = name;
		this.plugins = plugins;
		this.distributions = distribs;
	}

	
	public ArrayList<String> getDistributions() {
		return distributions;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((plugins == null) ? 0 : plugins.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureEclipse other = (FeatureEclipse) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (plugins == null) {
			if (other.plugins != null)
				return false;
		} else if (!plugins.equals(other.plugins))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getPlugins() {
		return plugins;
	}
}
