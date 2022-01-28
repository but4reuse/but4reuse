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

	static IEclipsePreferences prefs;

	// static initialization
	static {
		if (Activator.getDefault() != null) {
			prefs = InstanceScope.INSTANCE.getNode(Activator.getDefault().getBundle().getSymbolicName());
		} else {
			prefs = null;
		}
	}

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
		if (prefs == null) {
			return 1.00;
		}
		return prefs.getDouble(AUTOMATIC_EQUAL_THRESHOLD, 1.00);
	}

	public static double getManualEqualThreshold() {
		if (prefs == null) {
			return 0.9;
		}
		return prefs.getDouble(ASK_USER_THRESHOLD, 0.9);
	}

	public static boolean isManualEqualActivated() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean(ASK_USER, false);
	}

	public static void setManualEqual(boolean value) {
		if (prefs == null) {
			return;
		}
		prefs.putBoolean(ASK_USER, value);
	}

	public static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static void setDeactivateManualEqualOnlyForThisTime(boolean b) {
		if (prefs == null) {
			return;
		}
		prefs.putBoolean(ASK_USER_DEACTIVATED_FOR_THIS_TIME, b);
	}

	public static boolean isDeactivateManualEqualOnlyForThisTime() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean(ASK_USER_DEACTIVATED_FOR_THIS_TIME, false);
	}

	public static boolean isAdaptConcurrently() {
		if (prefs == null) {
			return false;
		}
		return prefs.getBoolean(ADAPT_CONCURRENTLY, false);
	}
}
