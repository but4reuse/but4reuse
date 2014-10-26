package org.but4reuse.adapters.sourcecode.featurehouse.cide.gast;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ASTNode;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.Property;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyOne;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.PropertyWrapper;

//import fr.lip6.but4reuse.analysis.constructionprimitives.fst.featurehouse.cide.gast.PropertyType;

public class PropertyWrapper<T extends ASTNode, TWrappee extends ASTNode>
		extends Property {

	protected T value;
	private String wrappeeProperty;
	private boolean isRemoved = false;

	public PropertyWrapper(String name, T value, String wrappeeProperty) {
		super(name, PropertyType.ONE);
		this.value = value;
		this.wrappeeProperty = wrappeeProperty;
		this.isRemoved = false;
	}

	public ASTNode getValue() {
		if (!isRemoved)
			return value;
		else
			return getWrappee();
	}
	 
	public TWrappee getWrappee(){
		return ((PropertyOne<TWrappee>) value
				.getProperty(wrappeeProperty)).getValue();
	}

	public void setValue(T value) {
		if (value != null) {
			this.value = value;
			this.isRemoved = false;
		}
	}

	public boolean canRemoveSubtree(ASTNode node) {
		return !isRemoved;
	}

	public void removeSubtree(ASTNode node) {
		if (node == value)
			isRemoved = true;
	}

	void setParent(ASTNode parent) {
		super.setParent(parent);
		value.setParent(parent, this);
	}

	Property deepCopy() {
		return new PropertyWrapper<T, TWrappee>(new String(name), (T) value
				.deepCopy(), new String(wrappeeProperty));
	}

	public ASTNode[] getChildren() {
		return new ASTNode[] { getValue() };
	}
	
	public boolean isWrapper() {
		return true;
	}

}
