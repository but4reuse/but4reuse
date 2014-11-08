package org.but4reuse.adapters.sourcecode.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import printer.PrintVisitorException;
import cide.gparser.OffsetCharStream;
import cide.gparser.ParseException;
import tmp.generated_java15.Java15Parser;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class JavaLanguage implements InterfaceLanguage {

	private static final String PATHOUTPUT="./SPL-Construction-FH-Java-FST";
	@Override
	public boolean isMethod(FSTNode node) {
		
		if(node instanceof FSTTerminal ){
        	FSTTerminal nt = (FSTTerminal)node;
            return  (nt.getType().equals("MethodDecl"))	;
	    
		
		 }
		
		return false;
	}

	@Override
	public boolean isConstructor(FSTNode node) {
		
		if(node instanceof FSTTerminal ){
        	FSTTerminal nt = (FSTTerminal)node;
            return  (nt.getType().equals("ConstructorDecl"))	;
	    
		
		 }
		
		return false;
	}
	
	
public FSTNonTerminal parseFile(String path) throws FileNotFoundException, ParseException, PrintVisitorException{
		
		Java15Parser p = new Java15Parser(new OffsetCharStream( new FileInputStream(path)));
		
		p.CompilationUnit(false);
		
		FSTNonTerminal racine = (FSTNonTerminal)p.getRoot();
		return racine;
	}

@Override
public void generateCode(FSTNode n, String dir, String featName) {
    
	
	String rep =dir+"features";
	File repit = new File(rep);
	// Jabi repit.mkdirs();
    File fDir ;
	String packageName = this.getPackageName((FSTNonTerminal)n);
	String[] pack = packageName.split("\\.");
	
     String path = dir; // // Jabi rep+"/"+featName;

	 for (int x=0; x<pack.length; x++){
	        
	      path=path+"/"+(pack[x]);
	    }
     fDir = new File(path);
	 fDir.mkdirs();
	if (!fDir.exists())
	   fDir.mkdirs();
	
	
	
	JavaPrintVisitor jpv = new JavaPrintVisitor();
	try {
		
		jpv.processNode(n, fDir);
	} catch (PrintVisitorException e) {
		
		e.printStackTrace();
	}
}




@Override
public boolean isALanguageProgram(String absolutePath) {
	
	System.out.println("Test isLanguageProgram :"+absolutePath + "sreult ="+absolutePath.endsWith("java"));
	return absolutePath.endsWith("java");
}



protected String getPackageName(FSTNonTerminal nonTerminal) {
	
	String p ="";
	for (FSTNode node : nonTerminal.getChildren()) {
		if(node.getType().equals("PackageDeclaration")){
			FSTTerminal terminal = (FSTTerminal)node;
			 p = terminal.getBody();
		      break;			 
		}
		
		
	}
	String result="";
     String[] words = p.split("\\s"); //\\s : blanc
    for (int x=0; x<words.length; x++){
        if (!words[x].equals("package"))
        	result=result+(words[x]);
    }
    return result.replace(";", "");
 }


public static void deleteRecursive(File f)  {
	if (f.exists()){
		if (f.isDirectory()){
			File[] childs = f.listFiles();
			int i = 0;
			for(i = 0; i<childs.length; i++){
				deleteRecursive(childs[i]);
			}

		}
		f.delete();
	}
}

@Override
public String getPath() {
	// TODO Auto-generated method stub
	return PATHOUTPUT;
}

@Override
public boolean isImportDec(FSTTerminal terminal) {
	// TODO Auto-generated method stub
	 return  (terminal.getType().equals("ImportDeclaration"))	;
}
}

