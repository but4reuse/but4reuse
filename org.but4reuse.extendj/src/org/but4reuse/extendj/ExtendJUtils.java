package org.but4reuse.extendj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.utils.files.FileUtils;
import org.extendj.JavaCompiler;
import org.extendj.JavaPrettyPrinter;

public class ExtendJUtils {
	
	public static void main(String[] args) {
		File folder = new File("/home/colympio/workspace/puckTest/src/argoumlVariants/Original/src/");
		compile(getAllJavaFilesInFolder(folder),"bootclassaht");
	}
	
	
	public static void compile(List<File> javaFiles, String bootclasspath){
		//JavaPrettyPrinter.main(new String[]{"-help"});
		List<String> params = new ArrayList<String>();
		params.add("-verbose");
		params.add("-bootclasspath");
		params.add("/usr/lib/jvm/java-1.7.0-openjdk-amd64/jre/lib/rt.jar");
		params.add("-classpath");
		String lib = "";
		for (File file : FileUtils.getAllFiles(new File("/home/colympio/workspace/puckTest/src/argoumlVariants/Original/libs/"))){
			lib = lib + ":" + file.getAbsolutePath();
		}
		lib = lib.substring(1);
		params.add(lib);
		for (File file : javaFiles) {
			params.add(file.getAbsolutePath());
		}
		String[] args = new String[params.size()];
		int i = 0;
		for(String a : params){
			args[i] = a;
			i++;
		}
		JavaCompiler.main(args);
		System.out.println("finished");
	}
	
	public static List<File> getAllJavaFilesInFolder(File folder){
		List<File> javaFiles = new ArrayList<File>();
		List<File> files = FileUtils.getAllFiles(folder);
		for(File f : files){
			if(FileUtils.isExtension(f, "java")){
				javaFiles.add(f);
			}
			
		}
		return javaFiles;
	}

}
