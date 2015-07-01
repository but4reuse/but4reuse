package org.but4reuse.blockcreation.fca;

import com.googlecode.erca.Attribute;
import com.googlecode.erca.clf.Concept;
import com.googlecode.erca.clf.ConceptLattice;

/**
 * Remove parents intent concept filter
 * 
 * @author jabier.martinez
 * 
 */
public class RemoveParentsIntentConceptFilter {

	public static void filter(ConceptLattice lattice) {
		for (Concept concept : lattice.getConcepts()) {
			for (Concept parent : concept.getAllParents()) {
				// all parents returns also itself...
				if (parent != concept) {
					for (Attribute attribute : parent.getIntent()) {
						if (concept.getIntent().contains(attribute)) {
							concept.getIntent().remove(attribute);
						}
					}
				}
			}
		}
	}

}
