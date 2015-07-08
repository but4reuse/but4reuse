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

import org.eclipse.swt.graphics.GC;

/**
 * Defines the operations required to implement a visualiser drawing style.
 * 
 * @author mchapman
 */
public interface IVisualiserRenderer {

	/**
	 * The space in-between columns
	 * 
	 * @return the gap in pixels
	 */
	public int getSpacing();

	/**
	 * The space required for headers of each columns
	 * 
	 * @return the height in pixels
	 */
	public int getColumnHeaderHeight();

	/**
	 * The padding to be used around the edges of the visualiser
	 * 
	 * @return the margin size in pixels
	 */
	public int getMarginSize();

	/**
	 * Paint the title of a column, typically the name of the given IMember
	 * 
	 * @param gc
	 *            the graphics context to paint to
	 * @param m
	 *            the IMember for this column
	 * @param x
	 *            the horizontal coordinate for the start of this column
	 * @param colWidth
	 *            the width of the column
	 */
	public void paintColumnHeader(GC gc, IMember m, int x, int colWidth);

	/**
	 * Paint the actual column (but not any stripes)
	 * 
	 * @param gc
	 *            the graphics context to paint to
	 * @param m
	 *            the IMember for this column
	 * @param x
	 * @param y
	 * @param colWidth
	 * @param colHeight
	 * @param affected
	 *            indicates whether this column is affected by any stripes
	 */
	public void paintColumn(GC gc, IMember m, int x, int y, int colWidth, int colHeight, boolean affected);

}