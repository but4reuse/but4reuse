package org.but4reuse.adapters.cppcdt.callhierarchy.doxygen;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.but4reuse.utils.files.FileUtils;

/**
 * A class abstraction for a doxyfile (doxygen configuration file) .
 * 
 * @author sandu.postaru
 *
 */

public class DoxygenConfig {

	/**
	 * Name of the created file.
	 */
	private final String NAME = "Doxyfile";

	private final String OUTPUT = "/Doxygen";

	/**
	 * Name of the field containing the project source code that doxygen will
	 * analyze.
	 */
	private final String INPUT_LOCATION = "INPUT                  = ";

	/**
	 * Name of the field containing the output location for the doxygen files.
	 */

	private final String OUTPUT_LOCATION_HTML = "\n\nHTML_OUTPUT            = ";

	private final String OUTPUT_LOCATION_XML = "\nXML_OUTPUT             = ";

	/**
	 * Configuration file content.
	 */
	private String doxyfileContent;

	/**
	 * Configuration file.
	 */
	private File doxyfile;

	/**
	 * C++ source code location.
	 */
	private File cppSourceFolder;

	/**
	 * Output folder for doxygen
	 */
	private File outputFolderHtml;

	private File outputFolderXml;

	/**
	 * Creates a new Doxygen configuration file
	 * 
	 * @param cppSourceFolder
	 *            the location of the source code
	 */

	public DoxygenConfig(File cppSourceFolder) {
		if (!cppSourceFolder.isDirectory()) {
			this.cppSourceFolder = cppSourceFolder.getParentFile();
		} else {
			this.cppSourceFolder = cppSourceFolder;
		}
		doxyfileContent = DoxygenConfigSource.TEXT + INPUT_LOCATION + cppSourceFolder.getAbsolutePath()
				+ OUTPUT_LOCATION_HTML + cppSourceFolder.getAbsolutePath() + OUTPUT + "html" + OUTPUT_LOCATION_XML
				+ cppSourceFolder.getAbsolutePath() + OUTPUT + "xml";
	}

	/**
	 * Saves the file to to desired location .
	 * 
	 */

	protected void write() {

		Path path = Paths.get(cppSourceFolder.getAbsolutePath(), NAME);
		doxyfile = path.toFile();

		path = Paths.get(cppSourceFolder.getAbsolutePath(), OUTPUT + "html");
		outputFolderHtml = path.toFile();

		path = Paths.get(cppSourceFolder.getAbsolutePath(), OUTPUT + "xml");
		outputFolderXml = path.toFile();

		try {
			FileUtils.writeFile(doxyfile, doxyfileContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deletes the config file and the doxygen output folder from the user
	 * space.
	 */

	protected void dispose() {
		FileUtils.deleteFile(doxyfile);
		FileUtils.deleteFile(outputFolderHtml);
		FileUtils.deleteFile(outputFolderXml);
	}

	protected File getConfigFile() {
		return doxyfile;
	}

	protected File getOutputFolderHtml() {
		return outputFolderHtml;
	}

	protected File getOutputFolderXml() {
		return outputFolderXml;
	}

}
