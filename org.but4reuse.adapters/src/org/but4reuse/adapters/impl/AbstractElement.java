package org.but4reuse.adapters.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;

public abstract class AbstractElement implements IElement {

	/**
	 * Abstract IElement
	 * 
	 * @author jabier.martinez
	 */
	private Map<Object, List<Object>> owners = new HashMap<Object, List<Object>>();
	private Map<Object, Integer> minimumOwners = new HashMap<Object, Integer>();
	private Map<Object, Integer> maximumOwners = new HashMap<Object, Integer>();

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
	public Map<Object, List<Object>> getOwners() {
		return owners;
	}

	@Override
	public int getMaximumOwners(Object relationID) {
		return maximumOwners.get(relationID);
	}

	@Override
	public int getMinimumOwners(Object relationID) {
		return minimumOwners.get(relationID);
	}

	public void setMaximumOwners(Object relationID, int number) {
		maximumOwners.put(relationID, number);
	}

	public void setMinimumOwners(Object relationID, int number) {
		minimumOwners.put(relationID, number);
	}

	public void addOwner(Object relationID, Object owner) {
		List<Object> o = owners.get(relationID);
		if (o == null) {
			o = new ArrayList<Object>();
		}
		o.add(owner);
		owners.put(relationID, o);
	}

}
