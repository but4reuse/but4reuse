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
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * @author colympio
 * 
 *         The JavaSourceCodeAdapterExtension enable to get the dependencies
 *         with the called methods and parameters
 */
public class JavaSourceCodeAdapterExtension extends JavaSourceCodeAdapter {

	/**
	 * 
	 * Add dependencies to the elements using the list of elements and the
	 * sources folder uri
	 */
	@Override
	public void addMoreDependencies(List<IElement> elements, URI folderUri, IProgressMonitor monitor) {
		monitor.subTask("Adding extra dependencies with the Java adapter extension");
		try {
			File uriTempCSVfolder = null;
			File uriFile = org.but4reuse.utils.files.FileUtils.getFile(folderUri);
			if (uriFile.isFile()) {
				uriTempCSVfolder = new File(uriFile.getParentFile(), "tempFolderForCSV");
			} else {
				uriTempCSVfolder = new File(uriFile, "tempFolderForCSV");
			}
			PuckUtils.createCSV(folderUri, uriTempCSVfolder.toURI());

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

			Map<String, ArrayList<String>> param = createParams(nodeMap);
			edgeMap = addEdgeParamToList(edgeMap, param);
			Map<String, String> definitionMethod = createDefinitionMethodMap(nodeMap, edgeMap);
			Map<String, IElement> fstNodeMap = getFSTNodeElement(nodeMap, elements, definitionMethod);

			for (EdgeFromCSV currentEdge : edgeMap) {
				if (currentEdge.getType().equals("Uses") || currentEdge.getType().equals("Isa")
						|| currentEdge.getType().equals("Param")) {
					IElement e = fstNodeMap.get(currentEdge.getId());
					if (e != null) {
						for (String target : currentEdge.getTarget()) {
							IElement dep = fstNodeMap.get(target);
							if (dep == null) {
								System.out.println(
										"Warnging while adding extra dependencies: Target not found: " + target);
							} else
								((AbstractElement) e).addDependency(currentEdge.getType(), dep);
						}
					}
				}
			}

			if (uriTempCSVfolder != null) {
				PuckUtils.supressCSV(uriTempCSVfolder.toURI());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create a map of NodeFromCSV ids and FSTNodeElements pairs. If the
	 * NodeFromCSV is a definition, the node associated to the id is the
	 * corresponding method one.
	 * 
	 * @param nodeMap
	 * @param elements
	 * @param definitionMethod
	 * @return Map of corresponding NodeFromCSVElments ids and IElements
	 */
	Map<String, IElement> getFSTNodeElement(List<NodeFromCSV> nodeMap, List<IElement> elements,
			Map<String, String> definitionMethod) {

		Map<String, IElement> list = new HashMap<String, IElement>();
		NodeFromCSV tempNode;
		boolean condition = false;
		for (NodeFromCSV currentNode : nodeMap) {
			Iterator<IElement> iterator = elements.iterator();
			while (iterator.hasNext() && !condition) {
				IElement currentElement = (IElement) iterator.next();
				if (currentElement instanceof FSTNonTerminalNodeElement
						|| currentElement instanceof FSTTerminalNodeElement) {
					FSTNodeElement tempElement = ((FSTNodeElement) currentElement);
					// TODO see if i can replace the while by :if(temp != null){
					while (tempElement != null && !condition) {
						String id = getTrueId(currentNode, definitionMethod);
						tempNode = getNodeByID(id, nodeMap);
						if (isNodeEqualsToElement(tempNode, tempElement)) {
							list.put(currentNode.getId(), tempElement);
							condition = true;
						}
						// TODO remove that with the while
						tempElement = tempElement.getParent();
					}

				}

			}

			condition = false;
		}
		return list;

	}

	/**
	 * Search for a node by id
	 * 
	 * @param id
	 * @param nodeMap
	 * @return
	 */
	public NodeFromCSV getNodeByID(String id, List<NodeFromCSV> nodeMap) {
		for (NodeFromCSV currentNode : nodeMap) {
			if (currentNode.getId().equals(id)) {
				return currentNode;
			}
		}
		return null;

	}

	/**
	 * This method returns the method id if the node is a definition
	 * 
	 * @param node
	 * @param definitionMethod
	 * @return
	 */
	public static String getTrueId(NodeFromCSV node, Map<String, String> definitionMethod) {
		String id = null;

		if (node.getKind().equals("Definition"))
			id = definitionMethod.get(node.getId());
		else
			id = node.getId();

		return id;
	}

	/**
	 * Test if a NodeFromCSV is equals to an iElement by comparing their
	 * qualified names
	 * 
	 * @param node
	 * @param element
	 * @return
	 */
	public static boolean isNodeEqualsToElement(NodeFromCSV node, IElement element) {
		if (node != null && element != null) {
			String nodeName = node.getQualifiedName();
			if (node.getQualifiedName().contains(".")) {
				nodeName = nodeName.replace(".Definition", "");
			}

			JavaLanguage java = new JavaLanguage();
			String iElementName = java.getQualifiedName(((FSTNodeElement) element).getNode());
			iElementName = iElementName.replaceAll("[(].*[)]", "");

			return iElementName.equals(nodeName);
		} else {
			return false;
		}

	}

	/**
	 * Create a definition id => method id maps, which give the corresponding
	 * method id for a definition
	 * 
	 * @param listNode
	 * @param listEdge
	 * @return
	 */
	public Map<String, String> createDefinitionMethodMap(List<NodeFromCSV> listNode, List<EdgeFromCSV> listEdge) {
		Map<String, String> mapDefMeth = new HashMap<String, String>();

		for (NodeFromCSV currentNode : listNode) {
			if (currentNode.getKind() != null && currentNode.getKind().contains("Definition")) {
				for (EdgeFromCSV currentEdge : listEdge) {

					if (currentEdge.getTarget().contains(currentNode.getId())) {
						mapDefMeth.put(currentNode.getId(), currentEdge.getId());
					}
				}
			}
		}

		return mapDefMeth;
	}

	/**
	 * Create a nodeMap using the nodes matrix
	 * 
	 * @param matrixNodes
	 * @param edgeMap
	 * @return
	 */
	public List<NodeFromCSV> createNodeMap(String[][] matrixNodes) {
		ArrayList<NodeFromCSV> nodeMap = new ArrayList<NodeFromCSV>();
		for (int i = 0; i < matrixNodes.length; i++) {
			if (matrixNodes[i][0] != null && matrixNodes[i][1] != null && matrixNodes[i][2] != null) {
				nodeMap.add(new NodeFromCSV(matrixNodes[i][0], matrixNodes[i][1], matrixNodes[i][2], matrixNodes[i][3],
						matrixNodes[i][4]));
			}
		}
		return nodeMap;

	}

	/**
	 * Create a map for the parameters edges. The key are the methods and values
	 * are the target the parameters.
	 * 
	 * @param listNode
	 * @return
	 */
	public Map<String, ArrayList<String>> createParams(List<NodeFromCSV> listNode) {
		Map<String, ArrayList<String>> mapMethodParams = new HashMap<String, ArrayList<String>>();
		for (NodeFromCSV paramNode : listNode) {
			if (paramNode != null && paramNode.getKind() != null && paramNode.getKind().equals("Param")) {

				String idTarget = getParamClass(listNode, paramNode);
				String idKey = getMethodUsingParam(listNode, paramNode);

				if (idKey != null && idTarget != null) {
					if (mapMethodParams.containsKey(idKey)) {
						mapMethodParams.get(idKey).add(idTarget);
					} else {
						ArrayList<String> targetList = new ArrayList<String>();
						targetList.add(idTarget);
						mapMethodParams.put(idKey, targetList);
					}
				}
			}
		}
		return mapMethodParams;
	}

	/**
	 * Search for the id of the parameter class
	 * 
	 * @param listNode
	 * @param paramNode
	 * @return
	 */
	public String getParamClass(List<NodeFromCSV> listNode, NodeFromCSV paramNode) {
		for (NodeFromCSV currentNode : listNode) {
			if (currentNode != null && currentNode.getName() != null
					&& currentNode.getName().equals(paramNode.getType())) {
				return currentNode.getId();
			}
		}
		return null;
	}

	/**
	 * Search for the id of the method using the parameter.
	 * 
	 * @param listNode
	 * @param paramNode
	 * @return
	 */
	public String getMethodUsingParam(List<NodeFromCSV> listNode, NodeFromCSV paramNode) {
		int index = paramNode.getQualifiedName().lastIndexOf(".");
		String methodName = paramNode.getQualifiedName().substring(0, index);
		for (NodeFromCSV currentNode : listNode) {
			if (currentNode != null && currentNode.getKind() != null && currentNode.getKind().contains("Method")
					&& currentNode.getQualifiedName().equals(methodName)) {
				return currentNode.getId();
			}
		}
		return null;
	}

	/**
	 * The method adds the new edges to the edgeFromCSV list
	 * 
	 * @param list
	 * @param param
	 * @return
	 */
	public List<EdgeFromCSV> addEdgeParamToList(List<EdgeFromCSV> list, Map<String, ArrayList<String>> param) {
		for (String id : param.keySet()) {
			if (id != null) {
				EdgeFromCSV edge = new EdgeFromCSV(id, "Param");
				for (String target : param.get(id)) {
					edge.addTarget(target);
				}
				list.add(edge);
			}
		}
		return list;
	}

	/**
	 * Create the edgeMap using the edges matrix
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