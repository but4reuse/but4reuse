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
package org.eclipse.contribution.visualiser.palettes;

import org.eclipse.swt.graphics.RGB;

/**
 * Bright and high-contrast colours, good for projectors
 */
public class BrightVisualiserPalette extends DefaultVisualiserPalette {

	protected static RGB brightBlue = new RGB(0, 0, 255);

	protected static RGB brightGreen = new RGB(51, 219, 0);

	protected static RGB brightPurple = new RGB(156, 58, 255);

	protected static RGB brightPink = new RGB(255, 95, 141);

	protected static RGB brightOrange = new RGB(255, 147, 15);

	protected static RGB brightCyan = new RGB(0, 219, 215);

	protected static RGB brightYellow = new RGB(244, 241, 22);

	private static RGB[] rgbList = new RGB[] { brightBlue, brightGreen, brightPurple, brightPink, brightOrange,
			brightCyan, brightYellow,
			// now use some dark but high-contrast colours from default palette
			red5, green5, cyan5, blue5, purple5, orange5, swamp5, mint5, lightblue5, indigo5, pink5 };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IVisualiserPalette#
	 * getRGBValues()
	 */
	public RGB[] getRGBValues() {
		return rgbList;
	}

}
