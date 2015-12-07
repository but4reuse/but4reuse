package org.but4reuse.fca.utils;

import java.util.HashMap;
import java.util.Map;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;

import com.googlecode.erca.BinaryAttribute;
import com.googlecode.erca.Entity;
import com.googlecode.erca.ErcaFactory;
import com.googlecode.erca.clf.ConceptLattice;
import com.googlecode.erca.framework.algo.ConceptLatticeGenerator;
import com.googlecode.erca.rcf.FormalContext;
import com.googlecode.erca.rcf.RcfFactory;

/**
 * Formal Context Utils
 * 
 * @author jabier.martinez
 * 
 */
public class FCAUtils {

	public static FormalContext createFormalContext(AdaptedModel adaptedModel) {

		// Creates a formal context
		FormalContext fc = RcfFactory.eINSTANCE.createFormalContext();
		fc.setName("FormalContext");

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

	public static ConceptLattice createConceptLattice(FormalContext fc) {
		ConceptLatticeGenerator clg = new ConceptLatticeGenerator(fc);
		clg.generateConceptLattice();
		return clg.getConceptLattice();
	}
}
