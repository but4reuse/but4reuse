/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *     Matt Chapman - add priority ordering to providers + paletteID
 *******************************************************************************/
package org.eclipse.contribution.visualiser.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.interfaces.IContentProvider;
import org.eclipse.contribution.visualiser.interfaces.IMarkupProvider;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;
import org.eclipse.contribution.visualiser.simpleImpl.NullMarkupProvider;
import org.eclipse.contribution.visualiser.views.Visualiser;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

/**
 * The provider manager parses the contents of the defined extensions to the
 * extension-point org.eclipse.contribution.visualiser.providers. Two extensions
 * are allowed - one for the content of the visualiser and one for the markups
 * within the content of the visualiser. The markup one can be left out - but it
 * rather limits the usefulness of the visualiser.
 */
public class ProviderManager {

	// the name of the extension point
	public static final String PROVIDER_EXTENSION = "org.eclipse.contribution.visualiser.providers"; //$NON-NLS-1$

	private static IContentProvider contentP = null;

	private static IMarkupProvider markupP = null;

	private static ProviderManager instance = new ProviderManager();

	private static List contentProviders;

	private static ProviderDefinition currentPD = null;

	/**
	 * Get the currently active content provider
	 * 
	 * @return the currently active content provider
	 */
	public static IContentProvider getContentProvider() {
		return contentP;
	}

	/**
	 * Get the currently active markup provider
	 * 
	 * @return the currently active markup provider
	 */
	public static IMarkupProvider getMarkupProvider() {
		return markupP;
	}

	/**
	 * Private constructor - use getProviderManager because ProviderManager is a
	 * singleton
	 */
	private ProviderManager() {
	}

	/**
	 * Initialise the provider manager - read in the definitions for all
	 * providers and initialise the content provider and markup provider classes
	 */
	public static void initialise() {
		contentProviders = new ArrayList();
		IExtensionPoint exP = Platform.getExtensionRegistry().getExtensionPoint(PROVIDER_EXTENSION);
		IExtension[] exs = exP.getExtensions();

		for (int i = 0; i < exs.length; i++) {
			IConfigurationElement[] ces = exs[i].getConfigurationElements();
			for (int j = 0; j < ces.length; j++) {
				try {
					Object ext = ces[j].createExecutableExtension("contentProviderClass"); //$NON-NLS-1$

					if (ext instanceof IContentProvider) {
						markupP = (IMarkupProvider) ces[j].createExecutableExtension("markupProviderClass"); //$NON-NLS-1$
						markupP.initialise();
						contentP = (IContentProvider) ext;
						contentP.initialise();
						ProviderDefinition cpdef = new ProviderDefinition(ces[j].getAttribute("id"), //$NON-NLS-1$
								ces[j].getAttribute("name"), contentP, markupP); //$NON-NLS-1$
						contentProviders.add(cpdef);
						String desc = ces[j].getAttribute("description"); //$NON-NLS-1$
						if (desc != null)
							cpdef.setDescription(desc);
						String title = ces[j].getAttribute("title"); //$NON-NLS-1$
						if (title != null) {
							cpdef.setTitle(title);
						}
						String priorityString = ces[j].getAttribute("priority"); //$NON-NLS-1$
						if (priorityString != null) {
							try {
								cpdef.setPriority(new Integer(priorityString).intValue());
							} catch (NumberFormatException e) {
							}
						}
						String paletteID = ces[j].getAttribute("paletteid"); //$NON-NLS-1$
						if (paletteID != null) {
							cpdef.setPaletteID(paletteID);
						}
						String emptyMessage = ces[j].getAttribute("emptyMessage"); //$NON-NLS-1$
						if (emptyMessage != null) {
							cpdef.setEmptyMessage(emptyMessage);
						}

					}
				} catch (Exception ex) {
					System.err.println(ex);
					ex.printStackTrace();
				}
			}
		}
		if (markupP == null)
			markupP = new NullMarkupProvider();

		if (contentProviders.size() != 0) {
			// sort providers according to priority, highest priority first
			Collections.sort(contentProviders, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((ProviderDefinition) o2).getPriority() - ((ProviderDefinition) o1).getPriority();
				}
			});
			// If the user has previously selected a provider set it to be
			// selected,
			// otherwise select the first one - the one with the highest
			// priority
			String provider = VisualiserPreferences.getProvider();
			boolean set = false;
			for (int i = 0; i < contentProviders.size(); i++) {
				ProviderDefinition cp = (ProviderDefinition) contentProviders.get(i);
				String name = cp.getName();
				if (provider.equals(name)) {
					cp.setEnabled(true);
					set = true;
					break;
				}
			}
			if (!set) {
				ProviderDefinition cp = (ProviderDefinition) contentProviders.get(0);
				cp.setEnabled(true);
			}
		}
		if (VisualiserPlugin.menu != null) {
			VisualiserPlugin.menu.reset();
		}
	}

	/**
	 * Get the single instance of the ProviderManager
	 * 
	 * @return the single instance of the ProviderManager
	 */
	public static ProviderManager getProviderManager() {
		return instance;
	}

	/**
	 * Get all provider definitions
	 */
	public static ProviderDefinition[] getAllProviderDefinitions() {
		return (ProviderDefinition[]) contentProviders.toArray(new ProviderDefinition[0]);
	}

	/**
	 * Get the current provider definition
	 * 
	 * @return the current provider definition
	 */
	public static ProviderDefinition getCurrent() {
		return currentPD;
	}

	/**
	 * Set the current provider definition. Activates the asociated content and
	 * markup providers
	 * 
	 * @param definition
	 */
	public static void setCurrent(ProviderDefinition definition) {
		boolean needToUpdateVisualiser = false;
		currentPD = definition;
		// TODO: Ought to compare the provider instance rather than elements of
		// it
		if (!contentP.equals(definition.getContentProvider()))
			needToUpdateVisualiser = true;
		if (!markupP.equals(definition.getMarkupInstance()))
			needToUpdateVisualiser = true;

		PaletteManager.resetCurrent();

		// De-activate the previous provider
		markupP.deactivate();
		contentP.deactivate();

		contentP = definition.getContentProvider();
		markupP = definition.getMarkupInstance();

		// Activate the new provider
		markupP.activate();
		contentP.activate();

		if (needToUpdateVisualiser) {
			if (VisualiserPlugin.visualiser != null) {
				VisualiserPlugin.visualiser.setVisContentProvider(contentP);
				VisualiserPlugin.visualiser.setVisMarkupProvider(markupP);
			}
			if (VisualiserPlugin.menu != null) {
				VisualiserPlugin.menu.setVisMarkupProvider(markupP);
			}
			VisualiserPlugin.refresh();
		}
		String visTitle = definition.getTitle();
		Visualiser visualiser = VisualiserPlugin.visualiser;
		if (visualiser != null) {
			if (visTitle != null) {
				visualiser.refreshTitle(visTitle);
			} else {
				visualiser.refreshTitle(definition.getName());
			}
		}
	}
}