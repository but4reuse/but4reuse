package org.but4reuse.adapters.csv;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.CSVUtils;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Point;

/**
 * CSV Adapter
 * 
 * @author jabier.martinez
 */
public class CSVAdapter implements IAdapter {

	@Override
	/**
	 * is a csv file?
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "csv")) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Read the file and loop through the comma separated values
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int row = 0;
			while ((strLine = br.readLine()) != null) {
				String[] cells = strLine.split(";");
				int column = 0;
				for (String cell : cells) {
					// Create cell element
					CellElement cellElement = new CellElement(cell, row, column);
					// Add dependency to its position
					cellElement.addDependency(new PositionDependencyObject(new Point(row, column)));
					// Add marker
					// TODO now the marker is on the whole line, adjust to
					// column also
					cellElement.setMarkerInfo(uri, row);
					elements.add(cellElement);

					column++;
				}
				row++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	/**
	 * Create a csv file with the given cells
	 */
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {

		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "csvConstruction.csv");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			// Calculate max width and height
			int maxRow = Integer.MIN_VALUE;
			int maxColumn = Integer.MIN_VALUE;
			for (IElement element : elements) {
				if (element instanceof CellElement) {
					CellElement cell = (CellElement) element;
					int row = cell.getRow();
					int column = cell.getColumn();
					if (row > maxRow) {
						maxRow = row;
					}
					if (column > maxColumn) {
						maxColumn = column;
					}
				}
			}

			// Initialize the matrix
			List<List<Object>> matrix = new ArrayList<List<Object>>();
			for (int y = 0; y <= maxRow; y++) {
				List<Object> row = new ArrayList<Object>();
				for (int x = 0; x <= maxColumn; x++) {
					row.add("");
				}
				matrix.add(row);
			}

			// Put the values
			for (IElement element : elements) {
				if (element instanceof CellElement) {
					CellElement cell = (CellElement) element;
					int row = cell.getRow();
					int column = cell.getColumn();
					String value = cell.getValue();
					matrix.get(row).set(column, value);
				}
			}

			// Export it
			CSVUtils.exportCSV(uri, matrix);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
