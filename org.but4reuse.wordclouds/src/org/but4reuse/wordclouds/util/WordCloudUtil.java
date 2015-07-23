package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Cloud.Case;
import org.mcavallo.opencloud.Tag;

/**
 * @author Arthur A Toolbox to draw word cloud
 */
public class WordCloudUtil {

	/**
	 * Draw the word cloud cloud in the canvas can.
	 * 
	 * @param cmp
	 *            The ScrolledComposite the string will be drawn.
	 * @param cloud
	 *            This cloud contains strings that you want to draw in your
	 *            canvas.
	 */

	public static void drawWordCloud(Composite cmp, Cloud cloud) {
		int x = 10, y = 10;
		int maxH = 0;
		for (Control c : cmp.getChildren())
			c.dispose();
		cmp.update();
		int spaceHint = 10;
		for (Tag t : cloud.tags()) {

			Label l = new Label(cmp, SWT.NORMAL);
			Font f = new Font(Display.getCurrent(), "Arial", t.getWeightInt(), SWT.ITALIC);
			l.setFont(f);
			l.setText(t.getName());
			l.pack();

			if (x + l.getBounds().width > cmp.getBounds().width * 0.70 - 25) {
				x = 10;
				y += maxH + spaceHint;
				maxH = 0;
			}

			if (maxH < l.getBounds().height)
				maxH = l.getBounds().height;

			l.setLocation(x, y);
			x += spaceHint + l.getBounds().width;
		}

	}

	/**
	 * 
	 * It'll check if a name is already used as name.
	 * 
	 * @param list
	 *            A list which contains all name used.
	 * @param name
	 *            The name that we want check.
	 * @return True if the name is not present in the list, false otherwise.
	 */
	public static boolean checkName(List<String> list, String name) {
		for (String s : list) {
			if (s.compareToIgnoreCase(name) == 0)
				return false;
		}
		return true;
	}

	/**
	 * It will return a name not present in names. It'll combine words with the
	 * better weight in the cloud to create a name.
	 * 
	 * @param names
	 *            Existing name list.
	 * @param c
	 *            Word Cloud which contains words to use to create the name.
	 * @return a new name.
	 */
	public static String rename(List<String> names, Cloud c) {
		if (c.tags().size() == 0)
			return null;

		Cloud c_cp = new Cloud(c);
		Tag t = null;
		double score = 0;
		String name = "";

		for (Tag tag : c_cp.tags()) {
			if (tag.getScore() > score) {
				t = tag;
				score = tag.getScore();
			}
		}

		c_cp.removeTag(t);
		name = t.getName();
		String nameTmp = name;
		int cpt = 1;

		while (!checkName(names, name)) {
			if (c_cp.tags().size() != 0) {
				c_cp.removeTag(t);
				score = 0;
				for (Tag tag : c_cp.tags()) {
					if (tag.getScore() > score) {
						t = tag;
						score = tag.getScore();
					}
				}
				name += "_" + t.getName();
			} else {
				name = nameTmp + "_" + cpt;
				cpt++;
			}
		}

		return name;

	}

	/**
	 * It'll compare c1 with c2.
	 * 
	 * @param c1
	 *            The first cloud.
	 * @param c2
	 *            The second cloud.
	 * @return A rate which is the percent of words from c1 found in c2.
	 */

	public static double cmpClouds(Cloud c1, Cloud c2) {
		double cpt = 0;
		int res = 0;

		// We get all tags in c1 sorted.
		List<Tag> tags1 = c1.tags(new Tag.ScoreComparatorDesc());

		/*
		 * For each tag in c1 me check if he is in c2. if it's right we add in
		 * res the tag score. In the same time we make the score sum.
		 * 
		 * I use tags score to make comparison because if we found just words
		 * which is not important in the cloud it won't be relevant.
		 */
		for (int i = 0; i < tags1.size(); i++) {
			Tag t1 = tags1.get(i);
			Tag t2 = c2.getTag(t1.getName());
			if (t2 != null)
				res += t1.getScoreInt();
			cpt += t1.getScoreInt();

		}

		return (double) (res) / cpt;
	}

	/**
	 * It will create a word cloud using TD-IDF formula
	 * 
	 * @param list
	 *            An ArrayList of String ArrayList, each sub list contains all
	 *            words in one of your "Document"
	 * @param index
	 *            The index of the list that we want use to create the word
	 *            cloud
	 * @return A new word cloud
	 */
	public static Cloud createWordCloudIDF(ArrayList<ArrayList<String>> list, int index) {
		double nbBlock = list.size();
		double nbWords = list.get(index).size();

		ArrayList<String> wordsChecked = new ArrayList<String>();

		Cloud cloud_IDF = new Cloud(Case.CAPITALIZATION);
		cloud_IDF.setMaxTagsToDisplay(50);
		cloud_IDF.setMinWeight(5);
		cloud_IDF.setMaxWeight(50);

		for (String w : list.get(index)) {
			/*
			 * If we already add this words in the cloud we check the next
			 * words.
			 */
			if (countNbTimesWord(wordsChecked, w) != 0)
				continue;
			/*
			 * Here the score of the word w is calculated Formula TD-IDF (
			 * https://fr.wikipedia.org/wiki/TF-IDF )
			 */

			double nbBlock_isPresent = nbDocContainsWords(list, w);
			double nbTimeW = countNbTimesWord(list.get(index), w);
			double idf = Math.log(nbBlock / nbBlock_isPresent);
			double td = (nbTimeW / nbWords);
			double score = td * idf;

			cloud_IDF.addTag(new Tag(w, score));
			wordsChecked.add(w);

		}

		return cloud_IDF;

	}

	/**
	 * Count how many lists contain the string word
	 * 
	 * @param list
	 *            It will search the word in each sub list of list parameter.
	 * @param words
	 *            The word that we want to find.
	 * @return How many time we find the tag.
	 */

	private static int nbDocContainsWords(ArrayList<ArrayList<String>> list, String word) {
		int cpt = 0;
		for (ArrayList<String> l : list) {
			for (String w : l) {
				if (w.compareToIgnoreCase(word) == 0) {
					cpt++;
					break;
				}
			}
		}
		return cpt;
	}

	/**
	 * Count how many time the String word is found in words
	 * 
	 * @param words
	 *            The words list where we search.
	 * @param word
	 *            The word that we want to search.
	 * @return how many time the word was found.
	 */
	private static int countNbTimesWord(ArrayList<String> words, String word) {
		int cpt = 0;
		for (String w : words) {
			if (w.compareToIgnoreCase(word) == 0)
				cpt++;
		}
		return cpt;
	}

}
