package org.but4reuse.adapters.cppcdt.tests;

/**
 * This is the class responsible of launching all the test suites.
 * 
 * ********************* IMPORTANT **********************
 * Please note that in order to test the function call dependencies,
 * Doxygen needs to be installed and the path to the Doxygen binary (executable)
 * needs to be manually set in the class : org.but4reuse.adapters.cpp.tests.cppParser.CppHeaderAndSourceTest
 * line 34 -> private final String doxygenPath = "/usr/bin/doxygen"; 
 * 
 * By default it contains the path to the linux binary.
 * 
 * This modification is needed since a JUnit class can't receive any parameters in the constructor.
 * 
 * If the binary is not found the following exception will be raised 
 * java.io.IOException: Cannot run program "/usr/bin/doxygen": CreateProcess error=2
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ org.but4reuse.adapters.cppcdt.tests.cppParser.AllTests.class,
		org.but4reuse.adapters.cppcdt.tests.dotParser.AllTests.class,
		org.but4reuse.adapters.cppcdt.tests.xmlParser.AllTests.class })
public class AllTests {

}
