package org.but4reuse.fca.block.identification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.BlockElement;
import org.but4reuse.adaptedmodel.ElementWrapper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.fca.utils.FCAUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.BinaryAttribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;
import fr.labri.galatea.Entity;


/**
 * Formal Context Analysis block creation
 * 
 */
public class FCABlockIdentification implements IBlockIdentification {

	@Override
	public List<Block> identifyBlocks(List<AdaptedArtefact> adaptedArtefacts, IProgressMonitor monitor) {

		// Creates a formal context
		Context fc = new Context();

		// Blocks Empty
		List<Block> blocks = new ArrayList<Block>();

		// In R we will have, for each element, the indexes of the artefacts
		// where they appear
		LinkedHashMap<IElement, List<Integer>> R = new LinkedHashMap<IElement, List<Integer>>();

		Map<Attribute, IElement> attrIElementMap = new HashMap<Attribute, IElement>();

		// A map from IElement to the IElementWrappers that contains similar
		// IElement
		Map<IElement, List<ElementWrapper>> eewmap = new HashMap<IElement, List<ElementWrapper>>();
		for (int i = 0; i < adaptedArtefacts.size(); i++) {

			// Creates an entity.
			Entity ent = new Entity("Artefact " + i);
			fc.addEntity(ent);

			monitor.subTask("Block Creation. Preparation step " + (i + 1) + "/" + adaptedArtefacts.size());
			AdaptedArtefact currentList = adaptedArtefacts.get(i);
			EList<ElementWrapper> ownedElementWrappers = currentList.getOwnedElementWrappers();
			for(int j = 0; j < ownedElementWrappers.size(); ++j) {
				ElementWrapper ew = ownedElementWrappers.get(j);
				monitor.subTask("Block Creation. Preparation step " + (i + 1) + "/" + adaptedArtefacts.size() + " " + (j + 1) + "/" + ownedElementWrappers.size());

				// user cancel
				if (monitor.isCanceled()) {
					return blocks;
				}

				IElement e = (IElement) ew.getElement();
				eewmap.computeIfAbsent(e, k -> new ArrayList<ElementWrapper>()).add(ew);

				R.computeIfAbsent(e, k -> new ArrayList<Integer>()).add(i);
			}
		}

		monitor.subTask("Block Creation. Creating Blocks");

		// Iterate on eewmap to create blocks with their block elements
		int ei = 0;
		while (!R.isEmpty()) {
			// get first
			IElement e = R.keySet().iterator().next();

			// Creates a binary attribute.
			BinaryAttribute attr = new BinaryAttribute("Element " + ei);
			attrIElementMap.put(attr, e);
			fc.addAttribute(attr);
			ei++;

			List<Integer> artIndexes = R.get(e);
			for (Integer ia : artIndexes) {
				fc.addPair(fc.getEntity("Artefact " + ia), attr);
			}
			R.remove(e);
			monitor.subTask("Block Creation. Creating Blocks. Creating Formal Context." + R.size());
		}

		monitor.subTask("Block Creation. Creating concept lattice.");
		// Generate concept lattice
		ConceptOrder cl = FCAUtils.createConceptLattice(fc);
		
		// Add a block for each non empty concept
		int i = 0;
		final Set<Concept> concepts = cl.getConcepts();
		final int is = concepts.size();
		for (Concept c : cl.getConcepts()) {
			monitor.subTask("Block Creation. Creating Blocks." + " " + ++i + "/" + is);
			// getIntent returns also the intent of the parents, we are only
			// interested in the getSimplifiedIntent which is the one that only
			// belongs to this concept
			if (!c.getSimplifiedIntent().isEmpty()) {
				Block block = AdaptedModelFactory.eINSTANCE.createBlock();
				Set<Attribute> attrs = c.getSimplifiedIntent();
				for (Attribute attr : attrs) {
					IElement e = attrIElementMap.get(attr);
					BlockElement be = AdaptedModelFactory.eINSTANCE.createBlockElement();
					for (ElementWrapper ew : eewmap.get(e)) {
						be.getElementWrappers().add(ew);
					}
					block.getOwnedBlockElements().add(be);
				}
				blocks.add(block);
			}
		}

		monitor.subTask("Block Creation. Sorting blocks by descending frequency.");
		// Java8's built-in sort produces an ascending order, when using standard compare/compareTo.
		// We define the compare-method such that b1 is smaller than b0, if b1...size is bigger than b0...size.
		// (Notice: If b1...size - b0...size > 0, this indicates the need of swapping b1 and b0.)
		// This leads to the desired descending order of the b...size.
		blocks.sort((b0, b1) -> b1.getOwnedBlockElements().get(0).getElementWrappers().size() - b0.getOwnedBlockElements().get(0).getElementWrappers().size());

		// finished
		return blocks;
	}
}
