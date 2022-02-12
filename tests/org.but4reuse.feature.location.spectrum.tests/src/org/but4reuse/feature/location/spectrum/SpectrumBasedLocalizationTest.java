package org.but4reuse.feature.location.spectrum;

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
import org.but4reuse.block.identification.impl.SimilarElementsBlockIdentification;
import org.but4reuse.feature.location.LocatedFeature;
import org.but4reuse.feature.location.LocatedFeaturesUtils;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.FeatureListFactory;
import org.but4reuse.tests.utils.TestElementsCreator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

public class SpectrumBasedLocalizationTest {

	@Test
	public void basicSpectrumTest() {
		// Prepare scenario
		// A1	(F1)			1-10
		// A2	(F1, F2)		1-30
		// A3	(F1, F2, F3)	1-100
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		AdaptedArtefact a1 = AdaptedModelHelper
				.wrapElementsToCreateAdaptedArtefact(TestElementsCreator.createElements(10));
		Artefact one = ArtefactModelFactory.eINSTANCE.createArtefact();
		a1.setArtefact(one);
		AdaptedArtefact a2 = AdaptedModelHelper
				.wrapElementsToCreateAdaptedArtefact(TestElementsCreator.createElements(30));
		Artefact two = ArtefactModelFactory.eINSTANCE.createArtefact();
		a2.setArtefact(two);
		AdaptedArtefact a3 = AdaptedModelHelper
				.wrapElementsToCreateAdaptedArtefact(TestElementsCreator.createElements(100));
		Artefact three = ArtefactModelFactory.eINSTANCE.createArtefact();
		a3.setArtefact(three);
		adaptedArtefacts.add(a1);
		adaptedArtefacts.add(a2);
		adaptedArtefacts.add(a3);
		
		IBlockIdentification algo = new SimilarElementsBlockIdentification();
		List<Block> blocks = algo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());

		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		adaptedModel.getOwnedAdaptedArtefacts().addAll(adaptedArtefacts);
		adaptedModel.getOwnedBlocks().addAll(blocks);

		FeatureList featureList = FeatureListFactory.eINSTANCE.createFeatureList();
		Feature f1 = FeatureListFactory.eINSTANCE.createFeature();
		f1.setId("f1");
		f1.getImplementedInArtefacts().add(a1.getArtefact());
		f1.getImplementedInArtefacts().add(a2.getArtefact());
		f1.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f1);

		Feature f2 = FeatureListFactory.eINSTANCE.createFeature();
		f2.setId("f2");
		f2.getImplementedInArtefacts().add(a2.getArtefact());
		f2.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f2);

		Feature f3 = FeatureListFactory.eINSTANCE.createFeature();
		f3.setId("f3");
		f3.getImplementedInArtefacts().add(a3.getArtefact());
		featureList.getOwnedFeatures().add(f3);

		// test it
		// blocks are 1 per element (SimilarElementsBlockIdentification)
		SpectrumBasedLocalization fl = new SpectrumBasedLocalization();
		List<LocatedFeature> locatedFeatures = fl.locateFeatures(featureList, adaptedModel,
				RankingMetrics.getRankingMetricByName("Wong2"), 1.0, new NullProgressMonitor());

		Assert.assertEquals(100, locatedFeatures.size());
		for (Feature f : featureList.getOwnedFeatures()) {
			List<Block> locatedBlocks = LocatedFeaturesUtils.getBlocksOfFeature(locatedFeatures, f);
			if (f.getId().equals("f1")) {
				Assert.assertEquals(10, locatedBlocks.size());
			} else if (f.getId().equals("f2")) {
				Assert.assertEquals(20, locatedBlocks.size());
			} else if (f.getId().equals("f3")) {
				Assert.assertEquals(70, locatedBlocks.size());
			}
		}
	}
}
