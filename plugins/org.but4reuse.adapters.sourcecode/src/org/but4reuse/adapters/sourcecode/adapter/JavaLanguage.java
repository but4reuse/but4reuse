package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import printer.PrintVisitorException;
import tmp.generated_java15.Java15Parser;
import cide.gparser.OffsetCharStream;
import cide.gparser.ParseException;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class JavaLanguage implements ILanguage {

	@Override
	public boolean isMethod(FSTNode node) {
		if (node instanceof FSTTerminal) {
			FSTTerminal nt = (FSTTerminal) node;
			return (nt.getType().equals("MethodDecl"));
		}
		return false;
	}

	@Override
	public boolean isConstructor(FSTNode node) {
		if (node instanceof FSTTerminal) {
			FSTTerminal nt = (FSTTerminal) node;
			return (nt.getType().equals("ConstructorDecl"));
		}
		return false;
	}

	public FSTNonTerminal parseFile(File path) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		Java15Parser p = new Java15Parser(new OffsetCharStream(fis));
		try {
			p.CompilationUnit(false);
		} catch (ParseException e) {
			System.err.println(path);
			e.printStackTrace();
			return null;
		} catch (Error error) {
			// like cide.gparser.TokenMgrError
			System.err.println(path);
			error.printStackTrace();
			return null;
		}
		FSTNonTerminal root = (FSTNonTerminal) p.getRoot();
		return root;
	}

	@Override
	public void generateCode(FSTNode n, String path) {
		String packageName = this.getPackageName((FSTNonTerminal) n);
		String[] pack = packageName.split("\\.");
		for (int x = 0; x < pack.length; x++) {
			path = path + "/" + (pack[x]);
		}
		File fDir = new File(path);
		if (!fDir.exists()) {
			fDir.mkdirs();
		}
		JavaPrintVisitor jpv = new JavaPrintVisitor();
		try {
			jpv.processNode(n, fDir);
		} catch (PrintVisitorException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isALanguageProgram(String absolutePath) {
		return absolutePath.endsWith("java");
	}

	@Override
	public String getPackageName(FSTNonTerminal nonTerminal) {

		String p = "";
		for (FSTNode node : nonTerminal.getChildren()) {
			if (node.getType().equals("PackageDeclaration")) {
				FSTTerminal terminal = (FSTTerminal) node;
				p = terminal.getBody();
				break;
			}
		}
		String result = "";
		String[] words = p.split("\\s"); // \\s : whitespace
		for (int x = 0; x < words.length; x++) {
			if (!words[x].equals("package"))
				result = result + (words[x]);
		}
		return result.replace(";", "");
	}

	@Override
	public boolean isImportDec(FSTTerminal terminal) {
		return (terminal.getType().equals("ImportDeclaration"));
	}

	@Override
	public String getQualifiedName(FSTNode node) {
		String qualifiedName = "";
		if (node == null) {
			return "";
		}
		if (node.getParent() == null && node instanceof FSTNonTerminal)
			qualifiedName = this.getPackageName((FSTNonTerminal) node);
		else {
			qualifiedName = this.getQualifiedName((FSTNonTerminal) node.getParent());
			if (!qualifiedName.equals("")) {
				qualifiedName += ".";
			}
			qualifiedName += node.getName();
		}
		return qualifiedName;
	}

	public FSTNode getNodeWithName(FSTNode node, String name) {
		if (node.getName() == name)
			return node;
		if (node instanceof FSTNonTerminal)
			return getNodeWithName((FSTNode) ((FSTNonTerminal) node).getChildren(), name);
		return null;

	}

}
