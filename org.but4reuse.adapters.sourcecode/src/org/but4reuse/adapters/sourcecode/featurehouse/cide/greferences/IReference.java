package org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.greferences.IReferenceType;

public interface IReference {

	ASTNode getSource();

	ASTNode getTarget();

	IReferenceType getType();

}
