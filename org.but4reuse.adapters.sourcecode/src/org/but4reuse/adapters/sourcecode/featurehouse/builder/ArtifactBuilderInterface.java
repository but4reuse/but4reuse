package org.but4reuse.adapters.sourcecode.featurehouse.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.ParseException;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;

public interface ArtifactBuilderInterface {

	public abstract boolean acceptFile(File inputFile);

	public abstract LinkedList<FSTNonTerminal> getFeatures();

	public abstract void setBaseDirectoryName(String baseDirectoryName);

	public abstract String getBaseDirectoryName();

	public abstract void processFile(File inputFile) throws FileNotFoundException, ParseException;

	public abstract void preprocessFile(File file) throws FileNotFoundException;

	public abstract boolean isPreprocessNode();

	public abstract void setPreprocessNode(boolean preprocessNode);
}