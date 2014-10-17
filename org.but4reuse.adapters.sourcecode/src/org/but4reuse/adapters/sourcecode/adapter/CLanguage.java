package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.OffsetCharStream;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.ParseException;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.*;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.capprox.CApproxHeaderPrintVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.capprox.CApproxPrintVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.tmp.generated_capprox.CApproxParser;
import org.but4reuse.adapters.sourcecode.featurehouse.tmp.generated_java15.Java15Parser;


public class CLanguage implements InterfaceLanguage {

	private static final String PATHOUTPUT="./SPL-Construction-FH-C-FST/";
	
	@Override
	public boolean isMethod(FSTNode node) {
		// TODO Auto-generated method stub
		if(node instanceof FSTTerminal ){
        	FSTTerminal nt = (FSTTerminal)node;
            return  (nt.getType().equals("Func"))	;
	    
		
		 }
		
		return false;
	}

	@Override
	public boolean isConstructor(FSTNode node) {
		// TODO Auto-generated method stub
		return true	;
	    
		
		
	}
	
	
public FSTNonTerminal parseFile(String path) throws FileNotFoundException, ParseException, PrintVisitorException{
		
	File file = new File(path);
	
	
	CApproxParser parser = new CApproxParser(new OffsetCharStream( new FileInputStream(path)));
		
	parser.TranslationUnit(false);
		
	FSTNonTerminal racine = (FSTNonTerminal)parser.getRoot();
	
	//System.out.println(racine.toString());
	 LanguageConfigurator.filesNames.put(racine, file.getName());
	return racine;
	}

@Override
public void generateCode(FSTNode n, String dir, String featName) {
	
	String rep  = dir+"features";
	File repit = new File(rep);
	repit.mkdirs();
    File fDir ;
	
	
     String path = rep+"/"+featName;

	 
     fDir = new File(path);
	 fDir.mkdirs();
	if (!fDir.exists())
	   fDir.mkdirs();
	
	
	
	CApproxPrintVisitor  cpv = new CApproxPrintVisitor ();
	cpv.setNameFiles( LanguageConfigurator.filesNames);
	
	try {
		System.out.println ("C-Code Generation");
        System.out.println ("   "+fDir);
       // System.out.println(  n.toString());
		cpv.processNode(n, fDir);
	} catch (PrintVisitorException e) {
		
		e.printStackTrace();
	}
	CApproxHeaderPrintVisitor cpvh = new CApproxHeaderPrintVisitor();
	cpvh.setcApproxPrintVisitor(cpv);
	try {
		cpvh.processNode(n, fDir);
	} catch (PrintVisitorException e) {
		
		e.printStackTrace();
	}
	
}


public boolean isALanguageProgram(String absolutePath) {
	// TODO Auto-generated method stub
	return absolutePath.endsWith(".c") ||
			absolutePath.endsWith(".h");
}

@Override
public String getPath() {
	return PATHOUTPUT;
}

@Override
public boolean isImportDec(FSTTerminal terminal) {
	// TODO Auto-generated method stub
	return false;
}


}
