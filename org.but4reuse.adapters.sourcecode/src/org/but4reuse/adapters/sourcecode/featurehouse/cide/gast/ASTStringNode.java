package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import java.util.ArrayList;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTStringNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.IToken;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;

public class ASTStringNode extends ASTNode {
	private String value;

	public ASTStringNode(String value, IToken token) {
		super(new ArrayList<Property>(), token, token);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return value;
	}

	@Override
	public ASTNode deepCopy() {
		return new ASTStringNode(new String(value), firstToken);
	}
}
