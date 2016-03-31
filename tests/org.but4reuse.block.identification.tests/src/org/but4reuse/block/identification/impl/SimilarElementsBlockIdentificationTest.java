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
 * Test for similar elements block identification
 * 
 * @author jabier.martinez
 * 
 */
public class SimilarElementsBlockIdentificationTest {

	/**
	 * We create test 3 artefacts with shared elements and we check that the
	 * result is correct and always the same in the same order
	 */
	@Test
	public void blocksIdentified() {

		// Prepare
		IBlockIdentification algo = new SimilarElementsBlockIdentification();
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

		// 100 blocks
		Assert.assertEquals(100, blocks.size());

		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		adaptedModel.getOwnedAdaptedArtefacts().addAll(adaptedArtefacts);
		adaptedModel.getOwnedBlocks().addAll(blocks);

		// Check the common blocks
		Assert.assertEquals(10, AdaptedModelHelper.getCommonBlocks(adaptedModel).size());

		// Check that size is ok, all should be size 1. Check also that order is
		// correct
		int i = 0;
		for (Block b : blocks) {
			Assert.assertEquals(1, b.getOwnedBlockElements().size());
			List<IElement> elements = AdaptedModelHelper.getElementsOfBlock(b);
			Assert.assertEquals(i, ((TestElement) elements.get(0)).id);
			i++;
		}

	}

}
