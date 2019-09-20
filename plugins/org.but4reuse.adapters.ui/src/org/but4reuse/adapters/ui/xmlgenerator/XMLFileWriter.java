package org.but4reuse.adapters.ui.xmlgenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.but4reuse.adapters.IElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLFileWriter {
	private Map<BlockData, List<ArtefactData>> block_stats_reuse;
	private Map<BlockData, List<ArtefactData>> block_stats_unique;
	private Map<String, Double> artefact_stats;
	private ArrayList<ArtefactStat> levelofreuse_stats;
	private List<String> adaptersName;
	private Map<String, String> adapterConfig;
	private Map<String, String> modeConfig;

	public XMLFileWriter(List<String> adaptersName,
						 Map<BlockData, List<ArtefactData>> block_stats_reuse,
						 Map<BlockData, List<ArtefactData>> block_stats_unique,
						 Map<String, Double> artefact_stats,
						 ArrayList<ArtefactStat> levelofreuse_stats,
						 Map<String, String> adapterConfig,
						 Map<String, String> modeConfig
	) {
		this.adaptersName = adaptersName;
		this.block_stats_reuse = block_stats_reuse;
		this.block_stats_unique = block_stats_unique;
		this.artefact_stats = artefact_stats;
		this.levelofreuse_stats = levelofreuse_stats;
		this.adapterConfig = adapterConfig;
		this.modeConfig = modeConfig;
	}
	
	public void generateXmlfile(File f) {
		try {
			
			// Use DOM Parser to create xml file
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document doc = documentBuilder.newDocument();
			
			// root element
			Element root = doc.createElement("Stats");
			doc.appendChild(root);
			
			// List of adapters used and configurations of the JavaUnderstandingAdapter
			Element elem_adaptersName = doc.createElement("AdaptersName");
			root.appendChild(elem_adaptersName);
			for(String name : adaptersName) {
				Element node_adapter = doc.createElement("node");
				elem_adaptersName.appendChild(node_adapter);
				node_adapter.setAttribute("name",name);
				
				if(name.equals("Java Understanding")) {
					Element node_ignorePath = doc.createElement("node");
					node_adapter.appendChild(node_ignorePath);
					node_ignorePath.setAttribute("name", "IgnorePath");
					node_ignorePath.setAttribute("value", adapterConfig.get("IgnorePath"));
					
					Element node_mode = doc.createElement("node");
					node_adapter.appendChild(node_mode);
					node_mode.setAttribute("name", "Mode");
					node_mode.setAttribute("value", adapterConfig.get("Mode"));
					
					for(String config : modeConfig.keySet()) {
						Element node_mode_config = doc.createElement("node");
						node_mode.appendChild(node_mode_config);
						node_mode_config.setAttribute("name", config);
						node_mode_config.setAttribute("value", modeConfig.get(config));
					}
				}
			}
			
			// List of artefacts with their level of reuse 
			// and the artefacts which reuse the elements of this artefact
			Element elem_StatReuse = doc.createElement("StatReuse");
			root.appendChild(elem_StatReuse);
			for (ArtefactStat n: levelofreuse_stats) {
				Element node_StatReuse = doc.createElement("node");
				elem_StatReuse.appendChild(node_StatReuse);
				node_StatReuse.setAttribute("name", n.getName());
				node_StatReuse.setAttribute("levelofreuse", String.valueOf(n.getLevelOfReuse()));
				
				Element node_StatReuse_reuse = doc.createElement("node");
				node_StatReuse.appendChild(node_StatReuse_reuse);
				node_StatReuse_reuse.setAttribute("name", "Reused elements");
				node_StatReuse_reuse.setAttribute("number", String.valueOf(n.getNumberOfElementsShared()));
				
				for(String d : n.getListStats().keySet() ) {
					Element node_listStats = doc.createElement("node");
					node_StatReuse_reuse.appendChild(node_listStats);
					node_listStats.setAttribute("name", d);
					node_listStats.setAttribute("levelofreuse", String.valueOf(n.getListStats().get(d)));
					for(int i=0 ; i < n.getListElementsString(d).size() ; i++) {
						Element node_element = doc.createElement("element");
						node_listStats.appendChild(node_element);
						node_element.setAttribute("element", n.getListElements().get(d).get(i).getText());
					}
				}
				
				Element node_StatReuse_unique = doc.createElement("node");
				node_StatReuse.appendChild(node_StatReuse_unique);
				node_StatReuse_unique.setAttribute("name", "Unique elements");
				node_StatReuse_unique.setAttribute("number", String.valueOf(n.getListOfUniqueElements().size()));
				
				for(IElement ie : n.getListOfUniqueElements()) {
					Element node_StatReuse_unique_element = doc.createElement("element");
					node_StatReuse_unique.appendChild(node_StatReuse_unique_element);
					node_StatReuse_unique_element.setAttribute("element", ie.getText());
				}
			}
			
			// List of reused blocks with their content and the names of artefacts that contain this block
			Element elem_BlockReuseAnalyse = doc.createElement("BlockReuseAnalyse");
			root.appendChild(elem_BlockReuseAnalyse);
			for (BlockData n: block_stats_reuse.keySet()) {
				Element node_BlockReuseAnalyse = doc.createElement("node");
				elem_BlockReuseAnalyse.appendChild(node_BlockReuseAnalyse);
				node_BlockReuseAnalyse.setAttribute("name", n.getName());
				node_BlockReuseAnalyse.setAttribute("nbartefacts", String.valueOf(block_stats_reuse.get(n).size()));
				node_BlockReuseAnalyse.setAttribute("nbelems", String.valueOf(n.getNbElem()));
			
				Element node_BlockReuseAnalyse_artefacts = doc.createElement("artefacts");
				node_BlockReuseAnalyse.appendChild(node_BlockReuseAnalyse_artefacts);
				
				for(ArtefactData ad : block_stats_reuse.get(n)) {
					Element node_BlockReuseAnalyse_artefacts_stats = doc.createElement("artefact");
					node_BlockReuseAnalyse_artefacts.appendChild(node_BlockReuseAnalyse_artefacts_stats);
					node_BlockReuseAnalyse_artefacts_stats.setAttribute("name", ad.getName());
					node_BlockReuseAnalyse_artefacts_stats.setAttribute("percentage", String.valueOf(((n.getNbElem()*1.0)/ad.getNb_elems())*100));
				}
				
				Element node_BlockReuseAnalyse_contents = doc.createElement("content");
				node_BlockReuseAnalyse.appendChild(node_BlockReuseAnalyse_contents);
				
				for(IElement ie : n.getList()) {
					Element node_BlockReuseAnalyse_contents_stats = doc.createElement("element");
					node_BlockReuseAnalyse_contents.appendChild(node_BlockReuseAnalyse_contents_stats);
					node_BlockReuseAnalyse_contents_stats.setAttribute("name", ie.getText());
				}
			}
			
			// List of block that is unique to on artefact, with their content
			Element elem_BlockUniqueAnalyse = doc.createElement("BlockUniqueAnalyse");
			root.appendChild(elem_BlockUniqueAnalyse);
			for (BlockData n: block_stats_unique.keySet()) {
				Element node_BlockUniqueAnalyse = doc.createElement("node");
				elem_BlockUniqueAnalyse.appendChild(node_BlockUniqueAnalyse);
				node_BlockUniqueAnalyse.setAttribute("name", n.getName());
				node_BlockUniqueAnalyse.setAttribute("nbelems", String.valueOf(n.getNbElem()));
			
				Element node_BlockUniqueAnalyse_artefact = doc.createElement("artefact");
				node_BlockUniqueAnalyse.appendChild(node_BlockUniqueAnalyse_artefact);
				node_BlockUniqueAnalyse_artefact.setAttribute("name", block_stats_unique.get(n).get(0).getName());
				node_BlockUniqueAnalyse_artefact.setAttribute("percentage", String.valueOf(((n.getNbElem()*1.0)/block_stats_unique.get(n).get(0).getNb_elems())*100));
				
				Element node_BlockUniqueAnalyse_contents = doc.createElement("content");
				node_BlockUniqueAnalyse.appendChild(node_BlockUniqueAnalyse_contents);
				
				for(IElement ie : n.getList()) {
					Element node_BlockUniqueAnalyse_contents_stats = doc.createElement("element");
					node_BlockUniqueAnalyse_contents.appendChild(node_BlockUniqueAnalyse_contents_stats);
					node_BlockUniqueAnalyse_contents_stats.setAttribute("name", ie.getText());
				}
			}
			
			// Percentage of unique elements of each artefact
			Element elem_ArtefactAnalyse = doc.createElement("ArtefactAnalyse");
			root.appendChild(elem_ArtefactAnalyse);
			for (String n: artefact_stats.keySet()) {
				Element node_ArtefactAnalyse = doc.createElement("node");
				elem_ArtefactAnalyse.appendChild(node_ArtefactAnalyse);
				node_ArtefactAnalyse.setAttribute("name", n);
				node_ArtefactAnalyse.setAttribute("unique_percent", String.valueOf(artefact_stats.get(n)));
			}
			
			// Arrange file layout
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource domSource = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(f);
			transformer.transform(domSource, streamResult);
			
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public Map<BlockData, List<ArtefactData>> getBlock_stats_reuse() {
		return block_stats_reuse;
	}

	public void setBlock_stats_reuse(Map<BlockData, List<ArtefactData>> block_stats_reuse) {
		this.block_stats_reuse = block_stats_reuse;
	}

	public Map<BlockData, List<ArtefactData>> getBlock_stats_unique() {
		return block_stats_unique;
	}

	public void setBlock_stats_unique(Map<BlockData, List<ArtefactData>> block_stats_unique) {
		this.block_stats_unique = block_stats_unique;
	}

	public Map<String, Double> getArtefact_stats() {
		return artefact_stats;
	}

	public void setArtefact_stats(Map<String, Double> artefact_stats) {
		this.artefact_stats = artefact_stats;
	}

	public ArrayList<ArtefactStat> getLevelofreuse_stats() {
		return levelofreuse_stats;
	}

	public void setLevelofreuse_stats(ArrayList<ArtefactStat> levelofreuse_stats) {
		this.levelofreuse_stats = levelofreuse_stats;
	}

	public List<String> getAdaptersName() {
		return adaptersName;
	}

	public void setAdaptersName(List<String> adaptersName) {
		this.adaptersName = adaptersName;
	}

	public Map<String, String> getAdapterConfig() {
		return adapterConfig;
	}

	public void setAdapterConfig(Map<String, String> adapterConfig) {
		this.adapterConfig = adapterConfig;
	}

	public Map<String, String> getModeConfig() {
		return modeConfig;
	}

	public void setModeConfig(Map<String, String> modeConfig) {
		this.modeConfig = modeConfig;
	}
	
	
}
