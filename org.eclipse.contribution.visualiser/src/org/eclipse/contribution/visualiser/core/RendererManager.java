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
package org.eclipse.contribution.visualiser.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;
import org.eclipse.contribution.visualiser.renderers.DefaultVisualiserRenderer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * The renderer manager parses the contents of the defined extensions to the
 * extension-point org.eclipse.contribution.visualiser.renderers.
 * 
 * @author mchapman
 */
public class RendererManager {

	// the name of the extension point
	public static final String RENDERER_EXTENSION = "org.eclipse.contribution.visualiser.renderers"; //$NON-NLS-1$

	// the class name of the renderer which is the default one
	private static final String DEFAULT_RENDERER_CLASS = "org.eclipse.contribution.visualiser.renderers.DefaultVisualiserRenderer"; //$NON-NLS-1$

	private static List /* RendererDefinition */renderers;

	private static RendererDefinition current;

	/**
	 * Get a list of all the registered renderers
	 * 
	 * @return a list of RendererDefinition objects
	 */
	public static List /* RendererDefinition */getAllRendererDefinitions() {
		if (renderers == null) {
			initialiseRendererDefinitions();
		}
		return renderers;
	}

	/**
	 * Get the current renderer, either as set by the preferences, or if not set
	 * the default renderer
	 * 
	 * @return the current renderer
	 */
	public static RendererDefinition getCurrentRenderer() {
		if (current == null) {
			String name = VisualiserPreferences.getRendererName();
			if ((name != null) && (name.length() > 0)) {
				// find the renderer with the given name
				for (Iterator iter = getAllRendererDefinitions().iterator(); iter.hasNext();) {
					RendererDefinition r = (RendererDefinition) iter.next();
					if (r.getName().equals(name)) {
						current = r;
					}
				}
			}
			if (current == null) {
				// didn't find the given renderer, revert to default
				current = getDefaultRenderer();
			}
		}
		return current;
	}

	/**
	 * Return the renderer definition with the given name, or null if not found
	 * 
	 * @param name
	 * @return the RendererDefinition with the given name
	 */
	public static RendererDefinition getRendererByName(String name) {
		for (Iterator iter = getAllRendererDefinitions().iterator(); iter.hasNext();) {
			RendererDefinition r = (RendererDefinition) iter.next();
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Search for a registered renderer with the given name and if found, sets
	 * that renderer to be the current one
	 * 
	 * @param name
	 *            the name of the renderer
	 */
	public static void setCurrentRendererByName(String name) {
		RendererDefinition r = getRendererByName(name);
		if (r != null) {
			current = r;
		}
	}

	/**
	 * Get the defined default renderer (should only be used when the user
	 * hasn't specified a renderer)
	 * 
	 * @return the default RendererDefinition
	 */
	public static RendererDefinition getDefaultRenderer() {
		if (renderers == null) {
			initialiseRendererDefinitions();
		}
		for (Iterator iter = renderers.iterator(); iter.hasNext();) {
			RendererDefinition r = (RendererDefinition) iter.next();
			if (r.getRenderer() instanceof DefaultVisualiserRenderer) {
				if (r.getRenderer().getClass().getName().equals(DEFAULT_RENDERER_CLASS)) {
					return r;
				}
			}
		}
		return null;
	}

	/**
	 * Find the registered renderers from the defined extension point
	 */
	private static void initialiseRendererDefinitions() {
		renderers = new ArrayList();
		IExtensionPoint exP = Platform.getExtensionRegistry().getExtensionPoint(RENDERER_EXTENSION);
		IExtension[] exs = exP.getExtensions();

		for (int i = 0; i < exs.length; i++) {
			IConfigurationElement[] ces = exs[i].getConfigurationElements();
			for (int j = 0; j < ces.length; j++) {
				try {
					Object ext = ces[j].createExecutableExtension("class"); //$NON-NLS-1$
					if (ext instanceof IVisualiserRenderer) {
						String name = ces[j].getAttribute("name"); //$NON-NLS-1$
						RendererDefinition rd = new RendererDefinition(name, (IVisualiserRenderer) ext);
						renderers.add(rd);
					}
				} catch (CoreException e) {
					VisualiserPlugin.logException(e);
				}
			}
		}
	}
}