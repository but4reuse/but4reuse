package org.but4reuse.adapters.json;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.json.tools.Paths;
import org.junit.Assert;
import org.junit.Test;

import com.eclipsesource.json.JsonObject;

/**
 * JSON adapter test
 * 
 * @author jabier.martinez
 */
public class JsonAdapterTest {

	@Test
	public void basicTest() {

		File json = new File("data/simpleExample.json");
		URI uri = json.toURI();

		JsonAdapter jsonAdapter = new JsonAdapter();
		List<IElement> elements = jsonAdapter.adapt(uri, null, new Paths(), new Paths());

		// Check that we found the 3 elements
		// the root ObjectElement, the KeyElement "hello", and the ValueElement "world"
		Assert.assertTrue(elements.size() == 3);

		// Check that we can reconstruct the json
		JsonObject jsonObject = jsonAdapter.construct(elements, null);
		Assert.assertFalse("Constructed JSON is empty", jsonObject.isEmpty());

	}

}
