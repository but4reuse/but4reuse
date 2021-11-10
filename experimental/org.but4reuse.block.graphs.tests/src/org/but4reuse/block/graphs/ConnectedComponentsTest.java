package org.but4reuse.block.graphs;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.graphs.FCAStronglyConnectedComponents;
import org.but4reuse.block.graphs.FCAWeaklyConnectedComponents;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.tests.utils.TestElement;
import org.but4reuse.tests.utils.TestElementsCreator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

public class ConnectedComponentsTest {

	/**
	 * A unique artifact with two different root elements, the result should be
	 * two blocks (fca returns 1 block but then it is split in two)
	 */
	@Test
	public void blocksIdentified() {
		// Prepare
		
		List<IElement> elements = TestElementsCreator.createElements(10);
		IElement e0 = elements.get(0);
		IElement e1 = elements.get(1);
		for (int i = 2; i < elements.size(); i++) {
			if (i % 2 == 0) {
				((TestElement) elements.get(i)).addDependency(e0);
			} else {
				((TestElement) elements.get(i)).addDependency(e1);
			}
		}
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		AdaptedArtefact a1 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(elements);
		adaptedArtefacts.add(a1);
		
		IBlockIdentification algo = new FCAWeaklyConnectedComponents();
		List<Block> blocks = algo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());
		// Weakly: 2 blocks
		Assert.assertEquals(2, blocks.size());
		
		IBlockIdentification algo2 = new FCAStronglyConnectedComponents();
		List<Block> blocks2 = algo2.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());
		// Strongly: 10 blocks
		Assert.assertEquals(10, blocks2.size());
		
		// connect the two roots in both directions
		((TestElement) elements.get(0)).addDependency(e1);
		((TestElement) elements.get(1)).addDependency(e0);
		List<AdaptedArtefact> adaptedArtefacts2 = new ArrayList<AdaptedArtefact>();
		AdaptedArtefact a2 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(elements);
		adaptedArtefacts2.add(a2);
		
		List<Block> blocks3 = algo2.identifyBlocks(adaptedArtefacts2, new NullProgressMonitor());
		// Strongly: 9 blocks
		Assert.assertEquals(9, blocks3.size());
	}

	/**
	 * Two artifacts. The first one is only one root element. The second
	 * artifact is the same root element and then 5 chains of elements that
	 * connects to the root. Fca returns 2 blocks but then, the second one will
	 * be split in 5 so the result will be 6 blocks)
	 */
	@Test
	public void blocksIdentified2() {
		// Prepare	
		List<IElement> elements = TestElementsCreator.createElements(501);
		for (int i = 1; i < elements.size(); i++) {
			// create chains
			if (i % 101 != 0) {
				((TestElement) elements.get(i)).addDependency(elements.get(i - 1));
			}
			// put the dependency to the root
			if (i == 1 || i % 101 == 0) {
				((TestElement) elements.get(i)).addDependency(elements.get(0));
			}
		}
		
		// prepare adapted artefacts
		List<IElement> firstArtefactElements = new ArrayList<IElement>();
		firstArtefactElements.add(elements.get(0));
		AdaptedArtefact a1 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(firstArtefactElements);
		
		AdaptedArtefact a2 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(elements);
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		adaptedArtefacts.add(a1);
		adaptedArtefacts.add(a2);
		
		IBlockIdentification algo = new FCAWeaklyConnectedComponents();
		List<Block> blocks = algo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());
		// Weakly: 6 blocks
		Assert.assertEquals(6, blocks.size());
		
		IBlockIdentification algo2 = new FCAStronglyConnectedComponents();
		List<Block> blocks2 = algo2.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());
		// Strongly: 501 blocks
		Assert.assertEquals(501, blocks2.size());
	}
}
