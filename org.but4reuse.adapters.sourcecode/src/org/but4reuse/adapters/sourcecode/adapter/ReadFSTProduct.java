package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;

import printer.PrintVisitorException;
import cide.gparser.ParseException;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class ReadFSTProduct {

	private HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> bodies_nodes = new HashMap<String, ArrayList<HashMap<String, FSTTerminal>>>();
	HashMap<String, String> MethodsOfProduct = null;

	public HashMap<String, String> getBody() {
		return MethodsOfProduct;
	}

	private List<IElement> artefact = null;

	public List<IElement> getArtefact() {
		return artefact;
	}

	public void readProduct(String pathToExplore) throws FileNotFoundException, ParseException, PrintVisitorException {

		UtiliClassFiles diskFileExplorer = new UtiliClassFiles(pathToExplore, true);
		ArrayList<String> allFiles = diskFileExplorer.listFiles(null, new File(pathToExplore));

		System.out.println("#####  Parsing Files  ##### :" + pathToExplore);

		FST2SoCPVisitor fst2cp = new FST2SoCPVisitor();
		ArrayList<FSTNode> theNodes = new ArrayList<FSTNode>();

		for (String fileName : allFiles) {
			System.out.println("   File:" + fileName);
			if (LanguageConfigurator.getLanguage().isALanguageProgram(fileName)) {

				FSTNonTerminal node = LanguageConfigurator.getLanguage().parseFile(fileName);
				theNodes.add(node);
				System.out.println("##### FILE  ADDED ##### :" + fileName);
			}
		}
		fst2cp.visit(theNodes);

		bodies_nodes = fst2cp.getBodies_nodes();
		artefact = fst2cp.getProduct();

		// artefact.setId(idProduct);

		this.MethodsOfProduct = fst2cp.getBody();

	}

	public HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> getBodies_nodes() {
		return bodies_nodes;
	}

	public HashMap<String, String> getMethodsOfProduct() {
		return MethodsOfProduct;
	}
}
