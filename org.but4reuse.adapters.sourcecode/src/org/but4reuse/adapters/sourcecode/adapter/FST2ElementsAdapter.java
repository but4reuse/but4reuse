package org.but4reuse.adapters.sourcecode.adapter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.BodyElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;
import org.but4reuse.adapters.sourcecode.FSTNonTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.FSTTerminalNodeElement;

import de.ovgu.cide.fstgen.ast.AbstractFSTPrintVisitor;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class FST2ElementsAdapter extends AbstractFSTPrintVisitor {

	public ArrayList<String> constructions = new ArrayList<String>();
	private HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> bodies_nodes = new HashMap<String, ArrayList<HashMap<String, FSTTerminal>>>();

	public HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> getBodies_nodes() {
		return bodies_nodes;

	}

	private List<IElement> productElements = new ArrayList<IElement>();

	private HashMap<String, String> body = new HashMap<String, String>();

	public HashMap<String, String> getBody() {
		return body;
	}

	public FST2ElementsAdapter(PrintStream out) {
		super(out);
		generateSpaces = true;
	}

	public FST2ElementsAdapter() {
		super();
		generateSpaces = true;

	}

	/**
	 * Visit the roots of each code file
	 * 
	 * @param nodes
	 * @return
	 */
	public boolean adapt(List<FSTNode> nodes) {
		for (FSTNode node : nodes) {
			// parent element is null
			adapt(null, node);
		}
		return true;
	}

	public boolean adapt(FSTNodeElement parentNodeElement, FSTNode node) {

		// Non terminal
		if (node instanceof FSTNonTerminal) {
			FSTNonTerminal nonTerminal = (FSTNonTerminal) node;
			String key = nonTerminal.getName() + nonTerminal.getType();
			// if (!this.constructions.contains(key)){
			this.constructions.add(key);

			FSTNonTerminalNodeElement fstNt = new FSTNonTerminalNodeElement();
			fstNt.setName(nonTerminal.getName());
			fstNt.setType(nonTerminal.getType());
			fstNt.setNode(nonTerminal);
			// add dependency
			if (parentNodeElement != null) {
				fstNt.setParent(parentNodeElement);
				fstNt.addDependency("parentNode", parentNodeElement);
			}
			productElements.add(fstNt);
			for (FSTNode n : nonTerminal.getChildren()) {
				adapt(fstNt, n);
			}
		}

		// Terminal
		if (node instanceof FSTTerminal) {

			FSTTerminal terminal = (FSTTerminal) node;

			// TODO ???
			// Omit package declarations
			if (!LanguageManager.getLanguage().isImportDec(terminal) && !terminal.getType().equals("InitializerDecl")
					&& !terminal.getName().contains("auto") && !terminal.getType().equals("EnumConstant1")) {

				String key = terminal.getName() + terminal.getType();
				// if (!this.constructions.contains(key)){
				this.constructions.add(key);
				if (LanguageManager.getLanguage().isMethod(terminal)
						|| (LanguageManager.getLanguage().isConstructor(terminal))) {

					String methodName = terminal.getName();
					String methodBody = terminal.getBody();

					body.put(methodName, deleteteComments(methodBody));

					addToBodiesNodes(methodName, deleteteComments(methodBody), terminal);

				}

				FSTTerminalNodeElement fstTN = new FSTTerminalNodeElement();
				fstTN.setName(terminal.getName());
				fstTN.setType(terminal.getType());
				fstTN.setNode(terminal);
				// add dependency
				if (parentNodeElement != null) {
					fstTN.setParent(parentNodeElement);
					fstTN.addDependency("parentNode", parentNodeElement);
				}
				productElements.add(fstTN);

				// Add Body Element
				BodyElement bodyElement = new BodyElement();
				bodyElement.setBody(terminal.getBody());
				bodyElement.setParent(fstTN);
				bodyElement.addDependency("body", fstTN);
				// Set maximum number of dependencies. A method can only have
				// one body
				fstTN.setMaximumDependencies("body", 1);
				fstTN.setMinimumDependencies("body", 1);
				productElements.add(bodyElement);
			}
		}

		return true;
	}

	private void addToBodiesNodes(String methodName, String methodBody, FSTTerminal terminal) {

		if (bodies_nodes.containsKey(methodName)) {

			ArrayList<HashMap<String, FSTTerminal>> b = bodies_nodes.get(methodBody);
			for (HashMap<String, FSTTerminal> bb : b) {

				if (bb.containsKey(methodBody))
					return;
			}
			// new body
			HashMap<String, FSTTerminal> newB = new HashMap<String, FSTTerminal>();
			newB.put(methodBody, terminal);
			b.add(newB);
		} else {
			HashMap<String, FSTTerminal> newB = new HashMap<String, FSTTerminal>();
			newB.put(methodBody, terminal);
			ArrayList<HashMap<String, FSTTerminal>> newA = new ArrayList<HashMap<String, FSTTerminal>>();
			newA.add(newB);
			bodies_nodes.put(methodBody, newA);

		}

	}

	// TODO ???
	private static String deleteteComments(String test) {
		String[] result = test.split("\n");
		String chaine = "";
		int x = 0;
		while (x < result.length) {
			if (result[x].startsWith("/*")) {
				x++;
				while ((x < result.length) && !result[x].replaceAll("\\s", "").startsWith("*/")) {
					x++;
				}
				x++;
			}
			if ((x < result.length) && !result[x].replaceAll("\\s", "").startsWith("//")) {
				chaine = chaine + result[x] + "\n";
			}
			x++;
		}
		return chaine.replaceAll("\\s", "");
	}

	@Override
	protected boolean isSubtype(String type, String expectedType) {
		// TODO ???
		return false;
	}

	public List<IElement> getProduct() {
		return productElements;
	}

}
