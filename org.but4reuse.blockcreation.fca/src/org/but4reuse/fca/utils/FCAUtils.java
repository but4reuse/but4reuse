package org.but4reuse.fca.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.artefactmodel.Artefact;
import org.but4reuse.featurelist.Feature;
import org.but4reuse.featurelist.FeatureList;
import org.but4reuse.featurelist.helpers.FeatureListHelper;

import com.googlecode.erca.BinaryAttribute;
import com.googlecode.erca.Entity;
import com.googlecode.erca.ErcaFactory;
import com.googlecode.erca.clf.ConceptLattice;
import com.googlecode.erca.clf.ConceptLatticeFamily;
import com.googlecode.erca.framework.algo.ClfGenerator;
import com.googlecode.erca.framework.algo.ConceptLatticeGenerator;
import com.googlecode.erca.framework.io.out.RcfToXHTML;
import com.googlecode.erca.rcf.FormalContext;
import com.googlecode.erca.rcf.RcfFactory;
import com.googlecode.erca.rcf.RelationalContext;
import com.googlecode.erca.rcf.RelationalContextFamily;

/**
 * Formal Context Utils
 * 
 * @author jabier.martinez
 * 
 */
public class FCAUtils {

	/**
	 * Create artefacts/blocks formal context
	 * 
	 * @param adaptedModel
	 * @return formal context
	 */
	public static FormalContext createArtefactsBlocksFormalContext(AdaptedModel adaptedModel) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("ArtefactsBlocks");

		// Creates an entity per artefact
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			Entity ent = ErcaFactory.eINSTANCE.createEntity();
			ent.setName(aa.getArtefact().getName());
			fc.getEntities().add(ent);
		}

		Map<String, BinaryAttribute> blockNameMap = new HashMap<String, BinaryAttribute>();

		// Creates a binary attribute per block
		for (Block block : adaptedModel.getOwnedBlocks()) {
			BinaryAttribute attr = ErcaFactory.eINSTANCE.createBinaryAttribute();
			attr.setName(block.getName());
			fc.getAttributes().add(attr);
			blockNameMap.put(block.getName(), attr);
		}

		// Add pairs
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			for (Block block : AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa)) {
				fc.addPair(fc.getEntity(aa.getArtefact().getName()), blockNameMap.get(block.getName()));
			}
		}

		return fc;
	}

	/**
	 * Create blocks/artefacts formal context
	 * 
	 * @param adaptedModel
	 * @return formal context
	 */
	public static FormalContext createBlocksArtefactsFormalContext(AdaptedModel adaptedModel) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("BlocksArtefacts");

		// Creates an entity per block
		for (Block b : adaptedModel.getOwnedBlocks()) {
			Entity ent = ErcaFactory.eINSTANCE.createEntity();
			ent.setName(b.getName());
			fc.getEntities().add(ent);
		}

		Map<String, BinaryAttribute> artefactNameMap = new HashMap<String, BinaryAttribute>();

		// Creates a binary attribute per artefact
		for (AdaptedArtefact a : adaptedModel.getOwnedAdaptedArtefacts()) {
			BinaryAttribute attr = ErcaFactory.eINSTANCE.createBinaryAttribute();
			attr.setName(a.getArtefact().getName());
			fc.getAttributes().add(attr);
			artefactNameMap.put(a.getArtefact().getName(), attr);
		}

		// Add pairs
		for (AdaptedArtefact aa : adaptedModel.getOwnedAdaptedArtefacts()) {
			for (Block block : AdaptedModelHelper.getBlocksOfAdaptedArtefact(aa)) {
				fc.addPair(fc.getEntity(block.getName()), artefactNameMap.get(aa.getArtefact().getName()));
			}
		}

		return fc;
	}

	/**
	 * Create Artefacts/Features formal context
	 * 
	 * @param featureList
	 * @return formal context
	 */
	public static FormalContext createArtefactsFeaturesFormalContext(FeatureList featureList) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("ArtefactsFeatures");

		// Creates an entity per artefact
		for (Artefact a : FeatureListHelper.getArtefactModel(featureList).getOwnedArtefacts()) {
			Entity ent = ErcaFactory.eINSTANCE.createEntity();
			ent.setName(a.getName());
			fc.getEntities().add(ent);
		}

		Map<String, BinaryAttribute> featureNameMap = new HashMap<String, BinaryAttribute>();

		// Creates a binary attribute per feature
		for (Feature feature : featureList.getOwnedFeatures()) {
			BinaryAttribute attr = ErcaFactory.eINSTANCE.createBinaryAttribute();
			attr.setName(feature.getName());
			fc.getAttributes().add(attr);
			featureNameMap.put(feature.getName(), attr);
		}

		// Add pairs
		for (Artefact a : FeatureListHelper.getArtefactModel(featureList).getOwnedArtefacts()) {
			for (Feature feature : FeatureListHelper.getArtefactFeatures(featureList, a)) {
				fc.addPair(fc.getEntity(a.getName()), featureNameMap.get(feature.getName()));
			}
		}

		return fc;
	}

	/**
	 * Create Features/Artefacts formal context
	 * 
	 * @param featureList
	 * @return formal context
	 */
	public static FormalContext createFeaturesArtefactsFormalContext(FeatureList featureList) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("ArtefactsFeatures");

		// Creates an entity per feature
		for (Feature f : featureList.getOwnedFeatures()) {
			Entity ent = ErcaFactory.eINSTANCE.createEntity();
			ent.setName(f.getName());
			fc.getEntities().add(ent);
		}

		Map<String, BinaryAttribute> artefactNameMap = new HashMap<String, BinaryAttribute>();

		// Creates a binary attribute per artefact
		for (Artefact a : FeatureListHelper.getArtefactModel(featureList).getOwnedArtefacts()) {
			BinaryAttribute attr = ErcaFactory.eINSTANCE.createBinaryAttribute();
			attr.setName(a.getName());
			fc.getAttributes().add(attr);
			artefactNameMap.put(a.getName(), attr);
		}

		// Add pairs
		for (Feature feature : featureList.getOwnedFeatures()) {
			for (Artefact a : FeatureListHelper.getArtefactModel(featureList).getOwnedArtefacts()) {
				if (feature.getImplementedInArtefacts().contains(a)) {
					fc.addPair(fc.getEntity(feature.getName()), artefactNameMap.get(a.getName()));
				}
			}
		}

		return fc;
	}

	/**
	 * Create concept lattice
	 * 
	 * @param formal
	 *            context
	 * @return concept lattice
	 */
	public static ConceptLattice createConceptLattice(FormalContext fc) {
		ConceptLatticeGenerator clg = new ConceptLatticeGenerator(fc);
		clg.generateConceptLattice();
		return clg.getConceptLattice();
	}

	/**
	 * Create concept lattice family
	 * 
	 * @param relational
	 *            context family
	 * @return concept lattice family
	 */
	public static ConceptLatticeFamily createConceptLatticeFamily(RelationalContextFamily rcf) {
		ClfGenerator clfGenerator = new ClfGenerator(rcf);
		try {
			clfGenerator.generateClf();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clfGenerator.getClf();
	}

	public static ConceptLatticeFamily createArtefactsBlocksFeaturesConceptLatticeFamily(FeatureList featureList,
			AdaptedModel adaptedModel) {
		// Creates a relational context family
		RelationalContextFamily rcf = RcfFactory.eINSTANCE.createRelationalContextFamily();

		// Creates two formal contexts.
		// artefacts/blocks
		FormalContext fc1 = FCAUtils.createArtefactsBlocksFormalContext(adaptedModel);
		// artefacts/features
		FormalContext fc2 = FCAUtils.createArtefactsFeaturesFormalContext(featureList);
		rcf.getFormalContexts().add(fc1);
		rcf.getFormalContexts().add(fc2);

		// Creates a relational context.
		RelationalContext rc = RcfFactory.eINSTANCE.createRelationalContext();
		// rc.setScalingOperator("com.googlecode.erca.framework.algo.scaling.Narrow");
		rc.setSourceContext(fc1);
		rc.setTargetContext(fc2);
		rc.setName("BlocksFeatures");
		rcf.getRelationalContexts().add(rc);

		RcfToXHTML a = new RcfToXHTML(rcf);
		a.generateCode();
		System.out.println(a.getCode());

		ClfGenerator clfGenerator = new ClfGenerator(rcf);
		try {
			clfGenerator.generateClf();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clfGenerator.getClf();
	}
}
