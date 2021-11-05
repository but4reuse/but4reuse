package org.but4reuse.adapters.bytecode;

import org.but4reuse.adapters.IDependencyObject;

/**
 * Class that prevents to take a method without methods and fields used in it.
 * Currently not working
 *
 */

public class RequiredDependencyObject implements IDependencyObject{
	
	/**
	 * nameDependency is used to find needed method or field 
	 */
	
	private NameDependencyObject nameDependency;
	
	/**
	 * Constructor that create a new NameDependencyObject
	 * @param dependencyName The name of the dependency
	 */
	
	public RequiredDependencyObject(String dependencyName) {
		// TODO Auto-generated constructor stub
		nameDependency=new NameDependencyObject(dependencyName);
	}
	
	@Override
	public int getMinDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getMaxDependencies(String dependencyID) {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public String getDependencyObjectText() {
		// TODO Auto-generated method stub
		return nameDependency.getDependencyObjectText()+" is required";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NameDependencyObject)
			return nameDependency.equals((NameDependencyObject) obj);
		return super.equals(obj);
	}
}
