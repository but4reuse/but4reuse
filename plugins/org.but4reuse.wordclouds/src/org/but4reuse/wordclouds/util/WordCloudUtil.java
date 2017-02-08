package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.mcavallo.opencloud.Cloud;
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
		cmp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		for (Control c : cmp.getChildren())
			c.dispose();
		cmp.update();
		int spaceHint = 10;
		for (Tag t : cloud.tags()) {

			Label l = new Label(cmp, SWT.NORMAL);
			l.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

			Font f = new Font(Display.getCurrent(), "Arial", t.getWeightInt(), SWT.ITALIC);
			l.setFont(f);
			l.setText(t.getName());
			l.setToolTipText(String.format("%.2f", t.getScore()));
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
		 * For each tag in c1 me check if it is in c2. if it's right we add in
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

		if (cpt == 0)
			return 0;

		return (res) / cpt;
	}

	/**
	 * Get stop words defined in preferences
	 * 
	 * @return a non list of stop words
	 */
	public static List<String> getUserDefinedStopWords() {
		List<String> stopWords = new ArrayList<String>();
		String stopWordsString = Activator.getDefault().getPreferenceStore().getString(WordCloudPreferences.STOP_WORDS);
		if (stopWordsString == null || stopWordsString.isEmpty()) {
			return stopWords;
		}
		return Arrays.asList(stopWordsString.split(","));
	}

	public static List<String> getUserDefinedMultiWords() {
		List<String> multiWords = new ArrayList<String>();
		String multiWordsString = Activator.getDefault().getPreferenceStore()
				.getString(WordCloudPreferences.MULTI_WORDS);
		if (multiWordsString == null || multiWordsString.isEmpty()) {
			return multiWords;
		}
		return Arrays.asList(multiWordsString.split(","));
	}

	public static List<String> getUserDefinedSynonyms() {
		List<String> synonymWords = new ArrayList<String>();
		String synonymWordsString = Activator.getDefault().getPreferenceStore()
				.getString(WordCloudPreferences.SYNONYM_WORDS);
		if (synonymWordsString == null || synonymWordsString.isEmpty()) {
			return synonymWords;
		}
		return Arrays.asList(synonymWordsString.split(","));
	}

}
