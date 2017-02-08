/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Ben Dalziel - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.palettes;

public class PatternVisualiserPalette extends DefaultVisualiserPalette {

	protected static byte[][] pattern1 = new byte[][] { { 2, 0, 0, 7, 0 }, { 2, 0, 2, 7, 2 }, { 2, 0, 4, 7, 4 },
			{ 2, 0, 6, 7, 6 }, { 2, 0, 0, 0, 7 }, { 2, 2, 0, 2, 7 }, { 2, 4, 0, 4, 7 }, { 2, 6, 0, 6, 7 } };

	protected static byte[][] pattern2 = new byte[][] { { 2, 0, 0, 0, 7 }, { 2, 4, 0, 4, 7 } };

	protected static byte[][] pattern3 = new byte[][] { { 2, 0, 0, 7, 0 }, { 2, 0, 1, 7, 1 }, { 2, 0, 4, 7, 4 },
			{ 2, 0, 5, 7, 5 } };

	protected static byte[][] pattern4 = new byte[][] { { 2, 0, 0, 7, 7 }, { 2, 4, 0, 7, 3 }, { 2, 0, 4, 3, 7 } };

	protected static byte[][] pattern5 = new byte[][] { { 2, 0, 0, 0, 7 }, { 2, 2, 0, 2, 7 }, { 2, 4, 0, 4, 7 },
			{ 2, 6, 0, 6, 7 } };

	protected static byte[][] pattern6 = new byte[][] { { 2, 0, 0, 0, 0 }, { 2, 0, 4, 0, 4 }, { 2, 4, 0, 4, 0 },
			{ 2, 4, 4, 4, 4 } };

	protected static byte[][] pattern7 = new byte[][] { { 2, 0, 0, 0, 7 }, { 2, 0, 0, 7, 0 }, { 2, 0, 4, 7, 4 },
			{ 2, 4, 0, 4, 7 } };

	protected static byte[][] pattern8 = new byte[][] { { 2, 0, 0, 7, 0 }, { 2, 0, 4, 7, 4 } };

	protected static byte[][] pattern9 = new byte[][] { { 2, 0, 0, 0, 7 }, { 2, 1, 0, 1, 7 }, { 2, 4, 0, 4, 7 },
			{ 2, 5, 0, 5, 7 } };

	protected static byte[][] pattern10 = new byte[][] { { 2, 7, 0, 0, 7 }, { 2, 3, 0, 0, 3 }, { 2, 7, 4, 4, 7 } };

	protected static byte[][] pattern11 = new byte[][] { { 2, 0, 0, 7, 0 }, { 2, 0, 2, 7, 2 }, { 2, 0, 4, 7, 4 },
			{ 2, 0, 6, 7, 6 } };

	protected static byte[][][] patternList = new byte[][][] { pattern1, pattern2, pattern3, pattern4, pattern5,
			pattern6, pattern7, pattern8, pattern9, pattern10, pattern11 };

	/**
	 * Returns a pattern with a number of randomly created lines The density,
	 * between 5 and 20 related to the number of lines in the pattern
	 * 
	 * @return randomly generated Color
	 */
	public byte[][] getRandomPattern() {
		int density = ((int) (Math.random() * 15) + 5);

		byte[][] randomPattern = new byte[density][5];

		for (int i = 0; i < density; i++) {
			int x1 = ((int) (Math.random() * 8));
			int y1 = ((int) (Math.random() * 8));
			// Could make into pattern of lines insted of points by including
			// these coords
			// int x2 = ((int) (Math.random() * 8));
			// int y2 = ((int) (Math.random() * 8));

			randomPattern[i] = new byte[] { 2, (byte) x1, (byte) y1, (byte) x1, (byte) y1 };
		}
		return randomPattern;
	}

	public byte[][][] getPaletteContents() {
		return patternList;
	}
}
