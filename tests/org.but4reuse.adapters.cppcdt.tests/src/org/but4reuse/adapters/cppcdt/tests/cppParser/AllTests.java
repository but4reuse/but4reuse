package org.but4reuse.adapters.cppcdt.tests.cppParser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CppHeaderAndSourceTest.class, CppHeaderVisitorTest.class, CppSourceVisitorTest.class })
public class AllTests {

}
