package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;

public abstract class AbstractElement implements IElement {

	private static final String MAIN_DEPENDENCY_ID = "depends on";
	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<String, List<Object>> dependencies = new HashMap<String, List<Object>>();
	private Map<String, Integer> minDependencies = new HashMap<String, Integer>();
	private Map<String, Integer> maxDependencies = new HashMap<String, Integer>();

	@Override
	/**
	 * HashCode default implementation returns always 1 so it will be handled as a list.
	 * This method is intended to be overridden if a hash method could be provided to improve performance.
	 */
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		return getText();
	}

	@Override
	public Map<String, List<Object>> getDependencies() {
		return dependencies;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		return maxDependencies.get(dependencyID);
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		return minDependencies.get(dependencyID);
	}

	public void setMaximumDependencies(String dependencyID, int number) {
		maxDependencies.put(dependencyID, number);
	}

	public void setMinimumDependencies(String dependencyID, int number) {
		minDependencies.put(dependencyID, number);
	}

	public void addDependency(String dependencyID, Object dependency) {
		List<Object> o = dependencies.get(dependencyID);
		if (o == null) {
			o = new ArrayList<Object>();
		}
		if (!o.contains(dependency)) {
			o.add(dependency);
		}
		dependencies.put(dependencyID, o);
	}

	public void addDependency(String dependency) {
		addDependency(MAIN_DEPENDENCY_ID, dependency);
	}

	public void addDependencies(List<String> dependencies) {
		for (String dependency : dependencies) {
			addDependency(MAIN_DEPENDENCY_ID, dependency);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IElement) {
			// TODO get threshold
			return similarity((IElement) obj) == 1.0;
		}
		return super.equals(obj);
	}

}
