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
		// TODO Auto-generated method stub
		if (node instanceof FSTTerminal) {
			FSTTerminal nt = (FSTTerminal) node;
			return (nt.getType().equals("Func"));

		}

		return false;
	}

	@Override
	public boolean isConstructor(FSTNode node) {
		// TODO Auto-generated method stub
		return true;

	}

	public FSTNonTerminal parseFile(String path) throws FileNotFoundException, ParseException, PrintVisitorException {

		File file = new File(path);

		CApproxParser parser = new CApproxParser(new OffsetCharStream(new FileInputStream(path)));

		parser.TranslationUnit(false);

		FSTNonTerminal racine = (FSTNonTerminal) parser.getRoot();

		// System.out.println(racine.toString());
		LanguageManager.filesNames.put(racine, file.getName());
		return racine;
	}

	@Override
	public void generateCode(FSTNode n, String dir) {

		String rep = dir + "features";
		File repit = new File(rep);
		repit.mkdirs();
		File fDir;

		String path = rep; // +"/"+featName;

		fDir = new File(path);
		fDir.mkdirs();
		if (!fDir.exists())
			fDir.mkdirs();

		CApproxPrintVisitor cpv = new CApproxPrintVisitor();
		// cpv.setNameFiles( LanguageConfigurator.filesNames);

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

}
