package org.but4reuse.block.identification.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature identification algorithm as described by Tewfik Ziadi, Luz Frias,
 * Marcos Aurélio Almeida da Silva and Mikal Ziane Implementation and adaptation
 * by jabier.martinez and tewfik.ziadi
 */
public class IntersectionsBlockIdentification implements IBlockIdentification {

	@Override
	public List<Block> identifyBlocks(List<AdaptedArtefact> adaptedArtefacts, IProgressMonitor monitor) {

		// Blocks Empty
		List<Block> blocks = new ArrayList<Block>();

		// In R we will have, for each element, the indexes of the artefacts
		// where they appear
		// LinkedHashMap to maintain elements order
		Map<IElement, List<Integer>> R = new LinkedHashMap<IElement, List<Integer>>();

		// A map from IElement to the IElementWrappers that contains similar
		// IElement
		Map<IElement, List<ElementWrapper>> eewmap = new HashMap<IElement, List<ElementWrapper>>();
		int n = adaptedArtefacts.size();
		for (int i = 0; i < n; i++) {
			monitor.subTask("Block Creation. Intersections algorithm. Preparation step " + (i + 1) + "/" + n);
			AdaptedArtefact currentList = adaptedArtefacts.get(i);
			for (ElementWrapper ew : currentList.getOwnedElementWrappers()) {

				// user cancel
				if (monitor.isCanceled()) {
					return blocks;
				}

				IElement e = (IElement) ew.getElement();
				List<ElementWrapper> ews = eewmap.get(e);
				if (ews == null) {
					ews = new ArrayList<ElementWrapper>();
				}
				ews.add(ew);
				eewmap.put(e, ews);

				List<Integer> artefactIndexes = R.get(e);
				if (artefactIndexes == null) {
					artefactIndexes = new ArrayList<Integer>();
				}
				// the same element can be repeated in an artefact, add only
				// once
				if (!artefactIndexes.contains(i)) {
					artefactIndexes.add(i);
					R.put(e, artefactIndexes);
				}
			}
		}

		int step = 1;
		int totalSteps = R.keySet().size();

		// Iterate on R to create blocks with their block elements
		while (!R.isEmpty()) {
			monitor.subTask("Block Creation. Intersections algorithm. Creating Blocks. Step " + step + "/" + totalSteps);
			// TODO performance can be improved in find methods
			IElement keyOfMostFrequentElement = findMostFrequentElement(R);
			List<Integer> artefactIndexes = R.get(keyOfMostFrequentElement);
			List<IElement> intersection = findElementsContainedInArtefactIndexes(artefactIndexes, R);

			// Create Block
			Block block = AdaptedModelFactory.eINSTANCE.createBlock();
			for (IElement intersected : intersection) {
				BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
				List<ElementWrapper> ewlist = eewmap.get(intersected);
				for (ElementWrapper ewliste : ewlist) {
					be.getElementWrappers().add(ewliste);
				}
				block.getOwnedBlockElements().add(be);

				// Remove from R
				R.remove(intersected);
				step++;
			}
			blocks.add(block);

			// user cancel
			if (monitor.isCanceled()) {
				return blocks;
			}
		}

		// finished
		return blocks;
	}

	/**
	 * Find most frequent element in the artefacts.
	 * 
	 * @param a
	 *            relation of all elements with the list of artefacts where it
	 *            appears
	 * @return the artefact indexes
	 */
	public static IElement findMostFrequentElement(Map<IElement, List<Integer>> R) {
		IElement keyOfMostFrequent = null;
		int sizeOfMostFrequent = -1;

		for (IElement key : R.keySet()) {
			int currentSize = R.get(key).size();
			if (sizeOfMostFrequent < currentSize) {
				keyOfMostFrequent = key;
				sizeOfMostFrequent = currentSize;
			}
		}

		return keyOfMostFrequent;
	}

	/**
	 * Find elements that are contained in the given artefact indexes
	 * 
	 * @param artefactIndexes
	 * @param R
	 * @return the list of elements that are present exactly in the same
	 *         artefact indexes
	 */
	private static List<IElement> findElementsContainedInArtefactIndexes(List<Integer> artefactIndexes,
			Map<IElement, List<Integer>> R) {

		List<IElement> result = new ArrayList<IElement>();

		for (IElement key : R.keySet()) {
			if (R.get(key).containsAll(artefactIndexes)) {
				result.add(key);
			}
		}
		return result;
	}
}
