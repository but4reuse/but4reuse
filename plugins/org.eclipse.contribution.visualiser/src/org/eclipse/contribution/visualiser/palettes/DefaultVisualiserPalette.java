/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *     Matt Chapman - switch to IVisualiserPalette, and RGB instead of Color 
 *******************************************************************************/
package org.eclipse.contribution.visualiser.palettes;

import org.eclipse.contribution.visualiser.interfaces.IVisualiserPalette;
import org.eclipse.swt.graphics.RGB;

/**
 * Default implementation of IVisualiserPalette providing a default palette for
 * visualiser providers.
 */
public class DefaultVisualiserPalette implements IVisualiserPalette {

	// RGB Colour constants
	protected static RGB red1 = new RGB(255, 128, 128);

	protected static RGB yellow1 = new RGB(255, 255, 128);

	protected static RGB green1 = new RGB(128, 255, 128);

	protected static RGB cyan1 = new RGB(128, 255, 255);

	protected static RGB blue1 = new RGB(128, 128, 255);

	protected static RGB purple1 = new RGB(255, 128, 255);

	protected static RGB orange1 = new RGB(255, 192, 128);

	protected static RGB swamp1 = new RGB(192, 255, 128);

	protected static RGB mint1 = new RGB(128, 255, 192);

	protected static RGB lightblue1 = new RGB(128, 192, 255);

	protected static RGB indigo1 = new RGB(192, 128, 255);

	protected static RGB pink1 = new RGB(255, 128, 192);

	protected static RGB red2 = new RGB(255, 64, 64);

	protected static RGB yellow2 = new RGB(255, 255, 64);

	protected static RGB green2 = new RGB(64, 255, 64);

	protected static RGB cyan2 = new RGB(64, 255, 255);

	protected static RGB blue2 = new RGB(64, 64, 255);

	protected static RGB purple2 = new RGB(255, 64, 255);

	protected static RGB orange2 = new RGB(255, 170, 64);

	protected static RGB swamp2 = new RGB(170, 255, 64);

	protected static RGB mint2 = new RGB(64, 255, 170);

	protected static RGB lightblue2 = new RGB(64, 170, 255);

	protected static RGB indigo2 = new RGB(170, 64, 255);

	protected static RGB pink2 = new RGB(255, 64, 170);

	protected static RGB red3 = new RGB(255, 0, 0);

	protected static RGB yellow3 = new RGB(255, 255, 0);

	protected static RGB green3 = new RGB(0, 255, 0);

	protected static RGB cyan3 = new RGB(0, 255, 255);

	protected static RGB blue3 = new RGB(0, 0, 255);

	protected static RGB purple3 = new RGB(255, 0, 255);

	protected static RGB orange3 = new RGB(255, 128, 0);

	protected static RGB swamp3 = new RGB(128, 255, 0);

	protected static RGB mint3 = new RGB(0, 255, 128);

	protected static RGB lightblue3 = new RGB(0, 128, 255);

	protected static RGB indigo3 = new RGB(128, 0, 255);

	protected static RGB pink3 = new RGB(255, 0, 128);

	protected static RGB red4 = new RGB(192, 0, 0);

	protected static RGB yellow4 = new RGB(192, 192, 0);

	protected static RGB green4 = new RGB(0, 192, 0);

	protected static RGB cyan4 = new RGB(0, 192, 192);

	protected static RGB blue4 = new RGB(0, 0, 192);

	protected static RGB purple4 = new RGB(192, 0, 192);

	protected static RGB orange4 = new RGB(192, 100, 0);

	protected static RGB swamp4 = new RGB(100, 192, 0);

	protected static RGB mint4 = new RGB(0, 192, 100);

	protected static RGB lightblue4 = new RGB(0, 100, 192);

	protected static RGB indigo4 = new RGB(100, 0, 192);

	protected static RGB pink4 = new RGB(192, 0, 100);

	protected static RGB red5 = new RGB(128, 0, 0);

	protected static RGB yellow5 = new RGB(128, 128, 0);

	protected static RGB green5 = new RGB(0, 128, 0);

	protected static RGB cyan5 = new RGB(0, 128, 128);

	protected static RGB blue5 = new RGB(0, 0, 128);

	protected static RGB purple5 = new RGB(128, 0, 128);

	protected static RGB orange5 = new RGB(128, 64, 0);

	protected static RGB swamp5 = new RGB(64, 128, 0);

	protected static RGB mint5 = new RGB(0, 128, 64);

	protected static RGB lightblue5 = new RGB(0, 64, 128);

	protected static RGB indigo5 = new RGB(64, 0, 128);

	protected static RGB pink5 = new RGB(128, 0, 64);

	protected static RGB[] rgbList = new RGB[] { cyan2, blue2, purple2, swamp2, orange2, indigo2, lightblue2, red2,
			yellow2, green2, pink2, mint2, red4, yellow4, green4, cyan4, blue4, purple4, orange4, swamp4, mint4,
			lightblue4, indigo4, pink4, red3, yellow3, green3, cyan3, blue3, purple3, orange3, swamp3, mint3,
			lightblue3, indigo3, pink3, red1, yellow1, green1, cyan1, blue1, purple1, orange1, swamp1, mint1,
			lightblue1, indigo1, pink1, red5, yellow5, green5, cyan5, blue5, purple5, orange5, swamp5, mint5,
			lightblue5, indigo5, pink5 };

	/**
	 * Returns a colour with random R,G and B values that are between 50 and 250
	 * and are multiples of 5.
	 * 
	 * @return randomly generated Color
	 */
	public RGB getRandomRGBValue() {
		int r = (((int) (Math.random() * 40)) * 5 + 50);
		int g = (((int) (Math.random() * 40)) * 5 + 50);
		int b = (((int) (Math.random() * 40)) * 5 + 50);
		return new RGB(r, g, b);
	}

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
