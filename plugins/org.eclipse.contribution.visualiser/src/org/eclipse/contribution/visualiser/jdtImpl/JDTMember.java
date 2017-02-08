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
package org.eclipse.contribution.visualiser.jdtImpl;

import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.jdt.core.IJavaElement;

/**
 * The JDT implementation of IMember.
 */
public class JDTMember extends SimpleMember {

	private IJavaElement javaElement;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *            - the member's name
	 * @param je
	 *            - the corresponding IJavaElement
	 */
	public JDTMember(String name, IJavaElement je) {
		super(name);
		javaElement = je;
	}

	/**
	 * Gets the Java element represented by this member
	 * 
	 * @return the IJavaElement represented
	 */
	public IJavaElement getResource() {
		return javaElement;
	}

	public String toString() {
		return VisualiserMessages.JDTMember
				+ ":[" + fullname + "] " + VisualiserMessages.Size + ":[" + size.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
