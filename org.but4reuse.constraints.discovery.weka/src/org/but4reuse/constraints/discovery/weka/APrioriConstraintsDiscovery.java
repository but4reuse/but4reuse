package org.but4reuse.constraints.discovery.weka;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.constraints.discovery.weka.utils.WekaUtils;
import org.but4reuse.feature.constraints.IConstraint;
import org.but4reuse.feature.constraints.IConstraintsDiscovery;
import org.but4reuse.feature.constraints.impl.ConstraintImpl;
import org.but4reuse.featurelist.FeatureList;
import org.eclipse.core.runtime.IProgressMonitor;

import weka.associations.Apriori;
import weka.associations.AprioriItemSet;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * Apriori association rules
 * 
 * @author jabier.martinez
 */
public class APrioriConstraintsDiscovery implements IConstraintsDiscovery {

	public List<IConstraint> discover(FeatureList featureList, final AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		monitor.subTask("Constraints discovery with APriori association rules learner");
		List<IConstraint> constraints = new ArrayList<IConstraint>();

		// Create instances, the order of the blocks are maintained
		Instances m_instances = WekaUtils.createInstances(adaptedModel, false);
		Apriori apriori = new Apriori();
		apriori.setMinMetric(1);
		int m_numRules = 1000;
		apriori.setNumRules(m_numRules);
		try {
			apriori.buildAssociations(m_instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FastVector[] fastVectors = apriori.getAllTheRules();
		FastVector leftSide = fastVectors[0];
		FastVector rightSide = fastVectors[1];
		// The fastVectors[2] are Doubles that corresponds to the confidence
		// we do not need them, always 1 for us

		for (int i = 0; i < leftSide.size(); i++) {
			IConstraint constraint = new ConstraintImpl();
			AprioriItemSet left = (AprioriItemSet) leftSide.elementAt(i);
			AprioriItemSet right = (AprioriItemSet) rightSide.elementAt(i);
			List<Integer> positiveLeft = getPositives(left);
			List<Integer> negativeLeft = getNegatives(left);
			List<Integer> positiveRight = getPositives(right);
			List<Integer> negativeRight = getNegatives(right);

			// For the moment binary requires relations are added otherwise
			// freetext constraints in featureide tool syntax
			if (positiveLeft.size() == 1 && negativeLeft.isEmpty() && positiveRight.size() == 1
					&& negativeRight.isEmpty()) {
				constraint.setType(IConstraint.REQUIRES);
				constraint.setBlock1(adaptedModel.getOwnedBlocks().get(positiveLeft.get(0)));
				constraint.setBlock2(adaptedModel.getOwnedBlocks().get(positiveRight.get(0)));
				constraint.setText(constraint.getBlock1().getName() + "requires" + constraint.getBlock2().getName());
			}
			// Just add it as free text
			if (constraint.getType() == null) {
				constraint.setType(IConstraint.FREETEXT);
				constraint.setText(getFeatureIDELikeText(adaptedModel, positiveLeft, negativeLeft, positiveRight,
						negativeRight));
			}
			List<String> explanations = new ArrayList<String>();
			explanations.add(new String("APriori association rule"));
			constraint.setExplanations(explanations);
			constraints.add(constraint);
		}
		return constraints;
	}

	private String getFeatureIDELikeText(AdaptedModel adaptedModel, List<Integer> positiveLeft,
			List<Integer> negativeLeft, List<Integer> positiveRight, List<Integer> negativeRight) {
		StringBuilder text = new StringBuilder();
		for (Integer integer : positiveLeft) {
			text.append(adaptedModel.getOwnedBlocks().get(integer).getName().replaceAll(" ", "_") + " and ");
		}
		for (Integer integer : negativeLeft) {
			text.append("not " + adaptedModel.getOwnedBlocks().get(integer).getName().replaceAll(" ", "_") + " and ");
		}
		text.setLength(text.length() - " and ".length());
		text.append(" implies ");
		for (Integer integer : positiveRight) {
			text.append(adaptedModel.getOwnedBlocks().get(integer).getName().replaceAll(" ", "_") + " and ");
		}
		for (Integer integer : negativeRight) {
			text.append("not " + adaptedModel.getOwnedBlocks().get(integer).getName().replaceAll(" ", "_") + " and ");
		}
		text.setLength(text.length() - " and ".length());
		return text.toString();
	}

	private List<Integer> getPositives(AprioriItemSet itemSet) {
		List<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < itemSet.items().length; i++) {
			if (itemSet.itemAt(i) == 1) {
				ints.add(i);
			}
		}
		return ints;
	}

	private List<Integer> getNegatives(AprioriItemSet itemSet) {
		List<Integer> ints = new ArrayList<Integer>();
		for (int i = 0; i < itemSet.items().length; i++) {
			if (itemSet.itemAt(i) == 0) {
				ints.add(i);
			}
		}
		return ints;
	}

}
