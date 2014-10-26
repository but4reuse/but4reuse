package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.IASTVisitor;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ISourceFile;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;

public interface IASTNode {

	public void accept(IASTVisitor visitor);

	public Property getProperty(String name);

	public ISourceFile getRoot();

	public ASTNode getParent();

	public String getId();

	public int getStartPosition();

	public int getLength();
	
	public ASTNode deepCopy();
}
