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
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * CSV Adapter
 * 
 * @author jabier.martinez
 */
public class CSVAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory()
				&& FileUtils.getExtension(file).equalsIgnoreCase("csv")) {
			return true;
		}
		return false;
	}

	@Override
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
					CellElement lineElement = new CellElement(cell,row,column);
					// TODO now the marker is on the whole line, adjust to column also
					lineElement.setMarkerInfo(uri,row);
					elements.add(lineElement);
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
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
		// TODO construct
	}

}
