package org.but4reuse.adapter.sourcecode.extension;

import java.io.File;
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
import org.but4reuse.utils.files.CSVUtils;

public class JavaSourceCodeAdapterExtension extends JavaSourceCodeAdapter {

	/**
	 * Add dependencies to the elements using 2 CSV files (node and edge)
	 */
	@Override
	public void addMoreDependencies(List<IElement> elements, File fileNode, File fileEdge) {

		// TODO Auto-generated method stub
		fileNode = new File("/home/colympio/workspace/puckTest/out/nodes.csv");
		fileEdge = new File("/home/colympio/workspace/puckTest/out/edges.csv");
		JavaLanguage java = new JavaLanguage();
		for (IElement iElement : elements) {
			if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
				System.out.println("a ::::" + java.getQualifiedName(((FSTNodeElement) iElement).getNode()));
			} else {
				System.out.println("b ::::" + iElement.getText());
			}
		}
		List<EdgeFromCSV> edgeMap = createEdgeMap(CSVUtils.importCSV(fileEdge.toURI()));
		System.out.println("edgesize = " + edgeMap.size());
		List<NodeFromCSV> nodeMap = createNodeMap(CSVUtils.importCSV(fileNode.toURI()));
		System.out.println("nodeMap size = " + nodeMap.size());
		Map<String, String> DefMeth = createDefinitionMethode(nodeMap, edgeMap);
		System.out.println("defmet size = " + DefMeth.size());
		Map<String, IElement> resultList = getFSTNodeElement(nodeMap, elements, DefMeth);
		System.out.println("resultlist size = " + resultList.size());

		for (EdgeFromCSV edge : edgeMap) {
			if (edge.getType().equals("Uses") || edge.getType().equals("Isa")) {
				IElement e = resultList.get(edge.getId());
				if (e != null) {
					for (String list : edge.getTarget()) {
						System.out.println("target :" + edge.getTarget());
						System.out.println("resultlistGEt: " + resultList.get(list));
						((AbstractElement) e).addDependency(edge.getType(), resultList.get(list));
					}
				}
			}
		}

		for (IElement iElement : elements) {
			// JavaLanguage java = new JavaLanguage();
			System.out.println(iElement.getClass());
			if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
				System.out.println("Qualified Name:" + java.getQualifiedName(((FSTNodeElement) iElement).getNode()));
			}
			System.out.println("name: " + iElement.getText());
			System.out.println("dependants: " + iElement.getDependants());
			System.out.println("dependancies: " + iElement.getDependencies() + "\n");
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
							list.put(id, temp);
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
	public Map<String, String> createDefinitionMethode(List<NodeFromCSV> listNode, List<EdgeFromCSV> listEdge) {
		Map<String, String> MapDefMeth = new HashMap<String, String>();

		for (NodeFromCSV nodeFromCSV : listNode) {
			if (nodeFromCSV.getKind().contains("Definition")) {
				for (EdgeFromCSV edgeFromCSV : listEdge) {

					if (edgeFromCSV.getTarget().contains(nodeFromCSV.getId())) {
						MapDefMeth.put(nodeFromCSV.getId(), edgeFromCSV.getId());
					}
				}
			}
		}

		return MapDefMeth;
	}

	/**
	 * Create a nodeMap using the node matrix
	 * 
	 * @param matrixNodes
	 * @param edgeMap
	 * @return
	 */
	public List<NodeFromCSV> createNodeMap(String[][] matrixNodes) {
		ArrayList<NodeFromCSV> nodeMap = new ArrayList<NodeFromCSV>();
		for (int i = 0; i < matrixNodes.length; i++) {
			nodeMap.add(new NodeFromCSV(matrixNodes[i][0], matrixNodes[i][1], matrixNodes[i][2], matrixNodes[i][3],
					matrixNodes[i][4]));
		}
		return nodeMap;

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