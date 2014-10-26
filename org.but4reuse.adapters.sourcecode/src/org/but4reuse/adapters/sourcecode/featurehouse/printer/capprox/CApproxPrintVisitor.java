package org.but4reuse.adapters.sourcecode.featurehouse.printer.capprox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.ArtifactPrintVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;
import org.but4reuse.adapters.sourcecode.featurehouse.tmp.generated_capprox.SimplePrintVisitor;

public class CApproxPrintVisitor extends ArtifactPrintVisitor {
	
	HashMap<FSTNode, String> nameFiles = null;
	public CApproxPrintVisitor() {
		super("C-File");
	}
	public void processNode(FSTNode node, File folderPath) throws PrintVisitorException {
		if(node instanceof FSTNonTerminal) {
			FSTNonTerminal nonterminal = (FSTNonTerminal)node;
			for(FSTNode child : nonterminal.getChildren()) {
				
				String fileName ;
				if (this.nameFiles!=null)
					fileName = folderPath.getPath() + File.separator +
					this.nameFiles.get(nonterminal);		
				
				else  fileName = folderPath.getPath() + File.separator + nonterminal.getName();

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
	public void setNameFiles(HashMap<FSTNode, String>  nf) {
		// TODO Auto-generated method stub
			this.nameFiles=nf;
	}
}
