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

import org.eclipse.contribution.visualiser.interfaces.IVisualiserPalette;

/**
 * A PaletteDefinition represents the definition of a visualiser colour set as
 * specified by someone extending the given extension point. Palette definitions
 * are managed by the PaletteManager.
 */
public class PaletteDefinition {
	private String id;

	private String name;

	private IVisualiserPalette palette;

	/**
	 * Create a new palette definition
	 * 
	 * @param id
	 *            the id of the palette
	 * @param name
	 *            the name of the palette
	 * @param palette
	 *            the actual palette instance
	 */
	public PaletteDefinition(String id, String name, IVisualiserPalette palette) {
		this.id = id;
		this.name = name;
		this.palette = palette;
	}

	/**
	 * Get the name of the palette represented by this palette definition
	 * 
	 * @return the palette name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the id of the palette represented by this palette definition
	 * 
	 * @return the palette id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Get the actual palette represented by this palette definition
	 * 
	 * @return the palette instance
	 */
	public IVisualiserPalette getPalette() {
		return palette;
	}
}