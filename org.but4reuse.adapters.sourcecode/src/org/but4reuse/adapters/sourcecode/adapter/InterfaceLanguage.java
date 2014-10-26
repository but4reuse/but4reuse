package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileNotFoundException;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.ParseException;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTTerminal;
import org.but4reuse.adapters.sourcecode.featurehouse.printer.PrintVisitorException;

public interface InterfaceLanguage {

	public boolean isMethod(FSTNode node);
	public boolean isConstructor(FSTNode node);
	public FSTNonTerminal parseFile(String path) throws FileNotFoundException, ParseException, 
	PrintVisitorException;
	public void generateCode(FSTNode n, String dirPath, String featName);
    public boolean isALanguageProgram(String absolutePath);
    public String getPath();
	public boolean isImportDec(FSTTerminal terminal);
	
	
}
