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
import weka.core.Instances;

/**
 * Apriori association rules
 * @author jabier.martinez
 */
public class APrioriConstraintsDiscovery implements IConstraintsDiscovery {

	@Override
	public List<IConstraint> discover(FeatureList featureList, final AdaptedModel adaptedModel, Object extra,
			IProgressMonitor monitor) {
		List<IConstraint> constraints = new ArrayList<IConstraint>();

		Instances m_instances = WekaUtils.createInstances(adaptedModel);
		Apriori apriori = new Apriori();
		apriori.setMinMetric(1);
		int m_numRules = 100;
		apriori.setNumRules(m_numRules);
		try {
			apriori.buildAssociations(m_instances);
		} catch (Exception e) {
			e.printStackTrace();
		}

		IConstraint constraint = new ConstraintImpl();
		constraint.setType(IConstraint.FREETEXT);
		List<String> explanations = new ArrayList<String>();
		explanations.add(new String(apriori.toString()));
		constraint.setExplanations(explanations);
		constraints.add(constraint);
		return constraints;
	}

}
