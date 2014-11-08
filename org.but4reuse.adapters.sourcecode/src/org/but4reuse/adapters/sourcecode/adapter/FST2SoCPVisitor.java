package org.but4reuse.adapters.sourcecode.adapter;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.FSTNonTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.FSTTerminalNodeElement;

import de.ovgu.cide.fstgen.ast.AbstractFSTPrintVisitor;
import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import de.ovgu.cide.fstgen.ast.FSTTerminal;

public class FST2SoCPVisitor extends AbstractFSTPrintVisitor  {
	
	 public ArrayList<String> constructions = new ArrayList<String>();
    private HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> bodies_nodes = 
    			new HashMap<String, ArrayList<HashMap<String, FSTTerminal>>>(); 
	
    public HashMap<String, ArrayList<HashMap<String, FSTTerminal>>> getBodies_nodes() {
		return bodies_nodes;
  
	}
    
   
	
    private List<IElement> product = new ArrayList<IElement>();
    
    private HashMap<String,  String> body = new HashMap<String,  String>();
	
	
	public HashMap<String, String> getBody() {
		return body;
	}
	

	public FST2SoCPVisitor(PrintStream out) {
		super(out); generateSpaces=true;
	}
	public FST2SoCPVisitor() {
		super(); generateSpaces=true;
		
		
	}
	
	public boolean visit(ArrayList<FSTNode> nodes){
		
		
		for (FSTNode node:nodes)
			 visit(node);
		
		return true;
	}
	
	public boolean visit(FSTNode theNodes) {
		
		//System.out.println(theNodes.getClass().getName());
		if (theNodes instanceof FSTNonTerminal){
			
			FSTNonTerminal nonTerminal = (FSTNonTerminal)theNodes;
			String key = nonTerminal.getName()+nonTerminal.getType();
		   // if (!this.constructions.contains(key)){
		    	this.constructions.add(key);
			   
		    	FSTNonTerminalNodeElement fstNt = new FSTNonTerminalNodeElement();
		    	
		    	fstNt.setName(nonTerminal.getName());
		    	fstNt.setType(nonTerminal.getType());
		    	fstNt.setNode(nonTerminal);
		        product.add(fstNt);
		        for (FSTNode n:nonTerminal.getChildren()){
		        		visit(n);
		        }
		      }
		
		
		
		if (theNodes instanceof FSTTerminal){
			
			
			FSTTerminal terminal = (FSTTerminal)theNodes;
			
		 //negliger les declarations de packages.
			if (!LanguageConfigurator.getLanguage().isImportDec(terminal)
					&& !terminal.getType().equals("InitializerDecl")
					&& !terminal.getName().contains("auto")) {
			
			String key = terminal.getName()+terminal.getType();
		    //if (!this.constructions.contains(key)){
		    	this.constructions.add(key);
			if(LanguageConfigurator.getLanguage().isMethod(terminal) || 
					(LanguageConfigurator.getLanguage().isConstructor(terminal))){
				
				String methodName = terminal.getName();
				String methodBody = terminal.getBody();
				
				body.put(methodName, deleteteComments(methodBody));
				
				
				addToBodiesNodes(methodName, deleteteComments(methodBody), terminal);
				
			}
			
		     FSTTerminalNodeElement fstTN = new FSTTerminalNodeElement();
		     
			 
		   
		     fstTN.setName(terminal.getName());
		     fstTN.setType(terminal.getType());
		     fstTN.setNode(terminal);
		     product.add(fstTN);
		        
		   }
			
		}
		
		
		
		return true;
	}
	private void addToBodiesNodes(String methodName, String methodBody,
			FSTTerminal terminal) {
		
		if (bodies_nodes.containsKey(methodName)){
			
			ArrayList<HashMap<String, FSTTerminal>> b= bodies_nodes.get(methodBody);
			for(HashMap<String, FSTTerminal> bb:b){
				
				if (bb.containsKey(methodBody))
					return;
			}
			//new body
			HashMap<String, FSTTerminal> newB = new HashMap<String, FSTTerminal>();
			newB.put(methodBody, terminal);
			b.add(newB);
		}	
		else {
			HashMap<String, FSTTerminal> newB = new HashMap<String, FSTTerminal>();
			newB.put(methodBody, terminal);
			ArrayList<HashMap<String, FSTTerminal>> newA = new ArrayList<HashMap<String, FSTTerminal>>();
			newA.add(newB);
			bodies_nodes.put(methodBody, newA);
				
			}
			
		}
		
	private static String deleteteComments(String test) {
		String[] result = test.split("\n");
		String chaine="";
		int x=0;
	    while (x<result.length){
	       	
	    	//System.out.println("Element :"+x + " :"+result[x]);
	    	
	    	if (result[x].startsWith("/*")) {
	    	    x++;	
	    		while ((x<result.length)&& !result[x].replaceAll("\\s",  "").startsWith("*/")){
	    			x++;
	    		}
	    		x++;
	    	}
	       	if ((x<result.length) &&!result[x].replaceAll("\\s",  "").startsWith("//")) {
	          chaine=chaine+result[x]+"\n";
	         
	    	}
	    	x++;
	    //	System.out.println("Iteration :"+x+ "  "+ chaine);
	    }
	      
	    return chaine.replaceAll("\\s", "") ;   
	    // return chaine.replaceAll("\\s", "");
		
		
	}
	
	@Override
	protected boolean isSubtype(String type, String expectedType) {
		// TODO Auto-generated method stub
		return false;
	}
	public List<IElement> getProduct() {
		// TODO Auto-generated method stub
		return product;
	}
	
}
