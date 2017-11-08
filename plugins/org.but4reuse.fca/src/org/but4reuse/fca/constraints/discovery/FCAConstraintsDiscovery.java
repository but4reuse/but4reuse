package org.but4reuse.fca.constraints.discovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.fca.utils.FCAUtils;
import org.but4reuse.feature.constraints.BasicExcludesConstraint;
import org.but4reuse.feature.constraints.BasicRequiresConstraint;
import org.but4reuse.feature.constraints.Constraint;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import fr.labri.galatea.Attribute;
import fr.labri.galatea.Concept;
import fr.labri.galatea.ConceptOrder;
import fr.labri.galatea.Context;

/**
 * FCA based constraints discovery
 * 
 */
public class FCAConstraintsDiscovery implements IConstraintsDiscovery {

	@Override
	public List<IConstraint> discover(FeatureList featureList, AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		List<IConstraint> constraintList = new ArrayList<IConstraint>();
		Context fc = FCAUtils.createArtefactsBlocksFormalContext(adaptedModel);
		ConceptOrder cl = FCAUtils.createConceptLattice(fc);

		// REQUIRES
		monitor.subTask("FCA: Checking Requires relations");
		for (Concept c : cl.getConcepts()) {
			// getIntent returns also the intent of the parents,
			// getSimplifiedIntent which is the one that only belongs to this
			// concept
			if (!c.getSimplifiedIntent().isEmpty()) {
				for (Attribute owner : c.getSimplifiedIntent()) {
					for (Attribute a : c.getIntent()) {
						if (!owner.equals(a)) {
							// if (!owner.sameAs(a)) {
							// add constraint
							Block ownerBlock = AdaptedModelHelper.getBlockByName(adaptedModel, owner.getName());
							Block block = AdaptedModelHelper.getBlockByName(adaptedModel, a.getName());
							Constraint constraint = new BasicRequiresConstraint(ownerBlock, block);
							constraint.addExplanation("Concept lattice found");
							constraintList.add(constraint);
						}
					}
				}
			}
		}

		// EXCLUDES
		monitor.subTask("FCA: Checking Excludes relations");
		// No concept have the two features

		Set<Attribute> visited = new HashSet<Attribute>();
		for (Attribute a1 : fc.getAttributes()) {
			for (Attribute a2 : fc.getAttributes()) {
				// mutual exclude is bidirectional, avoid duplicated
				if (!a1.equals(a2) && visited.contains(a2)) {
					boolean found = false;
					for (Concept c : cl.getConcepts()) {
						if (c.getIntent().contains(a1) && c.getIntent().contains(a2)) {
							found = true;
							break;
						}
					}
					if (!found) {
						// add constraint
						Block ownerBlock = AdaptedModelHelper.getBlockByName(adaptedModel, a1.getName());
						Block block = AdaptedModelHelper.getBlockByName(adaptedModel, a2.getName());
						Constraint constraint = new BasicExcludesConstraint(ownerBlock, block);
						constraint.addExplanation("No concept sharing both");
						constraintList.add(constraint);
					}
				}
			}
			visited.add(a1);
		}
		return constraintList;
	}

}
