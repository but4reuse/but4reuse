package org.but4reuse.adapters.eclipse.benchmark;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.artefactmodel.Artefact;

/**
 * Actual Feature
 * 
 * @author jabier.martinez
 */
public class ActualFeature implements Comparable<ActualFeature> {
	private String id;
	private String name;
	private String description;
	private List<String> includedFeatures;
	private List<String> requiredFeatures;
	private List<String> requiredPlugins;
	private List<String> plugins;
	private List<Artefact> artefacts = new ArrayList<Artefact>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ActualFeature) {
			return id.equals(((ActualFeature) o).getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public int compareTo(ActualFeature another) {
		return id.compareTo(another.getId());
	}

	public List<String> getIncludedFeatures() {
		return includedFeatures;
	}

	public void setIncludedFeatures(List<String> requiredFeatures) {
		this.includedFeatures = requiredFeatures;
	}

	public List<String> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<String> plugins) {
		this.plugins = plugins;
	}

	public List<String> getRequiredFeatures() {
		return requiredFeatures;
	}

	public void setRequiredFeatures(List<String> requiredFeatures) {
		this.requiredFeatures = requiredFeatures;
	}

	public List<String> getRequiredPlugins() {
		return requiredPlugins;
	}

	public void setRequiredPlugins(List<String> requiredPlugins) {
		this.requiredPlugins = requiredPlugins;
	}

	public List<Artefact> getArtefacts() {
		return artefacts;
	}

	public void setArtefacts(List<Artefact> artefacts) {
		this.artefacts = artefacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ActualFeature [id=" + id + ", name=" + name + "]";
	}

}
