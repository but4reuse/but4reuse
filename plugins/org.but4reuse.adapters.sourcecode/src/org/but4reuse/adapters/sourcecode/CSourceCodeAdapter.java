package org.but4reuse.adapters.sourcecode;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.adapter.CLanguage;
import org.but4reuse.adapters.sourcecode.adapter.Elements2FST;
import org.but4reuse.adapters.sourcecode.adapter.LanguageManager;
import org.but4reuse.adapters.sourcecode.adapter.ReadFSTProduct;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import de.ovgu.cide.fstgen.ast.FSTNode;

public class CSourceCodeAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// check if there is at least one c class
		File file = FileUtils.getFile(uri);
		if (file == null || !file.exists()) {
			return false;
		}
		return FileUtils.containsFileWithExtension(file, "c");
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		LanguageManager.setLanguage(new CLanguage());

		// Step1: Read the input product variant.
		List<IElement> artefact = null;
		ReadFSTProduct rp1 = new ReadFSTProduct();
		try {
			rp1.readProduct(uri);
			artefact = rp1.getArtefactElements();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return artefact;
	}

	@Override
	public void construct(URI uri, List<IElement> cps, IProgressMonitor monitor) {
		Elements2FST ss = new Elements2FST();
		List<FSTNode> nodesS = ss.elementsToFST(cps);
		String absPath = FileUtils.getFile(uri).getAbsolutePath();
		for (FSTNode n : nodesS) {
			LanguageManager.getLanguage().generateCode(n, absPath);
		}
	}

}
