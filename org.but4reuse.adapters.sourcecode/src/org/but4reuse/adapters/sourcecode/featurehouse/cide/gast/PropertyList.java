package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import java.util.ArrayList;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyList;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyZeroOrMore;

//import fr.lip6.but4reuse.analysis.constructionprimitives.fst.featurehouse.cide.gast.PropertyType;

/**
 * property like ZeroOrMore, but when the last element is removed, that also the
 * parent is removed. this is used e.g., in the throws clause, where the
 * "throws" keyword is removed once all exceptions are removed.
 * 
 * @author cKaestner
 * 
 * @param <T>
 */
public class PropertyList<T extends ASTNode> extends PropertyZeroOrMore<T> {

	public PropertyList(String name, ArrayList<T> value) {
		super(name, value, PropertyType.LIST);
	}

	public void removeSubtree(ASTNode value) {
		super.removeSubtree(value);
		if (this.valueList.isEmpty())
			removeParent();
		this.valueList.remove(value);
	}

	/**
	 * removes the parent node from it's parent (assume that it is optional!)
	 */
	protected void removeParent() {
		assert parent != null;
		Property parentsLocation = parent.getLocationInParent();
		assert parentsLocation != null;
		assert parentsLocation.canRemoveSubtree(parent) : "Parent must be optional in his parent when using the &LI annotation";
		parentsLocation.removeSubtree(parent);
	}

	@Override
	void setParent(ASTNode parent) {
		super.setParent(parent);
	}

	/**
	 * after cloning the IDs might change (due to renumbering) but are again
	 * consistent inside the AST
	 */
	Property deepCopy() {
		ArrayList<T> clonedList = new ArrayList<T>(valueList.size());
		for (T entry : valueList)
			clonedList.add((T) entry.deepCopy());
		return new PropertyList<T>(new String(name), clonedList);
	}

}
