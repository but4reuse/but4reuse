package org.but4reuse.adapter.sourcecode.extension;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
	public static void main(String[] args){
		File file = new File("/home/colympio/workspace/puckTest/src/mypackage/MyClass.java");
		URI uri = file.toURI();
		JavaSourceCodeAdapterExtension java = new JavaSourceCodeAdapterExtension();
		java.adapt(uri, null);
		//String test = "mypackage.MyClass.commonMethod({FormalParametersInternal})";
		//test = test.replaceAll("[(][{]*[}][)]", "a");
		//test = test.replaceAll("[(].*[)]", "");
		
		//System.out.println(test);
		/*HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		map.put("1", new ArrayList<String>());
		map.get("1").add("10");
		map.get("1").add("12");
		map.get("1").add("150");
		HashMap<String, String> NodeMap = new HashMap<String, String>();
		if (/*map.containsKey("1") || map.values().contains("150")) {
			System.out.println("yes");
		}
		else {
			System.out.println("nope");
		}*/
		
		
	}
}
