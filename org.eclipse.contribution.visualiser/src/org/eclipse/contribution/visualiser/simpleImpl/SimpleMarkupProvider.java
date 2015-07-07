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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.contribution.visualiser.core.PaletteManager;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.utils.MarkupUtils;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Example implementation of a markup provider. Loads its information about the
 * markups from a file. Manages the list of markups in a hashtable - the
 * hashtable maps the member ID to a list of 'Stripe instances' (markups).
 */
public class SimpleMarkupProvider implements IMarkupProvider {

	private Map colourMemory = new HashMap();
	private Map availableColours = new HashMap();

	private Map allocatedColours = new HashMap(); // indexed by RGB

	// Indexed by FULL membername, each entry is a List of Stripe objects.
	private Hashtable markups = null;

	private SortedSet markupKinds;

	/**
	 * Initialise the markup provider. This simple implementation does nothing
	 * here.
	 */
	public void initialise() {
	}

	/**
	 * Get a List of Stripes for the given member, which are its markups.
	 */
	public List getMemberMarkups(IMember member) {
		if (member != null && markups != null) {
			Object o = markups.get(member.getFullname());
			if (o instanceof List) {
				return (List) markups.get(member.getFullname());
			}
		}
		return null;
	}

	/**
	 * Add a Stripe to the member with the given name.
	 * 
	 * @param membername
	 * @param s
	 */
	public void addMarkup(String membername, Stripe s) {
		if (markups == null)
			markups = new Hashtable();
		List stripes = (List) markups.get(membername);
		if (stripes == null) {
			stripes = new ArrayList();
			stripes.add(s);
			markups.put(membername, stripes);
		} else {
			stripes.add(s);
		}
	}

	/**
	 * Add a markup kind
	 * 
	 * @param kind
	 */
	public void addMarkupKind(IMarkupKind kind) {
		markupKinds.add(kind);
	}

	/**
	 * Process all the Stripes that have been added to deal with the overlapping
	 * cases
	 */
	public void processMarkups() {
		Enumeration memkeys = markups.keys();
		while (memkeys.hasMoreElements()) {
			String memberID = (String) memkeys.nextElement();
			List unprocessedListOfStripes = (List) markups.get(memberID);
			MarkupUtils.processStripes(unprocessedListOfStripes);
		}
	}

	/**
	 * Get the markups for a group. Group markups are a stacked set of member
	 * markups.
	 */
	public List getGroupMarkups(IGroup group) {
		List stripes = new ArrayList();
		List kids = group.getMembers();
		int accumulatedOffset = 0;

		// Go through all the children of the group
		for (Iterator iter = kids.iterator(); iter.hasNext();) {
			IMember element = (IMember) iter.next();
			List l = getMemberMarkups(element);
			if (l != null) {
				for (Iterator iterator = l.iterator(); iterator.hasNext();) {
					Stripe elem = (Stripe) iterator.next();
					stripes.add(new Stripe(elem.getKinds(), elem.getOffset() + accumulatedOffset, elem.getDepth()));
				}
			}
			accumulatedOffset += element.getSize().intValue();
		}
		return stripes;
	}

	/**
	 * Get all the markup kinds.
	 * 
	 * @return a Set of IMarkupKinds
	 */
	public SortedSet getAllMarkupKinds() {
		// Created sorted list of markups
		if (markups == null)
			return null;
		if (markupKinds != null)
			return markupKinds;
		markupKinds = new TreeSet();

		Enumeration stripeLists = markups.elements();
		while (stripeLists.hasMoreElements()) {
			List stripelist = (List) stripeLists.nextElement();
			for (Iterator iter = stripelist.iterator(); iter.hasNext();) {
				Stripe element = (Stripe) iter.next();
				markupKinds.addAll(element.getKinds());
			}
		}
		return markupKinds;
	}

	// Color management

	/**
	 * Get the colour for a given kind
	 * 
	 * @param kind
	 *            - the kind
	 * @return the Color for that kind
	 */
	public Color getColorFor(IMarkupKind kind) {
		Color stripeColour = null;
		String p = "not unique"; //Note: String not displayed externally //$NON-NLS-1$ 
		String key = p + ":" + kind.getFullName(); //$NON-NLS-1$
		if (colourMemory.containsKey(key)) {
			stripeColour = (Color) colourMemory.get(key);
		} else {
			stripeColour = getNextColourFor(p);
			colourMemory.put(key, stripeColour);
		}
		return stripeColour;
	}

	/**
	 * Set the color for a kind.
	 * 
	 * @param kind
	 *            - the kind
	 * @param color
	 *            - the Color
	 */
	public void setColorFor(IMarkupKind kind, Color color) {
		colourMemory.put("not unique:" + kind.getName(), color); //Note: String not displayed externally //$NON-NLS-1$
	}

	/**
	 * Get the next assignable colour and assign it to the String argument.
	 * 
	 * @param p
	 *            - the kind
	 * @return new Color
	 */
	protected Color getNextColourFor(String p) {
		if (!availableColours.containsKey(p.toString())) {
			RGB[] rgb = PaletteManager.getCurrentPalette().getPalette().getRGBValues();
			List colourList = new ArrayList(Arrays.asList(rgb));
			availableColours.put(p.toString(), colourList);
		}

		List colours = (List) availableColours.get(p.toString());
		RGB v;
		if (!colours.isEmpty()) {
			v = (RGB) colours.get(0);
			colours.remove(0);
		} else {
			v = PaletteManager.getCurrentPalette().getPalette().getRandomRGBValue();
		}

		Object obj = allocatedColours.get(v);
		Color c;
		if ((obj != null) && (obj instanceof Color)) {
			c = (Color) obj;
		} else {
			c = new Color(Display.getDefault(), v);
			allocatedColours.put(v, c);
		}
		return c;
	}

	/**
	 * Empty the data structures that contain the stripe and kind information
	 */
	public void resetMarkupsAndKinds() {
		markups = new Hashtable();
		markupKinds = new TreeSet();
	}

	/**
	 * Reset the color memory
	 */
	public void resetColours() {
		availableColours = new HashMap();
		colourMemory = new HashMap();
	}

	/**
	 * Reset the color memory
	 */
	private void disposeColors() {
		for (Iterator iter = allocatedColours.keySet().iterator(); iter.hasNext();) {
			Color c = (Color) allocatedColours.get(iter.next());
			c.dispose();
		}
		allocatedColours = new HashMap();
	}

	/**
	 * Process a mouse click on a stripe. This implementation does nothing and
	 * returns true to allow the visualiser to perform the default operations.
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
		resetColours();
		disposeColors();
	}

}
