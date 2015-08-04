package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

import com.sun.javafx.geom.Vec3d;

public class FeatureLocationLSI implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {
		
		
		
		for (Feature f : featureList.getOwnedFeatures())
		{	
			ArrayList<HashMap<String,Integer>> list = 
					new ArrayList<HashMap<String,Integer>>();
			ArrayList<Block> featureBlocks = new ArrayList<Block>();
			
			for (Artefact a : f.getImplementedInArtefacts()) 
			{
				AdaptedArtefact aa = AdaptedModelHelper.getAdaptedArtefact(adaptedModel, a);
				 for(Block b : AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa))
				 {
					 if(featureBlocks.contains(b))
						continue;
					 featureBlocks.add(b);
					 HashMap<String,Integer> t = new HashMap<String,Integer>();
					 
					 for(IElement e : AdaptedModelHelper.getElementsOfBlock(b))
					 {
						 List<String> words = ((AbstractElement)e).getWords();
						 for(String w : words)
						 {
							 String tmp = w.toLowerCase();
							 if(t.containsKey(tmp))
								 t.put(tmp,t.get(tmp)+1);
							 else
								 t.put(tmp, 1);
						 }
					 }
					 list.add(t);
				 }
			}
			
		list.add(getFeatureWords(f));
		
		Matrix m = new Matrix(createMatrix(list));
		SingularValueDecomposition svd = m.svd();
		
		Matrix v = svd.getV();
		Matrix s = svd.getS();
		Matrix q = s.times(v.getMatrix(0,v.getRowDimension(),v.getColumnDimension()-1,v.getColumnDimension()-1));
		Vec3d veqQ = new Vec3d(0, 0, 0);
		for(int i =0;i< v.getRowDimension()-1;i++)
		{
			Matrix vector = s.times(v.getMatrix(0,v.getRowDimension(),i,i));		
		}
	
		
		}
		return null;
	}

	static public HashMap<String,Integer>getFeatureWords(Feature f)
	{
		HashMap<String,Integer> featureWords = new HashMap<String, Integer>();
		if (f.getName() != null) {
			StringTokenizer tk = new StringTokenizer(f.getName(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");

			while (tk.hasMoreTokens()) {
				for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
					String tmp = w.toLowerCase();
					 if(featureWords.containsKey(tmp))
						featureWords.put(tmp,featureWords.get(tmp)+1);
					 else
						 featureWords.put(tmp, 1);
				 }
				}
			}
		
		if (f.getDescription() != null) {
			StringTokenizer tk = new StringTokenizer(f.getDescription(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");
			while (tk.hasMoreTokens()) {
				for (String w : tk.nextToken().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
					String tmp = w.toLowerCase();
					 if(featureWords.containsKey(tmp))
						featureWords.put(tmp,featureWords.get(tmp)+1);
					 else
						 featureWords.put(tmp, 1);
				}
			}
		}
		return featureWords;
	}
	
	public static double[][] createMatrix(ArrayList<HashMap<String,Integer>> list)
	{
		ArrayList<String> words = new ArrayList<String>();
		
		int cpt =0;
		for(HashMap<String,Integer> t : list)
		{
			for(String s : t.keySet())
			{
				if(!words.contains(s))
				{
					cpt++;
					words.add(s);
				}
			}
		}
		
		Collections.sort(words);
		if (cpt == 0)
			return null;
		
		double matrix [][] = new double[list.size()][cpt];
		int i = 0;
		for(HashMap<String,Integer> t : list)
		{
			for(String w : words)
			{
				if(t.containsKey(w))
					matrix[i][words.indexOf(w)]=t.get(w);
				else
					matrix[i][words.indexOf(w)]=0;
			}
			i++;
		}
		
		return matrix;
	}
	
	public static double cosine(Vec3d u, Vec3d v)
	{
		return u.x*v.x+u.y*v.y+u.z*v.z;
	}
	
}
