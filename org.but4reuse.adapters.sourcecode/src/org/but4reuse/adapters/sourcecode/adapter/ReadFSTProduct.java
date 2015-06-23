package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;

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

	public void readProduct(URI uriToExplore) {
		File file = FileUtils.getFile(uriToExplore);
		List<File> allFiles = FileUtils.getAllFiles(file);
		FST2ElementsAdapter fst2elements = new FST2ElementsAdapter();
		List<FSTNode> theNodes = new ArrayList<FSTNode>();
		for (File f : allFiles) {
			String fileName = f.toString();
			if (LanguageManager.getLanguage().isALanguageProgram(fileName)) {
				FSTNonTerminal node = LanguageManager.getLanguage().parseFile(f);
				// node is null when there was an exception or error in the
				// parsing
				if (node != null) {
					// TODO report parsing errors to the user, not only console!
					theNodes.add(node);
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
}
