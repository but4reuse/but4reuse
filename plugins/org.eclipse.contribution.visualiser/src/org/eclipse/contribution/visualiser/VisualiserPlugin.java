/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *     Sian January - added Jobs support
 *******************************************************************************/
package org.eclipse.contribution.visualiser;

import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.contribution.visualiser.internal.preference.VisualiserPreferences;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.contribution.visualiser.views.Menu;
import org.eclipse.contribution.visualiser.views.Visualiser;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class for the Visualiser plugin.
 */
public class VisualiserPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.contribution.visualiser"; //$NON-NLS-1$

	public static int LOGLEVEL = 0;

	public static Visualiser visualiser;

	public static Menu menu;

	private static VisualiserPlugin plugin;

	/**
	 * 3.0 compatible Plugin constructor
	 */
	public VisualiserPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Getter method for the provider manager
	 * 
	 * @return the provider manager
	 */
	public static ProviderManager getProviderManager() {
		return ProviderManager.getProviderManager();
	}

	/**
	 * Refresh the Visualiser views.
	 */
	public static void refresh() {
		if (visualiser != null && visualiser.getSite() != null) {
			if (visualiser.getSite().getPage().isPartVisible(visualiser)) {
				VisualiserUpdateJob.getInstance().schedule();
			} else {
				visualiser.setNeedsUpdating();
			}
		}
	}

	/**
	 * Gets the active workbench window
	 */
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * Returns the shared instance as VisualiserPlugin is a singleton.
	 */
	public static VisualiserPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Set the Visualiser view
	 */
	public void setVisualiser(Visualiser visualiser) {
		VisualiserPlugin.visualiser = visualiser;
		// When the visualiser is known, tell it where to get its data from
		// and activate the content provider
		VisualiserPlugin.visualiser.setVisContentProvider(ProviderManager.getContentProvider());
		VisualiserPlugin.visualiser.setVisMarkupProvider(ProviderManager.getMarkupProvider());
		ProviderManager.getContentProvider().activate();
		if (VisualiserPlugin.menu != null) {
			VisualiserPlugin.refresh();
		}
	}

	/**
	 * Remove the Visualiser view
	 */
	public void removeVisualiser() {
		VisualiserPlugin.visualiser = null;
	}

	/**
	 * Set the Visualiser Menu view
	 */
	public void setMenu(Menu menu) {
		VisualiserPlugin.menu = menu;
		// When the menu is known, tell it where to get its data from
		// and activate the markup provider
		VisualiserPlugin.menu.setVisMarkupProvider(ProviderManager.getMarkupProvider());
		ProviderManager.getMarkupProvider().activate();
		if (VisualiserPlugin.visualiser != null) {
			VisualiserPlugin.refresh();
		}
	}

	/**
	 * Remove the Visualiser Menu view
	 */
	public void removeMenu() {
		VisualiserPlugin.menu = null;
	}

	/**
	 * Log the given message at the given log level.
	 * 
	 * @param logLevel
	 * @param message
	 */
	public static void log(int logLevel, String message) {
		if (logLevel <= LOGLEVEL) {
			System.err.println(message);
			VisualiserPlugin.getDefault().getLog()
					.log(new Status(Status.INFO, "org.eclipse.contribution.visualiser", 0, message, null)); //$NON-NLS-1$
		}
	}

	/**
	 * Write the given exception or error to the log file (without displaying a
	 * dialog)
	 * 
	 * @param e
	 */
	public static void logException(Throwable e) {
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			String message = e.getMessage();
			if (message == null) {
				message = e.toString();
			}
			status = new Status(IStatus.ERROR, VisualiserPlugin.PLUGIN_ID, IStatus.OK, message, e);
		}
		getDefault().getLog().log(status);
	}

	/**
	 * This method is called upon plug-in activation - process any defined
	 * extensions and add the resource change listener.
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		ProviderManager.initialise();
		VisualiserPreferences.initDefaults();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}
}

/*
 * Job that updates the Visualiser This update job includes collecting the
 * visualization data, wheras the two redraw jobs in the Visualiser and Menu
 * classes only cover the redrawing.
 */
class VisualiserUpdateJob extends Job {
	private static VisualiserUpdateJob theJob;

	private VisualiserUpdateJob(String name) {
		super(name);
	}

	public static VisualiserUpdateJob getInstance() {
		if (theJob == null) {
			theJob = new VisualiserUpdateJob(VisualiserMessages.Jobs_VisualiserUpdate);
			theJob.setUser(true);
			theJob.setPriority(Job.INTERACTIVE);
		}
		return theJob;
	}

	public IStatus run(IProgressMonitor monitor) {
		if (VisualiserPlugin.visualiser != null) {
			VisualiserPlugin.visualiser.updateDisplay(true, monitor);
		}
		return Status.OK_STATUS;
	}

}
