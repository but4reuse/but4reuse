package org.but4reuse.blockcreation.fca;

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
import org.but4reuse.blockcreation.IBlockCreationAlgorithm;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.erca.Attribute;
import com.googlecode.erca.BinaryAttribute;
import com.googlecode.erca.Entity;
import com.googlecode.erca.ErcaFactory;
import com.googlecode.erca.clf.Concept;
import com.googlecode.erca.clf.ConceptLattice;
import com.googlecode.erca.framework.algo.ConceptLatticeGenerator;
import com.googlecode.erca.rcf.FormalContext;
import com.googlecode.erca.rcf.RcfFactory;

/**
 * Formal Context Analysis block creation
 * 
 * @author jabier.martinez
 * 
 */
public class FCABlockCreation implements IBlockCreationAlgorithm {

	@Override
	public List<Block> createBlocks(List<AdaptedArtefact> adaptedArtefacts, IProgressMonitor monitor) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("FormalContext");

		// Blocks Empty
		List<Block> blocks = new ArrayList<Block>();

		// In R we will have, for each element, the indexes of the artefacts
		// where they appear
		LinkedHashMap<IElement, List<Integer>> R = new LinkedHashMap<IElement, List<Integer>>();

		Map<Attribute, IElement> attrIElementMap = new HashMap<Attribute, IElement>();

		// A map from IElement to the IElementWrappers that contains similar
		// IElement
		Map<IElement, List<ElementWrapper>> eewmap = new HashMap<IElement, List<ElementWrapper>>();
		int n = adaptedArtefacts.size();
		for (int i = 0; i < n; i++) {

			// Creates an entity.
			Entity ent = ErcaFactory.eINSTANCE.createEntity();
			ent.setName("Artefact " + i);
			fc.getEntities().add(ent);

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
		int ei = 0;
		while (!R.isEmpty()) {
			// get first
			IElement e = R.keySet().iterator().next();

			// Creates a binary attribute.
			BinaryAttribute attr = ErcaFactory.eINSTANCE.createBinaryAttribute();
			attr.setName("Element " + ei);
			attrIElementMap.put(attr, e);
			fc.getAttributes().add(attr);
			ei++;

			List<Integer> artIndexes = R.get(e);
			for (Integer ia : artIndexes) {
				fc.addPair(fc.getEntity("Artefact " + ia), attr);
			}
			R.remove(e);
		}

		// Generate concept lattice
		ConceptLatticeGenerator clg = new ConceptLatticeGenerator(fc);
		clg.generateConceptLattice();
		ConceptLattice cl = clg.getConceptLattice();

		// Add a block for each non empty concept
		for (Concept c : cl.getConcepts()) {
			// getIntent returns also the intent of the parents, we are only
			// interested in the getSimplifiedIntent which is the one that only
			// belongs to this concept
			if (!c.getSimplifiedIntent().isEmpty()) {
				Block block = AdaptedModelFactory.eINSTANCE.createBlock();
				List<Attribute> attrs = c.getSimplifiedIntent();
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

		blocks = reorderBlocksByFrequency(R, blocks);

		// finished
		return blocks;
	}

	// insertion sort
	private List<Block> reorderBlocksByFrequency(LinkedHashMap<IElement, List<Integer>> R, List<Block> blocks) {
		Block temp;
		for (int i = 1; i < blocks.size(); i++) {
			for (int j = i; j > 0; j--) {
				if(blocks.get(j).getOwnedBlockElements().get(0).getElementWrappers().size() > blocks.get(j-1).getOwnedBlockElements().get(0).getElementWrappers().size()){
					temp = blocks.get(j);
					blocks.set(j, blocks.get(j - 1));
					blocks.set(j - 1, temp);
				}
			}
		}
		return blocks;
	}

}
