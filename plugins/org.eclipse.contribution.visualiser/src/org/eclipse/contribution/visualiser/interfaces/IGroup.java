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

import java.util.List;

/**
 * The IGroup interface is a simple extension of IMember that supports children
 * members.
 */
public interface IGroup extends IMember {

	/**
	 * Return list of all children in this group, the list should contain
	 * IMembers.
	 */
	List getMembers();

	/**
	 * Add a new IMember to this group.
	 */
	void add(IMember member);

}
