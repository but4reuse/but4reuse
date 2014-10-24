/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Matt Chapman - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.interfaces;

import org.eclipse.swt.graphics.RGB;

/**
 * Interface for defining a colour palette for the visualiser
 */
public interface IVisualiserPalette {

	/**
	 * Return the array of RGB values for this palette. The array can be of any
	 * length - if the array is exhausted, the getRandomRGBValue() method will
	 * be used.
	 * 
	 * @return array of RGB values
	 */
	public RGB[] getRGBValues();

	/**
	 * Return another RGB value, to be used when the predefined values from
	 * getRGBValues() are exhausted.
	 * 
	 * @return RGB value
	 */
	public RGB getRandomRGBValue();

}