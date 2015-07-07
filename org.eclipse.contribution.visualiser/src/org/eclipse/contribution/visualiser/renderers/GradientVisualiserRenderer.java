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
import org.eclipse.swt.graphics.GC;

/**
 * Just like the "default" style, but gradient filled.
 * 
 * @author mchapman
 */
public class GradientVisualiserRenderer extends DefaultVisualiserRenderer {

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
			gc.setForeground(ColorConstants.white);
			gc.setBackground(lightGrayGradientColor);
			gc.fillGradientRectangle(x, yoff, colWidth, colHeight, true);
		} else {
			gc.setForeground(ColorConstants.darkGray);
			gc.setBackground(ColorConstants.lightGray);
			gc.fillGradientRectangle(x, yoff, colWidth, colHeight, true);
		}
		gc.setForeground(outlineColor);
		gc.drawRectangle(x, yoff, colWidth, colHeight);
	}

}
