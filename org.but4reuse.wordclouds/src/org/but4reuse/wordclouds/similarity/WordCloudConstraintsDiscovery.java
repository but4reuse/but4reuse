package org.but4reuse.wordclouds.similarity;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.impl.ConstraintImpl;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.wordclouds.activator.Activator;
import org.but4reuse.wordclouds.util.WordCloudUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.mcavallo.opencloud.Cloud;

public class WordCloudConstraintsDiscovery implements IConstraintsDiscovery {


	@Override
	public List<IConstraint> discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {

		List<IConstraint> constraintList = new ArrayList<IConstraint>();
		int nb_Block = adaptedModel.getOwnedBlocks().size();
		ArrayList<Cloud> clouds = new ArrayList<Cloud>();
		ArrayList<ArrayList<String>> listWords = new ArrayList<ArrayList<String>>();

		/*
		 * Gathering words from blocks in order to create words cloud IDF
		 */

		for (int i = 0; i < nb_Block; i++) {
			ArrayList<String> list = new ArrayList<String>();
			Block b = adaptedModel.getOwnedBlocks().get(i);
			for (IElement e : (AdaptedModelHelper.getElementsOfBlock(b))) {
				List<String> words = ((AbstractElement) e).getWords();
				for (String s : words)
					if (s.compareTo("") != 0)
						list.add(s.trim());
			}
			listWords.add(list);
		}

		/*
		 * Word Cloud IDF creation
		 */
		for (int i = 0; i < nb_Block; i++)
			clouds.add(WordCloudUtil.createWordCloudIDF(listWords, i));

		/*
		 * Constraints Discovery Requires
		 */

		long start = System.currentTimeMillis();

		boolean requires = Activator.getDefault().getPreferenceStore()
				.getBoolean(TermBasedConstraintsDiscoveryPreferences.REQUIRES);
		double rateMin = Activator.getDefault().getPreferenceStore()
				.getDouble(TermBasedConstraintsDiscoveryPreferences.HIGHT_SIM);

		if (requires) {
			for (int i = 0; i < nb_Block; i++) {
				Block b1 = adaptedModel.getOwnedBlocks().get(i);
				Cloud cloud_b1 = clouds.get(i);
				for (int j = 0; j < nb_Block; j++) {
					if (i == j)
						continue;

					Cloud cloud_b2 = clouds.get(j);
					Block b2 = adaptedModel.getOwnedBlocks().get(j);

					monitor.subTask("Checking Requires Relations for " + b1.getName() + " and " + b2.getName());

					if (monitor.isCanceled())
						return constraintList;

					/*
					 * We check if c1 is close to c2 (similarity) and if each
					 * time we have b1 in an artefact there is b2 in it too.
					 */
					double similarity = WordCloudUtil.cmpClouds(cloud_b1, cloud_b2);
					if (inSameArtefact(adaptedModel.getOwnedAdaptedArtefacts(), b1, b2) && similarity > rateMin) {
						IConstraint constraint = new ConstraintImpl();
						constraint.setType(IConstraint.REQUIRES);
						constraint.setBlock1(b1);
						constraint.setBlock2(b2);
						ArrayList<String> explanations = createConstraintMessageRequires(b1, b2, similarity);
						constraint.setExplanations(explanations);
						constraint.setNumberOfReasons(explanations.size());
						constraintList.add(constraint);
					}
				}
			}
			AdaptedModelManager.registerTime("Constraints discovery [Requires] ", System.currentTimeMillis() - start);
		}
		/*
		 * Constraints discovery Mutually Exclusion
		 */
		start = System.currentTimeMillis();
		boolean excludes = Activator.getDefault().getPreferenceStore()
				.getBoolean(TermBasedConstraintsDiscoveryPreferences.EXCLUDES);

		if (excludes) {
			for (int i = 0; i < nb_Block; i++) {
				Block b1 = adaptedModel.getOwnedBlocks().get(i);
				Cloud cloud_b1 = clouds.get(i);
				for (int j = 0; j < nb_Block; j++) {
					if (i == j || j < i)
						continue;

					Cloud cloud_b2 = clouds.get(j);
					Block b2 = adaptedModel.getOwnedBlocks().get(j);

					monitor.subTask("Checking Exclude Relations for " + b1.getName() + " and " + b2.getName());

					if (monitor.isCanceled())
						return constraintList;

					/*
					 * We check if c1 is close to c2 (similarity) and if we
					 * never have b1 and b2 in the same artefact
					 */
					double similarity = WordCloudUtil.cmpClouds(cloud_b1, cloud_b2);
					if (neverInSameArtefact(adaptedModel.getOwnedAdaptedArtefacts(), b1, b2) && similarity > rateMin) {
						IConstraint constraint = new ConstraintImpl();
						constraint.setType(IConstraint.MUTUALLY_EXCLUDES);
						constraint.setBlock1(b1);
						constraint.setBlock2(b2);
						ArrayList<String> explanations = createConstraintMessageExclusion(b1, b2, similarity);
						constraint.setExplanations(explanations);
						constraint.setNumberOfReasons(explanations.size());
						constraintList.add(constraint);
					}
				}
			}
			AdaptedModelManager.registerTime("Constraints discovery [Mutually Exclusion] ", System.currentTimeMillis()
					- start);
		}
		return constraintList;
	}

	/**
	 * Check if we always find b2 in an artefact when b1 is in it.
	 * 
	 * @param artefacts
	 *            Artefact list
	 * @param b1
	 *            The first block
	 * @param b2
	 *            The second block
	 * @return True if b2 is always here when b1 is. Otherwise false
	 */
	public boolean inSameArtefact(EList<AdaptedArtefact> artefacts, Block b1, Block b2) {
		for (AdaptedArtefact art : artefacts) {
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(art);
			if (blocks.contains(b1)) {
				if (!blocks.contains(b2))
					return false;
			}
		}
		return true;
	}

	/**
	 * Check if we never find blocks b1 and b2 in the same artefact
	 * 
	 * @param artefacts
	 *            Artefacts List
	 * @param b1
	 *            The first block
	 * @param b2
	 *            The second block
	 * @return True if b1 and b2 are never in the same artefact. Otherwise false
	 */
	public boolean neverInSameArtefact(EList<AdaptedArtefact> artefacts, Block b1, Block b2) {

		for (AdaptedArtefact art : artefacts) {
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(art);
			if (blocks.contains(b1)) {
				if (blocks.contains(b2))
					return false;
			}
		}
		return true;
	}

	/**
	 * Create a an simple String ArrayList about why b1 requires b2
	 * 
	 * @param b1
	 *            The first block
	 * @param b2
	 *            The second block
	 * @param similarity
	 *            The similarity between b1 and b2
	 * @return A set of strings which explain why b1 require b2"
	 */
	public ArrayList<String> createConstraintMessageRequires(Block b1, Block b2, double similarity) {

		ArrayList<String> explanations = new ArrayList<String>();
		String s = b1.getName() + " similarity  to " + b2.getName() + " : " + String.format("%.2f", similarity);
		explanations.add(s);

		for (AdaptedArtefact art : AdaptedModelManager.getAdaptedModel().getOwnedAdaptedArtefacts()) {
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(art);
			if (blocks.contains(b1)) {
				if (blocks.contains(b2))
					explanations.add(b1.getName() + " and " + b2.getName() + " are present in "
							+ art.getArtefact().getName());
			}
		}
		return explanations;
	}

	/**
	 * Create a an simple String ArrayList about why b1 and b2 mutually exclude
	 * themselves
	 * 
	 * @param b1
	 *            The first block
	 * @param b2
	 *            The second block
	 * @param similarity
	 *            The similarity between b1 and b2
	 * @return A set of strings which explain why b1 and b2 mutually exclude
	 *         themselves"
	 */
	public ArrayList<String> createConstraintMessageExclusion(Block b1, Block b2, double similarity) {

		ArrayList<String> explanations = new ArrayList<String>();
		String s = b1.getName() + " similarity  to " + b2.getName() + " : " + String.format("%.2f", similarity);
		explanations.add(s);

		for (AdaptedArtefact art : AdaptedModelManager.getAdaptedModel().getOwnedAdaptedArtefacts()) {
			List<Block> blocks = AdaptedModelHelper.getBlocksOfAdaptedArtefact(art);
			if (blocks.contains(b1)) {
				if (!blocks.contains(b2))
					explanations.add(b1.getName() + " is present in " + art.getArtefact().getName() + " and "
							+ b2.getName() + " isn't");
			}
		}
		return explanations;
	}
}
