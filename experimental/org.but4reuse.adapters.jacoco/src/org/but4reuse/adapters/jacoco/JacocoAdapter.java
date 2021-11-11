package org.but4reuse.adapters.jacoco;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.but4reuse.adapters.IAdapter;
import org.but4reuse.adapters.IElement;
import org.but4reuse.utils.files.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * JaCoCo adapter
 * 
 * @author jabier.martinez
 */
public class JacocoAdapter implements IAdapter {

	@Override
	public boolean isAdaptable(URI uri, IProgressMonitor monitor) {
		File file = FileUtils.getFile(uri);
		if (file != null && file.exists() && !file.isDirectory() && FileUtils.isExtension(file, "xml")) {
			return true;
		}
		return false;
	}

	@Override
	public List<IElement> adapt(URI uri, IProgressMonitor monitor) {
		List<IElement> elements = new ArrayList<IElement>();
		File file = FileUtils.getFile(uri);
		elements = adapt(file);
		return elements;
	}

	public List<IElement> adapt(File xmlFile) {
		List<IElement> elements = new ArrayList<IElement>();

		// parse the XML file using StaX
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = new FileInputStream(xmlFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			String currentPackage = null;
			String currentSourceFile = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String elementName = startElement.getName().getLocalPart();

					if (elementName.equals("package")) {
						currentPackage = startElement.getAttributeByName(new QName("name")).getValue();
					}

					if (elementName.equals("sourcefile")) {
						currentSourceFile = startElement.getAttributeByName(new QName("name")).getValue();
					}

					if (elementName.equals("line")) {
						String lineNumber = startElement.getAttributeByName(new QName("nr")).getValue();
						String coveredInstructions = startElement.getAttributeByName(new QName("ci")).getValue();
						Integer execInt = Integer.parseInt(coveredInstructions);
						if (execInt > 0) {
							// add element
							CoveredLineElement element = new CoveredLineElement();
							element.packageName = currentPackage;
							element.fileName = currentSourceFile;
							element.lineNumber = Integer.parseInt(lineNumber);
							elements.add(element);
						}
					}
				}
			}

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
				uri = new URI(uri.toString() + "executionTraces.txt");
			}
			// Create file if it does not exist
			File file = FileUtils.getFile(uri);
			FileUtils.deleteFile(file);
			FileUtils.createFile(file);

			for (IElement element : elements) {
				FileUtils.appendToFile(file, element.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
