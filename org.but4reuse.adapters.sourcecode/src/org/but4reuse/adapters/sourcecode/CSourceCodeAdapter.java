package org.but4reuse.adapters.sourcecode;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.adapter.CLanguage;
import org.but4reuse.adapters.sourcecode.adapter.LanguageManager;
import org.but4reuse.adapters.sourcecode.adapter.ReadFSTProduct;
import org.but4reuse.adapters.sourcecode.adapter.Elements2FST;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import builder.ArtifactBuilder;
import builder.capprox.CApproxBuilder;
import de.ovgu.cide.fstgen.ast.FSTNode;

public class CSourceCodeAdapter implements IAdapter {

	static ArtifactBuilder builder = null;

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
		builder = new CApproxBuilder();
		LanguageManager.setLanguage(new CLanguage());
		File file = FileUtils.getFile(uri);
		String dirVariant = file.getAbsolutePath();

		/*
		 * Step1: Read the input product variant.
		 */

		System.out.println("@ SPLIC Step 1 : Reading Input Products");
		System.out.println("@------------");
		List<IElement> artefact = null;
		ReadFSTProduct rp1 = new ReadFSTProduct();
		try {
			rp1.readProduct(dirVariant);
			artefact = rp1.getArtefact();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// handleBodies(rp1.getBody(), j);

		return artefact;
	}

	@Override
	public void construct(URI uri, List<IElement> cps, IProgressMonitor monitor) {
		Elements2FST ss = new Elements2FST();
		List<FSTNode> nodesS = ss.toFST(cps);

		String absPath = FileUtils.getFile(uri).getAbsolutePath();
		System.out.println("         @Code Generation for Feature :" + absPath);
		System.out.println("		                          @-------------------");
		for (FSTNode n : nodesS) {

			// if (n instanceof FSTNonTerminal){//String featName=f.getId();
			LanguageManager.getLanguage().generateCode(n, absPath);
			// }

		}

	}

}
