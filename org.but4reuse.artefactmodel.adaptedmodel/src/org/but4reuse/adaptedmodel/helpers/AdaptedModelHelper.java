package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Adapted Model Helper
 * @author jabier.martinez
 *
 */
public class AdaptedModelHelper {

	/**
	 * Adapt an artefact model to create the list of elements of each artefact
	 * 
	 * @param artefactModel
	 * @param adapters
	 * @param monitor
	 * @return
	 */
	public static AdaptedModel adapt(ArtefactModel artefactModel, List<IAdapter> adapters, IProgressMonitor monitor) {
		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();

		// TODO implement concurrency to improve performance
		for (Artefact artefact : artefactModel.getOwnedArtefacts()) {
			if (artefact.isActive()) {
				AdaptedArtefact adaptedArtefact = adapt(artefact, adapters, monitor);
				adaptedModel.getOwnedAdaptedArtefacts().add(adaptedArtefact);
				monitor.worked(1);
				if (monitor.isCanceled()) {
					return adaptedModel;
				}
			}
		}
		return adaptedModel;
	}

	/**
	 * Adapt artefact
	 * 
	 * @param artefact
	 * @param adapters
	 * @param monitor
	 * @return
	 */
	public static AdaptedArtefact adapt(Artefact artefact, List<IAdapter> adapters, IProgressMonitor monitor) {
		AdaptedArtefact adaptedArtefact = AdaptedModelFactory.eINSTANCE.createAdaptedArtefact();
		adaptedArtefact.setArtefact(artefact);

		String name = artefact.getName();
		if (name == null || name.length() == 0) {
			name = artefact.getArtefactURI();
		}
		monitor.subTask("Adapting: " + name);

		List<IElement> list = AdaptersHelper.getElements(artefact, adapters);
		for (IElement ele : list) {
			ElementWrapper ew = AdaptedModelFactory.eINSTANCE.createElementWrapper();
			ew.setElement(ele);
			adaptedArtefact.getOwnedElementWrappers().add(ew);
		}
		return adaptedArtefact;
	}

	public static boolean isIdentical(ElementWrapper e1, ElementWrapper e2) {
		if (e1.getElement() != null && e2.getElement() != null) {
			return ((IElement) e1).similarity((IElement) e2) == 1.0;
		} else {
			return false;
		}
	}

	public static double similarity(ElementWrapper e1, ElementWrapper e2) {
		if (e1.getElement() != null && e2.getElement() != null) {
			return ((IElement) e1).similarity((IElement) e2);
		} else {
			return 0;
		}
	}

	public static List<ElementWrapper> findElementWrappers(List<AdaptedArtefact> artefacts, IElement ie) {
		// expensive task, it is better if you use a hashmap IElement,
		// IElementWrappers instead
		List<ElementWrapper> elementWrappers = new ArrayList<ElementWrapper>();
		for (AdaptedArtefact artefact : artefacts) {
			for (ElementWrapper ew : artefact.getOwnedElementWrappers()) {
				if (ew.getElement().equals(ie)) {
					elementWrappers.add(ew);
				}
			}
		}
		return elementWrappers;
	}

	public static List<Block> checkBlockNames(List<Block> blocks) {
		int i = 0;
		for (Block block : blocks) {
			if (block.getName() == null || block.getName().isEmpty()) {
				block.setName("Block " + getNumberWithZeros(i, blocks.size() - 1));
				i++;
			}
		}
		return blocks;
	}

	public static List<Artefact> getArtefactsContainingBlockElement(BlockElement blockElement) {
		List<Artefact> artefacts = new ArrayList<Artefact>();
		for (ElementWrapper ew : blockElement.getElementWrappers()) {
			AdaptedArtefact aa = (AdaptedArtefact) ew.eContainer();
			if (!artefacts.contains(aa.getArtefact())) {
				artefacts.add(aa.getArtefact());
			}
		}
		return artefacts;
	}

	public static String getNumberWithZeros(int number, int maxNumber) {
		String _return = String.valueOf(number);
		for (int zeros = _return.length(); zeros < String.valueOf(maxNumber).length(); zeros++) {
			_return = "0" + _return;
		}
		return _return;
	}

	/**
	 * Get the name of the artefact model
	 * 
	 * @param adaptedModel
	 * @return the name or null
	 */
	public static String getName(AdaptedModel adaptedModel) {
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			ArtefactModel a = (ArtefactModel) aa.getArtefact().eContainer();
			if (a.getName() != null && a.getName().length() > 0) {
				return a.getName();
			}
		}
		return null;
	}

	/**
	 * Get artefact name
	 * 
	 * @param artefact
	 * @return
	 */
	public static String getArtefactName(Artefact artefact) {
		// Try with the name
		String name = artefact.getName();
		if (name == null || name.length() == 0) {
			// Get last fragment of uri otherwise
			name = artefact.getArtefactURI();
			if (name.contains("/")) {
				name = name.substring(name.lastIndexOf("/") + 1, name.length());
			}
		}
		return name;
	}

	/**
	 * Return the list of blocks that are present in an Adapted artefact
	 * TODO improve
	 * @param adaptedArtefact
	 * @return the list of blocks
	 */
	public static List<Block> getBlocksOfAdaptedArtefact(AdaptedArtefact adaptedArtefact) {
		List<Block> blocks = new ArrayList<Block>();
		for (ElementWrapper ew : adaptedArtefact.getOwnedElementWrappers()) {
			for (BlockElement be : ew.getBlockElements()) {
				Block block = (Block) be.eContainer();
				if (!blocks.contains(block)) {
					blocks.add(block);
				}
			}
		}
		
		// Sort them
		List<Block> orderedBlocks = new ArrayList<Block>();
		AdaptedModel am = (AdaptedModel)adaptedArtefact.eContainer();
		for(Block block : am.getOwnedBlocks()){
			if(blocks.contains(block)){
				orderedBlocks.add(block);
			}
		}
		
		return orderedBlocks;
	}
	
	/**
	 * Get the list of elements of one block
	 * @param block
	 * @return
	 */
	public static List<IElement> getElementsOfBlock(Block block){
		List<IElement> elements = new ArrayList<IElement>();
		for(BlockElement be : block.getOwnedBlockElements()){
			// TODO we just get the first one
			elements.add((IElement)be.getElementWrappers().get(0).getElement());
		}
		return elements;
	}

	/**
	 * Get the blocks that are on the adapted artefacts
	 * @param adaptedModel
	 * @return a non-empty list
	 */
	public static List<Block> getCommonBlocks(AdaptedModel adaptedModel) {
		// do not modify the adaptedModel
		List<Block> blocks = adaptedModel.getOwnedBlocks();
		List<Block> toBeRemoved = new ArrayList<Block>();
		for(AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()){
			List<Block> aaBlocks = getBlocksOfAdaptedArtefact(aa);
			for(Block b : blocks){
				if(!toBeRemoved.contains(b) && !aaBlocks.contains(b)){
					toBeRemoved.add(b);
				}
			}
		}
		List<Block> common = new ArrayList<Block>();
		for(Block b : blocks){
			if(!toBeRemoved.contains(b)){
				common.add(b);
			}
		}
		return common;
	}

}
