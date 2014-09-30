package org.but4reuse.adapters;

import java.util.List;
import java.util.Map;

/**
 * IElement of a given software artefact Use AbstractElement default
 * implementation to implement this interface.
 * 
 * @author jabier.martinez
 */
public interface IElement {

	/**
	 * similarity method
	 * 
	 * @param another
	 *            element
	 * @return a double from 1(equals) to 0(completely different) whether one
	 *         given element is the same as the current one
	 */
	public double similarity(IElement anotherElement);

	/**
	 * Get text
	 * 
	 * @return String representation of the element
	 */
	public String getText();

	/**
	 * This is useful for discovering structural constraints. An element can
	 * depend on another ielements or objects. Different dependency relations
	 * could exist for the same element. If there is only one dependencyID do
	 * not overwrite this method in AbstractElement implementation and just use
	 * the addDependency(Object) or addDependencies(List<Object>) method. An
	 * example of when several dependencyID are needed could be a Reference
	 * attribute of a given Class. The dependency can be both the class that
	 * owns the reference and the referenced objects. These different
	 * dependencyIDs can have different min and max values. For example (1,1)
	 * for the Class and (0,*) for the reference.
	 * 
	 * @return A Map of relationIDs as keys and the list of owner elements or
	 *         objects for the element.
	 */
	public Map<Object, List<Object>> getDependencies();

	/**
	 * Get minimum owners. Overwrite this method if they are constant values.
	 * Other option is to use the setter method in the AbstractElement
	 * implementation during the adapt method of the Adapter.
	 * 
	 * @param dependencyID
	 * @return the minimum number of owners that a ownership relation must have
	 */
	public int getMinDependencies(Object dependencyID);

	/**
	 * Get maximum owners
	 * 
	 * @param addDependency
	 *            (Object)
	 * @return the maximum number of owners that a ownership relation must have
	 */
	public int getMaxDependencies(Object dependencyID);
}
