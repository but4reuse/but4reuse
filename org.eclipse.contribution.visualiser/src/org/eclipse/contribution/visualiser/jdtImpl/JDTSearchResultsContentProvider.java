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
import java.util.List;

import org.eclipse.contribution.visualiser.core.ProviderManager;
import org.eclipse.contribution.visualiser.jdtImpl.JDTSearchResultsMarkupProvider.VisualiserPropertyListener;
import org.eclipse.jdt.internal.ui.search.JavaSearchResult;
import org.eclipse.search.internal.ui.SearchPlugin;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.search2.internal.ui.SearchView;

/**
 * The JDT Search Results Content Provider
 */
public class JDTSearchResultsContentProvider extends JDTContentProvider {

	private static VisualiserPropertyListener visListenerInstance = null;

	private ISearchResultViewPart searchView = null;

	/**
	 * Get all members - returns null if last search to populate the search view
	 * wasn't a java search.
	 * 
	 * @see org.eclipse.contribution.visualiser.interfaces.IContentProvider#getAllMembers()
	 */
	public List getAllMembers() {
		if (ProviderManager.getMarkupProvider() instanceof JDTSearchResultsMarkupProvider) {
			((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider()).resetMarkupsAndKinds();
			updateSearchInformation();
			if (!((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider()).isJavaSearch()) {
				return new ArrayList();
			}
		}
		return super.getAllMembers();
	}

	private void updateSearchInformation() {
		if (SearchPlugin.getActivePage() != null && NewSearchUI.getSearchResultView() != null) {
			if (searchView == null) {
				searchView = NewSearchUI.getSearchResultView();
			} else if (!(searchView.equals(NewSearchUI.getSearchResultView()))) {
				searchView = NewSearchUI.getSearchResultView();
				visListenerInstance = new VisualiserPropertyListener();
			}
			if (visListenerInstance == null) {
				visListenerInstance = new VisualiserPropertyListener();
			}
			searchView.addPropertyListener(visListenerInstance);
			if (searchView instanceof SearchView) {
				SearchView sv = (SearchView) searchView;
				if (sv.getCurrentSearchResult() instanceof JavaSearchResult) {
					((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider()).setJavaSearch(true);
					((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider())
							.setJavaSearchResult((JavaSearchResult) sv.getCurrentSearchResult());
				} else {
					((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider()).setJavaSearch(false);
					((JDTSearchResultsMarkupProvider) ProviderManager.getMarkupProvider()).setJavaSearchResult(null);
				}
			}
		}
	}

}
