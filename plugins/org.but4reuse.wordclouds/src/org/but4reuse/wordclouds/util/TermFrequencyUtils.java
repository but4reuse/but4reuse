package org.but4reuse.wordclouds.util;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.utils.strings.StringUtils;
import org.but4reuse.wordclouds.filters.IWordsProcessing;
import org.but4reuse.wordclouds.filters.IgnoreUpperCaseProcessing;
import org.but4reuse.wordclouds.filters.PartOfSpeechTagsFilter;
import org.but4reuse.wordclouds.filters.StemmingFilter;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

/**
 * Term frequency utils
 * 
 * @author jabier.martinez
 */
public class TermFrequencyUtils {

	public static List<String> getFeatureWords(Feature f) {
		List<String> featureWords = StringUtils.tokenizeString(f.getName());
		featureWords.addAll(StringUtils.tokenizeString(f.getDescription()));
		featureWords = Cloudifier.processWords(featureWords, getBasicProcessors(), new NullProgressMonitor());
		return featureWords;
	}

	public static List<String> getElementWords(IElement e) {
		return Cloudifier.processWords(((AbstractElement) e).getWords(), getBasicProcessors(),
				new NullProgressMonitor());
	}

	public static int calculateTermFrequency(Feature f, IElement element) {
		List<String> featureWords = getFeatureWords(f);
		List<String> elementWords = getElementWords(element);
		return calculateTermFrequency(featureWords, elementWords);
	}

	public static int calculateTermFrequency(List<String> list1, List<String> list2) {
		int same = 0;
		for (String w1 : list1) {
			same += calculateTermFrequency(w1, list2);
		}
		return same;
	}

	public static int calculateTermFrequency(String word, List<String> document) {
		int same = 0;
		for (String w2 : document) {
			if (word.compareToIgnoreCase(w2) == 0) {
				same++;
			}
		}
		return same;
	}

	public static Cloud createFeatureTfIdfCloud(Feature targetFeature, List<Feature> features) {
		int featureIndex = features.indexOf(targetFeature);
		List<List<String>> featuresWords = new ArrayList<List<String>>();
		for (Feature f : features) {
			featuresWords.add(getFeatureWords(f));
		}
		Cloud featureCloud = Cloudifier.cloudifyTFIDF(featuresWords, featureIndex, new NullProgressMonitor());
		return featureCloud;
	}

	public static double calculateTfIdf(Cloud featureTfIdfCloud, IElement targetElement) {
		double score = 0;
		List<String> elementWords = getElementWords(targetElement);
		for (Tag tag : featureTfIdfCloud.tags()) {
			for (String s : elementWords) {
				if (s.equalsIgnoreCase(tag.getName())) {
					score += tag.getScore();
				}
			}
		}
		return score;
	}

	public static List<IWordsProcessing> getBasicProcessors() {
		List<IWordsProcessing> processors = new ArrayList<IWordsProcessing>();
		processors.add(new IgnoreUpperCaseProcessing());
		processors.add(new StemmingFilter());
		processors.add(new PartOfSpeechTagsFilter());
		return processors;
	}
}
