package org.but4reuse.adapters.sourcecode.featurehouse.printer.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.ArtifactPrintVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;
import org.but4reuse.adapters.sourcecode.featurehouse.tmp.generated_java15.SimplePrintVisitor;

public class JavaPrintVisitor extends ArtifactPrintVisitor {

	public JavaPrintVisitor() {
		super("Java-File");
	}
	public void processNode(FSTNode node, File folderPath) throws PrintVisitorException {
		if(node instanceof FSTNonTerminal) {
			FSTNonTerminal nonterminal = (FSTNonTerminal)node;
			for(FSTNode child : nonterminal.getChildren()) {
				String fileName = folderPath.getPath() + File.separator + nonterminal.getName();

				SimplePrintVisitor visitor;
				try {
					visitor = new SimplePrintVisitor(new PrintStream(fileName));
					visitor.visit((FSTNonTerminal)child);
					visitor.getResult();
				} catch (FileNotFoundException e) {
					throw new PrintVisitorException(e.getMessage());
				}
			}
		} else {
			assert(!(node instanceof FSTNonTerminal));
		}
	}
}
