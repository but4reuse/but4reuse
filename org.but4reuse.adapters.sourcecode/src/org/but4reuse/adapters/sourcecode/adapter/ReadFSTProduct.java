package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class ReadFSTProduct {

	private HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> bodies_nodes = new HashMap<String, ArrayList<HashMap<String, FSTTerminal>>>();
	HashMap<String, String> methodsOfProduct = null;

	public HashMap<String, String> getBody() {
		return methodsOfProduct;
	}

	private List<IElement> artefact = null;

	public List<IElement> getArtefactElements() {
		return artefact;
	}

	public void readProduct(String pathToExplore) {
		List<String> allFiles = listFiles(null, new File(pathToExplore));
		FST2ElementsAdapter fst2elements = new FST2ElementsAdapter();
		List<FSTNode> theNodes = new ArrayList<FSTNode>();
		for (String fileName : allFiles) {
			if (LanguageManager.getLanguage().isALanguageProgram(fileName)) {
				try {
					FSTNonTerminal node = LanguageManager.getLanguage().parseFile(fileName);
					theNodes.add(node);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		fst2elements.adapt(theNodes);

		bodies_nodes = fst2elements.getBodies_nodes();
		artefact = fst2elements.getProduct();
		// artefact.setId(idProduct);
		this.methodsOfProduct = fst2elements.getBody();
	}

	public HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> getBodies_nodes() {
		return bodies_nodes;
	}

	public HashMap<String, String> getMethodsOfProduct() {
		return methodsOfProduct;
	}

	public static List<String> listFiles(List<String> files, File dir) {
		if (files == null) {
			files = new ArrayList<String>();
		}

		if (!dir.isDirectory()) {
			files.add(dir.toString());
			return files;
		}

		for (File file : dir.listFiles()) {
			listFiles(files, file);
		}
		return files;
	}
}
