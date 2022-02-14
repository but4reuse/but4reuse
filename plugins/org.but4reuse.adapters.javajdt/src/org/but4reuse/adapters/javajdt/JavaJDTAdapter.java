package org.but4reuse.adapters.javajdt;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Java adapter based on JDT
 * 
 * @author jabier.martinez
 */
public class JavaJDTAdapter implements IAdapter {

	private List<Class<?>> elementsToExclude = new ArrayList<Class<?>>();

	public void setElementsToExclude(List<Class<?>> elementsToExclude) {
		this.elementsToExclude = elementsToExclude;
	}

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// check the presence of a .java file
		File file = FileUtils.getFile(uri);
		if (file == null || !file.exists()) {
			return false;
		}
		return FileUtils.containsFileWithExtension(file, "java");
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		File inputFile = FileUtils.getFile(uri);

		// The list of java files contained in this uri
		List<File> javaFiles = new ArrayList<File>();
		// it is just one java file
		if (inputFile.isFile() && FileUtils.isExtension(inputFile, "java")) {
			javaFiles.add(inputFile);
		} else {
			// it is a folder containing java files
			List<File> files = FileUtils.getAllFiles(inputFile);
			for (File file : files) {
				if (FileUtils.isExtension(file, "java")) {
					javaFiles.add(file);
				}
			}
		}
		// Parse all java files
		JDTParser jdtParser = new JDTParser();
		List<IElement> elements = jdtParser.parse(javaFiles);

		if (!elementsToExclude.isEmpty()) {
			List<IElement> finalElements = new ArrayList<IElement>();
			for (IElement element : elements) {
				if (!elementsToExclude.contains(element.getClass())) {
					finalElements.add(element);
				}
			}
			return finalElements;
		}

		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO
		System.out.println("Construct is not completely supported yet");

		JDTConstructor jdtConstructor = new JDTConstructor();
		// Create file if it does not exist
		File outputFolder = FileUtils.getFile(uri);
		jdtConstructor.construct(outputFolder, elements);
	}
}
