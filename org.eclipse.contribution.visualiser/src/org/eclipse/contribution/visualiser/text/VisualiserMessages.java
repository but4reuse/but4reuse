/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ben Dalziel     - initial version
 *******************************************************************************/
package org.eclipse.contribution.visualiser.text;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 */
public class VisualiserMessages extends NLS {

	private static final String BUNDLE_NAME = VisualiserMessages.class.getName();

	private VisualiserMessages() {
		// Do not instantiate
	}

	public static String VisualiserPreferencePage_providersLabel;
	public static String VisualiserPreferencePage_description;
	public static String VisualiserPreferencePage_noDescription;
	public static String VisualiserPreferencePage_title;
	public static String VisualiserPreferencePage_providers;
	public static String VisualiserPreferencePage_drawingOptions;
	public static String VisualiserPreferencePage_drawingStyle;
	public static String VisualiserPreferencePage_colorSet;
	public static String VisualiserPreferencePage_stripeHeight;
	public static String VisualiserPreferencePage_colWidth;
	public static String VisualiserPreferencePage_preview;
	public static String VisualiserPreferencePage_preview_col1;
	public static String VisualiserPreferencePage_preview_col2;

	public static String VisualiserPreferencePage_stripeSizeDialog_title;
	public static String VisualiserPreferencePage_stripeSizeDialog_message;
	public static String VisualiserPreferencePage_stripeSizeDialog_togglemessage;

	public static String OnlyShow;
	public static String Stripe;
	public static String Default;
	public static String JDTMember;
	public static String ResourceMember;
	public static String SimpleMember;
	public static String SimpleGroup;
	public static String Size;
	public static String Children;

	public static String Visualiser;
	public static String Zoom_In_3;
	public static String Zooms_in_on_visualisation_4;
	public static String Zoom_Out_6;
	public static String Zooms_out_7;
	public static String Limit_view_9;
	public static String Limits_visualisation_to_affected_bars_only_10;
	public static String Package_View_12;
	public static String Changes_to_group_view_13;
	public static String Class_View_15;
	public static String Changes_to_member_view_16;
	public static String Select_All_20;
	public static String Select_None_21;
	public static String Preferences_24;
	public static String Preferences_Tip_25;
	public static String Absolute_Proportions;
	public static String Zoom_fittoview;

	public static String getColorForError;

	public static String Jobs_VisualiserUpdate;
	public static String Jobs_Update;
	public static String Jobs_VisualiserMenuUpdate;
	public static String Jobs_VisualiserRedraw;
	public static String Jobs_GettingData;
	public static String Jobs_UpdatingMenu;
	public static String Jobs_Drawing;

	public static String Change_color_for;

	public static String Visualiser_testString;
	public static String Pattern_for;

	static {
		NLS.initializeMessages(BUNDLE_NAME, VisualiserMessages.class);
	}
}
