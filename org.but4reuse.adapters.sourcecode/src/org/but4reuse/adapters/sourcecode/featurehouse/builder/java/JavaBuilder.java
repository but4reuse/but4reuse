package org.but4reuse.adapters.sourcecode.featurehouse.builder.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

import org.but4reuse.adapters.sourcecode.featurehouse.builder.ArtifactBuilder;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.OffsetCharStream;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.ParseException;
import org.but4reuse.adapters.sourcecode.featurehouse.de.ovgu.cide.fstgen.ast.FSTNonTerminal;
import org.but4reuse.adapters.sourcecode.featurehouse.tmp.generated_java15.Java15Parser;

public class JavaBuilder extends ArtifactBuilder {
	public JavaBuilder() {
		super(".java");
	}

	public void processNode(FSTNonTerminal parent, StringTokenizer st,
			File inputFile) throws FileNotFoundException, ParseException {
		FSTNonTerminal rootDocument = new FSTNonTerminal("Java-File", st
				.nextToken());
		parent.addChild(rootDocument);
		Java15Parser p = new Java15Parser(new OffsetCharStream(
				new FileInputStream(inputFile)));
		p.CompilationUnit(false);
		rootDocument.addChild(p.getRoot());
	}
}
