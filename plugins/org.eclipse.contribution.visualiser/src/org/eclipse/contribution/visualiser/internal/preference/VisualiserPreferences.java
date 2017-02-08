/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Sian Whiting - initial version
 *     Matt Chapman - add support for different palettes
 *******************************************************************************/
package org.eclipse.contribution.visualiser.internal.preference;

import org.eclipse.contribution.visualiser.VisualiserPlugin;
import org.eclipse.contribution.visualiser.core.ProviderDefinition;
import org.eclipse.contribution.visualiser.core.RendererManager;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Holds the preferences for the Visualiser as set via the
 * Workbench->preferences pages or the VisualiserPreferencesDialog, which is
 * launched for the Visualiser view.
 */
public class VisualiserPreferences {

	public static final String MAX_BAR = "org.eclipse.contribution.visualiser.preferences.maxbarwidth"; //$NON-NLS-1$

	public static final String MIN_BAR = "org.eclipse.contribution.visualiser.preferences.minbarwidth"; //$NON-NLS-1$

	public static final String PROVIDER = "org.eclipse.contribution.visualiser.preferences.provider"; //$NON-NLS-1$

	public static final String BAR_WIDTH = "org.eclipse.contribution.visualiser.preferences.barwidth"; //$NON-NLS-1$

	public static final String STRIPE_HEIGHT = "org.eclipse.contribution.visualiser.preferences.stripeheight"; //$NON-NLS-1$

	public static final String RENDERER = "org.eclipse.contribution.visualiser.preferences.renderer"; //$NON-NLS-1$

	public static final String PALETTE_PREFIX = "org.eclipse.contribution.visualiser.preferences.palette_"; //$NON-NLS-1$

	// Pattern Related
	public static final String PATTERN_STRIPE_HEIGHT = "org.eclipse.contribution.visualiser.preferences.patternstripeheight"; //$NON-NLS-1$

	public static final String USE_PATTERNS = "org.eclipse.contribution.visualiser.preferences.patterns"; //$NON-NLS-1$

	public static final String DO_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT = "org.eclipse.contribution.visualiser.preferences.doincreasestipeheight"; //$NON-NLS-1$

	public static final String DONT_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT = "org.eclipse.contribution.visualiser.preferences.dontincreasestipeheight"; //$NON-NLS-1$

	// default values
	public static final int STRIPE_SIZE_DEFAULT = 4;

	public static final int BAR_WIDTH_DEFAULT = 60;

	public static final int BAR_MIN_WIDTH = 18;

	public static final int PATTERN_STRIPE_SIZE_DEFAULT = 8;

	public static final boolean USE_PATTERNS_DEFAULT = false;

	public static final boolean INCREASE_VISUALISER_STRIPE_HEIGHT_AUTO_DEFAULT = false;

	/**
	 * Set up default values for preferences
	 */
	public static void initDefaults() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setDefault(PROVIDER, ""); //$NON-NLS-1$
		store.setDefault(BAR_WIDTH, BAR_WIDTH_DEFAULT);
		store.setDefault(STRIPE_HEIGHT, STRIPE_SIZE_DEFAULT);
		store.setDefault(MIN_BAR, BAR_MIN_WIDTH);
		store.setValue(MIN_BAR, BAR_MIN_WIDTH);
		store.setDefault(PATTERN_STRIPE_HEIGHT, PATTERN_STRIPE_SIZE_DEFAULT);
		store.setDefault(USE_PATTERNS, USE_PATTERNS_DEFAULT);
	}

	/**
	 * Get the maximum bar width for bars in the visualsier
	 * 
	 * @return the maximum bar width
	 */
	static public int getMaxBarSize() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getInt(MAX_BAR);
	}

	/**
	 * Get the minimum bar width for bars in the visualiser
	 * 
	 * @return the minimum bar width
	 */
	static public int getMinBarSize() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getInt(MIN_BAR);
	}

	/**
	 * Get the name of the current provider
	 * 
	 * @return the name of the current provider
	 */
	static public String getProvider() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getString(PROVIDER);
	}

	/**
	 * Get the bar width for the visualiser
	 * 
	 * @return the current bar width
	 */
	public static int getBarWidth() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getInt(BAR_WIDTH);
	}

	/**
	 * Get the stripe height for the visualiser
	 * 
	 * @return the current stripe height
	 */
	public static int getStripeHeight() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getInt(STRIPE_HEIGHT);
	}

	/**
	 * Get whether or not to use patterns
	 * 
	 * @return boolean - whether or not to use patterns
	 */
	public static boolean getUsePatterns() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(USE_PATTERNS);
	}

	/**
	 * Get the name of the chosen renderer
	 * 
	 * @return the current renderer's name
	 */
	public static String getRendererName() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getString(RENDERER);
	}

	/**
	 * Store the given name as the chosen renderer name
	 * 
	 * @param value
	 *            the renderer name
	 */
	public static void setRendererName(String value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(RENDERER, value);
		RendererManager.setCurrentRendererByName(value);
	}

	/**
	 * Get the default bar width for the visualiser
	 * 
	 * @return the default bar width
	 */
	public static int getDefaultBarWidth() {
		return BAR_WIDTH_DEFAULT;
	}

	/**
	 * Get the default stripe height for the visualiser
	 * 
	 * @return the default stripe height
	 */
	public static int getDefaultStripeHeight() {
		return STRIPE_SIZE_DEFAULT;
	}

	/**
	 * Get the default stripe height for the visualiser viewed with patterns
	 * 
	 * @return the default stripe height for patterns
	 */
	public static int getDefaultPatternStripeHeight() {
		return PATTERN_STRIPE_SIZE_DEFAULT;
	}

	/**
	 * Get the default option for whether or not to use patterns
	 * 
	 * @return boolean - use patterns or not
	 */
	public static boolean getDefaultUsePatterns() {
		return USE_PATTERNS_DEFAULT;
	}

	/**
	 * Return the chosen palette id for the given provider if one has been
	 * specified
	 * 
	 * @param providerID
	 * @return the id of the palette chosen by the given provider
	 */
	public static String getPaletteIDForProvider(String providerID) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		String pref = PALETTE_PREFIX + providerID;
		return store.getString(pref);
	}

	/**
	 * 
	 * 
	 * @return the id of the palette chosen by the given provider
	 */
	public static boolean getDoAutoIncreaseStripeHeight() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(DO_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT);
	}

	/**
	 * 
	 * 
	 * @return the id of the palette chosen by the given provider
	 */
	public static boolean getDontAutoIncreaseStripeHeight() {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(DONT_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT);
	}

	/**
	 * Store the given size as the chosen the bar width
	 * 
	 * @param value
	 *            width in pixels
	 */
	public static void setBarWidth(int value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(BAR_WIDTH, value);
	}

	/**
	 * Store the given size as the chosen stripe height
	 * 
	 * @param value
	 *            height in pixels
	 */
	public static void setStripeHeight(int value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(STRIPE_HEIGHT, value);
	}

	/**
	 * Set the option for whether or not to use patterns
	 * 
	 * @param boolean value - use patterns or not
	 */
	public static void setUsePatterns(boolean value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(USE_PATTERNS, value);
	}

	/**
	 * Store the given name as the chosen palette name for the specified
	 * provider
	 * 
	 * @param def
	 *            the provider
	 * @param value
	 *            the palette name
	 */
	public static void setPaletteIDForProvider(ProviderDefinition def, String value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		String pref = PALETTE_PREFIX + def.getID();
		store.setValue(pref, value);
	}

	/**
	 * Set the option for whether or not to use patterns
	 * 
	 * @param boolean value - use patterns or not
	 */
	public static void setDoIncreaseStripeHeight(boolean value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(DO_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT, value);
	}

	/**
	 * Set the option for whether or not to use patterns
	 * 
	 * @param boolean value - use patterns or not
	 */
	public static void setDontIncreaseStripeHeight(boolean value) {
		IPreferenceStore store = VisualiserPlugin.getDefault().getPreferenceStore();
		store.setValue(DONT_AUTO_INCREASE_VISUALISER_STRIPE_HEIGHT, value);
	}
}