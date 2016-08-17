package org.but4reuse.puck;

import org.but4reuse.utils.files.FileUtils;
import org.but4reuse.utils.files.PropertiesFileUtils;
import org.extendj.ast.JavaJastAddDG2AST;
import org.extendj.ast.JavaJastAddDG2AST$;

import puck.graph.io.CSVPrinter$;
import puck.util.FileHelper$;
import puck.util.PuckNoopLogger$;
import scala.collection.JavaConversions$;

import java.io.File;
import java.net.URI;
import java.util.*;

public class PuckUtils {

	public static void main(String[] args) {
		File one = new File("/home/colympio/workspace/puckTest/src/argoumlVariants/AllDisabled/");
		File two = new File("/home/colympio/workspace/puckTest/src/argoumlVariants/AllDisabled/");
		createCSV(one.toURI(), two.toURI());
	}

	public static void createCSV(URI uriRep, URI output) {

		if (uriRep != null && uriRep.getPath() != null) {
			//scala.collection.Iterator<String> stringEmptyIterator = (JavaConversions$.MODULE$
				//	.asScalaIterator(Collections.<String> emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());
			
			Map<String, String> properties = PropertiesFileUtils.getPuckProperties(uriRep);	
			ArrayList<String> listClassPath = new ArrayList<>();
					
			if (!properties.get("classpath").equals("none")) {
				for (File file : FileUtils.getAllFiles(new File(properties.get("classpath")))) {
					listClassPath.add(file.getAbsolutePath());
				}
			}
			
			scala.collection.Iterator<String> iteClasspath = JavaConversions$.MODULE$
					.asScalaIterator(listClassPath.iterator());

			ArrayList<String> listBootPath = new ArrayList<>();
			listBootPath.add(properties.get("bootclasspath"));
			
			scala.collection.Iterator<String> iteBootpath = JavaConversions$.MODULE$
					.asScalaIterator(listBootPath.iterator());

			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					org.but4reuse.utils.files.FileUtils.getFile(uriRep), ".java", fileEmptyIterator.toSeq());
			System.gc();
			JavaJastAddDG2AST dg2ast = JavaJastAddDG2AST$.MODULE$.fromFiles(fileFullPaths, fileFullPaths,
					iteClasspath.toList(), iteBootpath.toList(), tuple2emptyIterator.toList(), null,
					PuckNoopLogger$.MODULE$);

			File folderForOutput = org.but4reuse.utils.files.FileUtils.getFile(output);
			CSVPrinter$.MODULE$.apply(dg2ast.initialGraph(), folderForOutput, ";");
		}

	}

	public static void supressCSV(URI uriTempCSVfolder) {
		File dir = org.but4reuse.utils.files.FileUtils.getFile(uriTempCSVfolder);
		if (dir.exists()) {
			List<File> files = FileUtils.getAllFiles(dir);
			for (File file : files) {
				file.delete();
			}
			dir.delete();
		}
	}

	public ArrayList<String> createListPathClass() {
		return null;

	}
}
