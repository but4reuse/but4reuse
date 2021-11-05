package org.but4reuse.adapters.cppcdt;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.parser.CppParser;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class contains the C++ adapter implementation.
 * 
 * @author sandu.postaru
 */

public class CppAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {

		// check the presence of a .cpp file
		File file = FileUtils.getFile(uri);

		if (file == null || !file.exists()) {
			return false;
		}

		return FileUtils.containsFileWithExtension(file, CppParser.SOURCE_CPP)
				|| FileUtils.containsFileWithExtension(file, CppParser.SOURCE_C)
				|| FileUtils.containsFileWithExtension(file, CppParser.HEADER);
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {

		List<IElement> elements = Collections.emptyList();

		File file = FileUtils.getFile(uri);
		if (file.isDirectory()) {
			file = getProjectRoot(file);
		}

		try {
			elements = CppParser.parse(file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {

		for (IElement iElement : elements) {
			if (iElement instanceof CppElement) {
				CppElement element = (CppElement) iElement;
				// if the user wants to reconstruct again from the menu,
				// then reconstruct the elements

				element.resetConstructFlag();

				element.construct(uri);
			}
		}
	}

	/**
	 * Locates the folder that contains the source code files.
	 * 
	 * @param file
	 *            the root folder
	 * @return the folder containing the source code files.
	 */
	private File getProjectRoot(File file) {

		// we are sure that source files exist, since the adapter is invoked (is
		// adaptable)

		// source file found, return the parent (containing folder)
		if (FileUtils.isExtension(file, CppParser.SOURCE_CPP) || FileUtils.isExtension(file, CppParser.SOURCE_C)
				|| FileUtils.isExtension(file, CppParser.HEADER)) {
			return file.getParentFile();
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
			File root = null;

			for (File f : files) {
				root = getProjectRoot(f);
				if (root != null) {
					return root;
				}
			}
		}

		return null;
	}

}
