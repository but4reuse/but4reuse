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

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.interfaces.IGroup;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * Example content provider that lets the superclass SimpleContentProvider do
 * all the grunt work. This provider just loads the data from a file, parsing it
 * and making suitable calls to the superclass to keep track of the groups and
 * members. The important features are
 * 
 * 1) initialise() is called by the org.eclipse.contribution.visualiser when it
 * starts up and discovers a content provider, typically allowing the provider
 * to 'get ready' - in this case that means loading a file and building up the
 * group/member list. 2) addGroup() is called to define a new group to the
 * SimpleContentProvider 3) members are not added directly to the simple content
 * provider - they are simply added to the group, the SimpleContentProvider then
 * finds them when it traverses the groups it knows about.
 */
public class FileContentProvider extends SimpleContentProvider {

	private final static boolean debugLoading = false;

	/**
	 * Initialise the provider - reads in the information from a file
	 */
	public void initialise() {
		if (numberOfGroupsDefined() == 0) {
			try {
				URL url = VisualiserPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
				URL resolved = Platform.resolve(url);
				URL fileURL = new URL(resolved, "Content.vis"); //$NON-NLS-1$
				InputStream in = fileURL.openStream();
				loadVisContents(in);
				in.close();
			} catch (IOException ioe) {
				VisualiserPlugin.logException(ioe);
			}
		}
	}

	/**
	 * Log the given message
	 * 
	 * @param msgType
	 * @param msg
	 * @param e
	 */
	public static void log(int msgType, String msg, Exception e) {
		// An example of how to send log data to the .metadata/.log file.
		VisualiserPlugin.getDefault().getLog()
				.log(new Status(msgType, "org.eclipse.contribution.visualiser", 0, msg, e)); //$NON-NLS-1$
	}

	/**
	 * Loads the information for a visualisation from an input stream. Entries
	 * in the file are either of the form: Group:XX or Member:Y [Size:NNN]
	 * [Tip:SSSS]
	 * 
	 * A member entry must be after a Group entry, and the member is considered
	 * a member of that group.
	 * 
	 * @param in
	 *            input stream
	 */
	public void loadVisContents(InputStream in) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			IGroup grp = null;
			while (line != null && line.length() != 0) {
				if (line.startsWith("Group:")) { //$NON-NLS-1$
					// Once a group tag is found, all following members are
					// considered to be in that group
					String grpname = retrieveKeyValue("Group:", line); //$NON-NLS-1$
					grp = new SimpleGroup(grpname);
					addGroup(grp);

				} else if (line.startsWith("Member:")) { //$NON-NLS-1$
					String memname = retrieveKeyValue("Member:", line); //$NON-NLS-1$
					IMember mem = new SimpleMember(memname);

					// Size might not be specified, so don't try parsing a null
					// into an int !
					String sizeStr = retrieveKeyValue("Size:", line); //$NON-NLS-1$			
					if (sizeStr != null)
						mem.setSize(Integer.parseInt(sizeStr));

					String tipStr = retrieveKeyValue("Tip:", line); //$NON-NLS-1$
					if (tipStr != null)
						mem.setTooltip(tipStr);

					grp.add(mem);
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			log(IStatus.ERROR, "FileContentProvider failed to load file (FNF)", e); //$NON-NLS-1$
		} catch (IOException e) {
			log(IStatus.ERROR, "FileContentProvider failed to load file (FNF)", e); //$NON-NLS-1$
		}
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
	private static String retrieveKeyValue(String what, String where) {
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
