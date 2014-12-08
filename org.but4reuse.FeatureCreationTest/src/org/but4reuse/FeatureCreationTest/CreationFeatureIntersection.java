package org.but4reuse.FeatureCreationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CreationFeatureIntersection {

	public static void main(String[] args) {

		IElement el1= new IElement("name1",new ArrayList<String>(Arrays.asList("1", "2", "5")));
		IElement el2= new IElement("name2",new ArrayList<String>(Arrays.asList("1", "4", "5")));
		IElement el3= new IElement("name3",new ArrayList<String>(Arrays.asList("1")));
		IElement el4= new IElement("name4",new ArrayList<String>(Arrays.asList("4", "7", "5")));
		
		
		
		
		final ArrayList<IElement> atoms= new ArrayList<IElement>(Arrays.asList(el1,el2,el3,el4));
		
		
	    
		for (int i=0; i<atoms.size()-1; i++){ 
			ArrayList<ArrayList<String>> feature= new ArrayList<ArrayList<String>>();
			for (int j=i+1; j<atoms.size();j++)
			{
					// feature- block des plugins qui font partie d'une seule distribution
					if(atoms.get(i).getDistrib().size()==1)
						{
							int dist=Integer.parseInt(atoms.get(i).getDistrib().get(0));
							feature.get(dist).add(atoms.get(i).getNameSymbolic());
						}				
				
					else
					{
						int taille = atoms.get(i).getDistrib().size();
						int nbintersect=2;
						while(nbintersect<taille)
						{
							// fonct recursive
						}
						findDistribution(atoms.get(i).getDistrib(),atoms.get(j));
					}
			}			
			
			}
			}
	
		
	
	/****
	 * 
	 * return true si le plugin se trouve dans toutes les x distributions 
	 * @param d liste des distributions
	 * @param plugin  le plugin qu'on analyse
	 *
	 */
	public static boolean findDistribution(ArrayList<String> d, IElement plugin)
	{
		
		int taille=d.size();
		for(int i=0; i<taille; i++)
		{
			if (!isAvailabe(plugin, d.get(i)))
				return false;
		}
		// ajoute le plugin dans l'intersection des x distributions dans array d
		return true;
		
		
	}
	
	/**
	 * 
	 * @param plugin
	 * @param dis
	 * @return true si ce plugin se trouve dans la distriubtion dis
	 */
	public static boolean isAvailabe(IElement plugin, String dis)
	{
		for(int i=0; i<plugin.getDistrib().size();i++)
		{
			if(plugin.getDistrib().get(i).equals(dis))
			return true;
		}
         return false;
	}
	
	public static ArrayList<String> distributionsList(ArrayList<IElement> plugins){
		Set<String> set= new HashSet<String>();
	
		Iterator<IElement> it1 = plugins.iterator();
		while(it1.hasNext()){
			ArrayList<String> dis= it1.next().getDistrib();
			set.addAll(dis);
		}

		Iterator<String> it = set.iterator();
		ArrayList<String> finalDistribList = new ArrayList<>();
		while(it.hasNext()) {
			finalDistribList.add(it.next());
		}
		System.out.println(finalDistribList);
		return finalDistribList;
	}
	
	public static void createFeatures(){
		
	}
	
	
}
