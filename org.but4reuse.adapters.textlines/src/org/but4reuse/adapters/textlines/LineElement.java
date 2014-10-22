package org.but4reuse.adapters.textlines;

import java.net.URI;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.textlines.utils.LevenshteinDistanceStrategy;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

/**
 * Line element
 * 
 * @author jabier.martinez
 */
public class LineElement extends AbstractElement {

	public String line;

	// Constructor
	public LineElement(String line) {
		this.line = line;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof LineElement) {
			// similarity metric for two strings
			return LevenshteinDistanceStrategy.score(line, ((LineElement) anotherElement).line);
		}
		return 0;
	}

	@Override
	public String getText() {
		return line;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		return result;
	}

	// marker information
	public URI uri;
	public int lineNumber;

	@Override
	public IMarker getMarker() {
		IMarker marker = null;
		IFile ifile = WorkbenchUtils.getIFileFromURI(uri);
		if (ifile != null && ifile.exists()) {
			try {
				marker = ifile.createMarker(IMarker.TEXT);
				marker.setAttribute(IMarker.LOCATION, ifile.getName());
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return marker;
	}

	public void setMarkerInfo(URI uri, int lineNumber) throws CoreException {
		this.uri = uri;
		this.lineNumber = lineNumber;
	}

}
