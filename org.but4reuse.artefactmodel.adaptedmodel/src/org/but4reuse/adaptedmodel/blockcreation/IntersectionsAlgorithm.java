package org.but4reuse.adaptedmodel.blockcreation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Feature identification algorithm as described by Tewfik Ziadi, Luz Frias,
 * Marcos Aurélio Almeida da Silva and Mikal Ziane Implementation and adaptation
 * by jabier.martinez and tewfik.ziadi
 */
public class IntersectionsAlgorithm implements IBlockCreationAlgorithm {

	@Override
	public List<Block> createBlocks(List<AdaptedArtefact> adaptedArtefacts, IProgressMonitor monitor) {

		// Blocks Empty
		List<Block> blocks = new ArrayList<Block>();

		// In R we will have, for each element, the indexes of the artefacts
		// where they appear
		Map<IElement, List<Integer>> R = new HashMap<IElement, List<Integer>>();

		// A map from IElement to the IElementWrappers that contains similar
		// IElement
		Map<IElement, List<ElementWrapper>> eewmap = new HashMap<IElement, List<ElementWrapper>>();
		for (int i = 0; i < adaptedArtefacts.size(); i++) {
			monitor.subTask("Block Creation. Intersections algorithm. Preparation step " + (i+1) + "/" + adaptedArtefacts.size());
			AdaptedArtefact currentList = adaptedArtefacts.get(i);
			for (ElementWrapper ew : currentList.getOwnedElementWrappers()) {
				
				// user cancel
				if(monitor.isCanceled()){
					return blocks;
				}
				
				IElement e = (IElement) ew.getElement();
				List<ElementWrapper> ews = eewmap.get(e);
				if (ews == null) {
					ews = new ArrayList<ElementWrapper>();
				}
				ews.add(ew);
				eewmap.put(e, ews);

				List<Integer> products = R.get(e);
				if (products == null) {
					products = new ArrayList<Integer>();
				}
				products.add(i);
				R.put(e, products);
			}
		}

		monitor.subTask("Block Creation. Intersections algorithm. Creating Blocks");
		
		// Iterate on R to create blocks with their block elements
		while (!R.isEmpty()) {
			List<Integer> products = findMostFrequentCP(R);
			List<IElement> intersection = findIntersection(products, R);
			Block block = AdaptedModelFactory.eINSTANCE.createBlock();
			for (IElement intersected : intersection) {
				BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
				List<ElementWrapper> ewlist = eewmap.get(intersected);
				for (ElementWrapper ewliste : ewlist) {
					be.getElementWrappers().add(ewliste);
				}
				block.getOwnedBlockElements().add(be);
			}
			blocks.add(block);
		}
		return blocks;
	}

	private static List<Integer> findMostFrequentCP(Map<IElement, List<Integer>> R) {

		List<Integer> result = new ArrayList<Integer>();

		for (IElement key : R.keySet()) {
			if (result.size() < R.get(key).size())
				result = R.get(key);

		}
		return result;
	}

	private static List<IElement> findIntersection(List<Integer> products, Map<IElement, List<Integer>> R) {

		List<IElement> result = new ArrayList<IElement>();

		for (IElement key : R.keySet()) {
			if (R.get(key).containsAll(products)) {
				result.add(key);
			}
		}

		for (IElement fromResult : result) {
			R.remove(fromResult);
		}
		return result;
	}
}
