package org.but4reuse.adapters.sourcecode.adapter;

import java.io.FileNotFoundException;

import printer.PrintVisitorException;
import cide.gparser.ParseException;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

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
