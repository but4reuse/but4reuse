package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;

public interface IASTVisitor {

	public abstract boolean visit(ASTNode node);

	public abstract void postVisit(ASTNode node);

}