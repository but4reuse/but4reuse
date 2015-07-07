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
package org.eclipse.contribution.visualiser.renderers;

import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.utils.ColorConstants;
import org.eclipse.contribution.visualiser.views.VisualiserCanvas;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * Implements the visualiser renderer interface to provide the default
 * visualiser drawing style, which has plain columns plus Eclipse style headers
 * with rounded corners. Custom renderers can either implement the interface
 * directly or extend this class, or any of the other renderers.
 * 
 * @author mchapman
 */
public class DefaultVisualiserRenderer extends ClassicVisualiserRenderer {

	private int spacing = 4;

	private int margin = 6;

	protected Color outlineColor = new Color(Display.getDefault(), 172, 168, 153);

	protected Color outlineColor2 = new Color(Display.getDefault(), 214, 211, 195);

	protected Color lightGrayGradientColor = new Color(Display.getDefault(), 230, 230, 230);

	protected Color[] colorList = new Color[] { VisualiserCanvas.VIS_BG_COLOUR, outlineColor2, outlineColor };

	// data for constructing a left-hand rounded corner with title gradient
	protected static int cornerSize = 6;

	protected static byte[][] cornerData = new byte[][] { { 1, 5, 0, 5, 0 }, { 0, 1, 1, 1, 1 }, { 0, 2, 1, 2, 1 },
			{ 1, 3, 1, 3, 1 }, { 2, 4, 1, 5, 1 }, { 0, 1, 2, 1, 2 }, { 1, 2, 2, 2, 2 }, { 2, 3, 2, 3, 2 },
			{ 1, 1, 3, 1, 3 }, { 2, 2, 3, 2, 3 }, { 1, 0, 4, 0, 5 }, { 2, 1, 4, 1, 5 } };

	// store the rounded corners image data, to save recreating it each time
	private static ImageData header;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.contribution.visualiser.views.VisualiserRendering#
	 * getColumnHeaderHeight()
	 */
	public int getColumnHeaderHeight() {
		return super.getColumnHeaderHeight() + 2;
	}

	private void createHeader(int width) {
		Image headerImg = new Image(Display.getDefault(), width, getColumnHeaderHeight());
		GC gc = new GC(headerImg);

		gc.setBackground(colorList[0]);
		gc.fillRectangle(0, 0, width, getColumnHeaderHeight());

		// draw gradient
		gc.setForeground(ColorConstants.titleBackground);
		gc.setBackground(ColorConstants.titleGradient);
		gc.fillGradientRectangle(1, 1, width - 1, getColumnHeaderHeight() - 1, true);

		// draw left-hand corner
		for (int i = 0; i < cornerData.length; i++) {
			byte[] b = cornerData[i];
			gc.setForeground(colorList[b[0]]);
			if (b[1] == b[3] && b[2] == b[4]) {
				gc.drawPoint(b[1], b[2]);
			} else {
				gc.drawLine(b[1], b[2], b[3], b[4]);
			}
		}

		// draw right-hand corner
		for (int i = 0; i < cornerData.length; i++) {
			byte[] b = cornerData[i];
			gc.setForeground(colorList[b[0]]);
			if (b[1] == b[3] && b[2] == b[4]) {
				gc.drawPoint(width - b[1], b[2]);
			} else {
				gc.drawLine(width - b[1], b[2], width - b[3], b[4]);
			}
		}

		header = headerImg.getImageData();
		gc.dispose();
		headerImg.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.contribution.visualiser.views.VisualiserRendering#
	 * paintColumnHeader(org.eclipse.swt.graphics.GC,
	 * org.eclipse.contribution.visualiser.interfaces.IMember, int)
	 */
	public void paintColumnHeader(GC gc, IMember m, int x, int colWidth) {
		int title_y_start = getMarginSize();
		int title_y_height = getColumnHeaderHeight();

		if ((header == null) || (header.width != colWidth)) {
			createHeader(colWidth);
		}
		Image headerImg = new Image(Display.getDefault(), header);
		gc.drawImage(headerImg, x, title_y_start);
		headerImg.dispose();

		String name = getConstrainedString(gc, m.getName(), colWidth - 4);
		int xoff = (colWidth - gc.stringExtent(name).x) / 2;
		gc.setForeground(ColorConstants.white);
		gc.drawString(name, x + xoff, title_y_start + 4, true);
		gc.setForeground(ColorConstants.black);

		gc.setForeground(outlineColor);
		gc.drawLine(x, title_y_start + cornerSize, x, title_y_start + title_y_height);
		gc.drawLine(x + cornerSize, title_y_start, x + colWidth - cornerSize, title_y_start);
		gc.drawLine(x + colWidth, title_y_start + cornerSize, x + colWidth, title_y_start + title_y_height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer#getSpacing
	 * ()
	 */
	public int getSpacing() {
		return spacing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer#
	 * getMarginSize()
	 */
	public int getMarginSize() {
		return margin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.contribution.visualiser.views.VisualiserRendering#paintColumn
	 * (org.eclipse.swt.graphics.GC,
	 * org.eclipse.contribution.visualiser.interfaces.IMember, int, boolean)
	 */
	public void paintColumn(GC gc, IMember m, int x, int yoff, int colWidth, int colHeight, boolean affected) {
		if (affected) {
			gc.setBackground(ColorConstants.white);
		} else {
			gc.setBackground(ColorConstants.buttonDarkest);
		}
		gc.fillRectangle(x, yoff, colWidth, colHeight);
		gc.drawRectangle(x, yoff, colWidth, colHeight);
	}

}
