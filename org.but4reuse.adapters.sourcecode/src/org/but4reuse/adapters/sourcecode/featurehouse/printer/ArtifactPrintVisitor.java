package org.but4reuse.adapters.sourcecode.featurehouse.printer;

import java.io.File;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorInterface;

public abstract class ArtifactPrintVisitor implements PrintVisitorInterface {
	
	private String suffix;
	
	public ArtifactPrintVisitor(String suffix) {
		this.suffix = suffix;
	}
	
	protected String getSuffix() {
		return suffix;
	}

	public boolean acceptNode(FSTNode node) {
		if (node.getType().equals(getSuffix())) 
			return true;
		else
			return false;
	}
	
	public abstract void processNode(FSTNode node, File folderPath) throws PrintVisitorException;

}