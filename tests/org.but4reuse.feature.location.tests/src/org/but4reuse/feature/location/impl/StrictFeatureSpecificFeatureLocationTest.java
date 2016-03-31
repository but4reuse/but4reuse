package org.but4reuse.feature.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.artefactmodel.ArtefactModelFactory;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.impl.IntersectionsBlockIdentification;
import org.but4reuse.feature.location.IFeatureLocation;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.tests.utils.TestElementsCreator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Strict Feature Specific
 * 
 * @author jabier.martinez
 * 
 */
public class StrictFeatureSpecificFeatureLocationTest {

	@Test
	public void locatedFeatures() {
		// Prepare
		IBlockIdentification algo = new IntersectionsBlockIdentification();
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		AdaptedArtefact a1 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(10));
		Artefact one = ArtefactModelFactory.eINSTANCE.createArtefact();
		a1.setArtefact(one);
		AdaptedArtefact a2 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(30));
		Artefact two = ArtefactModelFactory.eINSTANCE.createArtefact();
		a2.setArtefact(two);
		AdaptedArtefact a3 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(TestElementsCreator
				.createElements(100));
		Artefact three = ArtefactModelFactory.eINSTANCE.createArtefact();
		a3.setArtefact(three);
		adaptedArtefacts.add(a1);
		adaptedArtefacts.add(a2);
		adaptedArtefacts.add(a3);
		List<Block> blocks = algo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());

		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		adaptedModel.getOwnedAdaptedArtefacts().addAll(adaptedArtefacts);
		adaptedModel.getOwnedBlocks().addAll(blocks);

		FeatureList featureList = FeatureListFactory.eINSTANCE.createFeatureList();
		Feature f1 = FeatureListFactory.eINSTANCE.createFeature();
		f1.getImplementedInArtefacts().add(a1.getArtefact());
		f1.getImplementedInArtefacts().add(a2.getArtefact());
		f1.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f1);

		Feature f2 = FeatureListFactory.eINSTANCE.createFeature();
		f2.getImplementedInArtefacts().add(a2.getArtefact());
		f2.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f2);

		Feature f3 = FeatureListFactory.eINSTANCE.createFeature();
		f3.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f3);

		// test it
		IFeatureLocation fl = new StrictFeatureSpecificFeatureLocation();
		List<LocatedFeature> locatedFeatures = fl.locateFeatures(featureList, adaptedModel, new NullProgressMonitor());

		Assert.assertEquals(3, locatedFeatures.size());

		for (LocatedFeature lf : locatedFeatures) {
			Assert.assertEquals(1, lf.getBlocks().size());
		}

		Assert.assertEquals(blocks.get(0), locatedFeatures.get(0).getBlocks().get(0));
		Assert.assertEquals(blocks.get(1), locatedFeatures.get(1).getBlocks().get(0));
		Assert.assertEquals(blocks.get(2), locatedFeatures.get(2).getBlocks().get(0));
	}
}
