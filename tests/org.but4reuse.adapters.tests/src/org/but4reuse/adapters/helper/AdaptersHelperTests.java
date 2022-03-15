package org.but4reuse.adapters.helper;

import java.util.List;

import org.but4reuse.adapters.IElement;
import org.but4reuse.adapters.impl.AbstractElement;
import org.but4reuse.tests.utils.TestElement;
import org.but4reuse.tests.utils.TestElementsCreator;
import org.junit.Assert;
import org.junit.Test;

public class AdaptersHelperTests {

	@Test
	public void testParentChildren() {
		List<IElement> testElements = TestElementsCreator.createElements(3);
		Assert.assertTrue(AdaptersHelper.getParentElements(testElements.get(0)).isEmpty());
		Assert.assertTrue(AdaptersHelper.getChildrenElements(testElements.get(0)).isEmpty());
		
		// Defined parent dependency
		((AbstractElement) testElements.get(1)).addDependency(TestElement.PARENT_DEPENDENCYID, testElements.get(0));
		((AbstractElement) testElements.get(2)).addDependency(TestElement.PARENT_DEPENDENCYID, testElements.get(0));
		
		// Other dependency
		((AbstractElement) testElements.get(1)).addDependency(testElements.get(2));
		
		Assert.assertEquals(2, AdaptersHelper.getChildrenElements(testElements.get(0)).size());
		Assert.assertTrue(AdaptersHelper.getChildrenElements(testElements.get(1)).isEmpty());
		Assert.assertTrue(AdaptersHelper.getChildrenElements(testElements.get(2)).isEmpty());
		
		Assert.assertTrue(AdaptersHelper.getParentElements(testElements.get(0)).isEmpty());
		Assert.assertEquals(1, AdaptersHelper.getParentElements(testElements.get(1)).size());
		Assert.assertEquals(1, AdaptersHelper.getParentElements(testElements.get(2)).size());
	}
}
