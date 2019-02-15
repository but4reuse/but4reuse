package org.but4reuse.adapters.ui.xmlgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;

public class ArtefactStat {
	
	/**
	 * Name of the artefact
	 */
	private String name; 
	
	/**
	 * Level of reuse : percentage of elements that this artefact shares with others artefacts
	 */
	private double levelOfReuse;
	
	/**
	 * Number of elements this artefact shared with others
	 */
	private int numberOfElementsShared;
	/**
	 * List of artefacts which reuse the element(s) of this artefact 
	 * with the level of reuse for each artefact
	 */
	private HashMap<String, Double> ListStats = new HashMap<>();
	
	/**
	 * List of element(s) reused by each artefact of ListStats
	 */
	private HashMap<String, List<IElement>> listElementsSharingWithOthers = new HashMap<>();
	
	/**
	 * List of element(s) unique to this artefact
	 */
	private List<IElement> listOfUniqueElements = new ArrayList<IElement>();
	
	public ArtefactStat(
			String name, 
			double levelOfReuse, 
			HashMap<String, List<IElement>> listElementsSharingWithOthers, 
			HashMap<String, Double> listStats, 
			List<IElement> listOfUniqueElements,
			int numberOfElementsShared) {
		super();
		this.name = name;
		this.levelOfReuse = levelOfReuse;
		this.listElementsSharingWithOthers = listElementsSharingWithOthers;
		this.ListStats = listStats;
		this.listOfUniqueElements = listOfUniqueElements;
		this.numberOfElementsShared = numberOfElementsShared;
	}

	public void setListElements(HashMap<String, List<IElement>> listElements) {
		listElementsSharingWithOthers = listElements;
	}
	public HashMap<String, List<IElement>> getListElements() {
		return listElementsSharingWithOthers;
	}
	public List<IElement> getListElementsString(String d) {
		return listElementsSharingWithOthers.get(d);
	}
	
	public String getName() {
		return name;
	}


	public double getLevelOfReuse() {
		return levelOfReuse;
	}


	public HashMap<String, Double> getListStats() {
		return ListStats;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setLevelOfReuse(double levelOfReuse) {
		this.levelOfReuse = levelOfReuse;
	}


	public void setListStats(HashMap<String, Double> listStats) {
		ListStats = listStats;
	}

	public List<IElement> getListOfUniqueElements() {
		return listOfUniqueElements;
	}

	public int getNumberOfElementsShared() {
		return numberOfElementsShared;
	}

	public void setNumberOfElementsShared(int numberOfElementsShared) {
		this.numberOfElementsShared = numberOfElementsShared;
	}
	
}
