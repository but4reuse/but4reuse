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
package org.eclipse.contribution.visualiser.simpleImpl;

import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;

/**
 * Simple implementation of a member. See the IMember interface for more
 * information.
 */
public class SimpleMember implements IMember {

	protected String name;
	protected String tooltip;
	protected Integer size;
	protected String fullname;
	protected IGroup containingGroup;

	/**
	 * The constructor. Takes the member's name as an argument.
	 * 
	 * @param s
	 */
	public SimpleMember(String s) {
		name = s;
		setSize(10); // Default size (no special significance to it being 10)
	}

	/**
	 * Get the String representation of this member. Contains the member's full
	 * name and size.
	 */
	public String toString() {
		return VisualiserMessages.SimpleMember + ":[" //$NON-NLS-1$
				+ fullname + "] " + VisualiserMessages.Size //$NON-NLS-1$
				+ ":[" + size.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void setFullName(String fullName) {
		fullname = fullName;
	}

	/**
	 * Get the full name for this member
	 */
	public String getFullname() {
		if (fullname == null) {
			if (containingGroup != null && containingGroup.getFullname() != null) {
				fullname = containingGroup.getFullname() + "." + name; //$NON-NLS-1$
			} else {
				fullname = name;
			}
		}
		return fullname;
	}

	/**
	 * Set the tooltip for this member
	 */
	public void setTooltip(String string) {
		tooltip = string;
	}

	/**
	 * Get the tooltip for this member. Returns the member's name if an
	 * alternative tooltip has not been set.
	 */
	public String getToolTip() {
		if (tooltip == null || tooltip.length() == 0)
			return getFullname();
		return tooltip;
	}

	/**
	 * Set the name for this member
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Get this member's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set this member's size
	 */
	public void setSize(int s) {
		size = new Integer(s);
	}

	/**
	 * Get the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Get the IGroup which contains this member
	 */
	public IGroup getContainingGroup() {
		return containingGroup;
	}

	/**
	 * Store the IGroup which contains this member
	 */
	public void setContainingGroup(IGroup grp) {
		containingGroup = grp;
	}

}
