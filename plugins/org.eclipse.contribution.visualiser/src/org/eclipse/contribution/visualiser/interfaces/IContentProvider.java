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

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * The implementation of this interface is responsible for returning information
 * about the bars that appear in the VSisualiser view. The related
 * implementation of IMarkupProvider provides the information about the colored
 * stripes on the bars.
 * 
 * A simple implementation is provided in SimpleContentProvider - this contains
 * basic implementations of some of the common features of a content provider In
 * particular, it manages groups/members automatically.
 * 
 * Important: The lists returned from the various get methods should either be
 * lists of classes implementing IGroup or classes implementing IMember. Simple
 * implementations of IGroup and IMember are SimpleGroup and SimpleMember.
 */
public interface IContentProvider {

	/**
	 * Return a list of all groups that the provider knows about.
	 * 
	 * @return List of IGroups
	 */
	public List getAllGroups();

	/**
	 * Return a list of all members in a group.
	 * 
	 * @return List of IMembers
	 */
	public List getAllMembers(IGroup group);

	/**
	 * Return a list of all members in all groups the provider knows about.
	 */
	public List getAllMembers();

	/**
	 * Called on Visualiser startup, to get the provider ready.
	 */
	public void initialise();

	/**
	 * Called when the user clicks on a member in the Visualiser - the
	 * information passed is - The full name of the member, e.g. "ABC.B" -
	 * Whether the click was actually on a colored area (stripe) in the member -
	 * The buttons pressed (1 is LH button, 3 is RH button)
	 * 
	 * The return value is whether the VSisualiser should take its normal action
	 * on this click. The normal action is that on a left hand mouse click, the
	 * visualiser subselects the clicked member (if in the group view, it swaps
	 * to a member view of the members in that group). If the right hand mouse
	 * button is clicked, it returns to the previous visualisation.
	 * 
	 * Remember: The markup provider is called with a similar method
	 * 'processMouseClick()' if the boolean markupWasClicked is true. This call
	 * to the markup provider will occur regardless of whether true or false is
	 * returned here.
	 */
	public boolean processMouseclick(IMember member, boolean markupWasClicked, int buttonClicked);

	/**
	 * Called when switching to this content provider to get the icon used in
	 * the Visualiser view for Member view. If null is returned default icons
	 * are used.
	 * 
	 * @return image to be used as member view icon
	 */
	public ImageDescriptor getMemberViewIcon();

	/**
	 * Called when switching to this content provider to get the icon used in
	 * the Visualiser view for Group view. If null is returned default icons are
	 * used.
	 * 
	 * @return image to be used as group view icon, or null if default is
	 *         required
	 */
	public ImageDescriptor getGroupViewIcon();

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
