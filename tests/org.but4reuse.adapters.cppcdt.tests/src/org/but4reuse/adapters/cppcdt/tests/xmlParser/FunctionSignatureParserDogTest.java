package org.but4reuse.adapters.cppcdt.tests.xmlParser;

public class FunctionSignatureParserDogTest extends FunctionSignatureParserTestHelper {

	private final String fileName = "classDog.xml";
	
	public FunctionSignatureParserDogTest(){
		initialise(fileName);
						
		expectedSignatureMap.put("$classDog.html#ac00e101c569d7cf3a7695d9ddcd841db", "Dog::Dog~char const");
		expectedSignatureMap.put("$classDog.html#a560f8b2eded2f5f14f139d7b05dcc8c8", "Dog::sound");
		expectedSignatureMap.put("$classDog.html#a11b7d7dd8ccac6d0b0a8a2d211a893da", "Dog::sleep");		
	}
	
}
