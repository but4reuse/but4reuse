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
			// System.out.println("CHILD of :"+nonterminal.getType()+
			// " "+nonterminal.getChildren().size());
			// for(FSTNode child : nonterminal.getChildren()) {
			String fileName = folderPath.getPath() + File.separator + nonterminal.getName() + ".java";

			SimplePrintVisitor visitor;
			try {
				visitor = new SimplePrintVisitor(new PrintStream(fileName));
				// System.out.println("Prinitng File :"+fileName);
				visitor.visit((nonterminal));
				// if (child instanceof FSTNonTerminal)
				// visitor.visit((FSTNonTerminal)child);
				// if (child instanceof FSTTerminal)
				// visitor.visit((FSTTerminal)child);
				visitor.getResult();

			} catch (FileNotFoundException e) {
				throw new PrintVisitorException(e.getMessage());
			}
			// }
		} else {
			assert (!(node instanceof FSTNonTerminal));
		}
	}

}
