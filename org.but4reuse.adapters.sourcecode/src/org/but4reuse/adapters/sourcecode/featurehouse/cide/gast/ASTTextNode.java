package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import java.util.ArrayList;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTTextNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.IToken;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;

public class ASTTextNode extends ASTNode {
	private String[] value;

	public ASTTextNode(String value, IToken token) {
		super(new ArrayList<Property>(), token, token);
		this.value = new String[] { value };
	}

	public ASTTextNode(String[] value, IToken token) {
		super(new ArrayList<Property>(), token, token);
		this.value = value;
	}

	public String getValue() {
		String result = "";
		for (int idx = 0; idx < value.length; idx++) {
			if (idx != 0)
				result += ",";
			result += value[idx];
		}
		return result;
	}

	public String toString() {
		return getValue();
	}

	@Override
	public ASTNode deepCopy() {
		return new ASTTextNode(value, firstToken);
	}
}
