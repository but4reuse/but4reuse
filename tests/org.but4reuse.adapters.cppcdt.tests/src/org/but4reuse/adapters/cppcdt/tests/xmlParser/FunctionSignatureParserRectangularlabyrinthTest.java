package org.but4reuse.adapters.cppcdt.tests.xmlParser;


public class FunctionSignatureParserRectangularlabyrinthTest extends FunctionSignatureParserTestHelper {

	private final String fileName = "classRectangularLabyrinth.xml";
	
	public FunctionSignatureParserRectangularlabyrinthTest(){
		initialise(fileName);
		
		expectedSignatureMap.put("$classRectangularLabyrinth.html#a4b3bbef834ed91b01381217e78e20e33", "RectangularLabyrinth::RectangularLabyrinth~int_int");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#a5beb4c55c895e91956d5cf0cdc114525", "RectangularLabyrinth::RemoveBorders~const std::vector<std::pair<int, int>>");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#ac3bc1363810113201e963e808c3dad52", "RectangularLabyrinth::PrintLabyrinthSVG~const std::string");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#a4b3bbef834ed91b01381217e78e20e33", "RectangularLabyrinth::RectangularLabyrinth~int~int");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#ad53b2603aeac750b0aaa5f81ca73903e", "RectangularLabyrinth::GenerateLabyrinth~DepthFirstSearch");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#aeeacfa01e736c6153a59633b29a33285", "RectangularLabyrinth::GetCoordinateBounds");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#aa1f70fa2e1b26d62dec6673fc1da38de", "RectangularLabyrinth::VertexIndex~int~int");
		expectedSignatureMap.put("$classRectangularLabyrinth.html#adbb69e80308acdc5bc8e2f74c98df9b6", "RectangularLabyrinth::InitialiseGraph");
	}
	
}
