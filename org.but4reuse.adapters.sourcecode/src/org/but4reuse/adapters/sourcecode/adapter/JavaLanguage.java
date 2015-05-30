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

	public FSTNonTerminal parseFile(String path) throws FileNotFoundException, ParseException, PrintVisitorException {
		Java15Parser p = new Java15Parser(new OffsetCharStream(new FileInputStream(path)));
		p.CompilationUnit(false);
		FSTNonTerminal root = (FSTNonTerminal) p.getRoot();
		return root;
	}

	@Override
	public void generateCode(FSTNode n, String path) {
		;
		String packageName = this.getPackageName((FSTNonTerminal) n);
		String[] pack = packageName.split("\\.");
		for (int x = 0; x < pack.length; x++) {
			path = path + "/" + (pack[x]);
		}
		File fDir = new File(path);
		// fDir.mkdirs();
		if (!fDir.exists()){
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
	
}
