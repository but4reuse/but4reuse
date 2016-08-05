package org.but4reuse.puck;

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

	public static void createCSV(URI uriRep, URI output) {
		if (uriRep != null && uriRep.getPath() != null) {
			scala.collection.Iterator<String> stringEmptyIterator = (JavaConversions$.MODULE$
					.asScalaIterator(Collections.<String> emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());


			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					org.but4reuse.utils.files.FileUtils.getFile(uriRep), ".java", fileEmptyIterator.toSeq());
			JavaJastAddDG2AST dg2ast = JavaJastAddDG2AST$.MODULE$.fromFiles(fileFullPaths,
					stringEmptyIterator.toList(), stringEmptyIterator.toList(), stringEmptyIterator.toList(),
					tuple2emptyIterator.toList(), null, PuckNoopLogger$.MODULE$);

			File folderForOutput = org.but4reuse.utils.files.FileUtils.getFile(output);
			CSVPrinter$.MODULE$.apply(dg2ast.initialGraph(), folderForOutput, ";");

		}
	}

	public static void supressCSV(URI uriTempCSVfolder) {
		File dir = org.but4reuse.utils.files.FileUtils.getFile(uriTempCSVfolder);
		if (dir.exists()) {
			dir.delete();
		}
	}
}
