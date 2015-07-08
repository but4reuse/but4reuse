/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Andy Clement - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.simpleImpl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMarkupKind;
import org.eclipse.core.runtime.Platform;

/**
 * Example implementation of a markup provider. It allows the superclass
 * 'SimpleMarkupProvider' to handle much of the grunt work, this subclass is
 * purely responsible for retrieving markup data from a file in a specified
 * format. The three key elements of this class are:
 * 
 * 1) initialise() is called by the org.eclipse.contribution.visualiser to get a
 * markup provider ready 2) the provider adds stripes to the
 * SimpleMarkupProvider using the method addMarkup(full_membername, stripe) 3)
 * after adding all the markups, it calls processMarkups on the superclass,
 * processMarkups analyses all the places where stripes overlap
 */
public class FileMarkupProvider extends SimpleMarkupProvider {

	private final static boolean debugLoading = false;
	private Map kinds;

	/**
	 * Initialise the provider - loads markup information from a file
	 */
	public void initialise() {
		kinds = new HashMap();
		try {
			URL url = VisualiserPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
			URL resolved = Platform.resolve(url);
			URL fileURL = new URL(resolved, "Markup.mvis"); //$NON-NLS-1$
			InputStream in = fileURL.openStream();
			loadMarkups(in);
			in.close();
		} catch (IOException ioe) {
			VisualiserPlugin.logException(ioe);
		}
	}

	/**
	 * Load the markup information from given input stream
	 * 
	 * @param in
	 */
	public void loadMarkups(InputStream in) {
		int scount = 0; // How many stripes added altogether

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();

			// Go through the file until we hit the end
			// Each line has a format like:
			// Stripe:ABC.A Kind:S1 Offset:5 Depth:1
			while (line != null && line.length() != 0) {

				// Process lines starting Stripe:
				if (line.startsWith("Stripe:")) { //$NON-NLS-1$
					String membername = null;
					String kindStr = null;
					int offset = 0;
					int depth = 1;

					// Retrieve the fully qualified membername, e.g. ABC.A
					membername = retrieveKeyValue("Stripe:", line); //$NON-NLS-1$

					// Retrieve the Kind:, e.g. S1
					kindStr = retrieveKeyValue("Kind:", line); //$NON-NLS-1$
					IMarkupKind kind;
					if (kinds.get(kindStr) instanceof IMarkupKind) {
						kind = (IMarkupKind) kinds.get(kindStr);
					} else {
						kind = new SimpleMarkupKind(kindStr);
						kinds.put(kindStr, kind);
					}

					// Retrieve the Offset:, e.g. 42
					offset = Integer.parseInt(retrieveKeyValue("Offset:", line)); //$NON-NLS-1$

					// Retrieve the Depth:, e.g. 30
					depth = Integer.parseInt(retrieveKeyValue("Depth:", line)); //$NON-NLS-1$

					// Create a new stripe and add it as a markup
					Stripe newstripe = new Stripe(kind, offset, depth);
					addMarkup(membername, newstripe);
					scount++;

					if (debugLoading)
						System.err.println("Loading new stripe: Adding " + newstripe + " for " + membername); //$NON-NLS-1$ //$NON-NLS-2$
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Problem loading markup data"); //$NON-NLS-1$
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Problem loading markup data"); //$NON-NLS-1$
			e.printStackTrace();
		}
		processMarkups();
	}

	/**
	 * Given a 'key' it looks for the key in a supplied string and returns the
	 * value after the key. For example, looking for "Fred:" in the string
	 * "Barney:40 Fred:45 Betty:40" would return "45". If values need to have
	 * spaces in then _ characters can be used, this method will translate those
	 * to spaces before it returns.
	 * 
	 * @param what
	 *            The key to look for
	 * @param where
	 *            The string to locate the key in
	 * @return the value after the key (whitespace is the value delimiter)
	 */
	private String retrieveKeyValue(String what, String where) {
		if (debugLoading)
			System.err.println("looking for '" + what + "' in '" + where + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (where.indexOf(what) == -1)
			return null;
		String postWhat = where.substring(where.indexOf(what) + what.length());
		String result = postWhat;
		if (result.indexOf(" ") != -1)result = postWhat.substring(0, postWhat.indexOf(" ")); //$NON-NLS-1$ //$NON-NLS-2$
		result = result.replace('_', ' ');
		if (debugLoading)
			System.err.println("Returning '" + result + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		return result;
	}

}
