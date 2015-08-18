package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.lsi.activator.Activator;
import org.but4reuse.feature.location.lsi.location.preferences.LSIPreferencePage;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class FeatureLocationLSI implements IFeatureLocation {

	@Override
	public List<LocatedFeature> locateFeatures(FeatureList featureList, AdaptedModel adaptedModel,
			IProgressMonitor monitor) {

		List<LocatedFeature> locatedFeatures = new ArrayList<LocatedFeature>();
		ArrayList<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();
		ArrayList<Block> featureBlocks = new ArrayList<Block>();

		/*
		 * We gather all words for each block 
		 */

			for (Block b : adaptedModel.getOwnedBlocks()) {
				
				featureBlocks.add(b);
				HashMap<String, Integer> t = new HashMap<String, Integer>();

				for (IElement e : AdaptedModelHelper.getElementsOfBlock(b)) {
					List<String> words = ((AbstractElement) e).getWords();
					for (String w : words) {
						String tmp = w.toLowerCase();
						if (t.containsKey(tmp))
							t.put(tmp, t.get(tmp) + 1);
						else
							t.put(tmp, 1);
					}
				}
				/*
				 * we add all words form the block in a list
				 */
				list.add(t);
			}

		/*
		 * if the list is empty it means we found 0 zero block for the
		 * current feature so it's not necessary to continue with the
		 * feature
		 */
		if (list.size() == 0)
			return locatedFeatures;
		
		for (Feature f : featureList.getOwnedFeatures()) {
			

		
			double vecQ [] = createQuery(list, getFeatureWords(f));
			

			/*
			 * Here we use the LSI for comparing words from the feature and
			 * words form the block
			 * https://fr.wikipedia.org/wiki/Analyse_s%C3%A9mantique_latente
			 */
			
			
			
			Matrix m = new Matrix(createMatrix(list));
			SingularValueDecomposition svd = m.svd();

			Matrix v = svd.getV();
			Matrix s = svd.getS();
			Matrix u = svd.getU();
			
			
		
			
			boolean fixed = Activator.getDefault().getPreferenceStore().getBoolean(LSIPreferencePage.FIXED);
			int nbDim;
			
			double dim =Activator.getDefault().getPreferenceStore().getDouble(LSIPreferencePage.DIM);
			if(fixed)
				nbDim = (int)dim;
			else
			{
				nbDim = (int)(dim * m.getRowDimension());
			}
			nbDim = Math.min(nbDim, m.getRowDimension());
			
			Matrix sk = s.getMatrix(0,nbDim-1,0,nbDim-1);
			Matrix uk = u.getMatrix(0, u.getRowDimension()-1,0,nbDim-1);
		    
			Matrix q = new Matrix(vecQ, vecQ.length);
		   
		    q = (sk.inverse().times(uk.transpose()).times(q));
			
		    for (int i = 0; i < m.getColumnDimension(); i++) {
				Block b = featureBlocks.get(i);
				Matrix ve = m.getMatrix(0,v.getRowDimension()-1,i,i);
				Matrix vector = sk.inverse().times(uk.transpose()).times(ve);
				
				double vecDoc[]  =  vector.getColumnPackedCopy();
			

				double cos =cosine(q.getColumnPackedCopy(), vecDoc);
				/*
				 * If the cosine between the feature vector and the block vector
				 * is > 0 it means that it's relevant to think that there are
				 * links between words form block and words from feature.
				 */

				// We change value which is between -1 and 1 to a value between
				// 0 and 1
				cos++;
				cos /= 2;
				if (cos >0.0)
					locatedFeatures.add(new LocatedFeature(f, b, cos));
			}

		}
		return locatedFeatures;
	}

	/**
	 * It will give the words from the feature
	 * 
	 * @param f
	 *            The feature
	 * @return A HashMap with the words from the description and feature name.
	 *         For each words we have how many times it was found
	 */
	static public HashMap<String, Integer> getFeatureWords(Feature f) {
		/*
		 * We gather and count words from the feature name
		 */
		HashMap<String, Integer> featureWords = new HashMap<String, Integer>();
		if (f.getName() != null) {
			StringTokenizer tk = new StringTokenizer(f.getName(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");

			while (tk.hasMoreTokens()) {
				String tmp = tk.nextToken().toLowerCase();
				if (featureWords.containsKey(tmp))
					featureWords.put(tmp, featureWords.get(tmp) + 1);
				else
					featureWords.put(tmp, 1);
			}
		}

		/*
		 * We gather and count words from the feature description
		 */
		if (f.getDescription() != null) {
			StringTokenizer tk = new StringTokenizer(f.getDescription(), " :!?*+²&~\"#'{}()[]-|`_\\^°,.;/§");
			while (tk.hasMoreTokens()) {

				String tmp = tk.nextToken().toLowerCase();
				if (featureWords.containsKey(tmp))
					featureWords.put(tmp, featureWords.get(tmp) + 1);
				else
					featureWords.put(tmp, 1);
			}
		}
		/*
		 * We return all words found
		 */
		return featureWords;
	}

	static public double [] createQuery(ArrayList<HashMap<String,Integer>> list, HashMap<String,Integer>map)
	{
		ArrayList<String> words = new ArrayList<String>();

		/*
		 * We count all different words In the matrix we must have for each
		 * words how many times it was found in the document even if it's 0
		 */
		int cpt = 0;
		for (HashMap<String, Integer> t : list) {
			for (String s : t.keySet()) {
				if (!words.contains(s)) {
					cpt++;
					words.add(s);
				}
			}
		}

		/*
		 * We sort the words list ( From LSI not necessary but made generally)
		 */
		Collections.sort(words);
		if (cpt == 0)
			return null;
		
		double tab[] = new double[words.size()];
		
		int i = 0;
		for (String w : words) {
			if (map.containsKey(w))
				tab[i] = map.get(w);
			else
				// If a words isn't in the HashMap it means that the word
				// did appear in the document so we see it time
				tab[i] = 0;
			i++;
		}
		
		
		
		return tab;
	}
	/**
	 * A Matrix which represents the words found in several documents In rows
	 * there are how many times a word is found in each document In columns
	 * there are how many times each words is found in a document
	 * 
	 * @param list
	 *            HashMap which contains for each document, words found and how
	 *            many times each word was found.
	 * @return A matrix
	 */
	public static double[][] createMatrix(ArrayList<HashMap<String, Integer>> list) {
		ArrayList<String> words = new ArrayList<String>();

		/*
		 * We count all different words In the matrix we must have for each
		 * words how many times it was found in the document even if it's 0
		 */
		int cpt = 0;
		for (HashMap<String, Integer> t : list) {
			for (String s : t.keySet()) {
				if (!words.contains(s)) {
					cpt++;
					words.add(s);
				}
			}
		}

		/*
		 * We sort the words list ( From LSI not necessary but made generally)
		 */
		Collections.sort(words);
		if (cpt == 0)
			return null;

		/*
		 * We fill the matrix we have one HashMap per document In the HashMap we
		 * have a words and how many times it was found
		 */
		double matrix[][] = new double[cpt][list.size()];
		int i = 0;
		for (HashMap<String, Integer> t : list) {
			for (String w : words) {
				if (t.containsKey(w))
					matrix[words.indexOf(w)][i] = t.get(w);
				else
					// If a words isn't in the HashMap it means that the word
					// did appear in the document so we see it time
					matrix[words.indexOf(w)][i] = 0;
			}
			i++;
		}

		return matrix;
	}

	/**
	 * Calculate the cosine between two vector
	 * 
	 * @param u
	 *            The vector U
	 * @param v
	 *            The vector V
	 * @return The cosine
	 */
	public static double cosine(double  u[], double v[]) {
		/*
		 * the formula for cosine between vector U and V is ( U * V ) / ( ||U||
		 * * ||V|| )
		 */

		double scalaire = 0.0;
		for(int i = 0; i<u.length;i++)
			scalaire+=u[i]*v[i];
		
		double normeU =0.0;
		for(int i = 0; i< u.length;i++)
			normeU+=u[i]*u[i];
		normeU = Math.sqrt(normeU);
		
		double normeV = 0.0;
		for(int i = 0; i<v.length;i++)
			normeV+=v[i]*v[i];
		normeV = Math.sqrt(normeV);

		double val  =scalaire / (normeU * normeV);

		if(Double.isNaN(val))
			val = -1;
		return 	val;
	}
	
	
}
