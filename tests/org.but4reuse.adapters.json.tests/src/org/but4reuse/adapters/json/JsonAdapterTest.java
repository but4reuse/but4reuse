package org.but4reuse.adapters.json;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.Paths;
import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * JSON adapter test
 * 
 * @author jabier.martinez
 */
public class JsonAdapterTest {

	@Test
	public void basicTest() {

		// {"hello": "world"}
		File json = new File("data/simpleExample.json");
		URI uri = json.toURI();
		JsonAdapter jsonAdapter = new JsonAdapter();
		List<IElement> elements = jsonAdapter.adapt(uri, null, false, new Paths(), new Paths(), new Paths(), new Paths());

		// Check that we found the 3 elements
		// the root ObjectElement, the KeyElement "hello", and the ValueElement "world"
		Assert.assertTrue(elements.size() == 3);

		// Check that we can reconstruct the json
		JsonObject jsonObject = jsonAdapter.construct(elements, null);
		Assert.assertFalse("Constructed JSON is empty", jsonObject.isEmpty());
	}

	@Test
	public void basicIncludeTest() {
		JsonAdapter jsonAdapter = new JsonAdapter();
		JsonValue root = Json.parse("{\"key1\":\"string1\",\"key2\":\"string2\",\"key3\":\"string3\"}");
		Paths includePaths = new Paths("key2");
		List<IElement> elements = jsonAdapter.adapt(root, null, false, includePaths, new Paths(), new Paths(), new Paths());
		// Check that we found the 3 elements, root + the key value pair of key2
		Assert.assertTrue(elements.size() == 3);
	}

	@Test
	public void includeTest() {
		JsonAdapter jsonAdapter = new JsonAdapter();
		JsonValue root = Json.parse(
				"{\"key1\":\"string1\",\"key2\": { \"subkey1\" : \"substring1\", \"subkey2\" : \"substring2\", \"subkey3\" : \"substring3\" },\"key3\":\"string3\"}");
		Paths includePaths = new Paths();
		includePaths.absolutePaths.add("key2");
		List<IElement> elements = jsonAdapter.adapt(root, null, false, includePaths, new Paths(), new Paths(), new Paths());
		// Check that we found the 9 elements
		Assert.assertTrue(elements.size() == 9);
	}

	@Test
	public void basicIgnoreTest() {
		JsonAdapter jsonAdapter = new JsonAdapter();

		JsonValue root = Json.parse("{\"hello\": [\"A\",{\"B\":\"C\"}]}");
		Paths ignorePaths = new Paths("hello");
		List<IElement> elements = jsonAdapter.adapt(root, null, false, new Paths(), new Paths(), ignorePaths, new Paths());

		// Check that we have 3 elements, the root, the key hello, and then the rest
		// ["beautiful",{"world":"w"}] is an ignored content element that is not
		// expanded
		Assert.assertTrue(elements.size() == 3);
	}

	@Test
	public void basicOmitTest() {

		JsonAdapter jsonAdapter = new JsonAdapter();

		JsonValue root = Json.parse("{\"key1\":\"string1\",\"key2\":\"string2\",\"key3\":\"string3\"}");
		Paths omitPaths = new Paths("key2");
		List<IElement> elements = jsonAdapter.adapt(root, null, false, new Paths(), omitPaths, new Paths(), new Paths());

		// Check that we found the 5 elements, root + two key value pairs
		Assert.assertTrue(elements.size() == 5);
	}
	
	@Test
	public void similarityTest() {

		JsonAdapter jsonAdapter = new JsonAdapter();

		JsonValue root1 = Json.parse("[\"A\",\"B\",\"C\"]");
		List<IElement> elements1 = jsonAdapter.adapt(root1, null, false, new Paths(), new Paths(), new Paths(), new Paths());
		
		JsonValue root2 = Json.parse("[\"A\",\"C\"]");
		List<IElement> elements2 = jsonAdapter.adapt(root2, null, false, new Paths(), new Paths(), new Paths(), new Paths());
		
		for (IElement e1 : elements1) {
			for (IElement e2 : elements2) {
				System.out.println(e1 + " " + e2 + " --> " + e1.similarity(e2));
			}
		}

	}

}
