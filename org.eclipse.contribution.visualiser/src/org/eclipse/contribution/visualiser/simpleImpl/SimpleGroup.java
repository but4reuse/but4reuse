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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;

/**
 * Simple implementation of a group, see IGroup/IMember for more information.
 */
public class SimpleGroup implements IGroup {

	protected String name;
	protected String tooltip;
	protected List kids = new ArrayList();

	/**
	 * The constructor - takes the group's name as an argument
	 * 
	 * @param n
	 *            - name
	 */
	public SimpleGroup(String n) {
		name = n;
	}

	/**
	 * Get the full name for the group. In this implementation this is the same
	 * as the name.
	 */
	public String getFullname() {
		return name;
	}

	/**
	 * Add a member to this group
	 */
	public void add(IMember m) {
		kids.add(m);
		m.setContainingGroup(this);
	}

	/**
	 * Get all the members contained in this group
	 */
	public List getMembers() {
		return kids;
	}

	/**
	 * Set the name of this group
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Get the name of this group
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the tooltip for this group
	 */
	public void setTooltip(String string) {
		tooltip = string;
	}

	/**
	 * Get this group's tooltip
	 */
	public String getToolTip() {
		if (tooltip != null) {
			return tooltip;
		}
		return name;
	}

	/**
	 * Set the size for this group. This implementaion does nothing in this
	 * method and automatically sets the size to the sum of all the sizes of its
	 * members
	 */
	public void setSize(int size) {
	}

	/**
	 * Get the size for this group. This is the sum of all sizes of its members.
	 */
	public Integer getSize() {
		int s = 0;
		Iterator i = kids.iterator();
		while (i.hasNext()) {
			IMember im = (IMember) i.next();
			s += im.getSize().intValue();
		}
		return new Integer(s);
	}

	/**
	 * Get the containing group. This currently has no meaning for groups and
	 * returns null as a group cannot be nested within another group.
	 */
	public IGroup getContainingGroup() {
		return null;
	}

	/**
	 * Set the containing group. Does nothing as a group cannot be nested within
	 * another group.
	 */
	public void setContainingGroup(IGroup grp) {
	}

	/**
	 * Get the String representation of this group. This contains the name, the
	 * size and the number of members.
	 */
	public String toString() {
		return VisualiserMessages.SimpleGroup + ":[" + name //$NON-NLS-1$
				+ "] " + VisualiserMessages.Size + ":["//$NON-NLS-1$ //$NON-NLS-2$
				+ getSize() + "]  "//$NON-NLS-1$
				+ VisualiserMessages.Children + ":["//$NON-NLS-1$
				+ kids.size() + "]";//$NON-NLS-1$
	}

}
