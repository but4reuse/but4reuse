/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *     Matt Chapman - added compare method
 *     Ian McGrath  - Fixed equals method
 *******************************************************************************/
package org.eclipse.contribution.visualiser.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;

/**
 * The stripe class represents a mark on a bar, stripes can be any depth and be
 * of multiple 'kinds' (kinds map to colors).
 */
public class Stripe implements Comparable {

	private List kinds;

	private int offset;

	private int depth;

	/**
	 * Returns true if the given Object is equal to this stripe
	 * 
	 * @param that
	 * @return true iff the given Object is equal to this stripe
	 */
	public boolean equals(Object that) {
		if (!(that instanceof Stripe))
			return false;
		Stripe stripe = (Stripe) that;
		if (depth != stripe.depth)
			return false;
		if (offset != stripe.offset)
			return false;
		if (!stringifyKinds().equals(stripe.stringifyKinds()))
			return false;
		return true;
	}

	/**
	 * Override hashCode because we have overridden equals.
	 */
	public int hashCode() {
		// To obey the contract, return a hashcode that is a function of all the
		// variables used in the equals method.

		int result = 17; // Begin with a non-zero number so that result is not
							// zero. (Value not important).
		result = 37 * result + depth; // Multiply by 37 (value not relevant) to
										// increase possible range of hashcodes
		result = 37 * result + offset;
		result = 37 * result + stringifyKinds().hashCode();
		return result;
	}

	/**
	 * Default constructor
	 */
	public Stripe() {
		kinds = new ArrayList();
	}

	/**
	 * Stripe constructor when the stripe is of the minimum depth (1).
	 * 
	 * @param k
	 *            The kind of stripe
	 * @param i
	 *            The offset down the bar where the stripe starts
	 */
	public Stripe(IMarkupKind k, int i) {
		kinds = new ArrayList();
		kinds.add(k);
		offset = i;
		depth = 1;
	}

	/**
	 * Stripe constructor where the kind, offset and depth can be specified.
	 * 
	 * @param k
	 *            The kind of the stripe
	 * @param o
	 *            The offset down the bar where the stripe starts
	 * @param d
	 *            The depth of the stripe
	 */
	public Stripe(IMarkupKind k, int o, int d) {
		kinds = new ArrayList();
		kinds.add(k);
		offset = o;
		depth = d;
	}

	/**
	 * Stripe constructor where the kinds, offset and depth can be specified.
	 * The input list 'kinds' should be a list of strings.
	 * 
	 * @param ks
	 *            The kinds of the stripe, should be strings
	 * @param o
	 *            The offset down the bar where the stripe starts
	 * @param d
	 *            The depth of the stripe
	 */
	public Stripe(List ks, int o, int d) {
		kinds = new ArrayList();
		kinds.addAll(ks);
		offset = o;
		depth = d;
	}

	/**
	 * Getter for the kinds of this stripe.
	 * 
	 * @return List of strings representing kinds
	 */
	public List getKinds() {
		return kinds;
	}

	/**
	 * Add the given kinds to this stripe
	 * 
	 * @param list
	 */
	public void addKinds(List list) {
		if (list != null)
			kinds.addAll(list);
	}

	/**
	 * Set the list of kinds for this Stripe to the given argument
	 * 
	 * @param list
	 */
	public void setKinds(List list) {
		kinds = new ArrayList();
		kinds.addAll(list);
	}

	/**
	 * Get the offset for this Stripe
	 * 
	 * @return the offset for this Stripe
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Get this Stripe's depth
	 * 
	 * @return this Stripe's depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Get a String representation of this Stripe
	 */
	public String toString() {
		return VisualiserMessages.Stripe + ": [" + offset + ":" + depth + ":" + (offset + depth) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ ":" + stringifyKinds() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Get a string representation of the kinds in this Stripe
	 * 
	 * @return a string representation of the kinds in this Stripe
	 */
	public String stringifyKinds() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < kinds.size(); i++) {
			sb.append(" " + kinds.get(i) + " "); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return sb.toString();
	}

	/**
	 * Set the offset for this Stripe
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;

	}

	/**
	 * Set the depth for this Stripe
	 * 
	 * @param i
	 */
	public void setDepth(int i) {
		depth = i;

	}

	/**
	 * Get the tooltip for this Stripe, which is a list of its kinds
	 */
	public String getToolTip() {
		return stringifyKinds();

	}

	/**
	 * Returns true if this Stripe has the given kind
	 * 
	 * @param kind
	 * @return true iff this Stripe has the given kind
	 */
	public boolean hasKind(IMarkupKind kind) {
		int found = -1;
		for (int i = 0; i < kinds.size() && found == -1; i++) {
			IMarkupKind element = (IMarkupKind) kinds.get(i);
			if (element.equals(kind))
				found = i;
		}
		return (found != -1);
	}

	/**
	 * Compare this Stripe to another Object, which should be a Stripe. Returns
	 * -1 if this stripe is considered less than the argument, 0 if the are
	 * considered equal and 1 if this Stripe is greater than the argument
	 * Stripe.
	 */
	public int compareTo(Object other) {
		if (getOffset() < ((Stripe) other).getOffset()) {
			return -1;
		} else if (getOffset() > ((Stripe) other).getOffset()) {
			return 1;
		}
		return 0;
	}

}