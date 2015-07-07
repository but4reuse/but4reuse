package org.but4reuse.worldcouds.visualisation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpringLayout.Constraints;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.visualisation.IVisualisation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.Cloud.Case;
import org.but4reuse.utils.workbench.*;

public class WordCloudVisualisation implements IVisualisation {

	/**
	 * A list which contains a word cloud for each identified features.
	 */
	private static ArrayList<Cloud> clouds;

	public WordCloudVisualisation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method returns the word cloud list.
	 * 
	 * @return the value of the attribute clouds.
	 */
	public static ArrayList<Cloud> getClouds() {
		return clouds;
	}

	@Override
	public void prepare(FeatureList featureList, AdaptedModel adaptedModel, Object extra, IProgressMonitor monitor) {

		clouds = new ArrayList<Cloud>();

		for (Block b : adaptedModel.getOwnedBlocks()) {

			Cloud cloud = null;
			cloud = new Cloud(Case.CAPITALIZATION);
			cloud.setMaxWeight(50);
			cloud.setMinWeight(5);
			cloud.setMaxTagsToDisplay(50);

			/*
			 * For each block we get all elements owned We use the method
			 * getWords for having several strings which could be used as block
			 * name.
			 */
			for (BlockElement e : b.getOwnedBlockElements()) {
				for (ElementWrapper wr : e.getElementWrappers()) {
					AbstractElement element = (AbstractElement) (wr.getElement());

					/*
					 * We put each string in the cloud.
					 */
					addWords(cloud, element.getWords());
				}

			}
			clouds.add(cloud);
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {

				/*
				 * WorkbenchUtils.forceShowView("org.but4reuse.wordclouds.view");
				 */
				WordCloudVis.update(0, false);
			}
		});

	}

	/**
	 * Add strings in a cloud
	 * 
	 * @param cloud
	 *            This is the cloud where strings will be added.
	 * @param words
	 *            The list which contains all strings to add in the cloud.
	 */
	private static void addWords(Cloud cloud, List<String> words) {
		for (String word : words) {
			word.trim();
			cloud.addTag(new Tag(word));
		}
	}

}
