/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.renderers;

import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.utils.ColorConstants;
import org.eclipse.swt.graphics.GC;

public class CylinderRenderer extends GradientVisualiserRenderer {

	/**
	 * Paint cylinders, with 'highlight' just a bit to the left of center.
	 */
	public void paintColumn(GC gc, IMember m, int x, int yoff, int colWidth, int colHeight, boolean affected) {

		if (affected) {
			gc.setForeground(lightGrayGradientColor);
			gc.setBackground(ColorConstants.white);
			gc.fillGradientRectangle(x, yoff, (int) (colWidth * 0.33) + 1, colHeight, false);
			gc.setBackground(lightGrayGradientColor);
			gc.setForeground(ColorConstants.white);
			gc.fillGradientRectangle(x + ((int) (colWidth * 0.33)), yoff, (int) (colWidth * 0.66) + 1, colHeight, false);
		} else {
			gc.setForeground(ColorConstants.darkGray);
			gc.setBackground(ColorConstants.lightGray);
			gc.fillGradientRectangle(x, yoff, (int) (colWidth * 0.33) + 1, colHeight, false);
			gc.setForeground(ColorConstants.lightGray);
			gc.setBackground(ColorConstants.darkGray);
			gc.fillGradientRectangle(x + ((int) (colWidth * 0.33)) + 1, yoff, (int) (colWidth * 0.66) + 1, colHeight,
					false);
		}
		gc.setForeground(outlineColor);
		gc.drawRectangle(x, yoff, colWidth, colHeight);
	}
}
