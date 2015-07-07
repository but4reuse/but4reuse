/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Helen Hawkins - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.jdtImpl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.contribution.visualiser.core.Stripe;
import org.eclipse.contribution.visualiser.interfaces.IMember;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupKind;
import org.eclipse.contribution.visualiser.simpleImpl.SimpleMarkupProvider;
import org.eclipse.contribution.visualiser.utils.JDTUtils;
import org.eclipse.contribution.visualiser.utils.MarkupUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.search.JavaSearchResult;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.text.Match;
import org.eclipse.search2.internal.ui.SearchView;
import org.eclipse.ui.IPropertyListener;

/**
 * The JDT Search Results Markup Provider
 */
public class JDTSearchResultsMarkupProvider extends SimpleMarkupProvider {

	// Cache: IMember -> List(Stripe)
	private static Hashtable markupCache = new Hashtable();

	private static boolean isJavaSearch = false;
	private static JavaSearchResult javaSearchResult = null;

	public JDTSearchResultsMarkupProvider() {
	}

	public static void resetCache() {
		markupCache.clear();
	}

	/**
	 * Get a List of Stripes for the given member, which are its markups.
	 */
	public List getMemberMarkups(IMember member) {

		if (javaSearchResult == null) {
			return null;
		}

		List cachedValue = (List) markupCache.get(member);
		if (cachedValue != null) {
			return cachedValue;
		}
		List markupList = super.getMemberMarkups(member);
		if (markupList != null) {
			return markupList;
		}

		List stripeList = new ArrayList();
		if (ProviderManager.getContentProvider() instanceof JDTSearchResultsContentProvider) {
			IJavaProject jp = ((JDTSearchResultsContentProvider) ProviderManager.getContentProvider())
					.getCurrentProject();
			if (jp != null) {
				List list = getMarkupInfo(member);
				if (list == null) {
					return null;
				}

				for (Iterator iter = list.iterator(); iter.hasNext();) {
					Integer number = (Integer) iter.next();
					if (javaSearchResult != null) {
						Stripe stripe = new Stripe(new SimpleMarkupKind(javaSearchResult.getLabel()), number.intValue());
						stripeList.add(stripe);
						addMarkup(member.getFullname(), stripe);
					}
				}
			}
		}
		MarkupUtils.processStripes(stripeList);
		markupCache.put(member, stripeList);
		return stripeList;
	}

	/**
	 * Returns a list of line numbers corresponding to the search results for
	 * the given IMember
	 * 
	 * @param IMember
	 * @return List of Integers
	 */
	private List getMarkupInfo(IMember member) {

		if (javaSearchResult == null) {
			return null;
		}

		List lineNumbers = new ArrayList();
		JDTMember jdtMember;
		ICompilationUnit cu = null;
		IResource r = null;
		if (member instanceof JDTMember) {
			jdtMember = (JDTMember) member;
			IJavaElement je = jdtMember.getResource();
			if (je.getElementType() == IJavaElement.COMPILATION_UNIT) {
				cu = (ICompilationUnit) je;
				try {
					r = cu.getUnderlyingResource();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		} else {
			return null;
		}

		Object[] elementsWhichMatch = javaSearchResult.getElements();
		for (int i = 0; i < elementsWhichMatch.length; i++) {
			IFile file = javaSearchResult.getFile(elementsWhichMatch[i]);
			if (file != null && (file.getFullPath().equals(r.getFullPath()))) {
				Match[] matches = javaSearchResult.computeContainedMatches(javaSearchResult, file);
				for (int j = 0; j < matches.length; j++) {
					int lineNumber = JDTUtils.getLineNumber(cu, matches[j].getOffset());
					lineNumbers.add(new Integer(lineNumber));
				}
			}
		}
		return lineNumbers;
	}

	/**
	 * Get all the markup kinds - which in this case is the label for the last
	 * run search (if it was a java search)
	 * 
	 * @return a Set of Strings
	 */
	public SortedSet getAllMarkupKinds() {
		SortedSet kinds = new TreeSet();
		if (ProviderManager.getContentProvider() instanceof JDTSearchResultsContentProvider) {
			if (javaSearchResult != null) {
				kinds.add(new SimpleMarkupKind(javaSearchResult.getLabel()));
			}
		}
		if (kinds.size() > 0) {
			return kinds;
		}
		return null;
	}

	/**
	 * Process a mouse click on a stripe. This method opens the editor at the
	 * line of the stripe clicked.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IMarkupProvider#processMouseclick(org.eclipse.contribution.visualiser.interfaces.IMember,
	 *      org.eclipse.contribution.visualiser.core.Stripe, int)
	 */
	public boolean processMouseclick(IMember member, Stripe stripe, int buttonClicked) {
		if (buttonClicked == 1) {
			if (member instanceof JDTMember) {
				IJavaElement jEl = ((JDTMember) member).getResource();
				if (jEl != null) {
					JDTUtils.openInEditor(jEl.getResource(), stripe.getOffset());
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Static inner class VisualiserPropertyListener which responds to changes
	 * in the Search View.
	 */
	static class VisualiserPropertyListener implements IPropertyListener {

		public void propertyChanged(Object source, int propId) {
			if (source instanceof SearchView) {
				SearchView searchView = (SearchView) source;
				ISearchResult searchResult = searchView.getCurrentSearchResult();
				if (searchResult instanceof JavaSearchResult) {
					isJavaSearch = true;
					if (((JavaSearchResult) searchResult).equals(javaSearchResult)) {
					} else {
						resetCache();
					}
					javaSearchResult = (JavaSearchResult) searchResult;
				} else {
					isJavaSearch = false;
					javaSearchResult = null;
					// need to refresh() visualiser here otherwise when use
					// history
					// to populate the search view with a search result which
					// isn't
					// a java one, then takes quite a few clicks to populate the
					// visualiser

				}
				VisualiserPlugin.refresh();
			}
		}
	}

	/**
	 * @return Returns whether the last run search is a JavaSearch or not.
	 */
	public boolean isJavaSearch() {
		return isJavaSearch;
	}

	/**
	 * Sets whether the search currently in the search results view is a
	 * JavaSearch or not
	 * 
	 * @param isJavaSearch
	 */
	public void setJavaSearch(boolean isJavaSearch) {
		JDTSearchResultsMarkupProvider.isJavaSearch = isJavaSearch;
	}

	/**
	 * Sets the last run JavaSearchResult
	 * 
	 * @param javaSearchResult
	 */
	public void setJavaSearchResult(JavaSearchResult javaSearchResult) {
		JDTSearchResultsMarkupProvider.javaSearchResult = javaSearchResult;
	}
}
