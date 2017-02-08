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

import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.swt.graphics.Image;

/**
 * Simple implementation of IMarkupKind. Will show in the Menu by default and
 * can contain an image as well as a name. Also implements comparable and
 * compares by name.
 */
public class SimpleMarkupKind implements IMarkupKind, Comparable {

	private final String name;
	private final Image icon;
	private final String fullName;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind
	 */
	public SimpleMarkupKind(String name) {
		this.name = name;
		this.icon = null;
		this.fullName = name;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind
	 * @param icon
	 *            - image
	 */
	public SimpleMarkupKind(String name, Image icon) {
		this.icon = icon;
		this.name = name;
		this.fullName = name;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind
	 * @param fullName
	 *            - fullName
	 */
	public SimpleMarkupKind(String name, String tooltip) {
		this.icon = null;
		this.name = name;
		this.fullName = tooltip;
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            - name of the kind
	 * @param icon
	 *            - image
	 */
	public SimpleMarkupKind(String name, String tooltip, Image icon) {
		this.icon = icon;
		this.name = name;
		this.fullName = tooltip;
	}

	/**
	 * Get the name of this kind.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupKind#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the icon for this kind.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupKind#getIcon()
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * Ask whether or not to show this kind in the Visualiser Menu.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupKind#showInMenu()
	 */
	public boolean showInMenu() {
		return true;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object object) {
		if (object instanceof SimpleMarkupKind && name != null) {
			int nameC = name.compareTo(((SimpleMarkupKind) object).name);
			if (nameC == 0) {
				return fullName.compareTo(((SimpleMarkupKind) object).fullName);
			}
			return nameC;
		}
		return 0;
	}

	/**
	 * Override the equals method to compare based on fields
	 */
	public boolean equals(Object object) {
		if (object instanceof SimpleMarkupKind) {
			SimpleMarkupKind smk = (SimpleMarkupKind) object;
			if (smk.name.equals(name)) {
				if (smk.fullName.equals(fullName)) {
					if (icon == null) {
						if (smk.icon == null) {
							return true;
						}
					} else if (icon.equals(smk.icon)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Override hashCode because we have overriden the equals method.
	 */
	public int hashCode() {
		if (icon == null) {
			return name.hashCode() + (37 * fullName.hashCode()); // multiply by
																	// arbitrary
																	// value of
																	// 37 to
																	// increase
																	// variation
		}
		return name.hashCode() + (37 * fullName.hashCode()) + icon.hashCode();
	}

	/**
	 * Get the String representation of this kind. Returns the name.
	 */
	public String toString() {
		return name;
	}

	/**
	 * Get the fullName
	 */
	public String getFullName() {
		return fullName;
	}

}
