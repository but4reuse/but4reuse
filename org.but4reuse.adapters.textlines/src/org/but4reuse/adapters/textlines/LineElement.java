package org.but4reuse.adapters.textlines;

import java.net.URI;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.markers.IMarkerElement;
import org.but4reuse.adapters.preferences.PreferencesHelper;
import org.but4reuse.utils.nlp.similarity.LevenshteinDistance;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Line element
 * 
 * @author jabier.martinez
 */
public class LineElement extends AbstractElement implements IMarkerElement {

	public String line;

	// Constructor
	public LineElement(String line) {
		this.line = line;
	}

	@Override
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof LineElement) {
			// similarity metric for two strings
			return LevenshteinDistance.score(line, ((LineElement) anotherElement).line);
		}
		return 0;
	}

	@Override
	public String getText() {
		return line;
	}

	@Override
	public int hashCode() {
		// We use the hash from the line string only if we are on identical mode
		if (PreferencesHelper.isOnlyIdenticalMode()) {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((line == null) ? 0 : line.hashCode());
			return result;
		}
		return super.hashCode();
	}

	// marker information
	public URI uri;
	public int lineNumber;

	@Override
	public IMarker getMarker() {
		IMarker marker = null;
		IResource ifile = WorkbenchUtils.getIResourceFromURI(uri);
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
