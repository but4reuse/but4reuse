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
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;

public class IntersectionsAlgorithm implements IBlockCreationAlgorithm {
		
	@Override
	public List<Block> createBlocks(List<AdaptedArtefact> artefacts) {
		
		// Prepare the data for the algorithm
		List<List<IElement>> lists = new ArrayList<List<IElement>>();
		for(AdaptedArtefact artefact: artefacts){
			List<IElement> list = new ArrayList<IElement>();
			for(ElementWrapper ew : artefact.getOwnedElementWrappers()){
				list.add((IElement)ew.getElement());
			}
			lists.add(list);
		}
		
		// Identify blocks algorithm
		List<List<IElement>> identifiedBlocks = identifyFeatures(lists);
		
		// Prepare the data for return
		List<Block> blocks = new ArrayList<Block>();
		for(List<IElement> blocki : identifiedBlocks){
			Block block = AdaptedModelFactory.eINSTANCE.createBlock();
			for(IElement ie : blocki){
				BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
				be.getElementWrappers().add(AdaptedModelHelper.findElementWrapper(artefacts, ie));
				block.getOwnedBlockElements().add(be);
			}
			blocks.add(block);
		}
		return blocks;
	}
	
	/**
	 * Feature identification algorithm as described by Tewfik Ziadi, Luz Frias,
	 * Marcos Aurélio Almeida da Silva and Mikal Ziane
	 * Implementation by jabier.martinez and tewfik.ziadi
	 * @param List
	 *            of Elements containing Subelements
	 * @return List of Features containing Subelements
	 * TODO Comment and refactor
	 */
	public static <T> List<List<T>> identifyFeatures(List<List<T>> listCps) {
		// F Empty
		List<List<T>> F = new ArrayList<List<T>>();

		Map<T, List<Integer>> R = new HashMap<T, List<Integer>>();
		for (int i = 0; i < listCps.size(); i++) {
			List<T> currentList = listCps.get(i);
			for (T cp : currentList) {
				List<Integer> products = R.get(cp);
				if (products == null) {
					List<Integer> prs = new ArrayList<Integer>();
					prs.add(i);
					R.put(cp, prs);
				} else {
					products.add(i);
					R.put(cp, products);
				}
			}
		}

		
		// Iterate on R
		while (!R.isEmpty()) {
			List<Integer> products = findMostFrequentCP(R);
			List<T> intersection = findIntersection(products, R);
			F.add(intersection);
		}
		return F;
	}

	private static <T> List<Integer> findMostFrequentCP(Map<T, List<Integer>> R) {

		List<Integer> result = new ArrayList<Integer>();

		for (T key : R.keySet()) {
			if (result.size() < R.get(key).size())
				result = R.get(key);

		}
		return result;
	}

	private static <T> List<T> findIntersection(List<Integer> products, Map<T, List<Integer>> R) {

		List<T> result = new ArrayList<T>();

		for (T key : R.keySet()) {
			if (R.get(key).containsAll(products)) {
				result.add(key);
			}
		}

		for (T fromResult : result) {
			R.remove(fromResult);
		}
		return result;
	}
}
