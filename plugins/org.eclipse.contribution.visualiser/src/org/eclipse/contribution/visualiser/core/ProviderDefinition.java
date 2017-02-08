/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *     Matt Chapman - add priority ordering to providers
 *******************************************************************************/
package org.eclipse.contribution.visualiser.core;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.interfaces.IContentProvider;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;

/**
 * A ProviderDefinition represents the definition of a visualiser provider as
 * specified by someone extending the given extension point. They are managed by
 * the ProviderManager.
 */
public class ProviderDefinition {
	private String name;
	private String id;
	private boolean enabled = false;
	private String description = ""; //$NON-NLS-1$
	private String title;
	private IContentProvider contentInstance;
	private IMarkupProvider markupInstance;
	private int priority = 0;
	private String paletteID;
	private String emptyMessage;

	/**
	 * The constructor - requires a content provider and a markup provider
	 * 
	 * @param className
	 * @param contentP
	 * @param markupP
	 */
	public ProviderDefinition(String id, String className, IContentProvider contentP, IMarkupProvider markupP) {
		this.id = id;
		this.name = className;
		contentInstance = contentP;
		markupInstance = markupP;
	}

	/**
	 * Get the name for this provider
	 * 
	 * @return the name for this provider
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the id for this provider
	 * 
	 * @return the provider id
	 */
	public String getID() {
		return id;
	}

	/**
	 * Set the description for this provider
	 * 
	 * @param d
	 */
	public void setDescription(String d) {
		description = d;
	}

	/**
	 * Get the description of this provider
	 * 
	 * @return the description of this provider
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Enable/disable this provider (set it as current).
	 * 
	 * @param checked
	 */
	public void setEnabled(boolean checked) {
		enabled = checked;
		if (enabled) {
			ProviderManager.setCurrent(this);
			VisualiserPlugin.getDefault().getPreferenceStore().setValue(VisualiserPreferences.PROVIDER, getName());
		}
	}

	/**
	 * Get the enabled state of this provider
	 * 
	 * @return the enabled state of this provider
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Get the IContentProvider associated with this provider
	 * 
	 * @return the IContentProvider associated with this provider
	 */
	public IContentProvider getContentProvider() {
		return contentInstance;
	}

	/**
	 * Get the IMarkupProvider associated with this provider
	 * 
	 * @return the IMarkupProvider associated with this provider
	 */
	public IMarkupProvider getMarkupInstance() {
		return markupInstance;
	}

	/**
	 * Set the title for this provider - used to augment the title of the
	 * Visualiser view
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the title for this provider definition
	 * 
	 * @return the title for this provider definition
	 */
	public String getTitle() {
		if (title != null) {
			return title;
		}
		return getName();
	}

	/**
	 * Set the priority of this provider
	 * 
	 * @param priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Get the priority of this provider
	 * 
	 * @return the priority of this provider
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Set the default palette for this provider
	 * 
	 * @param paletteID
	 */
	public void setPaletteID(String paletteID) {
		this.paletteID = paletteID;
	}

	/**
	 * Get the default palette for this provider, if one has been specified
	 * 
	 * @return the default palette id String
	 */
	public String getPaletteID() {
		return paletteID;
	}

	/**
	 * Set the empty message for this provider, which is displayed when there is
	 * no data available.
	 * 
	 * @param emptyMessage
	 */
	public void setEmptyMessage(String emptyMessage) {
		this.emptyMessage = emptyMessage;
	}

	/**
	 * @return Returns the empty message.
	 */
	public String getEmptyMessage() {
		return emptyMessage;
	}
}
