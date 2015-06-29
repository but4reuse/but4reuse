package org.but4reuse.adapters.json;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class JsonTools
{
	private static JsonValue mergeObject(JsonObject obj1, JsonObject obj2)
	{
		JsonObject obj = new JsonObject();
		
		for(String key : obj1.names())
			obj.set(key, obj1.get(key));
		
		for(String key : obj2.names())
			obj.set(key, obj2.get(key));
		
		return obj;
	}
	
	private static JsonValue mergeArray(JsonArray arr1, JsonArray arr2)
	{
		JsonArray arr = new JsonArray();
		
		if( arr1.size() < arr2.size() )
		{
			for( int i=0 ; i<arr1.size() ; i++)
				arr.add( JsonTools.merge(arr1.get(i), arr2.get(i)));
			
			for( int i=arr1.size() ; i<arr2.size() ; i++)
				arr.add(arr2.get(i));
		}
		else
		{
			for( int i=0 ; i<arr2.size() ; i++)
				arr.add( JsonTools.merge(arr1.get(i), arr2.get(i)));
			
			for( int i=arr2.size() ; i<arr1.size() ; i++)
				arr.add(arr1.get(i));
		}
		
		return arr;
	}
	
	public static JsonValue merge(JsonValue v1, JsonValue v2)
	{
		if(v1 == null && v2 == null)
			return JsonValue.NULL;
		
		if(v1 == null)
			return v2;
		if(v2 == null)
			return v1;
		
		if( v1.isObject() && v2.isObject() )
			return JsonTools.mergeObject(v1.asObject(), v2.asObject());
		
		if( v1.isArray() && v2.isArray() )
			return JsonTools.mergeArray(v1.asArray(), v2.asArray());
		
		return v1;
	}
}
