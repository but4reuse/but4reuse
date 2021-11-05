package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;

/**
 * An AbstractElement that contains the name of an implemented interface
 */

public class InterfaceNameElement extends AbstractElement{
	
	/**
	 * The interface's name
	 */
	
	private String interfaceName;
	
	/**
	 * Class name that implements this interface
	 */
	
	private String className;
	
	@Override
	public double similarity(IElement anotherElement) {
		// TODO Auto-generated method stub
		if(anotherElement instanceof InterfaceNameElement){
			InterfaceNameElement tmp=(InterfaceNameElement)anotherElement;
			if(className.equals(tmp.getClassName())&&interfaceName.equals(tmp.getInterfaceName()))
				return 1;
		}
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return className+"-Interface-"+interfaceName;
	}

	/**
	 *  Construct a new InterfaceNameElement
	 * @param interfaceName The interface name
	 * @param className The class name
	 */
	
	public InterfaceNameElement(String interfaceName, String className) {
		super();
		this.interfaceName = interfaceName;
		this.className = className;
	}

	/**
	 * Return the interface name
	 * @return The interface name
	 */
	
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * Return the owner class name
	 * @return The class name 
	 */
	
	public String getClassName() {
		return className;
	}
}
