package org.but4reuse.block.identification.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.tests.utils.TestElement;
import org.but4reuse.tests.utils.TestElementsCreator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for Intersections block identification (Interdependent elements algo)
 * 
 * @author jabier.martinez
 * 
 */
public class IntersectionsBlockIdentificationTest {

	/**
	 * We create test 3 artefacts with shared elements and we check that the
	 * result is correct and always the same in the same order
	 */
	@Test
	public void blocksIdentified() {

		// Prepare
		IBlockIdentification algo = new IntersectionsBlockIdentification();
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		AdaptedArtefact a1 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(10));
		AdaptedArtefact a2 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(30));
		AdaptedArtefact a3 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(100));
		adaptedArtefacts.add(a1);
		adaptedArtefacts.add(a2);
		adaptedArtefacts.add(a3);
		List<Block> blocks = algo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());

		// 3 blocks: one common and another two
		Assert.assertEquals(3, blocks.size());

		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		adaptedModel.getOwnedAdaptedArtefacts().addAll(adaptedArtefacts);
		adaptedModel.getOwnedBlocks().addAll(blocks);

		// Check that there is only one common
		Assert.assertEquals(1, AdaptedModelHelper.getCommonBlocks(adaptedModel).size());

		// Check that size is ok and that order is always the same
		Assert.assertEquals(10, blocks.get(0).getOwnedBlockElements().size());
		Assert.assertEquals(20, blocks.get(1).getOwnedBlockElements().size());
		Assert.assertEquals(70, blocks.get(2).getOwnedBlockElements().size());

		// Check that they are the correct elements and that elements are in the
		// correct order
		// Block 0, elements from 0 to 9
		List<IElement> elements = AdaptedModelHelper.getElementsOfBlock(blocks.get(0));
		for (int i = 0; i < elements.size(); i++) {
			Assert.assertEquals(i, ((TestElement) elements.get(i)).id);
		}
		// Block 1, elements from 10 to 29
		elements = AdaptedModelHelper.getElementsOfBlock(blocks.get(1));
		for (int i = 0; i < elements.size(); i++) {
			Assert.assertEquals(i + 10, ((TestElement) elements.get(i)).id);
		}
		// Block 2, elements from 30 to 99
		elements = AdaptedModelHelper.getElementsOfBlock(blocks.get(2));
		for (int i = 0; i < elements.size(); i++) {
			Assert.assertEquals(i + 30, ((TestElement) elements.get(i)).id);
		}
	}

}
