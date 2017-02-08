/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sian January - inital version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.internal.help;

import org.eclipse.contribution.visualiser.VisualiserPlugin;

/**
 * Help context ids for the Cross References UI Plug-in.
 * <p>
 * This interface contains constants only; it is not intended to be implemented
 * or extended.
 * </p>
 * 
 */
public interface IVisualiserHelpContextIds {

	public static final String PREFIX = VisualiserPlugin.PLUGIN_ID + '.';

	public static final String VISUALISER_VIEW = PREFIX + "visualiser_view_context"; //$NON-NLS-1$
	public static final String VISUALISER_MENU_VIEW = PREFIX + "visualiser_menu_view_context"; //$NON-NLS-1$

}
