package org.but4reuse.adapters.sourcecode.featurehouse.cide.languages;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.ISourceFile;
import org.but4reuse.adapters.sourcecode.featurehouse.cide.gparser.ParseException;

public interface ILanguageParser {
	ISourceFile getRoot() throws ParseException;
}
