package org.but4reuse.puck;

import org.apache.commons.io.FilenameUtils;
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

/**
 * 
 * @author colympio
 * 
 *         The class PuckUtils is the handler for the puck and the created
 *         csvfiles.
 */
public class PuckUtils {

	public final static String PUCK_PROPERTIES_FILE_NAME = "puck.properties";

	/**
	 * Create the CSV files using puck on the uriRep and the files are created
	 * in the output URI. The method tries to find a puck.properties file inside
	 * uriRep to get user-defined bootclasspath and classpath.
	 * 
	 * Example of puck.properties file:
	 * 
	 * bootclasspath=C:/Program Files/Java/jre1.5.0_22/lib/rt.jar
	 * classpath=C:/something/libs
	 * 
	 * @param uriRep
	 * @param output
	 */
	public static void createCSV(URI uriRep, URI output) throws Exception, Error {

		if (uriRep != null && uriRep.getPath() != null) {
			// scala.collection.Iterator<String> stringEmptyIterator =
			// (JavaConversions$.MODULE$.asScalaIterator(Collections.<String>
			// emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());

			File propertyFile = new File(FileUtils.getFile(uriRep), PUCK_PROPERTIES_FILE_NAME);
			String classPath = null;
			String bootClassPath = null;

			if (propertyFile.exists()) {
				classPath = PropertiesFileUtils.getValue(propertyFile, "classpath");
				bootClassPath = PropertiesFileUtils.getValue(propertyFile, "bootclasspath");
			} else {
				System.out
						.println("Using default settings, puck.properties not found (default rt.jar and empty classpath)");
			}

			ArrayList<String> listClassPath = new ArrayList<String>();

			if (classPath != null) {
				if (!classPath.equals("none")) {
					for (File currentFile : FileUtils.getAllFiles(new File(classPath))) {
						listClassPath.add(currentFile.getAbsolutePath());
					}
				}
			}

			scala.collection.Iterator<String> iteratorClasspath = JavaConversions$.MODULE$
					.asScalaIterator(listClassPath.iterator());

			ArrayList<String> listBootPath = new ArrayList<String>();
			if (bootClassPath != null) {
				listBootPath.add(bootClassPath);
			} else {
				listBootPath.add(FilenameUtils.separatorsToSystem((System.getProperty("java.home") + "\\lib\\rt.jar")));
			}

			scala.collection.Iterator<String> iteratorBootpath = JavaConversions$.MODULE$.asScalaIterator(listBootPath
					.iterator());

			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					org.but4reuse.utils.files.FileUtils.getFile(uriRep), ".java", fileEmptyIterator.toSeq());
			System.gc();

			JavaJastAddDG2AST dg2ast = JavaJastAddDG2AST$.MODULE$.fromFiles(fileFullPaths, fileFullPaths,
					iteratorClasspath.toList(), iteratorBootpath.toList(), tuple2emptyIterator.toList(), null,
					PuckNoopLogger$.MODULE$);

			File folderForOutput = org.but4reuse.utils.files.FileUtils.getFile(output);
			folderForOutput.mkdirs();
			CSVPrinter$.MODULE$.apply(dg2ast.initialGraph(), folderForOutput, ";");
		}

	}

	/**
	 * Erase the temporary CSV files and the folder
	 * 
	 * @param uriTempCSVfolder
	 */
	public static void supressCSV(URI uriTempCSVfolder) {
		File temporaryFolder = org.but4reuse.utils.files.FileUtils.getFile(uriTempCSVfolder);
		if (temporaryFolder.exists()) {
			List<File> temporaryFiles = FileUtils.getAllFiles(temporaryFolder);
			for (File currentFile : temporaryFiles) {
				currentFile.delete();
			}
			temporaryFolder.delete();
		}
	}
}
