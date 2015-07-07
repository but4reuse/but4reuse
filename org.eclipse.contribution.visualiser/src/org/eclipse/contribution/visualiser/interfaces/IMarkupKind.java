/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: Sian January - initial version
 * ...
 **********************************************************************/

package org.eclipse.contribution.visualiser.interfaces;

import org.eclipse.swt.graphics.Image;

/**
 * Interface for Markup kinds that are displayed in the Visualiser Menu
 */
public interface IMarkupKind {

	/**
	 * Get the name to display
	 * 
	 * @return markup kind name
	 */
	public String getName();

	/**
	 * Get the full name, which is displayed as the tooltip in the menu Clients
	 * should return the result of getName if no full name exists
	 * 
	 * @return
	 */
	public String getFullName();

	/**
	 * Get the image to display
	 * 
	 * @return markup kind image
	 */
	public Image getIcon();

	/**
	 * Show this kind in the Visualiser Menu?
	 * 
	 * @return true if kind should be shown
	 */
	public boolean showInMenu();

}
