package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileNotFoundException;

import printer.PrintVisitorException;
import cide.gparser.ParseException;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public interface ILanguage {

	/**
	 * Parse the file and convert it to an FST
	 * 
	 * @param path
	 * @return an FST
	 * @throws FileNotFoundException
	 * @throws ParseException
	 * @throws PrintVisitorException
	 */
	public FSTNonTerminal parseFile(File file);

	/**
	 * Check if it corresponds to a method
	 * 
	 * @param node
	 * @return
	 */
	public boolean isMethod(FSTNode node);

	/**
	 * Check if it corresponds to constructor method
	 * 
	 * @param node
	 * @return
	 */
	public boolean isConstructor(FSTNode node);

	/**
	 * Check if it is a import declaration
	 * 
	 * @param terminal
	 * @return
	 */
	public boolean isImportDec(FSTTerminal terminal);

	/**
	 * Generate the code of a given node
	 * 
	 * @param n
	 * @param dirPath
	 */
	public void generateCode(FSTNode n, String dirPath);

	/**
	 * Check if a concrete file can be parsed
	 * 
	 * @param absolutePath
	 * @return true if it can be parsed
	 */
	public boolean isALanguageProgram(String absolutePath);

	/**
	 * Get package name
	 * 
	 * @param node
	 * @return package name or empty string
	 */
	public String getPackageName(FSTNonTerminal node);

	/**
	 * Get qualified name
	 * 
	 * @param node
	 * @return qualified name or empty string
	 */
	public String getQualifiedName(FSTNode node);

	/**
	 * Get FSTNode using the nodes tree from a node.
	 * 
	 * @param nonTerminal
	 * @return FSTNode or null
	 */
	public FSTNode getNodeWithName(FSTNode node, String name);

}
