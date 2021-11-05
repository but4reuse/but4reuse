package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * An AbstractElement that represents the class's superclass
 */

public class SuperClassNameElement extends AbstractElement{
	
	/**
	 * The name of the superclass
	 */
	
	private String superClassName;
	
	/**
	 * Class name that owns this field
	 */
	
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof SuperClassNameElement){
			SuperClassNameElement tmp=(SuperClassNameElement)anotherElement;
			if(className.equals(tmp.getClassName())&&superClassName.equals(tmp.getSuperClassName()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-SuperClass-"+superClassName;
	}
	
	/**
	 * Constructs a new SuperClassNameElement
	 * @param superClassName the name of the superclass
	 * @param className the class name
	 */
	
	public SuperClassNameElement(String superClassName, String className) {
		super();
		this.superClassName = superClassName;
		this.className = className;
	}

	/**
	 * Return the superclass name
	 * @return the name of the superClass
	 */
	
	public String getSuperClassName() {
		return superClassName;
	}

	/**
	 * Return the class name that extends this? superclass
	 * @return className the class name
	 */
	
	public String getClassName() {
		return className;
	}
}
