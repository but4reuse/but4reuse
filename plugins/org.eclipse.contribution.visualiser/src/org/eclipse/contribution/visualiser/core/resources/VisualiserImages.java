/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.core.resources;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Central point for Images in the Visualiser plugin
 */
public class VisualiserImages {

	// URL for the directory containing the Visualiser icons
	private static URL iconBaseURL = null;

	public static final ImageDescriptor MEMBER_VIEW = create("member_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor CLASS_VIEW = create("class_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor PACKAGE_VIEW = create("package_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor GROUP_VIEW = create("group_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor FILE_VIEW = create("file_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor FOLDER_VIEW = create("folder_view.gif"); //$NON-NLS-1$
	public static final ImageDescriptor ZOOM_IN = create("zoom_in.gif"); //$NON-NLS-1$
	public static final ImageDescriptor ZOOM_OUT = create("zoom_out.gif"); //$NON-NLS-1$
	public static final ImageDescriptor LIMIT_MODE = create("limit_mode.gif"); //$NON-NLS-1$
	public static final ImageDescriptor LOCK = create("lock.gif"); //$NON-NLS-1$
	public static final ImageDescriptor PREFERENCES = create("preferences.gif"); //$NON-NLS-1$
	public static final ImageDescriptor CHANGE_STRIPE_MODE = create("change_mode.gif"); //$NON-NLS-1$
	public static final ImageDescriptor FIT_TO_VIEW = create("fit_to_view.gif"); //$NON-NLS-1$

	/**
	 * Get the URL for an icon file
	 * 
	 * @param name
	 * @return
	 * @throws MalformedURLException
	 */
	private static URL makeIconFileURL(String name) throws MalformedURLException {
		if (iconBaseURL == null) {
			String pathSuffix = "icons/"; //$NON-NLS-1$ 
			iconBaseURL = new URL(VisualiserPlugin.getDefault().getBundle().getEntry("/"), pathSuffix); //$NON-NLS-1$
		}
		return new URL(iconBaseURL, name);
	}

	/**
	 * Create an image with the given name in the icons/cme directory.
	 * 
	 * @param name
	 * @return the ImageDescriptor created
	 */
	private static ImageDescriptor create(String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

}
