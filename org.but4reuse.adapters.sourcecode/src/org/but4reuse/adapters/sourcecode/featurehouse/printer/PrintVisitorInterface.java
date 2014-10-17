package org.but4reuse.adapters.sourcecode.featurehouse.printer;

import java.io.File;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;

public interface PrintVisitorInterface {
	public abstract boolean acceptNode(FSTNode node);
	public abstract void processNode(FSTNode node, File folderPath) throws PrintVisitorException;
}
