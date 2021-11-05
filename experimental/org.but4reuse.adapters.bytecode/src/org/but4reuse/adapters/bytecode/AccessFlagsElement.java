package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;


/**
 * An AbstractElement that contains the class's access flag
 */

public class AccessFlagsElement extends AbstractElement{
	
	/**
	 * The access flag
	 */
	
	private int accessFlags;
	
	/**
	 * The class name
	 */
	
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof AccessFlagsElement){
			AccessFlagsElement tmp=(AccessFlagsElement)anotherElement;
			if(className.equals(tmp.getClassName())&&accessFlags==tmp.getAccessFlags()){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-AccessFlags-"+accessFlags;
	}

	/**
	 * Construct a new AccessFlagsElement
	 * @param accessFlags The access flag
	 * @param className The name of the class
	 */
	
	public AccessFlagsElement(int accessFlags, String className) {
		super();
		this.accessFlags = accessFlags;
		this.className = className;
	}

	/**
	 * Return the access flag
	 * @return The access flag
	 */
	
	public int getAccessFlags() {
		return accessFlags;
	}

	/**
	 * Return the name of the class
	 * @return The name of the class
	 */
	
	public String getClassName() {
		return className;
	}
}
