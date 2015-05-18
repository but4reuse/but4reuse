package org.but4reuse.adaptedmodel.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adaptedmodel.manager.AdaptedModelManager;
import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.helper.AdaptersHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModel;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Adapted Model Helper
 * 
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
		// Add info to the manager
		AdaptedModelManager.setAdaptedModel(adaptedModel);
		AdaptedModelManager.setAdapters(adapters);
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

	// TODO to be removed
	public static ElementWrapper findOneElementWrapper(List<AdaptedArtefact> artefacts, IElement ie) {
		// expensive task, it is better if you use a hashmap IElement,
		// IElementWrappers instead
		// List<ElementWrapper> elementWrappers = new
		// ArrayList<ElementWrapper>();
		for (AdaptedArtefact artefact : artefacts) {
			for (ElementWrapper ew : artefact.getOwnedElementWrappers()) {
				if (ew.getElement().equals(ie)) {
					return ew;
				}
			}
		}
		return null;
	}

	public static Map<IElement, ElementWrapper> createMapIEEW(AdaptedModel adaptedModel) {
		Map<IElement, ElementWrapper> result = new HashMap<IElement, ElementWrapper>();
		for (AdaptedArtefact artefact : adaptedModel.getOwnedAdaptedArtefacts()) {
			for (ElementWrapper ew : artefact.getOwnedElementWrappers()) {
				result.put((IElement) ew.getElement(), ew);
			}
		}
		return result;
	}

	public static Map<IElement, BlockElement> createMapIEBE(AdaptedModel adaptedModel) {
		Map<IElement, BlockElement> result = new HashMap<IElement, BlockElement>();
		for (Block block : adaptedModel.getOwnedBlocks()) {
			for (BlockElement be : block.getOwnedBlockElements()) {
				for (ElementWrapper ew : be.getElementWrappers()) {
					result.put((IElement) ew.getElement(), be);
				}
			}
		}
		return result;
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

	/**
	 * TODO move this somewhere else
	 * 
	 * @param number
	 * @param maxNumber
	 * @return
	 */
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
	 * Return the list of blocks that are present in an Adapted artefact TODO
	 * improve
	 * 
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
		AdaptedModel am = (AdaptedModel) adaptedArtefact.eContainer();
		for (Block block : am.getOwnedBlocks()) {
			if (blocks.contains(block)) {
				orderedBlocks.add(block);
			}
		}

		return orderedBlocks;
	}

	/**
	 * Get the list of elements of one block
	 * 
	 * @param block
	 * @return
	 */
	public static List<IElement> getElementsOfBlock(Block block) {
		List<IElement> elements = new ArrayList<IElement>();
		for (BlockElement be : block.getOwnedBlockElements()) {
			// TODO we just get the first one
			elements.add((IElement) be.getElementWrappers().get(0).getElement());
		}
		return elements;
	}

	public static List<IElement> getElementsOfAdaptedArtefact(AdaptedArtefact adaptedArtefact) {
		List<IElement> elements = new ArrayList<IElement>();
		for (ElementWrapper ew : adaptedArtefact.getOwnedElementWrappers()) {
			elements.add((IElement) ew.getElement());
		}
		return elements;
	}

	/**
	 * Get the blocks that are on the adapted artefacts
	 * 
	 * @param adaptedModel
	 * @return a non-empty list
	 */
	public static List<Block> getCommonBlocks(AdaptedModel adaptedModel) {
		// do not modify the adaptedModel
		List<Block> blocks = adaptedModel.getOwnedBlocks();
		List<Block> toBeRemoved = new ArrayList<Block>();
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			List<Block> aaBlocks = getBlocksOfAdaptedArtefact(aa);
			for (Block b : blocks) {
				if (!toBeRemoved.contains(b) && !aaBlocks.contains(b)) {
					toBeRemoved.add(b);
				}
			}
		}
		List<Block> common = new ArrayList<Block>();
		for (Block b : blocks) {
			if (!toBeRemoved.contains(b)) {
				common.add(b);
			}
		}
		return common;
	}

	/**
	 * Get all the dependency objects that points to a given element
	 * 
	 * @param adaptedModel
	 * @param element
	 * @return
	 */
	public static List<IDependencyObject> getDependingOnIElement(AdaptedModel adaptedModel, IElement element,
			Map<IElement, ElementWrapper> ieewMap) {
		List<IDependencyObject> result = new ArrayList<IDependencyObject>();
		BlockElement blockElement = ieewMap.get(element).getBlockElements().get(0);
		for (ElementWrapper ew : blockElement.getElementWrappers()) {
			IElement e = (IElement) ew.getElement();
			Map<String, List<IDependencyObject>> dependants = e.getDependants();
			for (String dk : dependants.keySet()) {
				List<IDependencyObject> ide = dependants.get(dk);
				for (IDependencyObject iDependencyObject : ide) {
					if (!result.contains(iDependencyObject)) {
						result.add(iDependencyObject);
					}
				}
			}
		}
		return result;
	}

	public static Set<IDependencyObject> getDependingOnIElementBE(AdaptedModel adaptedModel, IElement element,
			Map<IElement, BlockElement> iebeMap) {
		Set<IDependencyObject> result = new HashSet<IDependencyObject>();
		BlockElement blockElement = iebeMap.get(element);
		// Maybe only first...
		for (ElementWrapper ew : blockElement.getElementWrappers()) {
			IElement e = (IElement) ew.getElement();
			Map<String, List<IDependencyObject>> dependants = e.getDependants();
			// No ordering
			for (List<IDependencyObject> iDependencyObjects : dependants.values()) {
				for (IDependencyObject ido : iDependencyObjects) {
					if (!result.contains(ido)) {
						result.add(ido);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Get the number of ielements of a given type
	 * 
	 * @param adaptedModel
	 * @param class1
	 *            is the full qualified name with packages etc.
	 * @return
	 */
	public static int getNumberOfElementsOfType(AdaptedModel adaptedModel, String class1) {
		int i = 0;
		for (Block block : adaptedModel.getOwnedBlocks()) {
			for (IElement e : AdaptedModelHelper.getElementsOfBlock(block)) {
				if (e.getClass().getName().equals(class1)) {
					i++;
				}
			}
		}
		return i;
	}

	public static int getNumberOfElementsOfType(Block block, String class1) {
		int i = 0;
		for (IElement e : AdaptedModelHelper.getElementsOfBlock(block)) {
			if (e.getClass().getName().equals(class1)) {
				i++;
			}
		}
		return i;
	}

	public static int getNumberOfElementsOfType(AdaptedArtefact adaptedArtefact, String class1) {
		int i = 0;
		for (IElement e : AdaptedModelHelper.getElementsOfAdaptedArtefact(adaptedArtefact)) {
			if (e.getClass().getName().equals(class1)) {
				i++;
			}
		}
		return i;
	}

	public static Set<IElement> getAllElementsFromAllBlocks(AdaptedModel adaptedModel) {
		Set<IElement> all = new HashSet<IElement>();
		for (Block block : adaptedModel.getOwnedBlocks()) {
			for (IElement e : AdaptedModelHelper.getElementsOfBlock(block)) {
				all.add(e);
			}
		}
		return all;
	}

}
