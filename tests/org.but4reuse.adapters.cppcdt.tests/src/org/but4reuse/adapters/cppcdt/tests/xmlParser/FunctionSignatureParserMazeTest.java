package org.but4reuse.adapters.cppcdt.tests.xmlParser;


public class FunctionSignatureParserMazeTest extends FunctionSignatureParserTestHelper {

	private final String fileName = "classMaze.xml";
	
	public FunctionSignatureParserMazeTest(){
		initialise(fileName);
		
		expectedSignatureMap.put("$classMaze.html#ab77bd7986f146553c2addeba63da42f3", "Maze::Maze~int~int~int");
		expectedSignatureMap.put("$classMaze.html#a56bca363af71816d157c19e835a697fa", "Maze::GenerateMaze~MinimumSpanningtreeAlgorithm");
		expectedSignatureMap.put("$classMaze.html#a085fbc8016ae849cf4eb0833f56f1685", "Maze::PrintMazeSVG~const std::string");
		expectedSignatureMap.put("$classMaze.html#af7c56bde66e17b4119a84d70740167e9", "Maze::PrintMazePNG~const std::string");
		expectedSignatureMap.put("$classMaze.html#a6b200f5c63701711f8c4f0925bc73ace", "Maze::RemoveBorders~const std::vector<std::pair<int, int>>");
		expectedSignatureMap.put("$classMaze.html#a3921af33279687f56a53e2251fd1eee8", "Maze::InitialiseGraph");
		expectedSignatureMap.put("$classMaze.html#a26033d336295ea4bbf3db60fdf4c93dd", "Maze::GetCoordinateBounds");												
	}
	
}
