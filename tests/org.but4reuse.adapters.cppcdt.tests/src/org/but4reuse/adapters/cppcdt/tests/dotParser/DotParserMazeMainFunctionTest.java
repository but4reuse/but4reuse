package org.but4reuse.adapters.cppcdt.tests.dotParser;

import java.util.Arrays;

/**
 * Test file for the Maze class.
 *
 * @author sandu.postaru
 *
 */

public class DotParserMazeMainFunctionTest extends DotParserTestHelper {

	private final String MAZE_FILE_NAME = "main_8cpp_a0ddf1224851353fc92bfbff6f499fa97_cgraph.dot";		
	
	
	public DotParserMazeMainFunctionTest() {
		initialise(MAZE_FILE_NAME);	
				
		expectedFunctionNodes.put("Node0", "main");
		expectedFunctionNodes.put("Node1", "Maze::InitialiseGraph");
		expectedFunctionNodes.put("Node2", "Maze::GenerateMaze");
		expectedFunctionNodes.put("Node3", "MinimumSpanningtreeAlgorithm::MinimumSpanningTree");
		expectedFunctionNodes.put("Node4", "Maze::RemoveBorders");
		expectedFunctionNodes.put("Node5", "Maze::PrintMazeSVG");
		expectedFunctionNodes.put("Node6", "Maze::GetCoordinateBounds");
		expectedFunctionNodes.put("Node7", "Maze::PrintMazePNG");
		
				
		expectedFunctionCalls.put("Node0", Arrays.asList(new String[]{"Node1", "Node2", "Node5", "Node7"}));
		expectedFunctionCalls.put("Node2", Arrays.asList(new String[]{"Node3", "Node4"}));
		expectedFunctionCalls.put("Node5", Arrays.asList(new String[]{"Node6"}));
		expectedFunctionCalls.put("Node7", Arrays.asList(new String[]{"Node6"}));
		
	}		
}
