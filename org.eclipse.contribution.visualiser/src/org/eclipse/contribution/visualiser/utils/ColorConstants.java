/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.contribution.visualiser.utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of color-related constants.
 */
/* copied from org.eclipse.draw2d.ColorConstants */
public interface ColorConstants {

	/**
	 * @see SWT#COLOR_WIDGET_HIGHLIGHT_SHADOW
	 */
	Color buttonLightest = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	/**
	 * @see SWT#COLOR_WIDGET_BACKGROUND
	 */
	Color button = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	/**
	 * @see SWT#COLOR_WIDGET_NORMAL_SHADOW
	 */
	Color buttonDarker = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	/**
	 * @see SWT#COLOR_WIDGET_DARK_SHADOW
	 */
	Color buttonDarkest = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);

	/**
	 * @see SWT#COLOR_LIST_BACKGROUND
	 */
	Color listBackground = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	/**
	 * @see SWT#COLOR_LIST_FOREGROUND
	 */
	Color listForeground = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

	/**
	 * @see SWT#COLOR_WIDGET_BACKGROUND
	 */
	Color menuBackground = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	/**
	 * @see SWT#COLOR_WIDGET_FOREGROUND
	 */
	Color menuForeground = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
	/**
	 * @see SWT#COLOR_LIST_SELECTION
	 */
	Color menuBackgroundSelected = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION);
	/**
	 * @see SWT#COLOR_LIST_SELECTION_TEXT
	 */
	Color menuForegroundSelected = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);

	/**
	 * @see SWT#COLOR_TITLE_BACKGROUND
	 */
	Color titleBackground = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
	/**
	 * @see SWT#COLOR_TITLE_BACKGROUND_GRADIENT
	 */
	Color titleGradient = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	/**
	 * @see SWT#COLOR_TITLE_FOREGROUND
	 */
	Color titleForeground = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
	 */
	Color titleInactiveForeground = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
	 */
	Color titleInactiveBackground = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	/**
	 * @see SWT#COLOR_TITLE_INACTIVE_FOREGROUND
	 */
	Color titleInactiveGradient = Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);

	/**
	 * @see SWT#COLOR_INFO_FOREGROUND
	 */
	Color tooltipForeground = Display.getCurrent().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
	/**
	 * @see SWT#COLOR_INFO_BACKGROUND
	 */
	Color tooltipBackground = Display.getCurrent().getSystemColor(SWT.COLOR_INFO_BACKGROUND);

	/*
	 * Misc. colors
	 */
	/** One of the pre-defined colors */
	Color white = new Color(null, 255, 255, 255);
	/** One of the pre-defined colors */
	Color lightGray = new Color(null, 192, 192, 192);
	/** One of the pre-defined colors */
	Color gray = new Color(null, 128, 128, 128);
	/** One of the pre-defined colors */
	Color darkGray = new Color(null, 64, 64, 64);
	/** One of the pre-defined colors */
	Color black = new Color(null, 0, 0, 0);
	/** One of the pre-defined colors */
	Color red = new Color(null, 255, 0, 0);
	/** One of the pre-defined colors */
	Color orange = new Color(null, 255, 196, 0);
	/** One of the pre-defined colors */
	Color yellow = new Color(null, 255, 255, 0);
	/** One of the pre-defined colors */
	Color green = new Color(null, 0, 255, 0);
	/** One of the pre-defined colors */
	Color lightGreen = new Color(null, 96, 255, 96);
	/** One of the pre-defined colors */
	Color darkGreen = new Color(null, 0, 127, 0);
	/** One of the pre-defined colors */
	Color cyan = new Color(null, 0, 255, 255);
	/** One of the pre-defined colors */
	Color lightBlue = new Color(null, 127, 127, 255);
	/** One of the pre-defined colors */
	Color blue = new Color(null, 0, 0, 255);
	/** One of the pre-defined colors */
	Color darkBlue = new Color(null, 0, 0, 127);

}
