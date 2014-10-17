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
		List<IElement> cps = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		rootURI = file.toURI();
		adapt(file, cps);
		return cps;
	}

	private void adapt(File file, List<IElement> cps) {
		if (file.isDirectory()) {
			FolderElement directoryCP = new FolderElement();
			directoryCP.relativeURI = rootURI.relativize(file.toURI());
			cps.add(directoryCP);
			File[] files = file.listFiles();
			for (File subFile : files) {
				adapt(subFile, cps);
			}
		} else {
			FileElement fileCP = new FileElement();
			fileCP.relativeURI = rootURI.relativize(file.toURI());
			fileCP.pathToConstructByCopy = file.toPath();
			// TODO file content
			cps.add(fileCP);
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
