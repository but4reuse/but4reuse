package org.but4reuse.adapter.sourcecode.extension;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.sourcecode.FSTNodeElement;
import org.but4reuse.adapters.sourcecode.FSTNonTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.FSTTerminalNodeElement;
import org.but4reuse.adapters.sourcecode.adapter.JavaLanguage;
import org.junit.Test;

import de.ovgu.cide.fstgen.ast.FSTNode;
import de.ovgu.cide.fstgen.ast.FSTVisitor;

public class JavaSourceCodeAdapterExtensionTest {

	JavaSourceCodeAdapterExtension java;

	protected void setUp() throws Exception {
		java = new JavaSourceCodeAdapterExtension();
	}

	protected void tearDown() throws Exception {

	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateEdgeMap() {
		String[][] matrixEdge = { { "1", "2", "Uses" }, { "2", "4", "Contains" } };
		java = new JavaSourceCodeAdapterExtension();
		List<EdgeFromCSV> mapResult = java.createEdgeMap(matrixEdge);
		EdgeFromCSV edge1 = new EdgeFromCSV("1", "Uses");
		edge1.addTarget("2");
		EdgeFromCSV edge2 = new EdgeFromCSV("2", "Contains");
		edge2.addTarget("4");
		assertTrue(mapResult.contains(edge1));
		assertTrue(mapResult.contains(edge2));
	}

	@Test
	public void testCreateNodeMap() {
		List<NodeFromCSV> map = new ArrayList<NodeFromCSV>();
		java = new JavaSourceCodeAdapterExtension();
		String[][] matrixNode = { { "1", "Class", "Myclass", "package.MyClass", "nothing" },
				{ "2", "Class", "Myclass2", "package.MyClass2", "nothing" } };
		List<NodeFromCSV> mapResult = java.createNodeMap(matrixNode);
		NodeFromCSV node1 = new NodeFromCSV("1", "Class", "Myclass", "package.MyClass", "nothing");
		NodeFromCSV node2 = new NodeFromCSV("2", "Class", "Myclass2", "package.MyClass2", "nothing");
		map.add(node1);
		map.add(node2);
		assertTrue(mapResult.contains(node1));
		assertTrue(mapResult.contains(node2));
	}

	@Test
	public void testCreateDefinitionMethode() {
		List<NodeFromCSV> listNode = new ArrayList<NodeFromCSV>();
		java = new JavaSourceCodeAdapterExtension();
		NodeFromCSV node1 = new NodeFromCSV("1", "Definition", "Myclass", "package.MyClass", "nothing");
		NodeFromCSV node2 = new NodeFromCSV("2", "Class", "Myclass2", "package.MyClass2", "nothing");
		listNode.add(node1);
		listNode.add(node2);

		List<EdgeFromCSV> listEdge = new ArrayList<EdgeFromCSV>();
		EdgeFromCSV edge1 = new EdgeFromCSV("1", "Uses");
		edge1.addTarget("2");
		EdgeFromCSV edge2 = new EdgeFromCSV("2", "Contains");
		edge2.addTarget("1");
		listEdge.add(edge1);
		listEdge.add(edge2);

		Map<String, String> defTest = new HashMap<String, String>();
		defTest.put("1", "2");

		Map<String, String> DefMeth = java.createDefinitionMethode(listNode, listEdge);

		assertEquals(defTest, DefMeth);

	}

	@Test
	public void testIsNodeEqualsToElement() {
		
		// 14 ; Class ; MyClass ; mypackage.MyClass ;
		NodeFromCSV node = new NodeFromCSV("14", "class", "MyClass", "MyClass", "");
		NodeFromCSV node2 = new NodeFromCSV("15", "classe", "MyClass", "mypackage.MyClasse", "");
		NodeFromCSV node3 = new NodeFromCSV("12", "cladss", "MyClfass", "mypackage.MyClass", "");
		NodeFromCSV node4 = new NodeFromCSV("13", "class", "MyClass", "mypackage.MyClass", "");

		IElement element = new FSTNonTerminalNodeElement();
		((FSTNonTerminalNodeElement) element).setName("MyClass");
		FSTNode nodetest = new FSTNode("type","MyClass") {
			
			@Override
			public String printFST(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public FSTNode getShallowClone() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public FSTNode getDeepClone() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void accept(FSTVisitor arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		((FSTNonTerminalNodeElement) element).setNode(nodetest);

		assertFalse(JavaSourceCodeAdapterExtension.isNodeEqualsToElement(node2, element));
		assertFalse(JavaSourceCodeAdapterExtension.isNodeEqualsToElement(node3, element));
		assertFalse(JavaSourceCodeAdapterExtension.isNodeEqualsToElement(node4, element));
		assertTrue(JavaSourceCodeAdapterExtension.isNodeEqualsToElement(node, element));
	}

	@Test
	public void testGetResearch() {

		NodeFromCSV node = new NodeFromCSV("14", "class", "MyClass", "MyClass", "");;
		NodeFromCSV node2 = new NodeFromCSV("14", "Definition", "MyClass", "MyClass", "");;
		Map<String, String> defMeth = new HashMap<String, String>();
		defMeth.put("14","18");
		assertNotEquals("18", JavaSourceCodeAdapterExtension.getResearch(node, defMeth));
		assertEquals("18", JavaSourceCodeAdapterExtension.getResearch(node2, defMeth));
	}

	@Test
	public void testGetNodeByID() {

	}

	@Test
	public void testGetFSTNodeElement() {

	}

}
