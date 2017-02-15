package org.but4reuse.adapters.preferences;

import org.but4reuse.adapters.activator.Activator;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * @author jabier.martinez
 * 
 */
public class PreferencesHelper {

	static IEclipsePreferences prefs = InstanceScope.INSTANCE
			.getNode(Activator.getDefault().getBundle().getSymbolicName());

	public static final String AUTOMATIC_EQUAL_THRESHOLD = "automatic_threshold";
	public static final String ASK_USER_THRESHOLD = "ask_user_threshold";
	public static final String ASK_USER = "ask_user";
	public static final String ASK_USER_DEACTIVATED_FOR_THIS_TIME = "ask_user_deactivated_for_this_time";
	public static final String ADAPT_CONCURRENTLY = "adapt_concurrently";

	public static boolean isOnlyIdenticalMode() {
		if (getAutomaticEqualThreshold() == 1) {
			if (!isManualEqualActivated() || getManualEqualThreshold() == 1) {
				return true;
			}
		}
		return false;
	}

	public static double getAutomaticEqualThreshold() {
		return prefs.getDouble(AUTOMATIC_EQUAL_THRESHOLD, 1.00);
	}

	public static double getManualEqualThreshold() {
		return prefs.getDouble(ASK_USER_THRESHOLD, 0.9);
	}

	public static boolean isManualEqualActivated() {
		return prefs.getBoolean(ASK_USER, false);
	}

	public static void setManualEqual(boolean value) {
		prefs.putBoolean(ASK_USER, value);
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static void setDeactivateManualEqualOnlyForThisTime(boolean b) {
		prefs.putBoolean(ASK_USER_DEACTIVATED_FOR_THIS_TIME, b);
	}

	public static boolean isDeactivateManualEqualOnlyForThisTime() {
		return prefs.getBoolean(ASK_USER_DEACTIVATED_FOR_THIS_TIME, false);
	}

	public static boolean isAdaptConcurrently() {
		return prefs.getBoolean(ADAPT_CONCURRENTLY, false);
	}
}
