package org.but4reuse.adapters.csv;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.markers.IMarkerElement;
import org.but4reuse.utils.workbench.WorkbenchUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Cell Element
 * 
 * @author jabier.martinez
 */
public class CellElement extends AbstractElement implements IMarkerElement {

	private String value;
	private int row;
	private int column;

	public CellElement(String value, int row, int column) {
		this.value = value;
		this.row = row;
		this.column = column;
	}

	@Override
	/**
	 * Identical when same position and value
	 */
	public double similarity(IElement anotherElement) {
		if (anotherElement instanceof CellElement) {
			CellElement cellElement = (CellElement) anotherElement;
			if (cellElement.getRow() == row && cellElement.getColumn() == column
					&& cellElement.getValue().equals(value)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public String getText() {
		return "(" + row + "," + column + ") " + value;
	}

	public String getValue() {
		return value;
	}

	// marker information
	public URI uri;
	public int lineNumber;

	public void setMarkerInfo(URI uri, int lineNumber) {
		this.uri = uri;
		this.lineNumber = lineNumber;
	}

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

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();
		words.add(value);
		return words;
	}
}
