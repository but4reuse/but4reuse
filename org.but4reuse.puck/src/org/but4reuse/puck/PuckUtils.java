package org.but4reuse.puck;

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
		File one = new File("/home/colympio/runtime-New_configuration(1)/testWork/");
		File two = new File("/home/colympio/runtime-New_configuration(1)/testWork");
		createCSV(one.toURI(),two.toURI());
	}

	public static void createCSV(URI uriRep, URI output) {
		if (uriRep != null && uriRep.getPath() != null) {
			scala.collection.Iterator<String> stringEmptyIterator = (JavaConversions$.MODULE$
					.asScalaIterator(Collections.<String> emptyIterator()));

			scala.collection.Iterator<scala.Tuple2<String, String>> tuple2emptyIterator = JavaConversions$.MODULE$
					.asScalaIterator(Collections.<scala.Tuple2<String, String>> emptyIterator());

			scala.collection.Iterator<File> fileEmptyIterator = JavaConversions$.MODULE$.asScalaIterator(Collections
					.<File> emptyIterator());
			URI uriForTry = null;
			try {
				uriForTry = new URI(uriRep.getScheme(), uriRep.getHost(), new File(uriRep.getPath()).getParent(), uriRep.getFragment());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scala.collection.immutable.List<String> fileFullPaths = FileHelper$.MODULE$.findAllFiles(
					org.but4reuse.utils.files.FileUtils.getFile(uriForTry), ".java", fileEmptyIterator.toSeq());
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
