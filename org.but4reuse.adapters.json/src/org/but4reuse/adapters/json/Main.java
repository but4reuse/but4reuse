package org.but4reuse.adapters.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Main
{
	public static void jsonFunction(JsonValue v)
	{
		System.out.println("jsonvalue");
	}
	
	public static void main(String args[])
	{
		JsonArray arr = new JsonArray();
		arr.add(4);
		arr.set(1, 2);
		for(int i = 0 ; i < arr.size() ; i++)
		{
			System.out.println(i);
		}
	}
}
