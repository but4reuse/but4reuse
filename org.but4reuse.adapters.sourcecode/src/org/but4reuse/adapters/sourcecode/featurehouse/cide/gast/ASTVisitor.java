package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.IASTVisitor;

public class ASTVisitor implements IASTVisitor {

	/* (non-Javadoc)
	 * @see cide.gast.IASTVisitor#visit(cide.gast.ASTNode)
	 */
	public boolean visit(ASTNode node){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see cide.gast.IASTVisitor#postVisit(cide.gast.ASTNode)
	 */
	public void postVisit(ASTNode node){
		
	}
	
}
