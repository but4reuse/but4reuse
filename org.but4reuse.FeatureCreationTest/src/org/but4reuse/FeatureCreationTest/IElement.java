package org.but4reuse.FeatureCreationTest;

import java.util.ArrayList;

public class IElement {

	private String nameSymbolic;
	private ArrayList<String> distrib;
	
	public IElement() {
		// TODO Auto-generated constructor stub
	}
	
	public IElement( String nameSymb, ArrayList<String> dis)
	{
		nameSymbolic=nameSymb;
		distrib=dis;
	}

	public String getNameSymbolic() {
		return nameSymbolic;
	}

	public void setNameSymbolic(String nameSymbolic) {
		this.nameSymbolic = nameSymbolic;
	}

	public ArrayList<String> getDistrib() {
		return distrib;
	}

	public void setDistrib(ArrayList<String> distrib) {
		this.distrib = distrib;
	}

	

}
