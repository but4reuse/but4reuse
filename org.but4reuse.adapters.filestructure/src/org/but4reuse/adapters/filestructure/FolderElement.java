package org.but4reuse.adapters.filestructure;

import java.io.File;
import java.net.URI;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.files.FileUtils;

/**
 * Folder Element
 * @author jabier.martinez
 */
public class FolderElement extends AbstractElement {

	public URI relativeURI;

	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		if (anotherElement instanceof FolderElement) {
			if (((FolderElement) anotherElement).relativeURI.equals(relativeURI)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return relativeURI.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relativeURI == null) ? 0 : relativeURI.hashCode());
		return result;
	}
	

	public boolean construct(URI uri) {
		try {
			URI newDirectoryURI = uri.resolve(relativeURI);
			File a = FileUtils.getFile(newDirectoryURI);
			if (!a.exists()) {
				a.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
