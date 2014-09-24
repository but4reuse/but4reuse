package org.but4reuse.adapters;

import java.util.List;
import java.util.Map;

/**
 * IElement of a given software artefact
 * Use AbstractElement default implementation to implement this interface.
 * @author jabier.martinez
 */
public interface IElement {

	/**
	 * similarity method
	 * @param another element
	 * @return a double from 1(equals) to 0(completely different) whether one given element is the same as the current one
	 */
	public double similarity(IElement anotherElement);
	
	/**
	 * Get text
	 * @return String representation of the element
	 */
	public String getText();
	
	/**
	 * This is useful for discovering structural constraints. An element can be owned by other elements or objects. Different ownership relations could exist for the same element. If there is only one relationID just use the same all the time.
	 * @return A Map of relationIDs as keys and the list of owner elements or objects for the element.
	 */
	public Map<Object,List<Object>> getOwners();
	
	/**
	 * Get minimum owners
	 * @param relationID
	 * @return the minimum number of owners that a ownership relation must have
	 */
	public int getMinimumOwners(Object relationID);
	
	/**
	 * Get maximum owners
	 * @param relationID
	 * @return the maximum number of owners that a ownership relation must have
	 */
	public int getMaximumOwners(Object relationID);
}
