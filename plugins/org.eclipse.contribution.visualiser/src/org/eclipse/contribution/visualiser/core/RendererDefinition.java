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

import org.eclipse.contribution.visualiser.interfaces.IVisualiserRenderer;

/**
 * A RendererDefinition represents the definition of a visualiser drawing style
 * as specified by someone extending the given extension point. They are managed
 * by the RendererManager.
 * 
 * @author mchapman
 */
public class RendererDefinition {
	private String name;

	private IVisualiserRenderer renderer;

	/**
	 * Create a new renderer definition
	 * 
	 * @param name
	 *            the name of the renderer
	 * @param renderer
	 *            the actual renderer instance
	 */
	public RendererDefinition(String name, IVisualiserRenderer renderer) {
		this.name = name;
		this.renderer = renderer;
	}

	/**
	 * Get the name of the renderer represented by this renderer definition
	 * 
	 * @return the renderer name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the actual renderer represented by this renderer definition
	 * 
	 * @return the renderer instance
	 */
	public IVisualiserRenderer getRenderer() {
		return renderer;
	}
}