/**
 * Credits
 * 
 * package edu.sussex.nlp.jws;
 * 'WuAndPalmer':
 *  David Hope, 2008
 */
package org.but4reuse.utils.nlp.similarity;
//package edu.sussex.nlp.jws;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

// 'WuAndPalmer':
// David Hope, 2008

public class WuAndPalmer {
	private IDictionary dict = null;
	private ArrayList<ISynsetID> roots = null;

	public WuAndPalmer(IDictionary dict, ArrayList<ISynsetID> roots) {
		// System.out.println("... WuAndPalmer");
		this.dict = dict;
		this.roots = roots;
	}

	// wup(1) 2 * depth(LCS) / depth (synset1) + depth(synset2)
	public double wup(String w1, int s1, String w2, int s2, String pos) {
		double wup = 0.0;
		IIndexWord word1 = null;
		IIndexWord word2 = null;
		// get the WordNet words in *any* POS
		if (pos.equalsIgnoreCase("n")) {
			word1 = dict.getIndexWord(w1, POS.NOUN);
			word2 = dict.getIndexWord(w2, POS.NOUN);
		}
		if (pos.equalsIgnoreCase("v")) {
			word1 = dict.getIndexWord(w1, POS.VERB);
			word2 = dict.getIndexWord(w2, POS.VERB);
		}
		// [error check]: check the words exist in WordNet
		if (word1 == null) {
			// System.out.println(w1 + "(" + pos + ") not found in WordNet " + dict.getVersion());
			return (0); // 0 is an error code
		}
		if (word2 == null) {
			// System.out.println(w2 + "(" + pos + ") not found in WordNet " + dict.getVersion());
			return (0); // 0 is an error code
		}
		// [error check]: check the sense numbers are not greater than the true
		// number of senses in WordNet
		List<IWordID> word1IDs = word1.getWordIDs();
		List<IWordID> word2IDs = word2.getWordIDs();
		if (s1 > word1IDs.size()) {
			// System.out.println(w1 + " sense: " + s1 + " not found in WordNet " + dict.getVersion());
			return (0); // 0 is an error code
		}
		if (s2 > word2IDs.size()) {
			// System.out.println(w2 + " sense: " + s2 + " not found in WordNet " + dict.getVersion());
			return (0); // 0 is an error code
		}
		// ...........................................................................................................................................
		// get the {synsets}
		IWordID word1ID = word1.getWordIDs().get(s1 - 1); // get the right sense
															// of word 1
		ISynset synset1 = dict.getWord(word1ID).getSynset();

		IWordID word2ID = word2.getWordIDs().get(s2 - 1); // get the right sense
															// of word 2
		ISynset synset2 = dict.getWord(word2ID).getSynset();
		// ...........................................................................................................................................
		// System.out.println("\n.................................................\n"
		// + w1 + "#" + s1 + w2 + "#" + s2 +
		// "\n.................................................\n");

		// [Case 1.] same synset
		if (synset1.equals(synset2)) {
			return (1.0);
		} else // [Case 2.] find the wup score (as per Perl ... mad!!!)
		{
			ArrayList<ArrayList<ISynsetID>> paths1 = paths(synset1);
			ArrayList<ArrayList<ISynsetID>> paths2 = paths(synset2);
			double maxscore = 0.0;

			for (ArrayList<ISynsetID> p1 : paths1) {
				for (ArrayList<ISynsetID> p2 : paths2) {
					double score = looking(p1, p2);
					if (score > maxscore) {
						maxscore = score;
					}
				}
			}
			wup = maxscore;

		} // else
		return (wup);
	}

	private double looking(ArrayList<ISynsetID> p1, ArrayList<ISynsetID> p2) {
		double d1 = (p1.size() + 1.0);
		double d2 = (p2.size() + 1.0);
		double lcs = 0.0;
		double score = 0.0;

		// get the lowest subsumer for the 2 paths
		ArrayList<ISynsetID> joins = new ArrayList<ISynsetID>();
		joins.addAll(p1);
		joins.retainAll(p2);
		// if there is no subsumer then the 'lowest subsumer' must be the
		// fake<root> at a depth of 1.0
		if (joins.isEmpty()) {
			lcs = 1.0;
		} else {
			// find the { lowest(s) } for the 2 paths, there may be> 1 lowest
			// subsumer ???not sure that that is true here???
			double min = Double.MAX_VALUE; // initalise 'lowest'
			ISynsetID minjoin = null;
			for (ISynsetID j : joins) {
				int x = p1.indexOf(j);
				int y = p2.indexOf(j);
				double xy = (x + y);
				if (xy < min) {
					min = xy;
					minjoin = j;
				}
			}
			// Case 1.
			if (minjoin.equals(p1.get(0))) {
				lcs = d1;
			}
			// Case 2.
			else if (minjoin.equals(p2.get(0))) {
				lcs = d2;
			}
			// Case 3.
			else {
				ArrayList<ArrayList<ISynsetID>> pathsLCS = paths(dict.getSynset(minjoin));
				double minpath = Double.MAX_VALUE;
				for (ArrayList<ISynsetID> p : pathsLCS) {
					double pl = p.size();
					if (pl < minpath) {
						minpath = pl;
					}
				}
				lcs = minpath + 1.0;
			} // else
		} // else

		score = (2.0 * lcs) / (d1 + d2);
		// EasyIn.pause("--score:\t" + score);
		if (score > 1.0) // paths have overstretched viz. the longest (shortest
							// path) is shorter than the longest path
		{
			return (0.0);
		}

		return (score);
	}

	private ArrayList<ArrayList<ISynsetID>> paths(ISynset synset) {
		ArrayList<ArrayList<ISynsetID>> paths = new ArrayList<ArrayList<ISynsetID>>();
		ArrayList<ISynsetID> start = new ArrayList<ISynsetID>();
		start.add(synset.getID());
		paths.add(start);
		boolean OK = true;

		while (OK) {
			for (int i = 0; i < paths.size(); i++) {
				OK = false;
				ArrayList<ISynsetID> path = paths.get(i);
				ArrayList<ArrayList<ISynsetID>> newpaths = new ArrayList<ArrayList<ISynsetID>>();
				ISynsetID end = path.get(path.size() - 1);
				HashSet<ISynsetID> up = hypernyms(end);
				if (!up.isEmpty()) {
					OK = true;
					for (ISynsetID h : up) {
						ArrayList<ISynsetID> newpath = new ArrayList<ISynsetID>();
						newpath.addAll(path);
						newpath.add(h);
						newpaths.add(newpath);
					}
				}
				paths.addAll(newpaths); // !!! overgenerates, but, hey !!!
			} // for
		} // while
			// .....................................................................................................................
			// paths output
		ArrayList<ArrayList<ISynsetID>> returnpaths = new ArrayList<ArrayList<ISynsetID>>();

		for (ArrayList<ISynsetID> p : paths) {
			// Collections.reverse(p); // reverse path so that we can follow the
			// Perl version
			if (roots.contains(p.get(p.size() - 1))) // check that path ends
														// with a <root>
			{
				returnpaths.add(p);
			}
		}
		return (returnpaths);
	}

	private HashSet<ISynsetID> hypernyms(ISynsetID sid) {
		HashSet<ISynsetID> hypernyms = new HashSet<ISynsetID>();
		ISynset synset = dict.getSynset(sid);
		hypernyms.addAll(synset.getRelatedSynsets(Pointer.HYPERNYM));
		hypernyms.addAll(synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE));
		return (hypernyms);
	}

	// wup(2) all senses
	public TreeMap<String, Double> wup(String w1, String w2, String pos) {
		// apple#pos#sense banana#pos#sense pathscore
		TreeMap<String, Double> map = new TreeMap<String, Double>();

		IIndexWord word1 = null;
		IIndexWord word2 = null;
		// get the WordNet words in *any* POS
		if (pos.equalsIgnoreCase("n")) {
			word1 = dict.getIndexWord(w1, POS.NOUN);
			word2 = dict.getIndexWord(w2, POS.NOUN);
		}
		if (pos.equalsIgnoreCase("v")) {
			word1 = dict.getIndexWord(w1, POS.VERB);
			word2 = dict.getIndexWord(w2, POS.VERB);
		}

		// [error check]: check the words exist in WordNet
		if (word1 != null && word2 != null) {
			// get the wup scores for the (sense pairs)
			List<IWordID> word1IDs = word1.getWordIDs(); // all senses of word 1
			List<IWordID> word2IDs = word2.getWordIDs(); // all senses of word 2
			int sx = 1;
			ISynset synset1 = null;
			ISynset synset2 = null;
			for (IWordID idX : word1IDs) {
				int sy = 1;
				for (IWordID idY : word2IDs) {
					double pathscore = wup(w1, sx, w2, sy, pos);
					map.put((w1 + "#" + pos + "#" + sx + "," + w2 + "#" + pos + "#" + sy), pathscore);
					sy++;
				}
				sx++;
			}
		} else {
			return (map); // nothing!!!
		}
		return (map);
	}

	// wup(3) all senses of word 1 vs. a specific sense of word 2
	public TreeMap<String, Double> wup(String w1, String w2, int s2, String pos) {
		// apple#pos#sense banana#pos#sense pathscore
		TreeMap<String, Double> map = new TreeMap<String, Double>();

		IIndexWord word1 = null;
		IIndexWord word2 = null;
		// get the WordNet words
		if (pos.equalsIgnoreCase("n")) {
			word1 = dict.getIndexWord(w1, POS.NOUN);
			word2 = dict.getIndexWord(w2, POS.NOUN);
		}
		if (pos.equalsIgnoreCase("v")) {
			word1 = dict.getIndexWord(w1, POS.VERB);
			word2 = dict.getIndexWord(w2, POS.VERB);
		}
		// [error check]: check the words exist in WordNet
		if (word1 != null && word2 != null) {
			// get the wup scores for the (sense pairs)
			List<IWordID> word1IDs = word1.getWordIDs(); // all senses of word 1
			int movingsense = 1;
			for (IWordID idX : word1IDs) {
				double pathscore = wup(w1, movingsense, w2, s2, pos);
				map.put((w1 + "#" + pos + "#" + movingsense + "," + w2 + "#" + pos + "#" + s2), pathscore);
				movingsense++;
			}
		} else {
			return (map);
		}
		return (map);
	}

	// wup(4) a specific sense of word 1 vs. all senses of word 2
	public TreeMap<String, Double> wup(String w1, int s1, String w2, String pos) {
		// (key)apple#pos#sense banana#pos#sense (value)pathscore
		TreeMap<String, Double> map = new TreeMap<String, Double>();
		IIndexWord word1 = null;
		IIndexWord word2 = null;
		// get the WordNet words
		if (pos.equalsIgnoreCase("n")) {
			word1 = dict.getIndexWord(w1, POS.NOUN);
			word2 = dict.getIndexWord(w2, POS.NOUN);
		}
		if (pos.equalsIgnoreCase("v")) {
			word1 = dict.getIndexWord(w1, POS.VERB);
			word2 = dict.getIndexWord(w2, POS.VERB);
		}
		// [error check]: check the words exist in WordNet
		if (word1 != null && word2 != null) {
			// get the wup scores for the (sense pairs)
			List<IWordID> word2IDs = word2.getWordIDs(); // all senses of word 2
			int movingsense = 1;
			for (IWordID idX : word2IDs) {
				double pathscore = wup(w1, s1, w2, movingsense, pos);
				map.put((w1 + "#" + pos + "#" + s1 + "," + w2 + "#" + pos + "#" + movingsense), pathscore);
				movingsense++;
			}
		} else {
			return (map);
		}
		return (map);
	}

	// get max score for all sense pairs
	public double max(String w1, String w2, String pos) {
		double max = 0.0;
		TreeMap<String, Double> pairs = wup(w1, w2, pos);
		for (String p : pairs.keySet()) {
			double current = pairs.get(p);
			if (current > max) {
				max = current;
			}
		}
		return (max);
	}

	// test
	public static void main(String[] args) {
		// dummy WordNet setup - allows one to run the examples from this Class
		// ...........................................................
		// WordNet vers. that you want to use (assumes that you have downloaded
		// the version in question)
		// String vers = "3.0";
		// *your* WordNet(vers.) is here ...
		// String wnhome = "C:/Program Files/WordNet/" + vers + "/dict";
		// *your* IC files are here ... (assumes that you have downloaded the IC
		// files which correspond to the WordNet(vers.)
		// String icfile = "C:/Program Files/WordNet/" + vers +
		// "/WordNet-InfoContent-" + vers + "/ic-semcor.dat";
		// URL url = null;
		// try {
		// url = new URL("file", null, wnhome);
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// }
		// if (url == null)
		// return;
		// IDictionary dict = new Dictionary(url);
		// try {
		// dict.open();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		IDictionary dict = null;
		try {
			String path = new File("lib") + File.separator + "dict";
			URL url = new URL("file", null, path);

			// construct the dictionary object and open it
			dict = new Dictionary(url);
			dict.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// <roots>
		ArrayList<ISynsetID> roots = new ArrayList<ISynsetID>();
		ISynset synset = null;
		Iterator<ISynset> iterator = null;
		List<ISynsetID> hypernyms = null;
		List<ISynsetID> hypernym_instances = null;
		iterator = dict.getSynsetIterator(POS.NOUN);
		while (iterator.hasNext()) {
			synset = iterator.next();
			hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM); // !!! if
																	// any of
																	// these
																	// point
																	// back (up)
																	// to synset
																	// then we
																	// have an
																	// inf. loop
																	// !!!
			hypernym_instances = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
			if (hypernyms.isEmpty() && hypernym_instances.isEmpty()) {
				roots.add(synset.getID());
			}
		}
		iterator = dict.getSynsetIterator(POS.VERB);
		while (iterator.hasNext()) {
			synset = iterator.next();
			hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM); // !!! if
																	// any of
																	// these
																	// point
																	// back (up)
																	// to synset
																	// then we
																	// have an
																	// inf. loop
																	// !!!
			hypernym_instances = synset.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE);
			if (hypernyms.isEmpty() && hypernym_instances.isEmpty()) {
				roots.add(synset.getID());
			}
		}

		// ....................................................................................................................................................................
		WuAndPalmer wup = new WuAndPalmer(dict, roots);
		// ....................................................................................................................................................................

		// Examples Of Use
		NumberFormat formatter = new DecimalFormat("0.####");

		/*
		 * // wup(1) specific senses double pathscore = wup.wup("cat", 2, "dog",
		 * 4, "n"); // "word1", sense#, "word2", sense#, "POS"
		 * System.out.println("specific senses"); //if(pathscore != 0) // 0 is
		 * an error code i.e it means that something isn't right e.g. words are
		 * not in WordNet, wrong POS etc //{ System.out.println("wup:\t" +
		 * formatter.format(pathscore)); //} System.out.println();
		 */

		// wup(2) all senses: a value (score) of 0 is an error code for a pair
		System.out.println("all senses");
		TreeMap<String, Double> map = wup.wup("dog", "hunting_dog", "n"); // "word1",
																			// "word2",
																			// "POS"
		// TreeMap<String,Double> map = wup.wup("cat", "dog", "n"); // "word1",
		// "word2", "POS"
		// TreeMap<String,Double> map = wup.wup("be", "eat", "v"); // "word1",
		// "word2", "POS"
		// TreeMap<String,Double> map = wup.wup("eat", "consume", "v"); //
		// "word1", "word2", "POS"
		ArrayList<Double> values = new ArrayList<Double>();
		values.addAll(map.values());
		ArrayList<String> closed = new ArrayList<String>();
		Collections.sort(values);
		Collections.reverse(values);
		for (Double d : values) {
			for (String pair : map.keySet()) {
				double s = map.get(pair);
				if (s == d) {
					if (!closed.contains(pair)) {
						System.out.println(pair + "\t" + formatter.format(map.get(pair)));
						closed.add(pair);
					}
				}
			}
		}
		System.out.println();

		/*
		 * // max value (i.e. highest score!) : get the highest score for 2
		 * words double maxvalue = wup.max("apple", "banana", "n"); // "word1",
		 * "word2", "POS" System.out.println("max value");
		 * System.out.println(formatter.format(maxvalue)); System.out.println();
		 * 
		 * 
		 * // wup(3) all senses of word 1 vs. a specific sense of word 2: a
		 * value (score) of 0 is an error code for a pair TreeMap<String,Double>
		 * map3 = wup.wup("apple", "banana", 2, "n"); // "word1", "word2",
		 * sense#, "POS"
		 * System.out.println("all senses of word 1 vs. fixed sense of word 2");
		 * for(String pair : map3.keySet()) { System.out.println(pair + "\t" +
		 * formatter.format(map3.get(pair))); } System.out.println();
		 * 
		 * // wup(4) a specific sense of word 1 vs. all senses of word 2: a
		 * value (score) of 0 is an error code for a pair TreeMap<String,Double>
		 * map4 = wup.wup("apple", 1, "banana", "n"); // "word1", sense#,
		 * "word2", "POS"
		 * System.out.println("fixed sense of word 1 vs. all senses of word 2");
		 * for(String pair : map4.keySet()) { System.out.println(pair + "\t" +
		 * formatter.format(map4.get(pair))); }
		 */
	}
}