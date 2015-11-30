package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.filters.CamelCaseSplitter;
import org.but4reuse.wordclouds.filters.IWordsProcessing;
import org.but4reuse.wordclouds.filters.IgnoreUpperCaseProcessing;
import org.but4reuse.wordclouds.filters.MultiWordsFilter;
import org.but4reuse.wordclouds.filters.PartOfSpeechTagsFilter;
import org.but4reuse.wordclouds.filters.StemmingFilter;
import org.but4reuse.wordclouds.filters.StopWordsFilter;
import org.but4reuse.wordclouds.filters.SynonymsFilter;
import org.but4reuse.wordclouds.preferences.WordCloudPreferences;
import org.mcavallo.opencloud.Cloud;

public class Cloudifier {

	public static Cloud cloudify(List<String> words) {
		List<String> tags = processWords(words);
		Cloud c = new Cloud();
		c.setMaxTagsToDisplay(Activator.getDefault().getPreferenceStore().getInt(WordCloudPreferences.WORDCLOUD_NB_W));
		c.setMaxWeight(50);
		c.setMinWeight(5);
		for (String s : tags) {
			c.addTag(s);
		}
		return c;
	}

	private static List<String> processWords(List<String> words) {
		List<IWordsProcessing> processors = new ArrayList<IWordsProcessing>();
		
		processors.add(new CamelCaseSplitter());
		processors.add(new IgnoreUpperCaseProcessing());
		processors.add(new StopWordsFilter());
		processors.add(new MultiWordsFilter());
		processors.add(new StemmingFilter());
		processors.add(new SynonymsFilter());
		processors.add(new PartOfSpeechTagsFilter());
		
		for (IWordsProcessing processor : processors) {
			words = processor.processWords(words);
		}
		return words;
	}

}
