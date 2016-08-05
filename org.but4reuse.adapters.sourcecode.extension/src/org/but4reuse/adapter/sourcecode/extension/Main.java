package org.but4reuse.adapter.sourcecode.extension;

import java.io.File;
import java.net.URI;

public class Main {
	public static void main(String[] args) {
		File file = new File("/home/colympio/workspace/puckTest/src/mypackage/MyClass.java");
		file = file.getParentFile();
		URI uri = file.toURI();
		JavaSourceCodeAdapterExtension java = new JavaSourceCodeAdapterExtension();
		java.adapt(uri, null);
	}
}
