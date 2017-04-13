package org.but4reuse.utils.strings;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * String Utils
 * 
 * @author jabier.martinez
 */
public class StringUtilsTests {

		@Test
		public void testCamelCase() {
			List<String> split = StringUtils.splitCamelCase("CamelCase");
			Assert.assertEquals(2, split.size());
			Assert.assertEquals("Camel", split.get(0));
			Assert.assertEquals("Case", split.get(1));
		}
		
		@Test
		public void testTokenize() {
			List<String> tokenize = StringUtils.tokenizeString("abbaaccc", "a");
			Assert.assertEquals(2, tokenize.size());
			Assert.assertEquals("bb", tokenize.get(0));
			Assert.assertEquals("ccc", tokenize.get(1));
		}

}
