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
import org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.contribution.visualiser.utils.ColorConstants;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Implements the visualiser renderer interface to provide the classic
 * visualiser drawing style.
 * 
 * @author mchapman
 */
public class ClassicVisualiserRenderer implements IVisualiserRenderer {

	private int spacing = 4;

	private int columnTitleHeight = -1;// 18;

	private int margin = 6;

	private Font sysFont;

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
	 * @see org.eclipse.contribution.visualiser.views.VisualiserRendering#
	 * getColumnHeaderHeight()
	 */
	public int getColumnHeaderHeight() {
		if (columnTitleHeight < 0) {
			sysFont = Display.getCurrent().getSystemFont();
			Image tmpImg = new Image(Display.getCurrent(), 1, 1);
			GC gc = new GC(tmpImg);
			gc.setFont(sysFont);
			String s = VisualiserMessages.Visualiser_testString;
			// determine string height, and scale up a little to increase
			// padding
			columnTitleHeight = (gc.stringExtent(s).y * 11) / 10 + 4;
			gc.dispose();
			tmpImg.dispose();
		}
		return columnTitleHeight;
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
	 * @see org.eclipse.contribution.visualiser.views.VisualiserRendering#
	 * paintColumnHeader(org.eclipse.swt.graphics.GC,
	 * org.eclipse.contribution.visualiser.interfaces.IMember, int)
	 */
	public void paintColumnHeader(GC gc, IMember m, int x, int colWidth) {
		int title_y_start = getMarginSize();
		int title_y_height = getColumnHeaderHeight();

		gc.setBackground(ColorConstants.buttonDarker);
		gc.fillRectangle(x, title_y_start, colWidth, title_y_height);
		String name = getConstrainedString(gc, m.getName(), colWidth - 4);
		int xoff = (colWidth - gc.stringExtent(name).x) / 2;
		if (sysFont == null) {
			sysFont = Display.getCurrent().getSystemFont();
		}
		gc.setFont(sysFont);
		gc.drawString(name, x + xoff, title_y_start + 4, true);

		// draw 3d effect
		gc.setForeground(ColorConstants.white);
		gc.drawLine(x, title_y_start, x, title_y_start + title_y_height);
		gc.drawLine(x, title_y_start, x + colWidth, title_y_start);
		gc.setForeground(ColorConstants.black);
		gc.drawLine(x + colWidth, title_y_start, x + colWidth, title_y_start + title_y_height);
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

	/*
	 * Shortens the given string if necessary to fit the given width
	 */
	protected String getConstrainedString(GC gc, String name, int width) {
		String elip = "..."; //$NON-NLS-1$
		if (width <= gc.stringExtent(elip).x) {
			return elip;
		}
		int sw = gc.stringExtent(name).x;
		boolean needElipsis = false;
		int targetWidth = width;
		while (sw > targetWidth) {
			if (!needElipsis) {
				needElipsis = true;
				targetWidth -= gc.stringExtent(elip).x;
			}
			name = name.substring(0, name.length() - 1);
			if (name.length() <= 1) {
				// couldn't make the string short enough, will just have to clip
				// the dots
				return elip;
			}
			sw = gc.stringExtent(name).x;
		}
		if (needElipsis) {
			name += elip;
		}
		return name;
	}

}
