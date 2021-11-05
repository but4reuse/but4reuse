package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IDependencyObject;

/**
 * Class that prevents to take two fields with the same name, two methods with the same name and signature, two same interfaces and two superclasses.
 */

public class NameDependencyObject implements IDependencyObject{
	
	/**
	 * The name that defines the owner of the NameDependencyObject
	 */
	
	private String name;
	
	/**
	 * Construct a new NameDependencyObject
	 * @param name The name that defines the owner
	 */
	
	public NameDependencyObject(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
	}

	@Override
	public int getMinDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String getDependencyObjectText() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NameDependencyObject)
			return name.equals(((NameDependencyObject) obj).name);
		return super.equals(obj);
	}
}
