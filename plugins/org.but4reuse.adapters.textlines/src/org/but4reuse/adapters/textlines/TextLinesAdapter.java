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

/**
 * Text Lines adapter
 * 
 * @author jabier.martinez
 */
public class TextLinesAdapter implements IAdapter {

	@Override
	/**
	 * TODO Maybe use Eclipse Text Content Type (Preferences -> General -> Content Types -> Text category)
	 */
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "txt")) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Create a LineElement from each line in the file
	 */
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int lineNumber = 0;
			while ((strLine = br.readLine()) != null) {
				if (TAB_SEPARATED) {
					String[] pieces = strLine.split("\t");
					for (String piece : pieces) {
						LineElement lineElement = new LineElement(piece);
						lineElement.setMarkerInfo(uri, lineNumber);
						elements.add(lineElement);
					}
				} else {
					LineElement lineElement = new LineElement(strLine);
					lineElement.setMarkerInfo(uri, lineNumber);
					elements.add(lineElement);
				}
				lineNumber++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public void construct(URI uri, List<IElement> elements, IProgressMonitor monitor) {
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
