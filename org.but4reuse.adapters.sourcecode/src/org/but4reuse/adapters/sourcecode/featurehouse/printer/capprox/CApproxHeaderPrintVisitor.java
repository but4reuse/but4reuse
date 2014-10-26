package org.but4reuse.adapters.sourcecode.featurehouse.printer.capprox;

import java.io.File;
import java.util.HashMap;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.ArtifactPrintVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.capprox.CApproxPrintVisitor;

public class CApproxHeaderPrintVisitor extends ArtifactPrintVisitor {
   private CApproxPrintVisitor cApproxPrintVisitor = new CApproxPrintVisitor();

   public void setcApproxPrintVisitor(CApproxPrintVisitor cApproxPrintVisitor) {
		this.cApproxPrintVisitor = cApproxPrintVisitor;
	}

	
	public CApproxHeaderPrintVisitor(){
	super ("H-File");
   }

   @Override
   public void processNode(FSTNode node, File folderPath)
	    throws PrintVisitorException {
	cApproxPrintVisitor.processNode(node, folderPath);
	
   }

}
