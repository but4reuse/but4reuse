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
package org.eclipse.contribution.visualiser.jdtImpl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleGroup;

/**
 * The JDT implementation of IGroup. Each group represents a package in the JDT
 * model.
 */
public class JDTGroup extends SimpleGroup {

	/**
	 * Default constructor.
	 * 
	 * @param name
	 *            - the group's name
	 */
	public JDTGroup(String name) {
		super(name);
		this.name = name;
		this.tooltip = name;
	}

	/**
	 * Add all the IMembers in the given list
	 * 
	 * @param members
	 */
	public void addMembers(List members) {
		for (Iterator memIter = members.iterator(); memIter.hasNext();) {
			IMember mem = (IMember) memIter.next();
			add(mem);
		}
	}

}
