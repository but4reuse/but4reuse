package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.but4reuse.adapters.IElement;

public abstract class AbstractElement implements IElement {

	private static final String MAIN_DEPENDENCY_ID = "dependency";
	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<Object, List<Object>> dependencies = new HashMap<Object, List<Object>>();
	private Map<Object, Integer> minDependencies = new HashMap<Object, Integer>();
	private Map<Object, Integer> maxDependencies = new HashMap<Object, Integer>();
	

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
	public Map<Object, List<Object>> getDependencies() {
		return dependencies;
	}

	@Override
	public int getMaxDependencies(Object dependencyID) {
		return maxDependencies.get(dependencyID);
	}

	@Override
	public int getMinDependencies(Object dependencyID) {
		return minDependencies.get(dependencyID);
	}

	public void setMaximumDependencies(Object dependencyID, int number) {
		maxDependencies.put(dependencyID, number);
	}

	public void setMinimumDependencies(Object dependencyID, int number) {
		minDependencies.put(dependencyID, number);
	}

	public void addDependency(Object dependencyID, Object dependency) {
		List<Object> o = dependencies.get(dependencyID);
		if (o == null) {
			o = new ArrayList<Object>();
		}
		if(!o.contains(dependency)){
			o.add(dependency);
		}
		dependencies.put(dependencyID, o);
	}
	
	public void addDependency(Object dependency){
		addDependency(MAIN_DEPENDENCY_ID, dependency);
	}
	
	public void addDependencies(List<Object> dependencies){
		for(Object dependency: dependencies){
			addDependency(MAIN_DEPENDENCY_ID, dependency);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IElement) {
			// TODO get threshold
			return similarity((IElement) obj)==1.0;
		}
		return super.equals(obj);
	}
	
}
