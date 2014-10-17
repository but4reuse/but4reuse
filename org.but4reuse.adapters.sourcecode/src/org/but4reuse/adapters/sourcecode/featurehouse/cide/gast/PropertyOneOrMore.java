package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import java.util.ArrayList;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyOneOrMore;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyZeroOrMore;

public class PropertyOneOrMore<T extends ASTNode> extends PropertyZeroOrMore<T> {

	public PropertyOneOrMore(String name, ArrayList<T> value) {
		super(name, value);
	}

	public void removeSubtree(ASTNode value) {
		if (this.valueList.indexOf(value) != 0)
			super.removeSubtree(value);
	}

	public boolean canRemoveSubtree(ASTNode node) {
		return this.valueList.indexOf(node) != 0;
	}

	/**
	 * after cloning the IDs might change (due to renumbering) but are again
	 * consistent inside the AST
	 */
	Property deepCopy() {
		ArrayList<T> clonedList = new ArrayList<T>(valueList.size());
		for (T entry : valueList)
			clonedList.add((T) entry.deepCopy());
		return new PropertyOneOrMore<T>(new String(name), clonedList);
	}

}
