package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.utils.BytecodeUtils;
import org.objectweb.asm.tree.MethodNode;

/**
 * An AbstractElement that represents a class method
 */

public class MethodElement extends AbstractElement{
	
	/**
	 * The method's data
	 */
	
	private MethodNode method;
	
	/**
	 * Class name that owns this method
	 */
	
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof MethodElement){
			MethodElement tmp=(MethodElement)anotherElement;
			if(className.equals(tmp.getClassName())&&BytecodeUtils.methodComparator(method, tmp.getMethod()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Method-"+method.name+"-"+method.desc;
	}

	/**
	 * Constructs a new MethodElement
	 * @param method The method
	 * @param className the method's owner
	 */
	
	public MethodElement(MethodNode method, String className) {
		super();
		this.method = method;
		this.className = className;
	}

	/**
	 * Return the method's data
	 * @return The method
	 */
	
	public MethodNode getMethod() {
		return method;
	}

	/**
	 * Return the owner class name
	 * @return The class name
	 */
	
	public String getClassName() {
		return className;
	}
}
