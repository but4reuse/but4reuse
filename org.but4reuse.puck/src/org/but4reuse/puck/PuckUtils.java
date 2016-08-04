package org.but4reuse.puck;

import org.extendj.*;
import org.extendj.ast.JavaJastAddDG2AST;
import org.extendj.ast.JavaJastAddDG2AST$;

import puck.graph.io.CSVPrinter$;
import puck.util.FileHelper$;
import puck.util.PuckNoopLogger$;
import scala.collection.JavaConversions$;

import java.io.File;
import java.util.*;

public class PuckUtils {

	public static void main(String[] args) {
		createCSV("/home/colympio/workspace/puckTest2/src/mypackage");
	}

	public static void createCSV(String directory) {
		if (directory != null) {
			scala.collection.Iterator<String> stringEmptyIterator = (JavaConversions$.MODULE$
					.asScalaIterator(Collections.<String> emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());

			// scala.collection.Iterator<String> fileFullPathsIterator =
			// JavaConversions$.MODULE$.asScalaIterator(Arrays.asList(args).iterator()).toList();

			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					new File(directory), ".java", fileEmptyIterator.toSeq());
			JavaJastAddDG2AST dg2ast = JavaJastAddDG2AST$.MODULE$.fromFiles(fileFullPaths,
					stringEmptyIterator.toList(), stringEmptyIterator.toList(), stringEmptyIterator.toList(),
					tuple2emptyIterator.toList(), null, PuckNoopLogger$.MODULE$);

			File dir = new File("/tmp/out");
			if (!dir.exists())
				dir.mkdir();

			CSVPrinter$.MODULE$.apply(dg2ast.initialGraph(), dir, ";");
		}
	}

	public static void supressCSV() {
		File dir = new File("/tmp/out");
		if (dir.exists()) {
			dir.delete();
		}
	}
}
