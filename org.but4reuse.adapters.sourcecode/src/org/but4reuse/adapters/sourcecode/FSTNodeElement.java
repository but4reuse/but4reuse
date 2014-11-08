package org.but4reuse.adapters.sourcecode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.sourcecode.adapter.LanguageConfigurator;
import org.but4reuse.utils.files.FileUtils;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;

public abstract class FSTNodeElement extends AbstractElement {

	// private static URI relativeURI;

	public Path pathToConstructByCopy;

	private FSTNode node;

	private String name;

	private String type;

	public boolean construct(URI uri) {
		if (node instanceof FSTNonTerminal) {
			// Create a directory to save the Feature
			File a = FileUtils.getFile(uri);
			try {
				FileUtils.createFile(a);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Copy the content
			LanguageConfigurator.getLanguage().generateCode(this.node, a.getAbsolutePath(), "f1");
		}
		return true;
	}

	@Override
	public String getText() {
		return getName() + " " + getType();
	}

	public void setNode(FSTNode node) {
		this.node = node;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FSTNode getNode() {
		return node;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

}
