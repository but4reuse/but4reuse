package org.but4reuse.wordclouds.visualisation;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.but4reuse.visualisation.IVisualisation;
import org.but4reuse.wordclouds.util.Cloudifier;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.mcavallo.opencloud.Cloud;

public class WordCloudVisualisation implements IVisualisation {

	/**
	 * A list which contains a word cloud for each identified features.
	 */
	private static List<Cloud> clouds;
	private static List<Cloud> clouds_idf;

	public WordCloudVisualisation() {
	}

	/**
	 * This method returns the word cloud list.
	 * 
	 * @return the value of the attribute clouds.
	 */
	public static List<Cloud> getClouds() {
		return clouds;
	}

	public static List<Cloud> getCloudsIDF() {
		return clouds_idf;
	}

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {
		clouds = new ArrayList<Cloud>();
		clouds_idf = new ArrayList<Cloud>();
		List<List<String>> listWords = new ArrayList<List<String>>();

		for (Block b : adaptedModel.getOwnedBlocks()) {
			/*
			 * For each block we get all elements owned We use the method
			 * getWords for having several strings which could be used as block
			 * name.
			 */
			List<String> words = new ArrayList<String>();
			for (IElement e : (AdaptedModelHelper.getElementsOfBlock(b))) {
				words.addAll(((AbstractElement) e).getWords());
			}
			Cloud cloud = Cloudifier.cloudify(words);
			clouds.add(cloud);
			listWords.add(words);
		}

		for (int i = 0; i < listWords.size(); i++) {
			clouds_idf.add(Cloudifier.cloudifyTFIDF(listWords, i));
		}
	}

	@Override
	public void show() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				WorkbenchUtils.forceShowView("org.but4reuse.wordclouds.view");
				WordCloudView.update(0, false);
			}
		});

	}

}
