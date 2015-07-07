/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.utils;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;

/**
 * Utility class for common JDT functions required by providers
 */
public class JDTUtils {

	/**
	 * Open the resource in the editor and highlight the given line if greater
	 * then zero.
	 * 
	 * @param res
	 *            the resource
	 * @param lineNumber
	 *            the number of the line to be selected
	 */
	public static void openInEditor(IResource res, int lineNumber) {
		try {
			IMarker marker = res.createMarker(IMarker.MARKER);
			if (lineNumber >= 0) {
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			}
			openInEditor(marker);
			marker.delete();
		} catch (CoreException coreEx) {
			VisualiserPlugin.logException(coreEx);
		}
	}

	/**
	 * Open the editor at the location of the given marker.
	 * 
	 * @param marker
	 */
	public static void openInEditor(IMarker marker) {
		IWorkbenchPage page = VisualiserPlugin.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, marker);
		} catch (PartInitException e) {
			VisualiserPlugin.logException(e);
		}
	}

	/**
	 * Get the line number for the class declaration in the given IJavaElement,
	 * which should be an ICompilationUnit. If not found returns 0.
	 * 
	 * @param jElem
	 * @return line number
	 */
	public static int getClassDeclLineNum(IJavaElement jElem) {
		if (jElem instanceof ICompilationUnit) {
			try {
				ICompilationUnit cUnit = (ICompilationUnit) jElem;
				IType type = cUnit.findPrimaryType();
				if (type != null) {
					return getLineNumFromOffset(cUnit, type.getNameRange().getOffset());
				}
			} catch (JavaModelException jme) {
				jme.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * Get the line number for the given offset in the given ICompilationUnit
	 * 
	 * @param ICompilationUnit
	 * @param offSet
	 * 
	 * @return int lineNumber
	 */
	private static int getLineNumFromOffset(ICompilationUnit cUnit, int offSet) {
		try {
			String source = cUnit.getSource();
			IType type = cUnit.findPrimaryType();
			if (type != null) {
				String sourcetodeclaration = source.substring(0, offSet);
				int lines = 0;
				char[] chars = new char[sourcetodeclaration.length()];
				sourcetodeclaration.getChars(0, sourcetodeclaration.length(), chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (chars[i] == '\n') {
						lines++;
					}
				}
				return lines + 1;
			}
		} catch (JavaModelException jme) {
			jme.printStackTrace();
		}
		return 0;
	}

	public static int getLineNumber(ICompilationUnit cUnit, int offSet) {
		return getLineNumFromOffset(cUnit, offSet);
	}

}
