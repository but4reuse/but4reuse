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

package org.eclipse.contribution.visualiser.simpleImpl;

import org.eclipse.swt.graphics.Image;

/**
 * Simple implementation of IMarkupKind that does not show in the Menu by
 * default.
 */
public class StealthMarkupKind extends SimpleMarkupKind {

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind.
	 */
	public StealthMarkupKind(String name) {
		super(name);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind
	 * @param icon
	 *            - associated image
	 */
	public StealthMarkupKind(String name, Image icon) {
		super(name, icon);
	}

	/**
	 * Returns false - do not show this kind in the Visualiser Menu
	 */
	public boolean showInMenu() {
		return false;
	}

}
