package org.but4reuse.adapters.cppcdt.tests.dotParser;

import java.util.Arrays;

/**
 * Test file for the Animal project, main function.
 *
 * @author sandu.postaru
 *
 */

public class DotParserAnimalMainFunctionTest extends DotParserTestHelper {

	private final String MAZE_FILE_NAME = "main_8cpp_ae66f6b31b5ad750f1fe042a706a4e3d4_cgraph.dot";		
	
	
	public DotParserAnimalMainFunctionTest() {
		initialise(MAZE_FILE_NAME);	
				
		expectedFunctionNodes.put("Node0", "main");
		expectedFunctionNodes.put("Node1", "Animal::sound");
		expectedFunctionNodes.put("Node2", "Animal::sleep");
		
				
		expectedFunctionCalls.put("Node0", Arrays.asList(new String[]{"Node1", "Node2"}));				
	}		
}
