package org.but4reuse.adapters.json.tools;

public class AdapterTools {
	private static int id = 0;

	public static int getUniqueId() {
		return id++;
	}
}
