/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 * 	   Sian Whiting - added changeMode() and hasMultipleModes() methods
 *******************************************************************************/
package org.eclipse.contribution.visualiser.interfaces;

import java.util.List;
import java.util.SortedSet;

import org.eclipse.swt.graphics.Color;

import org.eclipse.contribution.visualiser.core.Stripe;

/**
 * The implementation of this interface is responsible for returning information
 * about the colored stripes that appear on the resources in the visualiser view
 * (The set of resources being determined by the implementation of the
 * IContentProvider).
 * 
 * A simple implementation is provided in SimpleMarkupProvider - this provides
 * basic implementations of some of the common features of a markup provider
 * 
 * 1) It manages the colors automatically. 2) It considers group markups to be
 * collected markups for the members of the group.
 * 
 * 
 * Important: The lists returned from get methods should be lists of Stripe
 * instances.
 */
public interface IMarkupProvider {

	/**
	 * Called when the visualiser initializes and discovers a markup provider
	 * implementation. Typically initialise will get the markups 'ready'.
	 */
	public void initialise();

	/**
	 * Return a list of all stripes in effect on this member.
	 */
	public List getMemberMarkups(IMember member);

	/**
	 * Return a list of all stripes in effect on all members of the group.
	 */
	public List getGroupMarkups(IGroup group);

	/**
	 * Return a list of all possible 'kinds' covered by all the stripes in the
	 * visualisation. This is used by the visualiser menu. The return value is a
	 * set of IMarkupKinds.
	 */
	public SortedSet getAllMarkupKinds();

	/**
	 * When the color picker is used in the visualiser menu to change the color
	 * for a particular kind, this call ensures the logic responsible for color
	 * management is told.
	 */
	public void setColorFor(IMarkupKind kind, Color color);

	/**
	 * Ask for a color for a given kind - if one is not currently allocated, it
	 * will be selected from those available.
	 */
	public Color getColorFor(IMarkupKind element);

	/**
	 * Called when the user clicks on a stripe on a member in the visualiser -
	 * the information passed is - The full name of the member, e.g. "ABC.B" -
	 * The stripe that was clicked (which might contain multiple 'kinds') - The
	 * buttons pressed (1 is LH button, 3 is RH button)
	 * 
	 * The return value is whether the visualiser should take its normal action
	 * on this click. The normal action is that on a left hand mouse click, the
	 * visualiser subselects the clicked member (if in the group view, it swaps
	 * to a member view of the members in that group). If the right hand mouse
	 * button is clicked, it returns to the previous visualisation.
	 * 
	 * Remember: The content provider is always called with a similar method
	 * 'processMouseClick()'. The call to the markup provider will occur
	 * regardless of whether true or false is returned here.
	 */
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked);

	/**
	 * Called when this provider is selected.
	 */
	public void activate();

	/**
	 * Called when this provider is currently active and another provider is
	 * selected.
	 */
	public void deactivate();

}
