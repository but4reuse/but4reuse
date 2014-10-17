package org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast;

import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTFeatureNode;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;


public class FSTFeatureNode extends FSTNonTerminal {
	public FSTFeatureNode(String name) {
		super("Feature", name);
	}
	public boolean compatibleWith(FSTNode node) {
		return this.getType().equals(node.getType());
	}
	
    public FSTNode getShallowClone() {
    	return new FSTFeatureNode(getName());
    }
}
