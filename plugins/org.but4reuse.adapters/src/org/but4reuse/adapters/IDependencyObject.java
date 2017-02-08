package org.but4reuse.adapters;

public interface IDependencyObject {

	/**
	 * Get minimum owners. Overwrite this method if they are constant values.
	 * Other option is to use the setter method in the AbstractElement
	 * implementation during the adapt method of the Adapter.
	 * 
	 * @param dependencyID
	 * @return the minimum number of owners that a ownership relation must have
	 */
	public int getMinDependencies(String dependencyID);

	/**
	 * Get maximum owners
	 * 
	 * @param addDependency
	 *            (String)
	 * @return the maximum number of owners that a ownership relation must have
	 */
	public int getMaxDependencies(String dependencyID);

	/**
	 * Get dependency object text
	 * 
	 * @return
	 */
	public String getDependencyObjectText();

}
