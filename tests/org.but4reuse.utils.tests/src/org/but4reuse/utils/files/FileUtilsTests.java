package org.but4reuse.utils.files;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * File Utils
 * 
 * @author jabier.martinez
 */
public class FileUtilsTests {
	@Test
	public void testFileExtensionUtils() {
		File file = new File("b.txt");
		Assert.assertEquals("txt", FileUtils.getExtension(file));
		Assert.assertEquals("txt", FileUtils.getExtension("a.txt"));
		Assert.assertEquals("a", FileUtils.getFileNameWithoutExtension("a.txt"));
	}
}
