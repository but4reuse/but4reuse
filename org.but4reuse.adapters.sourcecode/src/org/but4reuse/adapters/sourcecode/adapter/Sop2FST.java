package org.but4reuse.adapters.sourcecode.adapter;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class Sop2FST {

	ArrayList<FSTNode> result = new ArrayList<FSTNode>();

	ArrayList<FSTNode> rootOfFeatures = new ArrayList<FSTNode>();

	public List<FSTNode> toFST(List<IElement> feature) {

		ArrayList<FSTNode> retained = new ArrayList<FSTNode>();

		for (IElement t : feature) {

			FSTNodeElement cfst = (FSTNodeElement) t;
			FSTNode node = cfst.getNode();
			ArrayList<FSTNode> np = this.getElementsToKeep(node);
			FSTNode rootOfNode = this.getRoot(np);

			if (rootOfFeatures.contains(rootOfNode)) {

				// System.out.println("CLONAGE");
				ArrayList<FSTNode> clones = clone(np);

				for (FSTNode d : clones) {

					if (!checkNode(retained, d))
						retained.add(d);
				}

			}

			else {

				for (FSTNode n : this.getElementsToKeep(node)) {
					if (!retained.contains(n)) {
						retained.add(n);

					}

				}

			}

		}

		// traitement
		ArrayList<FSTNode> roots = getRoots(retained);

		List<FSTNode> newRoots = new ArrayList<FSTNode>();
		for (FSTNode root : roots) {

			if (!rootOfFeatures.contains(root))
				rootOfFeatures.add(root);
			root = cleanChildren(root, retained);
			newRoots.add(root);
		}

		return newRoots;

	}

	private boolean checkNode(ArrayList<FSTNode> conserver, FSTNode d) {
		// TODO Auto-generated method stub

		for (FSTNode h : conserver) {

			if (h.getName().equals(d.getName()) && h.getType().equals(d.getType()))
				return true;
		}
		return false;
	}

	private ArrayList<FSTNode> clone(ArrayList<FSTNode> parents) {
		// TODO Auto-generated method stub
		ArrayList<FSTNode> result = new ArrayList<FSTNode>();
		int i = 0;
		FSTNode n = null;
		String nameFile;
		while (i < parents.size() - 1) {
			nameFile = LanguageConfigurator.filesNames.get(parents.get(i));
			n = parents.get(i).getShallowClone();
			n.setParent(parents.get(i + 1).getShallowClone());
			result.add(n);
			LanguageConfigurator.filesNames.put(n, nameFile);
			i++;
		}
		FSTNode root = parents.get(i).getShallowClone();
		nameFile = LanguageConfigurator.filesNames.get(parents.get(i));
		if (n != null)
			n.setParent(root);
		LanguageConfigurator.filesNames.put(n, nameFile);
		result.add(root);
		return result;
	}

	private ArrayList<FSTNode> getRoots(ArrayList<FSTNode> conserver) {
		// TODO Auto-generated method stub

		ArrayList<FSTNode> res = new ArrayList<FSTNode>();
		for (FSTNode n : conserver) {
			// System.out.println(" Get Root Traitement "+n.getName()+" "+n.getType());
			if (n.getParent() == null) {
				if (!res.contains(n)) {
					res.add(n);
					// System.out.println(" Get Root Added "+n.getName()+" "+n.getType());
				}
			}
		}

		return res;
	}

	public FSTNode cleanChildren(FSTNode node, ArrayList<FSTNode> conserver) {

		if (node instanceof FSTTerminal) {

			FSTNode newNode = node.getShallowClone();
			;
			LanguageConfigurator.filesNames.put(newNode, LanguageConfigurator.filesNames.get(node));
			return newNode;

		} else {

			FSTNonTerminal nnt = (FSTNonTerminal) node;
			FSTNonTerminal newNode = (FSTNonTerminal) nnt.getShallowClone();

			for (FSTNode child : nnt.getChildren()) {

				if (checkNode(conserver, child) || (child.getName().equals("-")) || (child.getType().equals("Id"))
						|| (child.getName().equals("#include")) || (child.getType().equals("ImportDeclaration"))) {

					newNode.addChild(cleanChildren(child, conserver));

				}

			}
			LanguageConfigurator.filesNames.put(newNode, LanguageConfigurator.filesNames.get(node));
			return newNode;
		}

	}

	ArrayList<FSTNode> parents = new ArrayList<FSTNode>();

	/*
	 * public void printFeature(Feature f){
	 * 
	 * System.out.println(" ==============================");
	 * System.out.println("Feature : "+f.getId() + " "+ f.size()+ " node");
	 * System.out.println("------------DEBUT --------");
	 * 
	 * for(Trigger t:f){
	 * 
	 * StatementPrimitive st = (StatementPrimitive)t; ConstructionPrimitive
	 * consPrimitive = st.getPrimitive(); CreateFSTNode cfst =
	 * (CreateFSTNode)consPrimitive; FSTNode node = cfst.getNode();
	 * System.out.println(cfst.getNode().getName());
	 * 
	 * } System.out.println("------------FIN --------");
	 * System.out.println(" ==============================");
	 * 
	 * }
	 */

	private FSTNode getRoot(ArrayList<FSTNode> parent) {
		// TODO Auto-generated method stub
		for (FSTNode n : parents) {

			if (n.getParent() == null)
				return n;
		}
		return null;
	}

	private ArrayList<FSTNode> getElementsToKeep(FSTNode node) {
		// TODO Auto-generated method stub

		ArrayList<FSTNode> pr = new ArrayList<FSTNode>();
		pr.add(0, node);
		FSTNode parent = node.getParent();

		int i = 1;
		while (parent != null) {

			if (!checkNode(pr, parent)) {
				pr.add(i, parent);
				// System.out.println(node.getName()+"  : PARENT ADDED :"+parent.getName()+
				// "  "+parent.getType());
			}
			parent = parent.getParent();
			i++;

		}

		// System.out.println("Les parents de :"+node.getName());
		// System.out.println("      "+pr.size());
		return pr;
	}

}