/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: Sian January - initial version
 * ...
 **********************************************************************/

package org.eclipse.contribution.visualiser.markerImpl;

import org.eclipse.contribution.visualiser.simpleImpl.SimpleMember;
import org.eclipse.contribution.visualiser.text.VisualiserMessages;
import org.eclipse.core.resources.IResource;

/**
 * Member that has an IResource (normally a file) associated with it
 */
public class ResourceMember extends SimpleMember {

	protected IResource resource;

	/**
	 * @param name
	 *            - name
	 * @param resource
	 *            - associated IResource
	 */
	public ResourceMember(String name, IResource resource) {
		super(name);
		this.resource = resource;
	}

	/**
	 * @return Returns the resource.
	 */
	public IResource getResource() {
		return resource;
	}

	/**
	 * Get the full name for this member
	 */
	public String getFullname() {
		return resource.getProjectRelativePath().toString();
	}

	/**
	 * @param resource
	 *            The resource to set.
	 */
	public void setResource(IResource resource) {
		this.resource = resource;
	}

	public String toString() {
		return VisualiserMessages.ResourceMember
				+ ":[" + resource.getFullPath() + "] " + VisualiserMessages.Size + ":[" + size.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
