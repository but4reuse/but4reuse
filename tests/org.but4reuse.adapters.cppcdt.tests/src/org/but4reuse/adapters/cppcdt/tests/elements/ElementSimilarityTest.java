package org.but4reuse.adapters.cppcdt.tests.elements;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.but4reuse.adaptedmodel.AdaptedArtefact;
import org.but4reuse.adaptedmodel.AdaptedModel;
import org.but4reuse.adaptedmodel.AdaptedModelFactory;
import org.but4reuse.adaptedmodel.Block;
import org.but4reuse.adaptedmodel.helpers.AdaptedModelHelper;
import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.cppcdt.activator.Activator;
import org.but4reuse.adapters.cppcdt.elements.CppElement;
import org.but4reuse.adapters.cppcdt.elements.FunctionHeader;
import org.but4reuse.adapters.cppcdt.elements.HeaderFile;
import org.but4reuse.adapters.cppcdt.elements.StatementImpl;
import org.but4reuse.adapters.cppcdt.parser.CppParser;
import org.but4reuse.adapters.cppcdt.preferences.CppAdapterPreferencePage;
import org.but4reuse.adapters.cppcdt.tests.cppParser.CppHeaderAndSourceTest;
import org.but4reuse.block.identification.IBlockIdentification;
import org.but4reuse.block.identification.impl.IntersectionsBlockIdentification;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test file for the similarity function.
 *
 * @author sandu.postaru
 *
 */


public class ElementSimilarityTest {

	private List<CppElement> elements;
	private List<IElement> iElements;
	private File root;
	
	private final double PRECISION = 0.;		 

	public ElementSimilarityTest() {
		
		elements = new LinkedList<CppElement>();
		root = Paths.get("data", "cppParser", "completeExample").toFile();				
		
		// enable the analysis of function calls
		// since the tests are run in a new eclipse environment we need to	
		Activator.getDefault().getPreferenceStore().setDefault(CppAdapterPreferencePage.USE_FUNCTION_CALL_HIERARCHY,
				true);
		Activator.getDefault().getPreferenceStore().setDefault(CppAdapterPreferencePage.DOXYGEN_PATH,
				CppHeaderAndSourceTest.doxygenPath);
		

		try {
			iElements = CppParser.parse(root);
		} catch (CoreException e) {

			e.printStackTrace();
		}

		for (IElement iElement : iElements) {
			Assert.assertTrue(iElement instanceof CppElement);
			elements.add((CppElement) iElement);
		}
	}
	
	
	// Check the identity property
	@Test
	public void sameRealElementTest(){
		
		for(CppElement element : elements){
			
			double similarity = element.similarity(element);
			Assert.assertEquals(1.0, similarity, PRECISION);
		}
	}	
	
	// Check the similarity for elements having a 2 layer parent hierarchy
	@Test
	public void madeUpElementTest(){
		
		CppElement headerFile = new HeaderFile(null, null, "headerFile.h", "headerFile.h");
		
		// ----------- one parent hierarchy
		CppElement function1 = new FunctionHeader(null, headerFile, "function1", "function1");
		// same content and parent
		CppElement function1BisTrue = new FunctionHeader(null, headerFile, "function1", "function1");
		// same content different parent
		CppElement function1BisFalse= new FunctionHeader(null, null, "function1", "function1");
		
		// ----------- two parents hierarchy
		CppElement statement1 = new StatementImpl(null, function1, "statement1", "statement1");
		// same content and parent
		CppElement statement1BisTrue = new StatementImpl(null, function1BisTrue, "statement1", "statement1");
		// same content different parent
		CppElement statement1BisFalse = new StatementImpl(null, function1BisFalse, "statement1", "statement1");
		
	
		// headerFile
		double sim = headerFile.similarity(headerFile);
		Assert.assertEquals(1., sim, PRECISION);
		
		sim = headerFile.similarity(function1);
		Assert.assertEquals(0., sim, PRECISION);
		
		sim = headerFile.similarity(function1BisTrue);
		Assert.assertEquals(0., sim, PRECISION);
		
		sim = headerFile.similarity(function1BisFalse);
		Assert.assertEquals(0., sim, PRECISION);
		
		
		
		// function1		
		sim = function1.similarity(function1);
		Assert.assertEquals(1., sim, PRECISION);
				
		sim = function1.similarity(function1BisTrue);
		Assert.assertEquals(1., sim, PRECISION);
		
		sim = function1.similarity(function1BisFalse);
		Assert.assertEquals(0., sim, PRECISION);
		
		sim = function1.similarity(statement1);
		Assert.assertEquals(0., sim, PRECISION);
		
		
		
		// statement1		
		sim = statement1.similarity(statement1);
		Assert.assertEquals(1., sim, PRECISION);
		
		sim = statement1.similarity(statement1BisTrue);
		Assert.assertEquals(1., sim, PRECISION);
		
		sim = statement1.similarity(statement1BisFalse);
		Assert.assertEquals(0., sim, PRECISION);
		
		sim = statement1.similarity(headerFile);
		Assert.assertEquals(0., sim, PRECISION);		
	}
	
	// Check the intersection results (internal use of similarity)
	@Test
	public void intersectionSimilarityTest(){
		
		IBlockIdentification intersectionAlgo = new IntersectionsBlockIdentification();
		
		List<IElement> artefact1Elements = iElements;
		AdaptedArtefact artefact1 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(artefact1Elements);
		
		// remove the first element
		List<IElement> artefact2Elements = new ArrayList<IElement>(iElements);
		artefact2Elements.remove(0);
		AdaptedArtefact artefact2 = AdaptedModelHelper.wrapElementsToCreateAdaptedArtefact(artefact2Elements);
		
		// adapted artefact
		List<AdaptedArtefact> adaptedArtefacts = new ArrayList<AdaptedArtefact>();
		adaptedArtefacts.add(artefact1);
		adaptedArtefacts.add(artefact2);
		
		// intersection process
		List<Block> blocks = intersectionAlgo.identifyBlocks(adaptedArtefacts, new NullProgressMonitor());
		

		
		// 2 blocks, one containing the common elements, one containing the others
		Assert.assertEquals(2, blocks.size());
		
		AdaptedModel adaptedModel = AdaptedModelFactory.eINSTANCE.createAdaptedModel();
		adaptedModel.getOwnedAdaptedArtefacts().addAll(adaptedArtefacts);
		adaptedModel.getOwnedBlocks().addAll(blocks);
		
		
		// 1 common block
		int commonBlocks = AdaptedModelHelper.getCommonBlocks(adaptedModel).size();		
		Assert.assertEquals(1, commonBlocks);
		
		// 1 element less for the common elements (compared to the original elements)
		List<IElement> commonElements = AdaptedModelHelper.getElementsOfBlock(blocks.get(0));
		Assert.assertEquals(iElements.size() - 1, commonElements.size());
		
		// 1 element contained in the intersection elements (the one that we removed)
		List<IElement> intersectionElements = AdaptedModelHelper.getElementsOfBlock(blocks.get(1));
		Assert.assertEquals(1, intersectionElements.size());
		
		// check that it's indeed the element that we removed
		
		// similarity check
		Assert.assertEquals(1., iElements.get(0).similarity(intersectionElements.get(0)), PRECISION);

		// reference check
		Assert.assertEquals(iElements.get(0), intersectionElements.get(0));
		
	}
}
