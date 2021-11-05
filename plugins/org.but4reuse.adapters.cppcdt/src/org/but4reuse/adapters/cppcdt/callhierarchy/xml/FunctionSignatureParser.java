package org.but4reuse.adapters.cppcdt.callhierarchy.xml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.but4reuse.adapters.cppcdt.utils.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class maps for all the functions their id to their real signature. For
 * more information on why this is needed, check out the
 * org.but4reuse.adapters.cpp.callhierarchy.doxygen.DoxygenAnalyser class which
 * contains a complete description.
 * 
 * @author sandu.postaru
 *
 */

public class FunctionSignatureParser {

	/**
	 * Type separator used for the function signatures. The $ symbols was chose
	 * since it's a invalid character for a function name in C++ so there is no
	 * confusion between the name and the signature
	 * 
	 * EXAMPLE : void Dog::bark(int, double) -> Dog::bark~int~double
	 * 
	 */

	public static final Character TYPE_SEPARATOR = '~';

	private DocumentBuilder documentBuilder;

	/** Map containing the id and the function signature */
	private Map<String, String> signatureMap;

	public FunctionSignatureParser() {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		signatureMap = new HashMap<String, String>();
	}

	public void parse(File file) {
		try {
			Document document = documentBuilder.parse(file);
			NodeList nodes = document.getElementsByTagName("compoundname");
			String className = "";

			List<Pair<String, String>> functions = new LinkedList<Pair<String, String>>();

			if (nodes.getLength() > 0) {
				className = nodes.item(0).getTextContent();
			}

			StringBuffer signature = new StringBuffer();

			// possible function definitions
			NodeList memberDefs = document.getElementsByTagName("memberdef");

			for (int i = 0; i < memberDefs.getLength(); i++) {
				Node node = memberDefs.item(i);

				NamedNodeMap atrs = node.getAttributes();

				// function definition
				if (atrs.getNamedItem("kind").getNodeValue().equals("function")) {
					String id = (atrs.getNamedItem("id").getNodeValue()).trim();
					String functionName = "";

					signature.setLength(0);

					NodeList children = node.getChildNodes();

					// extract the name and the type
					for (int j = 0; j < children.getLength(); j++) {
						Node n = children.item(j);

						if (n.getNodeName().equals("name")) {
							functionName = n.getTextContent();

						} else if (n.getNodeName().equals("param")) {
							NodeList paramChildren = n.getChildNodes();

							if (paramChildren.getLength() > 1) {

								String type = paramChildren.item(1).getTextContent();
								type = type.replaceAll("\\*|&", "").replaceAll("< ", "<").replace(" >", ">");

								signature.append(TYPE_SEPARATOR + type);
							}
						}
					}

					String completeSignature;

					if (functionName.equals("main")) {
						completeSignature = (functionName + signature).trim();
						id = "main";
						functions.add(new Pair<String, String>(completeSignature, id.trim()));
					} else {
						completeSignature = (className + "::" + functionName + signature).trim();
						functions.add(new Pair<String, String>(completeSignature, transformId(id).trim()));
					}

				}

				if (!functions.isEmpty()) {
					updateSignatureMap(functions);
				}
			}

		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	private void updateSignatureMap(List<Pair<String, String>> elements) {
		for (Pair<String, String> p : elements) {

			signatureMap.put(p.second, p.first);
		}
	}

	public Map<String, String> getSignatureMap() {
		return signatureMap;
	}

	/**
	 * Transforms the .xml id into the .dot format that is used in the call
	 * graphs.
	 * 
	 * @param id
	 *            xml id
	 * @return the .dot id
	 */
	private String transformId(String id) {
		StringBuffer temp = new StringBuffer(id);

		int index = temp.indexOf("_");
		temp.replace(index, index + 2, ".html#");
		temp.insert(0, "$");

		return temp.toString();
	}
}
