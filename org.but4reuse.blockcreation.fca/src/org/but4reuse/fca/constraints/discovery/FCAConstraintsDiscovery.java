package org.but4reuse.fca.constraints.discovery;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.fca.utils.FCAUtils;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.impl.ConstraintImpl;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;

import com.googlecode.erca.Attribute;
import com.googlecode.erca.clf.Concept;
import com.googlecode.erca.clf.ConceptLattice;
import com.googlecode.erca.rcf.FormalContext;

/**
 * FCA based constraints discovery
 * 
 */
public class FCAConstraintsDiscovery implements IConstraintsDiscovery {

	@Override
	public List<IConstraint> discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		List<IConstraint> constraintList = new ArrayList<IConstraint>();
		FormalContext fc = FCAUtils.createArtefactsBlocksFormalContext(adaptedModel);
		ConceptLattice cl = FCAUtils.createConceptLattice(fc);

		// REQUIRES
		monitor.subTask("FCA: Checking Requires relations");
		for (Concept c : cl.getConcepts()) {
			// getIntent returns also the intent of the parents,
			// getSimplifiedIntent which is the one that only belongs to this
			// concept
			if (!c.getSimplifiedIntent().isEmpty()) {
				for (Attribute owner : c.getSimplifiedIntent()) {
					for (Attribute a : c.getIntent()) {
						if (!owner.sameAs(a)) {
							// add constraint
							Block ownerBlock = AdaptedModelHelper.getBlockByName(adaptedModel, owner.getDescription());
							Block block = AdaptedModelHelper.getBlockByName(adaptedModel, a.getDescription());
							IConstraint constraint = new ConstraintImpl();
							constraint.setType(IConstraint.REQUIRES);
							constraint.setBlock1(ownerBlock);
							constraint.setBlock2(block);
							List<String> messages = new ArrayList<String>();
							messages.add("Concept lattice found");
							constraint.setExplanations(messages);
							constraint.setNumberOfReasons(messages.size());
							constraintList.add(constraint);
						}
					}
				}
			}
		}

		// EXCLUDES
		monitor.subTask("FCA: Checking Excludes relations");
		// No concept have the two features

		for (int i = 0; i < fc.getAttributes().size(); i++) {
			Attribute a1 = fc.getAttributes().get(i);
			for (int j = 0; j < fc.getAttributes().size(); j++) {
				Attribute a2 = fc.getAttributes().get(j);
				// mutual exclude is bidirectional, avoid duplicated
				if (j != i && i < j) {
					boolean found = false;
					for (Concept c : cl.getConcepts()) {
						if (!c.getSimplifiedIntent().isEmpty()) {
							if (contains(c.getIntent(), a1) && contains(c.getIntent(), a2)) {
								found = true;
								break;
							}
						}
					}
					if (!found) {
						// add constraint
						Block ownerBlock = AdaptedModelHelper.getBlockByName(adaptedModel, a1.getDescription());
						Block block = AdaptedModelHelper.getBlockByName(adaptedModel, a2.getDescription());
						IConstraint constraint = new ConstraintImpl();
						constraint.setType(IConstraint.MUTUALLY_EXCLUDES);
						constraint.setBlock1(ownerBlock);
						constraint.setBlock2(block);
						List<String> messages = new ArrayList<String>();
						messages.add("No concept sharing both");
						constraint.setExplanations(messages);
						constraint.setNumberOfReasons(messages.size());
						constraintList.add(constraint);
					}
				}
			}
		}
		return constraintList;
	}

	/**
	 * Apparently equals does not work
	 * 
	 * @param attributes
	 * @param attr
	 * @return boolean
	 */
	private boolean contains(EList<Attribute> attributes, Attribute attr) {
		for (Attribute a : attributes) {
			if (attr.sameAs(a)) {
				return true;
			}
		}
		return false;
	}

}
