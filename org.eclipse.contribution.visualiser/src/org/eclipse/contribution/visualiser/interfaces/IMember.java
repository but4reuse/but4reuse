/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.interfaces;

/**
 * IMember is the interface that members of the view should implement. Members
 * of a view have the following characteristics:
 * 
 * - Name e.g. "Apple" - Fullname e.g. "Fruits.Apple" - Tooltip text e.g.
 * "A green fruit that grows on trees" - Size e.g. 20 - Containing group
 * reference e.g. "GROUP:Fruits"
 */
public interface IMember {

	void setName(String string);

	/**
	 * Get this member's name
	 * 
	 * @return this member's name
	 */
	String getName();

	/**
	 * Get the full (or fully qualified) name for this member
	 * 
	 * @return the full name
	 */
	String getFullname();

	/**
	 * Set the tooltip
	 * 
	 * @param tooltip
	 *            - the tooltip
	 */
	void setTooltip(String tooltip);

	/**
	 * Get the tooltip
	 * 
	 * @return the tooltip
	 */
	String getToolTip();

	/**
	 * Set the size of this member
	 * 
	 * @param size
	 *            - the size
	 */
	void setSize(int size);

	/**
	 * Get the size
	 * 
	 * @return the size
	 */
	Integer getSize();

	/**
	 * Get the group which contains this member
	 * 
	 * @return the group which contains this member
	 */
	IGroup getContainingGroup();

	/**
	 * Set the group which contains this member
	 * 
	 * @param grp
	 */
	void setContainingGroup(IGroup grp);

}
