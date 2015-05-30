package org.but4reuse.adapters.sourcecode;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.adapter.JavaLanguage;
import org.but4reuse.adapters.sourcecode.adapter.LanguageManager;
import org.but4reuse.adapters.sourcecode.adapter.ReadFSTProduct;
import org.but4reuse.adapters.sourcecode.adapter.Elements2FST;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import builder.ArtifactBuilder;
import builder.java.JavaBuilder;
import de.ovgu.cide.fstgen.ast.FSTNode;

public class JavaSourceCodeAdapter implements IAdapter {

	static ArtifactBuilder builder = null;

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// check if there is at least one java class
		File file = FileUtils.getFile(uri);
		if (file == null || !file.exists()) {
			return false;
		}
		return FileUtils.containsFileWithExtension(file, "java");
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {

		LanguageManager.setLanguage(new JavaLanguage());
		builder = new JavaBuilder();

		File file = FileUtils.getFile(uri);
		String dirVariant = file.getAbsolutePath();

		// Step1: Read the input product variant.
		ReadFSTProduct rp1 = new ReadFSTProduct();
		rp1.readProduct(dirVariant);
		List<IElement> elements = rp1.getArtefactElements();

		// handleBodies(rp1.getBody(), j);

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		Elements2FST elements2FST = new Elements2FST();
		List<FSTNode> nodesS = elements2FST.elementsToFST(elements);
		String absPath = FileUtils.getFile(uri).getAbsolutePath();
		for (FSTNode n : nodesS) {
			LanguageManager.getLanguage().generateCode(n, absPath);
		}
	}

}
