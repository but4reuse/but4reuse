package org.but4reuse.adapters.eclipse;

import java.net.URI;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.markers.IMarkerElement;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * File Element
 * @author jabier.martinez
 */
public class FileElement extends AbstractElement implements IMarkerElement {

	private URI uri;
	private URI relativeURI;

	@Override
	public double similarity(IElement anotherElement) {
		// When they have the same relative URI
		// TODO add a preference option to check file content
		if (anotherElement instanceof FileElement) {
			if (((FileElement) anotherElement).getRelativeURI().equals(getRelativeURI())) {
				return 1;
			}
		}
		return 0;
	}

	
	@Override
	public String getText() {
		return getRelativeURI().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getRelativeURI() == null) ? 0 : getRelativeURI().hashCode());
		return result;
	}


	public URI getRelativeURI() {
		return relativeURI;
	}


	public void setRelativeURI(URI relativeURI) {
		this.relativeURI = relativeURI;
	}

	@Override
	public IMarker getMarker() {
		IMarker marker = null;
		IResource ifile = WorkbenchUtils.getIResourceFromURI(getUri());
		if (ifile != null && ifile.exists()) {
			try {
				marker = ifile.createMarker(IMarker.TEXT);
				marker.setAttribute(IMarker.LOCATION, ifile.getName());
				//marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return marker;
	}


	public URI getUri() {
		return uri;
	}


	public void setUri(URI uri) {
		this.uri = uri;
	}

}
