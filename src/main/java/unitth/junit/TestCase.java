/*
 *    This file is part of UnitTH
 *
 *   UnitTH is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   UnitTH is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with UnitTH if not, see <http://www.gnu.org/licenses/>.
 *
 * =======================================================================
 * $Id: TestCase.java,v 1.3 2010/05/14 15:21:39 andnyb Exp $
 * -----------------------------------------------------------------------
 * 
 * =======================================================================
 */

package unitth.junit;

/**
 * This class represents a single unique test case in any module in any run. Its
 * unique identifier is the module name and test case name. Apart from the name
 * the class only contain attributes for the test status
 * <code>TestCaseVerdict</code> and the time it took to execute the test case.
 * 
 * @author andnyb
 */
public class TestCase extends TestItem {

	private TestCaseVerdict verdict;
	private String className;
	private String moduleName;

	/**
	 * Ctor, sets the initial verdict to pass
	 */
	public TestCase() {
		verdict = TestCaseVerdict.e_PASS;
	}

	/**
	 * Returns the name of the test class.
	 * 
	 * @return The name of the test class.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Returns the module name of the test class.
	 * 
	 * @return The module name of the test class.
	 */
	public String getModuleName() {
		return moduleName;
	}
	
	/**
	 * Returns the full name of the test case in the following format
	 * <module name>::<class name>::<test case name>
	 * 
	 * @return The full name of the test case.
	 */
	public String getFullName() {
		return moduleName + "::" + className + "::" + name;
	}
	
	/**
	 * Returns the verdict of the test case.
	 * 
	 * @return The verdict for this test case.
	 */
	public TestCaseVerdict getVerdict() {
		return verdict;
	}

	/**
	 * Sets the name of the test class for this test case.
	 * 
	 * @param s
	 *            The test class name.
	 */
	public void setClassName(String s) {
		className = s;
	}

	/**
	 * Sets the name of the test module for this test case.
	 * 
	 * @param s
	 *            The test class name.
	 */
	public void setModuleName(String s) {
		moduleName = s;
	}

	/**
	 * Sets the verdict of this test case.
	 * 
	 * @param v
	 *            The verdict to assign to this test case.
	 */
	public void setVerdict(TestCaseVerdict v) {
		verdict = v;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + " " + verdict + " " + executionTime;
	}
}

/* eof */
