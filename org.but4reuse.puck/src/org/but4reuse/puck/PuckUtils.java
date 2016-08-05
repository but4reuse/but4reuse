package org.but4reuse.puck;

import org.extendj.*;
import org.extendj.ast.JavaJastAddDG2AST;
import org.extendj.ast.JavaJastAddDG2AST$;

import puck.graph.io.CSVPrinter$;
import puck.util.FileHelper$;
import puck.util.PuckNoopLogger$;
import scala.collection.JavaConversions$;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class PuckUtils {

	public static void main(String[] args) {
		//createCSV("/home/colympio/workspace/puckTest2/src/mypackage");
	}

	public static void createCSV(URI uriRep, URI output) {
		if (uriRep != null && uriRep.getPath() != null) {
			scala.collection.Iterator<String> stringEmptyIterator = (JavaConversions$.MODULE$
					.asScalaIterator(Collections.<String> emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());

			// scala.collection.Iterator<String> fileFullPathsIterator =
			// JavaConversions$.MODULE$.asScalaIterator(Arrays.asList(args).iterator()).toList();

			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					new File(uriRep.getPath()), ".java", fileEmptyIterator.toSeq());
			JavaJastAddDG2AST dg2ast = JavaJastAddDG2AST$.MODULE$.fromFiles(fileFullPaths,
					stringEmptyIterator.toList(), stringEmptyIterator.toList(), stringEmptyIterator.toList(),
					tuple2emptyIterator.toList(), null, PuckNoopLogger$.MODULE$);

			
			File dir = new File(uriRep.getPath()+"/out");
			if (!dir.exists())
				dir.mkdir();

			CSVPrinter$.MODULE$.apply(dg2ast.initialGraph(), dir, ";");
			try {
				output = new URI(dir.getPath());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void supressCSV(URI uriTempCSVfolder) {
		File dir = new File(uriTempCSVfolder.getPath());
		if (dir.exists()) {
			dir.delete();
		}
	}
}
