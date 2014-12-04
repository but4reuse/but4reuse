package org.but4reuse.adapters.sourcecode.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;

import de.ovgu.cide.fstgen.ast.FSTNode;

public class CodeGeneration {

	public static void generate(String featName, String pathImpl, List<IElement> feature) {

		Sop2FST s = new Sop2FST();

		List<FSTNode> nodes = s.toFST(feature);

		// featuresToFile(pathImpl + "features_nodes.txt", nodes,
		// sif.getAllP());

		System.out.println("Printing Feature :" + featName);

		for (FSTNode n : nodes) {

			LanguageConfigurator.getLanguage().generateCode(n, pathImpl, featName);

		}

		// allResultFeatures = nodes.keySet();

		// GenerateFeatureModelFST gfm2 = new GenerateFeatureModelFST();
		// gfm2.generateFeatureModelWithBehavior(features, new
		// ArrayList<Constraint>(), si_products,pathImpl);

	}

}
