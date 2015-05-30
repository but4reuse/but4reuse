package org.but4reuse.adapters.sourcecode.adapter;

import java.util.List;

import org.but4reuse.adapters.IElement;

import de.ovgu.cide.fstgen.ast.FSTNode;

public class CodeGeneration {

	public static void generate(String featName, String pathImpl, List<IElement> feature) {

		Elements2FST s = new Elements2FST();

		List<FSTNode> nodes = s.toFST(feature);

		// featuresToFile(pathImpl + "features_nodes.txt", nodes,
		// sif.getAllP());

		System.out.println("Printing Feature :" + featName);

		for (FSTNode n : nodes) {

			LanguageManager.getLanguage().generateCode(n, pathImpl);

		}

		// allResultFeatures = nodes.keySet();

		// GenerateFeatureModelFST gfm2 = new GenerateFeatureModelFST();
		// gfm2.generateFeatureModelWithBehavior(features, new
		// ArrayList<Constraint>(), si_products,pathImpl);

	}

}
