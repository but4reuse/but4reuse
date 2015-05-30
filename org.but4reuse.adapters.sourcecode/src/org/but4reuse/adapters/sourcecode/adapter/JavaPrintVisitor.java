package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import printer.ArtifactPrintVisitor;
import printer.PrintVisitorException;
import tmp.generated_java15.SimplePrintVisitor;

public class JavaPrintVisitor extends ArtifactPrintVisitor {

	public JavaPrintVisitor() {
		super("Java-File");
	}

	public void processNode(FSTNode node, File folderPath) throws PrintVisitorException {
		if (node instanceof FSTNonTerminal) {
			FSTNonTerminal nonterminal = (FSTNonTerminal) node;
			String fileName = folderPath.getPath() + File.separator + nonterminal.getName() + ".java";
			SimplePrintVisitor visitor;
			try {
				visitor = new SimplePrintVisitor(new PrintStream(fileName));
				visitor.visit((nonterminal));
				visitor.getResult();
			} catch (FileNotFoundException e) {
				throw new PrintVisitorException(e.getMessage());
			}
		} else {
			assert (!(node instanceof FSTNonTerminal));
		}
	}
}
