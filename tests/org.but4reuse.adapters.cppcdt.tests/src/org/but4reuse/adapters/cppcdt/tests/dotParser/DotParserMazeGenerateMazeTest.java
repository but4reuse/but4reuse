package org.but4reuse.adapters.cppcdt.tests.dotParser;

import java.util.Arrays;

/**
 * Test file for the Maze class.
 *
 * @author sandu.postaru
 *
 */

public class DotParserMazeGenerateMazeTest extends DotParserTestHelper {

	private final String MAZE_FILE_NAME = "classMaze_a56bca363af71816d157c19e835a697fa_cgraph.dot";		
	
	
	public DotParserMazeGenerateMazeTest() {
		initialise(MAZE_FILE_NAME);	
				
		expectedFunctionNodes.put("Node1", "Maze::GenerateMaze");
		expectedFunctionNodes.put("Node2", "MinimumSpanningtreeAlgorithm::MinimumSpanningTree");
		expectedFunctionNodes.put("Node3", "Maze::RemoveBorders");
				
		expectedFunctionCalls.put("Node1", Arrays.asList(new String[]{"Node2", "Node3"}));
	}		
}
