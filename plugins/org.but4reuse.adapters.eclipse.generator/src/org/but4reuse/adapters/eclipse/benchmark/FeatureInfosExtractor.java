package org.but4reuse.adapters.eclipse.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.but4reuse.utils.files.PropertiesFileUtils;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Feature Infos extractor
 * 
 * @author jabier.martinez
 * 
 */
public class FeatureInfosExtractor {

	public static ActualFeature getFeatureInfos(String absolutePath) {
		ActualFeature actualFeature = new ActualFeature();
		try {
			File xmlFile = new File(absolutePath, FeatureHelper.FEATURE_XML);
			// Use DOM xml parser
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			// Get the id
			String id = doc.getDocumentElement().getAttribute("id");
			actualFeature.setId(id);

			// Get the name
			String name = doc.getDocumentElement().getAttribute("label");
			if (name.contains("%")) {
				String key = PropertiesFileUtils.getKey(name);
				name = PropertiesFileUtils.getValue(new File(xmlFile.getParentFile(), FeatureHelper.FEATURE_PROPERTIES), key);
			}
			actualFeature.setName(name);

			// Get the description
			String description = doc.getDocumentElement().getElementsByTagName("description").item(0).getTextContent();
			if (description.startsWith("\n")) {
				description = description.replaceFirst("\n", "");
			}
			if (description.contains("%")) {
				description = description.replaceAll("\\s+", "");
				String key = PropertiesFileUtils.getKey(description);
				description = PropertiesFileUtils.getValue(new File(xmlFile.getParentFile(), FeatureHelper.FEATURE_PROPERTIES),
						key);
			}
			actualFeature.setDescription(description);

			// Get the list of plugins of the feature
			List<String> plugins = new ArrayList<String>();
			NodeList nList = doc.getElementsByTagName("plugin");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					// TODO for the moment only os win32_x86
					String os = eElement.getAttribute("os");
					if (os.isEmpty() || os.equalsIgnoreCase("win32")) {
						String arch = eElement.getAttribute("arch");
						if (arch.isEmpty() || arch.equalsIgnoreCase("x86_64")) {
							String pluginId = eElement.getAttribute("id");
							// avoid duplicated with different versions as in
							// e4.rcp with equinox.launcher
							if (!plugins.contains(pluginId)) {
								plugins.add(pluginId);
							}
						}
					}
				}
			}
			actualFeature.setPlugins(plugins);

			// Get the included/nested features
			List<String> included = new ArrayList<String>();
			nList = doc.getElementsByTagName("includes");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String optional = eElement.getAttribute("optional");
					// Check optional
					if (!optional.equalsIgnoreCase("true")) {
						included.add(eElement.getAttribute("id"));
					}
				}
			}
			actualFeature.setIncludedFeatures(included);

			// Get the required plugins and features
			List<String> requiredPlugins = new ArrayList<String>();
			List<String> requiredFeatures = new ArrayList<String>();
			nList = doc.getElementsByTagName("import");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String requiredId = eElement.getAttribute("plugin");
					if (!requiredId.isEmpty()) {
						requiredPlugins.add(requiredId);
					}
					requiredId = eElement.getAttribute("feature");
					if (!requiredId.isEmpty()) {
						requiredFeatures.add(requiredId);
					}
				}
			}
			actualFeature.setRequiredPlugins(requiredPlugins);
			actualFeature.setRequiredFeatures(requiredFeatures);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actualFeature;
	}

}
