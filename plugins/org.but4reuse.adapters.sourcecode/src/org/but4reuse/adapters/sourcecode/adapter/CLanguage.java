package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import printer.PrintVisitorException;
import printer.capprox.CApproxHeaderPrintVisitor;
import printer.capprox.CApproxPrintVisitor;
import tmp.generated_capprox.CApproxParser;
import cide.gparser.OffsetCharStream;
import cide.gparser.ParseException;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class CLanguage implements ILanguage {

	@Override
	public boolean isMethod(FSTNode node) {
		// TODO ???
		if (node instanceof FSTTerminal) {
			FSTTerminal nt = (FSTTerminal) node;
			return (nt.getType().equals("Func"));
		}
		return false;
	}

	@Override
	public boolean isConstructor(FSTNode node) {
		// TODO ???
		return true;
	}

	public FSTNonTerminal parseFile(File path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		CApproxParser parser = new CApproxParser(new OffsetCharStream(fis));

		try {
			parser.TranslationUnit(false);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		FSTNonTerminal root = (FSTNonTerminal) parser.getRoot();
		// Complete the information with the name which is not included by
		// default
		root.setName(path.getName());

		LanguageManager.filesNames.put(root, path.getName());
		return root;
	}

	@Override
	public void generateCode(FSTNode n, String path) {

		File fDir = new File(path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}

		CApproxPrintVisitor cpv = new CApproxPrintVisitor();
		// cpv.setNameFiles( LanguageConfigurator.filesNames);
		try {
			cpv.processNode(n, fDir);
		} catch (PrintVisitorException e) {
			e.printStackTrace();
		}

		try {
			System.out.println("C-Code Generation");
			System.out.println("   " + fDir);
			// System.out.println( n.toString());
			cpv.processNode(n, fDir);
		} catch (PrintVisitorException e) {
			e.printStackTrace();
		}
		CApproxHeaderPrintVisitor cpvh = new CApproxHeaderPrintVisitor();
		// cpvh.setcApproxPrintVisitor(cpv);

		try {
			cpvh.processNode(n, fDir);
		} catch (PrintVisitorException e) {
			e.printStackTrace();
		}

	}

	public boolean isALanguageProgram(String absolutePath) {
		return absolutePath.endsWith(".c") || absolutePath.endsWith(".h");
	}

	@Override
	public boolean isImportDec(FSTTerminal terminal) {
		// TODO ???
		return false;
	}

	@Override
	public String getPackageName(FSTNonTerminal nonTerminal) {
		// TODO ???
		return "";
	}

	@Override
	public String getQualifiedName(FSTNode node) {
		//TODO ???
		return "";
	}

	@Override
	public FSTNode getNodeWithName(FSTNode node, String name) {
		if (node.getName() == name)
			return node;
		if (node instanceof FSTNonTerminal)
			return getNodeWithName((FSTNode) ((FSTNonTerminal) node).getChildren(), name);
		return null;
	}

}
