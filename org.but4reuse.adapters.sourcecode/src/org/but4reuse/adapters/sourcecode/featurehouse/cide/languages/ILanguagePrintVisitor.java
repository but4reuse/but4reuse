package org.but4reuse.adapters.sourcecode.featurehouse.cide.languages;

import org.but4reuse.adapters.sourcecode.featurehouse.cide.gast.IASTVisitor;

public interface ILanguagePrintVisitor extends IASTVisitor {
	String getResult();
}
