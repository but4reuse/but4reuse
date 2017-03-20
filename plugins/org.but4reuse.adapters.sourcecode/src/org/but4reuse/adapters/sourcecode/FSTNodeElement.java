package org.but4reuse.adapters.sourcecode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.sourcecode.adapter.LanguageManager;
import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.strings.StringUtils;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;

/**
 * FSTNodeElement
 */
public abstract class FSTNodeElement extends AbstractElement {

	public Path pathToConstructByCopy;

	private FSTNode fstNode;

	private FSTNodeElement parent;

	private String name;

	private String type;

	@Override
	public String getText() {
		String t = "";
		FSTNode parent = fstNode.getParent();
		while (parent != null) {
			parent = parent.getParent();
			t = t + "\t";
		}
		String name = "";
		if (!fstNode.getName().equals("-")) {
			name = fstNode.getName();
		}
		return t + fstNode.getType() + ": " + name;
	}

	public void setNode(FSTNode node) {
		this.fstNode = node;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FSTNode getNode() {
		return fstNode;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public FSTNodeElement getParent() {
		return parent;
	}

	public void setParent(FSTNodeElement parent) {
		this.parent = parent;
	}

	public boolean construct(URI uri) {
		if (fstNode instanceof FSTNonTerminal) {
			// Create a directory to save the Feature
			File a = FileUtils.getFile(uri);
			try {
				FileUtils.createFile(a);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Copy the content
			LanguageManager.getLanguage().generateCode(this.fstNode, a.getAbsolutePath());
		}
		return true;
	}

	@Override
	public double similarity(IElement anotherElement) {
		// Check that they are both NonTerminal, or both Terminal
		if (!(anotherElement.getClass().equals(getClass()))) {
			return 0;
		}

		FSTNodeElement element = (FSTNodeElement) anotherElement;

		// Check same name and type
		if (fstNode.getName().equals(element.getNode().getName())
				&& fstNode.getType().equals(element.getNode().getType())) {
			// Check same package name
			if (element.getNode() instanceof FSTNonTerminal && fstNode instanceof FSTNonTerminal) {
				String anotherElementPackage = LanguageManager.getLanguage()
						.getPackageName((FSTNonTerminal) element.getNode());
				String thisPackageName = LanguageManager.getLanguage().getPackageName((FSTNonTerminal) fstNode);
				if (!anotherElementPackage.equals(thisPackageName)) {
					return 0;
				}
			}

			// Check the same ancestors
			double ancestorsSimilarity = ancestorsSimilarity(element, this);
			return ancestorsSimilarity;
		} else {
			return 0;
		}
	}

	public static double ancestorsSimilarity(FSTNodeElement element_1, FSTNodeElement element_2) {
		// Check same name and type of all the ancestors
		FSTNodeElement element1 = element_1;
		FSTNodeElement element2 = element_2;

		while (element1.getParent() != null) {
			element1 = element1.getParent();
			element2 = element2.getParent();
			// Not the same number of ancestors
			if (element2 == null) {
				return 0;
			}
			// Check something in the ancestor
			if (!element1.getName().equals(element2.getName())
					|| !element1.getNode().getType().equals(element2.getNode().getType())) {
				return 0;
			}
		}
		// Everything was ok
		return 1;
	}

	@Override
	public List<String> getWords() {
		String sub = name;

		// remove parameter types. e.g. getExtension(File-File)
		if (sub.contains("(")) {
			sub = sub.substring(0, sub.indexOf("("));
		}

		// sometimes is just this symbol, so return empty
		if (sub.equals("-")) {
			return new ArrayList<String>();
		}
		// normally is one word, but we still tokenize, e.g. word1_word2
		return StringUtils.tokenizeString(sub);
	}

	@Override
	public int hashCode() {
		// for the moment, they must have the same name and type to have the
		// chance of being equal, so this hashcode improves performance
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

}
