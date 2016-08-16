package org.but4reuse.adapter.sourcecode.extension;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;
import org.but4reuse.adapters.sourcecode.FSTNonTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.FSTTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.JavaSourceCodeAdapter;
import org.but4reuse.adapters.sourcecode.adapter.JavaLanguage;
import org.but4reuse.puck.PuckUtils;
import org.but4reuse.utils.files.CSVUtils;
import org.but4reuse.utils.files.FileUtils;

public class JavaSourceCodeAdapterExtension extends JavaSourceCodeAdapter {

	/**
	 * Add dependencies to the elements using 2 CSV files (node and edge)
	 */
	@Override
	public void addMoreDependencies(List<IElement> elements, URI uri) {
		File uriTempCSVfolder = null;
		File uriFile = org.but4reuse.utils.files.FileUtils.getFile(uri);
		if(uriFile.isFile()){
			uriTempCSVfolder = new File(uriFile.getParentFile(),"tempFolderForCSV");
		}else{
			uriTempCSVfolder = new File(uriFile,"tempFolderForCSV");
		}
		
		uriTempCSVfolder.mkdirs();
		System.out.println(uriTempCSVfolder.toURI().toString());
		PuckUtils.createCSV(uri, uriTempCSVfolder.toURI());
		
		List<EdgeFromCSV> edgeMap = null;
		List<NodeFromCSV> nodeMap = null;
		
		for (File file : FileUtils.getAllFiles(uriTempCSVfolder)) {
			if (file.getPath().contains("edges")) {
				edgeMap = createEdgeMap(CSVUtils.importCSV(file.toURI()));
			}
			if (file.getPath().contains("nodes")) {
				nodeMap = createNodeMap(CSVUtils.importCSV(file.toURI()));
			}
		}
		
		Map<String, String> defMeth = createDefinitionMethod(nodeMap, edgeMap);
		Map<String, IElement> resultList = getFSTNodeElement(nodeMap, elements, defMeth);

		for (EdgeFromCSV edge : edgeMap) {
			if (edge.getType().equals("Uses") || edge.getType().equals("Isa")) {
				IElement e = resultList.get(edge.getId());
				if (e != null) {
					for (String target : edge.getTarget()) {
						IElement dep = resultList.get(target);
						if(dep == null){
							System.out.println("Error not found");
							System.out.println("target : "+target);
						}
						else
							((AbstractElement) e).addDependency(edge.getType(), dep);
					}
				}
			}
		}

		if (uriTempCSVfolder != null) {
			PuckUtils.supressCSV(uriTempCSVfolder.toURI());
		}

	}

	/**
	 * Create a map of NodeFromCSV ids and FSTNodeElements. If NodeFromCSV is a
	 * definition the node associated to the id is the methode one.
	 * 
	 * @param nodeMap
	 * @param elements
	 * @param defMeth
	 * @return Map of corresponding NodeFromCSVElments ids and IElements
	 */
	Map<String, IElement> getFSTNodeElement(List<NodeFromCSV> nodeMap, List<IElement> elements,
			Map<String, String> defMeth) {

		Map<String, IElement> list = new HashMap<String, IElement>();
		NodeFromCSV nodeTemp;
		boolean condition = false;
		for (NodeFromCSV node : nodeMap) {
			Iterator<IElement> it = elements.iterator();
			while (it.hasNext() && !condition) {
				IElement iElement = (IElement) it.next();
				if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
					FSTNodeElement temp = ((FSTNodeElement) iElement);
					while (temp != null && !condition) {
						String id = getResearch(node, defMeth);
						nodeTemp = getNodeByID(id, nodeMap);
						if (isNodeEqualsToElement(nodeTemp, temp)) {
							list.put(node.getId(), temp);
							condition = true;
						}
						temp = temp.getParent();
					}

				}

			}
			
			condition = false;
		}
		return list;

	}

	/**
	 * @param id
	 * @param nodeMap
	 * @return
	 */
	public NodeFromCSV getNodeByID(String id, List<NodeFromCSV> nodeMap) {
		for (NodeFromCSV nodeFromCSV : nodeMap) {
			if (nodeFromCSV.getId().equals(id)) {
				return nodeFromCSV;
			}
		}
		return null;

	}

	/**
	 * Return the id of the corresponding target in definotion/methode map if
	 * the node is a definition
	 * 
	 * @param node
	 * @param defMeth
	 * @return
	 */
	public static String getResearch(NodeFromCSV node, Map<String, String> defMeth) {
		String id = null;

		if (node.getKind().equals("Definition"))
			id = defMeth.get(node.getId());
		else
			id = node.getId();

		return id;
	}

	/**
	 * Verifiy if a node is equal to a FSTNodeElement
	 * 
	 * @param node
	 * @param element
	 * @return
	 */
	public static boolean isNodeEqualsToElement(NodeFromCSV node, IElement element) {
		if (node != null && element != null) {
			String nodeString = node.getQualifiedName();
			if (node.getQualifiedName().contains(".")) {
				nodeString = nodeString.replace(".Definition", "");
			}

			JavaLanguage java = new JavaLanguage();
			String iElementName = java.getQualifiedName(((FSTNodeElement) element).getNode());
			iElementName = iElementName.replaceAll("[(].*[)]", "");
			if (iElementName.contains("lang")) {
				System.out.println("Yes");
			}
			//System.out.println(iElementName +"    "+nodeString);
			//System.out.println("equlas:" + iElementName.equals(nodeString));
			return iElementName.equals(nodeString);
		} else {
			return false;
		}

	}

	/**
	 * Create the map definition/methode
	 * 
	 * @param listNode
	 * @param listEdge
	 * @return
	 */
	public Map<String, String> createDefinitionMethod(List<NodeFromCSV> listNode, List<EdgeFromCSV> listEdge) {
		Map<String, String> mapDefMeth = new HashMap<String, String>();

		for (NodeFromCSV nodeFromCSV : listNode) {
			if (nodeFromCSV.getKind()!= null && nodeFromCSV.getKind().contains("Definition")) {
				for (EdgeFromCSV edgeFromCSV : listEdge) {

					if (edgeFromCSV.getTarget().contains(nodeFromCSV.getId())) {
						mapDefMeth.put(nodeFromCSV.getId(), edgeFromCSV.getId());
					}
				}
			}
		}

		return mapDefMeth;
	}

	/**
	 * Create a nodeMap using the node matrix
	 * 
	 * @param matrixNodes
	 * @param edgeMap
	 * @return
	 */
	public List<NodeFromCSV> createNodeMap(String[][] matrixNodes) {
		//TODO correct the error
		ArrayList<NodeFromCSV> nodeMap = new ArrayList<NodeFromCSV>();
		for (int i = 0; i < matrixNodes.length; i++) {
			if(matrixNodes[i][0]!=null && matrixNodes[i][1]!=null && matrixNodes[i][2]!=null){
				nodeMap.add(new NodeFromCSV(matrixNodes[i][0], matrixNodes[i][1], matrixNodes[i][2], matrixNodes[i][3],
						matrixNodes[i][4]));
			}
			
		}
		return nodeMap;

	}
	
	public Map<String, ArrayList<String>> createParams(List<NodeFromCSV> listNode) {
		Map<String, ArrayList<String>> mapClassParams = new HashMap<String, ArrayList<String>>();
		for (NodeFromCSV paramNode : listNode) {
			if (paramNode!= null && paramNode.getKind()!=null && paramNode.getKind().equals("Param")) {
				
				String idTarget = getIdTarget(listNode, paramNode);
				String idKey = getIdKey(listNode,paramNode);
				
				if (idKey != null && idTarget != null) {
					if(mapClassParams.containsKey(idKey)) {
						mapClassParams.get(idKey).add(idTarget);
					}else {
						ArrayList<String> targetList = new ArrayList<String>();
						targetList.add(idTarget);
						mapClassParams.put(idKey, targetList);
					}
				}
			}
		}
		return mapClassParams;
	}
	
	public String getIdTarget(List<NodeFromCSV> listNode,NodeFromCSV paramNode) {
		for (NodeFromCSV currentNode : listNode) {
			if (currentNode != null && currentNode.getName()!=null && currentNode.getName().equals(paramNode.getType())) {
				return currentNode.getId();
			}
		}
		return null;
	}
	
	public String getIdKey(List<NodeFromCSV> listNode,NodeFromCSV paramNode) {
		String[] qualifiedNameParts = paramNode.getQualifiedName().split(".");
		for (String string : qualifiedNameParts) {
			for (NodeFromCSV currentNode : listNode) {
				if (currentNode!= null && currentNode.getKind()!=null && currentNode.getKind().equals("Class") && currentNode.getName().equals(string)) {
					return currentNode.getId();
				}
			}
		}
		return null;
	}


	/**
	 * Create the edgeMap using the edge matrix
	 * 
	 * @param matrixEdge
	 * @return
	 */
	public List<EdgeFromCSV> createEdgeMap(String[][] matrixEdge) {
		ArrayList<EdgeFromCSV> map = new ArrayList<EdgeFromCSV>();
		boolean verif = false;
		for (int i = 0; i < matrixEdge.length; i++) {
			for (EdgeFromCSV edgeFromCSV : map) {
				if (edgeFromCSV.getId() == matrixEdge[i][0]) {
					verif = true;
					edgeFromCSV.addTarget(matrixEdge[i][1]);
				}
			}
			if (!verif) {
				EdgeFromCSV newEdge = new EdgeFromCSV(matrixEdge[i][0], matrixEdge[i][2]);
				newEdge.addTarget(matrixEdge[i][1]);
				map.add(newEdge);

			}
			verif = false;
		}
		return map;
	}

}