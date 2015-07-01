package org.but4reuse.adapters.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class Main {
	public static void main(String args[]) {
		JsonObject o1 = new JsonObject();
		JsonObject o2 = new JsonObject();

		o1.add("key", 1);
		o2.add("key", 1);

		JsonArray a1 = new JsonArray();
		JsonArray a2 = new JsonArray();

		a1.add(1);
		a1.add(o1);
		a2.add(1);
		a2.add(o2);

		System.out.println(o1.equals(o2));
		System.out.println(a1.equals(a2));
	}
}
