package org.but4reuse.adapters.filestructure;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.utils.files.FileUtils;

/**
 * File Element
 * @author jabier.martinez
 */
public class FileElement extends AbstractElement {

	public URI relativeURI;
	public Path pathToConstructByCopy;

	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		// TODO add a preference option to check file content
		if (anotherElement instanceof FileElement) {
			if (((FileElement) anotherElement).relativeURI.equals(relativeURI)) {
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
			// Create parent folders structure
			URI newDirectoryURI = uri.resolve(relativeURI);
			File a = FileUtils.getFile(newDirectoryURI);
			if (!a.getParentFile().exists()) {
				a.getParentFile().mkdirs();
			}
			if (!a.exists()) {
				// Copy the content
				Files.copy(pathToConstructByCopy, a.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
