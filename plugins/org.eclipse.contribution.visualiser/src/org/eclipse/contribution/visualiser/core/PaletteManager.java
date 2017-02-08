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
import org.eclipse.contribution.visualiser.interfaces.IVisualiserPalette;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;
import org.eclipse.contribution.visualiser.palettes.DefaultVisualiserPalette;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * The palette manager parses the contents of the defined extensions to the
 * extension-point org.eclipse.contribution.visualiser.palettes.
 */
public class PaletteManager {

	// the name of the extension point
	public static final String PALETTE_EXTENSION = "org.eclipse.contribution.visualiser.palettes"; //$NON-NLS-1$

	// the class name of the palette which is the default one
	private static final String DEFAULT_PALETTE_CLASS = "org.eclipse.contribution.visualiser.palettes.DefaultVisualiserPalette"; //$NON-NLS-1$

	private static List /* PaletteDefinition */palettes;

	private static PaletteDefinition current;

	/**
	 * Get a list of all the registered palettes
	 * 
	 * @return a list of PaletteDefinition objects
	 */
	public static List /* PaletteDefinition */getAllPaletteDefinitions() {
		if (palettes == null) {
			initialisePaletteDefinitions();
		}
		return palettes;
	}

	/**
	 * Get the current palette, either as set by the preferences, or if not set
	 * the default palette specified by the current provider, or if that is not
	 * set, use the default palette implementation
	 * 
	 * @return the current palette
	 */
	public static PaletteDefinition getCurrentPalette() {
		if (current == null) {
			String pid = VisualiserPreferences.getPaletteIDForProvider(ProviderManager.getCurrent().getID());
			if ((pid != null) && (pid.length() > 0)) {
				// find the palette with the given id
				current = getPaletteByID(pid);
			}
			if (current == null) {
				// didn't find the given palette, try the provider
				pid = ProviderManager.getCurrent().getPaletteID();
				if ((pid != null) && (pid.length() > 0)) {
					current = getPaletteByID(pid);
				}
			}
			if (current == null) {
				// still no luck, resort to default
				current = getDefaultPalette();
			}
		}
		return current;
	}

	/**
	 * Return the palette definition with the given name, or null if not found
	 * 
	 * @param name
	 * @return the PaletteDefinition found, or null
	 */
	public static PaletteDefinition getPaletteByName(String name) {
		for (Iterator iter = getAllPaletteDefinitions().iterator(); iter.hasNext();) {
			PaletteDefinition r = (PaletteDefinition) iter.next();
			if (r.getName().equals(name)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Return the palette definition with the given id, or null if not found
	 * 
	 * @param id
	 * @return the PaletteDefinition with the given id, or null
	 */
	public static PaletteDefinition getPaletteByID(String id) {
		for (Iterator iter = getAllPaletteDefinitions().iterator(); iter.hasNext();) {
			PaletteDefinition r = (PaletteDefinition) iter.next();
			if (r.getID().equals(id)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Reset the current choice of palette, so that next time it is requested,
	 * the usual process is used to determine the chosen palette. This should be
	 * called whenever the provider is changed, in case the palette needs to
	 * change accordingly.
	 * 
	 */
	public static void resetCurrent() {
		current = null;
	}

	/**
	 * Search for a registered palette with the given name and if found, sets
	 * that palette to be the current one
	 * 
	 * @param name
	 *            the name of the palette
	 */
	public static void setCurrentPaletteByName(String name) {
		PaletteDefinition r = getPaletteByName(name);
		if (r != null) {
			current = r;
		}
	}

	/**
	 * Get the defined default palette
	 * 
	 * @return the default PaletteDefinition
	 */
	public static PaletteDefinition getDefaultPalette() {
		if (palettes == null) {
			initialisePaletteDefinitions();
		}
		for (Iterator iter = palettes.iterator(); iter.hasNext();) {
			PaletteDefinition r = (PaletteDefinition) iter.next();
			if (r.getPalette() instanceof DefaultVisualiserPalette) {
				if (r.getPalette().getClass().getName().equals(DEFAULT_PALETTE_CLASS)) {
					return r;
				}
			}
		}
		return null;
	}

	public static PaletteDefinition getDefaultForProvider(ProviderDefinition def) {
		PaletteDefinition r = getPaletteByID(def.getPaletteID());
		if (r == null) {
			r = PaletteManager.getDefaultPalette();
		}
		return r;
	}

	/**
	 * Find the registered palette from the defined extension point
	 */
	private static void initialisePaletteDefinitions() {
		palettes = new ArrayList();
		IExtensionPoint exP = Platform.getExtensionRegistry().getExtensionPoint(PALETTE_EXTENSION);
		IExtension[] exs = exP.getExtensions();

		for (int i = 0; i < exs.length; i++) {
			IConfigurationElement[] ces = exs[i].getConfigurationElements();
			for (int j = 0; j < ces.length; j++) {
				try {
					Object ext = ces[j].createExecutableExtension("class"); //$NON-NLS-1$
					if (ext instanceof IVisualiserPalette) {
						String name = ces[j].getAttribute("name"); //$NON-NLS-1$
						String id = ces[j].getAttribute("id"); //$NON-NLS-1$
						PaletteDefinition rd = new PaletteDefinition(id, name, (IVisualiserPalette) ext);
						palettes.add(rd);
					}
				} catch (CoreException e) {
					VisualiserPlugin.logException(e);
				}
			}
		}
	}
}