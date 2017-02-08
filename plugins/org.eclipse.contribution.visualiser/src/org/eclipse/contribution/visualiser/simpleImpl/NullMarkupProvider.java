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

import java.util.List;
import java.util.SortedSet;

import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.swt.graphics.Color;

/**
 * The null marker provider, just returns nulls for all questions it gets asked.
 */
public class NullMarkupProvider implements IMarkupProvider {

	/**
	 * Get the markups for the given member - returns null
	 */
	public List getMemberMarkups(IMember member) {
		return null;
	}

	/**
	 * Get the markups for the given group - returns null
	 */
	public List getGroupMarkups(IGroup group) {
		return null;
	}

	/**
	 * Get the colour for the given String - returns null
	 */
	public Color getColorFor(IMarkupKind string) {
		return null;
	}

	/**
	 * Get the set of all markup kinds - returns null
	 */
	public SortedSet getAllMarkupKinds() {
		return null;
	}

	/**
	 * Initialise the provider - does nothing
	 */
	public void initialise() {
	}

	/**
	 * Set the colour for the given String - does nothing
	 */
	public void setColorFor(IMarkupKind kind, Color color) {
	}

	/**
	 * Processs a mouse click on a stripe. Does nothing. Returns true - default
	 * behavior should be performed.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupProvider#processMouseclick(IMember,
	 *      Stripe, int)
	 */
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		return true;
	}

	/**
	 * Activate the provider
	 */
	public void activate() {
	}

	/**
	 * Deactivate the provider
	 */
	public void deactivate() {
	}

}
