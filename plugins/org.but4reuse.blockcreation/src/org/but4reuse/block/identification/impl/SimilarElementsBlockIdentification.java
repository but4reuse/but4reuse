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
				artefactIndexes.add(i);
				R.put(e, artefactIndexes);
			}
		}

		monitor.subTask("Block Creation. Creating Blocks");

		// Iterate on eewmap to create blocks with their block elements
		while (!R.isEmpty()) {
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

		// finished
		return blocks;
	}

}
