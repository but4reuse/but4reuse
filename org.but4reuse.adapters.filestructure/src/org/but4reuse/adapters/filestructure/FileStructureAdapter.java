package org.but4reuse.adapters.filestructure;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * File structure adapter
 * 
 * @author jabier.martinez
 */
public class FileStructureAdapter implements IAdapter {

	URI rootURI;

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		// Any folder is adaptable
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && file.isDirectory()) {
			return true;
		}
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		rootURI = file.toURI();
		// start the containment tree traversal, with null as initial container
		adapt(file, elements, null);
		// in elements we have the result
		return elements;
	}

	private void adapt(File file, List<IElement> elements, IElement container) {
		if (file.isDirectory()) {
			FolderElement folderElement = new FolderElement();
			folderElement.relativeURI = rootURI.relativize(file.toURI());
			// Add dependency
			if(container!=null){
				folderElement.addDependency(container);
			}
			elements.add(folderElement);
			File[] files = file.listFiles();
			for (File subFile : files) {
				adapt(subFile, elements, folderElement);
			}
		} else {
			FileElement fileElement = new FileElement();
			fileElement.relativeURI = rootURI.relativize(file.toURI());
			fileElement.pathToConstructByCopy = file.toPath();
			// Add dependency
			if(container!=null){
				fileElement.addDependency(container);
			}
			elements.add(fileElement);
		}
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// Delegate to the element construction methods
		for (IElement element : elements) {
			if (!monitor.isCanceled()) {
				monitor.subTask(element.getText());
				if (element instanceof FolderElement) {
					((FolderElement) element).construct(uri);
				} else if (element instanceof FileElement) {
					((FileElement) element).construct(uri);
				}
			}
			monitor.worked(1);
		}
	}

}
