package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReference;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReferenceType;

public class Reference implements IReference {

	private ASTNode target;
	private ASTNode source;
	private IReferenceType type;

	public Reference(IReferenceType referenceType, ASTNode source,
			ASTNode target) {
		this.type = referenceType;
		this.source = source;
		this.target = target;
	}

	public ASTNode getSource() {
		return source;
	}

	public ASTNode getTarget() {
		return target;
	}

	public IReferenceType getType() {
		return type;
	}

}
