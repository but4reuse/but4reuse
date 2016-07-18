package org.but4reuse.adapter.sourcecode.extension;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IDependencyObject;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;
import org.but4reuse.adapters.sourcecode.FSTNonTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.FSTTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.JavaSourceCodeAdapter;
import org.but4reuse.adapters.sourcecode.adapter.JavaLanguage;
import org.but4reuse.utils.files.CSVUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTNonTerminal;

public class JavaSourceCodeAdapterExtension extends JavaSourceCodeAdapter {

	/**
	 * 
	 */
	@Override
	public void addMoreDependencies(List<IElement> elements) {

		// TODO Auto-generated method stub
		File fileNode;
		fileNode = new File("/home/colympio/workspace/puckTest/out/nodes.csv");
		File fileEdge = new File("/home/colympio/workspace/puckTest/out/edges.csv");

		List<EdgeFromCSV> edgeMap = createEdgeMap(CSVUtils.importCSV(fileEdge.toURI()));
		System.out.println("edgesize = "+edgeMap.size());
		System.out.println(edgeMap.toString());
		List<NodeFromCSV> nodeMap = createNodeMap(CSVUtils.importCSV(fileNode.toURI()));
		System.out.println("nodeMap size = "+nodeMap.size());
		Map<String, String> DefMeth = createDefinitionMethode(nodeMap, edgeMap);
		System.out.println("defmet size = "+DefMeth.size());
		Map<String, IElement> resultList = getFSTNodeElement(nodeMap, elements, DefMeth);
		System.out.println("resultlist size = "+resultList.size());
		System.out.println("resultlist = "+resultList.toString());

		IElement e;
		ArrayList<IDependencyObject> dependancies;
		for (EdgeFromCSV edge : edgeMap) {
			if (edge.getType().equals("Uses") || edge.getType().equals("Isa")) {
				e = resultList.get(edge.getId());
				dependancies = new ArrayList<IDependencyObject>();
				for (String list : edge.getTarget()) {
					dependancies.add(resultList.get(list));
				}
				if(e!=null){
					System.out.println("AAAAAAAAAAAAAADDDDDDDDDDDDDDDDD");
					System.out.println(dependancies);
					((AbstractElement) e).addDependencies(dependancies);
				}
				
			}
		}

		for (IElement iElement : elements) {
			JavaLanguage java = new JavaLanguage();
			System.out.println(iElement.getClass());
			if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
				System.out.println("Qualified Name:" + java.getQualifiedName(((FSTNodeElement) iElement).getNode()));
			}
			System.out.println("name: " + iElement.getText());
			System.out.println("dependants: " + iElement.getDependants());
			System.out.println("dependancies: " + iElement.getDependencies()+"\n");
		}

	}

	Map<String, IElement> getFSTNodeElement(List<NodeFromCSV> nodeMap, List<IElement> elements,
			Map<String, String> defMeth) {
		JavaLanguage java = new JavaLanguage();

		
		Map<String, IElement> list = new HashMap<String, IElement>();
		NodeFromCSV nodeTemp;
		boolean condition = false;
		int i = 0;
		// for node
		// for element
		// if element.type equals ClassDeclaration or MethodDeclaration
		// if isNodeEqualsToElement(node,element)
		// list.put(node.id,element)
		for (NodeFromCSV node : nodeMap) {
			Iterator<IElement> it = elements.iterator();
			while (it.hasNext() && !condition) {
				IElement iElement = (IElement) it.next();
				if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
					String id = getResearch(node, defMeth);
					nodeTemp = getNodeByID(id, nodeMap);
					if (isNodeEqualsToElement(nodeTemp, iElement)) {
						list.put(id, iElement);
						condition = true;
					}
				}

			}
			condition = false;
		}

		/**
		 * public static boolean isNodeEqualsToElement (node , element) {
		 * nodequalified = node.qualified if(node is definition){ nodequalified
		 * = nodequalified.replace(".Definition","") } elementqualified =
		 * element.qualified if(nodequalified.equals(elementqualified)){
		 * if(sameTypesParameters etc.){ return true } } return false }
		 **/
/*
		int condition2 = 0;
		for (IElement iElement : elements) {
			if (iElement instanceof FSTNonTerminalNodeElement || iElement instanceof FSTTerminalNodeElement) {
				Iterator<NodeFromCSV> it2 = nodeMap.iterator();
				System.out.println("=====================================");
				while (it2.hasNext() && condition2 == 0) {
					nodeTemp = it2.next();

					/*System.out.println(i);
					System.out.println("Node Qualified name: " + nodeTemp.getQualifiedName());
					System.out.println("iElement Qualified name: "
							+ (java.getQualifiedName(((FSTNodeElement) iElement).getNode())));
					System.out.println("Node name: " + nodeTemp.getName());
					System.out.println("iElement name: " + ((FSTNodeElement) iElement).getNode().getName());
					if (nodeTemp.getName().equals(((FSTNodeElement) iElement).getName())) {
						System.out.println("Adding");
						list.put(nodeTemp.getId(), iElement);
						condition2 = 1;
					}

				}
				System.out.println("=====================================");
				i++;
				condition2 = 0;
			}

		}
*/
		return list;

	}

	public NodeFromCSV getNodeByID(String id, List<NodeFromCSV> nodeMap) {
		for (NodeFromCSV nodeFromCSV : nodeMap) {
			if (nodeFromCSV.getId().equals(id)) {
				return nodeFromCSV;
			}
		}
		return null;

	}

	public static String getResearch(NodeFromCSV node, Map<String, String> defMeth) {
		String id = null;

		if (node.getType().equals("Definition"))
			id = defMeth.get(node.getId());
		else
			id = node.getId();

		return id;
	}

	public static boolean isNodeEqualsToElement(NodeFromCSV node, IElement element) {
		if (node != null && element != null) {
			String nodeString = node.getQualifiedName();
			if (node.getQualifiedName().contains(".")) {
				nodeString = nodeString.replace(".Definition", "");
			}

			JavaLanguage java = new JavaLanguage();
			String iElementName = java.getQualifiedName(((FSTNodeElement) element).getNode());
			iElementName = iElementName.replaceAll("[(].*[)]", "");
			
			//String elementString = null;
			/*String[] tab2 = java.getQualifiedName(((FSTNodeElement) element).getNode()).split(".");
			System.out.println(((FSTNodeElement) element).getNode());
			System.out.println();
			for (int i = 0; i < tab2.length - 1; i++) {
				elementString += tab2[i] + ".";
			}*/
			
			boolean resultat = iElementName.equals(nodeString);
			System.out.println(iElementName);
			System.out.println(nodeString);
			System.out.println(resultat);
			if (resultat) {
				System.out.println("AAAAAA");
			}
			return resultat;
		}
		else {
			return false;
		}
		

	}

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
		int condition = 0;
		Iterator<NodeFromCSV> it;
		for (int i = 0; i < matrixNodes.length; i++) {
			nodeMap.add(new NodeFromCSV(matrixNodes[i][0], matrixNodes[i][1], matrixNodes[i][2], matrixNodes[i][3],
					matrixNodes[i][4]));
			//System.out.println("id : " + matrixNodes[i][0] + " ||kind : " + matrixNodes[i][1] + " ||name : "
				//	+ matrixNodes[i][2] + " ||qualifiedName : " + matrixNodes[i][3] + " ||type : " + matrixNodes[i][4]);
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

	/*public List<EdgeFromCSV> Shrinklist(String[][] matrixEdge) {

	}*/
}