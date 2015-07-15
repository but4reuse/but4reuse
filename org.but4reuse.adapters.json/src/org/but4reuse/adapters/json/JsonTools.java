package org.but4reuse.adapters.json;

public class JsonTools {
	
	private static int id = 0;
	
	public static int generateId() {
		id++;
		return id;
	}
}
