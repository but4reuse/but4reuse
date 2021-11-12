package org.but4reuse.adapters.cppcdt.tests.dotParser;

import java.util.Arrays;

/**
 * Test file for the maze init function.
 *
 * @author sandu.postaru
 *
 */

public class DotParserMazeInitGraphFunctionTest extends DotParserTestHelper {

	private final String MAZE_FILE_NAME = "classRectangularMaze_a3483081dbd98259a799b8e577c41d81b_cgraph.dot";		
	
	
	public DotParserMazeInitGraphFunctionTest() {
		initialise(MAZE_FILE_NAME);	
				
		expectedFunctionNodes.put("Node2", "RectangularMaze::InitialiseGraph");
		expectedFunctionNodes.put("Node3", "Maze::InitialiseGraph");
		expectedFunctionNodes.put("Node4", "RectangularMaze::VertexIndex");
		
				
		expectedFunctionCalls.put("Node2", Arrays.asList(new String[]{"Node3", "Node4"}));				
	}		
}
