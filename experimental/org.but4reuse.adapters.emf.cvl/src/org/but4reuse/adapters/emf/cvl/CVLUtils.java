package org.but4reuse.adapters.emf.cvl;

import cvl.AND_;
import cvl.CompositeVariability;
import cvl.CvlFactory;
import cvl.ExecutablePrimitiveTerm;
import cvl.IMPLIES;
import cvl.NOT;

/**
 * CVL Utils
 * 
 * @author jabier.martinez
 */
public class CVLUtils {

	/**
	 * Add requires constraint
	 * 
	 * @param root
	 * @param left
	 * @param right
	 */
	public static void addRequiresConstraint(CompositeVariability root, CompositeVariability left,
			CompositeVariability right) {
		IMPLIES implies = CvlFactory.eINSTANCE.createIMPLIES();
		implies.setName("IMPLIES");
		ExecutablePrimitiveTerm leftTerm = CvlFactory.eINSTANCE.createExecutablePrimitiveTerm();
		ExecutablePrimitiveTerm rightTerm = CvlFactory.eINSTANCE.createExecutablePrimitiveTerm();
		leftTerm.setPrimitive(left);
		rightTerm.setPrimitive(right);
		leftTerm.setName(left.getName());
		rightTerm.setName(right.getName());
		implies.setLeft(leftTerm);
		implies.setRight(rightTerm);
		root.getVariabilitySpecification().add(implies);
	}

	/**
	 * Add mutual exclusion constraint
	 * 
	 * @param root
	 * @param left
	 * @param right
	 */
	public static void addMutualExclusionConstraint(CompositeVariability root, CompositeVariability left,
			CompositeVariability right) {
		NOT not = CvlFactory.eINSTANCE.createNOT();
		AND_ and = CvlFactory.eINSTANCE.createAND_();
		not.setName("NOT");
		not.setTerm(and);
		and.setName("AND");
		ExecutablePrimitiveTerm leftTerm = CvlFactory.eINSTANCE.createExecutablePrimitiveTerm();
		ExecutablePrimitiveTerm rightTerm = CvlFactory.eINSTANCE.createExecutablePrimitiveTerm();
		leftTerm.setName(left.getName());
		rightTerm.setName(right.getName());
		leftTerm.setPrimitive(left);
		rightTerm.setPrimitive(right);
		and.setLeft(leftTerm);
		and.setRight(rightTerm);
		root.getVariabilitySpecification().add(not);
	}

}
