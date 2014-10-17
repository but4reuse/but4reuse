package org.but4reuse.adapters.sourcecode;

import org.but4reuse.adapters.IElement;



public class FSTNonTerminalNodeElement extends FSTNodeElement  {

	@Override
	public String getText() {
		return getName()+" "+getType();
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (!(anotherElement instanceof FSTNonTerminalNodeElement)) return 0;
		
	  	   FSTNonTerminalNodeElement nonTerminalCP = (FSTNonTerminalNodeElement) anotherElement;
			
	  	   if(this.getName().equals(nonTerminalCP.getNode().getName())&&
	  			   this.getNode().getType().equals(nonTerminalCP.getNode().getType())){
	  		   return 1;
	  	   } else {
	  		   return 0;
	  	   }
	}

}
