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
 * This algorithm create a Block for each group of similar elements. That means
 * that a Block will have only one BlockElement that will reference to the
 * similar Elements from the Artefacts. This way we have an algorithm that
 * provides finer-granularity than the IntersectionsBlockCreationAlgorithm
 * 
 * @author jabier.martinez
 */
public class SimilarElementsBlockIdentification implements IBlockIdentification {

	// order the blocks by frequency, default is true
	private boolean orderByFrequency = true;

	@Override
	public List<Block> identifyBlocks(List<AdaptedArtefact> adaptedArtefacts, IProgressMonitor monitor) {

		// Blocks Empty
		List<Block> blocks = new ArrayList<Block>();

		// In R we will have, for each element, the indexes of the artefacts
		// where they appear
		Map<IElement, List<Integer>> R = new LinkedHashMap<IElement, List<Integer>>();

		// A map from IElement to the IElementWrappers that contains similar
		// IElement
		Map<IElement, List<ElementWrapper>> eewmap = new HashMap<IElement, List<ElementWrapper>>();
		int n = adaptedArtefacts.size();
		for (int i = 0; i < n; i++) {
			monitor.subTask("Block Creation. Preparation step " + (i + 1) + "/" + n);
			AdaptedArtefact currentAdaptedArtefact = adaptedArtefacts.get(i);
			for (ElementWrapper ew : currentAdaptedArtefact.getOwnedElementWrappers()) {

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

				if (orderByFrequency) {
					List<Integer> artefactIndexes = R.get(e);
					if (artefactIndexes == null) {
						artefactIndexes = new ArrayList<Integer>();
					}
					artefactIndexes.add(i);
					R.put(e, artefactIndexes);
				} else {
					R.put(e, null);
				}
			}
		}

		monitor.subTask("Block Creation. Creating Blocks");

		if (orderByFrequency) {
			// Iterate on eewmap to create blocks with their block elements
			while (!R.isEmpty()) {
				System.out.println(R.size());
				IElement e = IntersectionsBlockIdentification.findMostFrequentElement(R);
				// Create Block
				Block block = AdaptedModelFactory.eINSTANCE.createBlock();
				BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
				for (ElementWrapper ew : eewmap.get(e)) {
					be.getElementWrappers().add(ew);
				}
				block.getOwnedBlockElements().add(be);
				blocks.add(block);
				R.remove(e);
			}
		} else {
			for (IElement e : R.keySet()) {
				// Create Block
				Block block = AdaptedModelFactory.eINSTANCE.createBlock();
				BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
				for (ElementWrapper ew : eewmap.get(e)) {
					be.getElementWrappers().add(ew);
				}
				block.getOwnedBlockElements().add(be);
				blocks.add(block);
			}
		}

		// finished
		return blocks;
	}

	/**
	 * The same as the default but without ordering by frequency
	 * 
	 * @param adaptedArtefacts
	 * @param monitor
	 * @param orderByFrequency
	 * @return list of blocks
	 */
	public List<Block> identifyBlocks(List<AdaptedArtefact> adaptedArtefacts, boolean orderByFrequency, IProgressMonitor monitor) {
		this.orderByFrequency = orderByFrequency;
		return identifyBlocks(adaptedArtefacts, monitor);
	}

}
