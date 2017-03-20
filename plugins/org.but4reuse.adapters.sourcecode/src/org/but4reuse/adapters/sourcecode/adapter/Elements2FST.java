package org.but4reuse.adapters.sourcecode.adapter;

import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.BodyElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class Elements2FST {

	List<FSTNode> result = new ArrayList<FSTNode>();

	List<FSTNode> rootOfFeatures = new ArrayList<FSTNode>();

	public List<FSTNode> elementsToFST(List<IElement> elements) {
		ArrayList<FSTNode> retained = new ArrayList<FSTNode>();
		for (IElement element : elements) {
			// if it is a BodyElement create the parent
			if (element instanceof BodyElement) {
				BodyElement be = (BodyElement) element;
				element = be.getParent();
			}
			// calculate elements to keep
			if (element instanceof FSTNodeElement) {
				FSTNodeElement nodeElement = (FSTNodeElement) element;
				FSTNode node = nodeElement.getNode();
				List<FSTNode> np = this.getElementsToKeep(node);
				FSTNode rootOfNode = this.getRoot(np);
				if (rootOfFeatures.contains(rootOfNode)) {
					List<FSTNode> clones = clone(np);
					for (FSTNode d : clones) {
						if (!checkNode(retained, d)) {
							retained.add(d);
						}
					}
				} else {
					for (FSTNode n : this.getElementsToKeep(node)) {
						if (!retained.contains(n)) {
							retained.add(n);
						}
					}
				}
			}
		}

		List<FSTNode> roots = getRoots(retained);

		List<FSTNode> newRoots = new ArrayList<FSTNode>();
		for (FSTNode root : roots) {
			if (!rootOfFeatures.contains(root))
				rootOfFeatures.add(root);
			root = cleanChildren(root, retained);
			newRoots.add(root);
		}
		return newRoots;
	}

	private boolean checkNode(List<FSTNode> conserver, FSTNode d) {
		for (FSTNode h : conserver) {
			if (h.getName().equals(d.getName()) && h.getType().equals(d.getType()))
				return true;
		}
		return false;
	}

	private List<FSTNode> clone(List<FSTNode> parents) {
		List<FSTNode> result = new ArrayList<FSTNode>();
		int i = 0;
		FSTNode n = null;
		String nameFile;
		while (i < parents.size() - 1) {
			nameFile = LanguageManager.filesNames.get(parents.get(i));
			n = parents.get(i).getShallowClone();
			n.setParent(parents.get(i + 1).getShallowClone());
			result.add(n);
			LanguageManager.filesNames.put(n, nameFile);
			i++;
		}
		FSTNode root = parents.get(i).getShallowClone();
		nameFile = LanguageManager.filesNames.get(parents.get(i));
		if (n != null)
			n.setParent(root);
		LanguageManager.filesNames.put(n, nameFile);
		result.add(root);
		return result;
	}

	private List<FSTNode> getRoots(ArrayList<FSTNode> conserver) {
		List<FSTNode> res = new ArrayList<FSTNode>();
		for (FSTNode n : conserver) {
			// System.out.println(" Get Root "+n.getName()+" "+n.getType());
			if (n.getParent() == null) {
				if (!res.contains(n)) {
					res.add(n);
					// System.out.println(" Get Root Added "+n.getName()+"
					// "+n.getType());
				}
			}
		}

		return res;
	}

	public FSTNode cleanChildren(FSTNode node, List<FSTNode> conserver) {

		if (node instanceof FSTTerminal) {
			FSTNode newNode = node.getShallowClone();
			LanguageManager.filesNames.put(newNode, LanguageManager.filesNames.get(node));
			return newNode;
		} else {
			FSTNonTerminal nnt = (FSTNonTerminal) node;
			FSTNonTerminal newNode = (FSTNonTerminal) nnt.getShallowClone();

			for (FSTNode child : nnt.getChildren()) {
				// TODO ???
				if (checkNode(conserver, child) || (child.getName().equals("-")) || (child.getType().equals("Id"))
						|| (child.getName().equals("#include")) || (child.getType().equals("ImportDeclaration"))) {

					newNode.addChild(cleanChildren(child, conserver));

				}

			}
			LanguageManager.filesNames.put(newNode, LanguageManager.filesNames.get(node));
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

	private FSTNode getRoot(List<FSTNode> parent) {
		for (FSTNode n : parents) {
			if (n.getParent() == null)
				return n;
		}
		return null;
	}

	private List<FSTNode> getElementsToKeep(FSTNode node) {
		List<FSTNode> pr = new ArrayList<FSTNode>();
		pr.add(0, node);
		FSTNode parent = node.getParent();
		int i = 1;
		while (parent != null) {
			if (!checkNode(pr, parent)) {
				pr.add(i, parent);
			}
			parent = parent.getParent();
			i++;
		}
		return pr;
	}

}