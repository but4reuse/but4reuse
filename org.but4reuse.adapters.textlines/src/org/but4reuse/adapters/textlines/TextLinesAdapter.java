package org.but4reuse.adapters.textlines;

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
import org.eclipse.core.runtime.IStatus;

public class TextLinesAdapter implements IAdapter {

	@Override
	/**
	 * Files with txt or info extension
	 * TODO use Eclipse Text Content Type (Preferences -> General -> Content Types -> Text category)
	 */
	public boolean isAdaptable(URI uri, IStatus status, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && file.getName().endsWith(".txt")
				|| file.getName().endsWith(".info")) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Create a LineElement from each line in the file
	 */
	public List<IElement> adapt(URI uri, IStatus status, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (TAB_SEPARATED) {
					String[] pieces = strLine.split("\t");
					for (String piece : pieces) {
						LineElement locCP = new LineElement(piece);
						elements.add(locCP);
					}
				} else {
					LineElement lineElement = new LineElement(strLine);
					elements.add(lineElement);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IStatus status, IProgressMonitor monitor) {
		try {
			// Use the given file or use a default name if a folder was given
			if (uri.toString().endsWith("/")) {
				uri = new URI(uri.toString() + "linesConstruction.txt");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.createFile(file);

			for (IElement element : elements) {
				FileUtils.appendToFile(file, element.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO provide a regex in preferences to select a separator
	boolean TAB_SEPARATED = false;

}
