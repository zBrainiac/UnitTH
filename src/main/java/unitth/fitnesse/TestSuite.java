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
 * $Id$
 * =======================================================================
 */
package unitth.fitnesse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import unitth.core.TestItemUtils;

public class TestSuite extends TestItem {

	private HashMap<String, TestCase> testCases = null;

	private int noPassed = 0;
	private int noTestCases = 0;
	private double passPct = 0.0;
	private int runIdx = 0;	
	private int noSubTestSuites = 0;
	
	public TestSuite() {
		testCases = new HashMap<String, TestCase>();
	}
	
	public void setTestCases(HashMap<String, TestCase> testCases) {
		this.testCases = testCases;
	}

	public void setPassPct(double passPct) {
		this.passPct = passPct;
	}

	public double getPassPctDouble() {
		return passPct*100.0;
	}

	public String getPassPct() {
		return TestItemUtils.passPctToString(getPassPctDouble());
	}

	public int getNoPassed() {
		return noPassed;
	}

	public String toString() {
		String ret = "+- TestSuite -----------------------------+\n"
				+ "  Name: "
				+ name
				+ "\n"
				+ "  Run date: "
				+ runDate
				+ "\n"
				+ "  Pass %: "
				+ getPassPctDouble()
				+ "\n"
				+ "  Number of test cases: "
				+ testCases.size()
				+ "\n"
				+ "  Passed: "
				+ noPassed
				+ "\n"
				+ "  Failed: "
				+ (noTestCases - noPassed)
				+ "\n"
				+ "  Assertions: "
				+ noAssertions
				+ "\n"
				+ "  Right: "
				+ noRights
				+ "\n"
				+ "  Wrong: "
				+ noWrongs
				+ "\n"
				+ "  Ignored: "
				+ noIgnores
				+ "\n" + "  Exceptions: " + noExceptions + "\n";

		if (testCases.size() > 0) {
			ret += "\n";

			Collection<TestCase> tcc = testCases.values();
			Iterator<TestCase> tci = tcc.iterator();

			while (tci.hasNext()) {
				TestCase tc = tci.next();
				ret += tc.toString() + "\n";
			}
		}
		return ret;
	}

	/**
	 * 1) First run through all sub test suites 2) Run through all test cases in
	 * this test suite
	 */
	public void calcStats() {
		Collection<TestCase> tcc = testCases.values();
		Iterator<TestCase> tci = tcc.iterator();

		while (tci.hasNext()) {
			TestCase tc = tci.next();
			tc.calcStats();

			if (true == tc.isPassed()) {
				noPassed++;
			}
			noTestCases++;
			noAssertions += tc.getNoAssertions();
			noRights += tc.getRights();
			noWrongs += tc.getWrongs();
			noIgnores += tc.getIgnores();
			noExceptions += tc.getExceptions();

			if (runDate.equals("NaN")) {
				if (tc.getRunDate().equals("NaN")) {
					runDate = "NaN";
				} else {
					runDate = tc.getRunDate();
				}
			} else if (runDate.compareTo(tc.getRunDate()) > 0) {
				if (tc.getRunDate().equals("NaN")) {
					runDate = "NaN";
				} else {
					runDate = tc.getRunDate();
				}
			}
		}
		passPct = ((double) noPassed / (double) noTestCases);
	}
	
	public int getNoSubTestSuites() {
		return this.noSubTestSuites;
	}
	
	/**
	 * Returns the number of unique test cases.
	 * 
	 * @return no of unique test cases.
	 */
	public int getNoTestCases() {
		return this.noTestCases;
	}

	public int getNoFailed() {
		return this.noTestCases - this.noPassed;
	}

	public int getRights() {
		return noRights;
	}

	public int getWrongs() {
		return noWrongs;
	}

	public int getIgnores() {
		return noIgnores;
	}

	public int getExceptions() {
		return noExceptions;
	}

	public void addTestCase(TestCase tc) {
		testCases.put(tc.getQualifiedName(), tc);
	}

	public TestCase getTestCase(String name) {
		return testCases.get(name);
	}

	public HashMap<String, TestCase> getTestCases() {
		return testCases;
	}
	
	public void addFrom(TestSuite ts) {
		
		noPassed += ts.getNoPassed();
		noAssertions += ts.getNoAssertions();
		noRights += ts.getRights();
		noWrongs += ts.getWrongs();
		noIgnores += ts.getIgnores();
		noExceptions += ts.getExceptions();
		noTestCases += ts.getNoTestCases();
		
		if (runDate.equals("NaN")) {
			if (ts.getRunDate().equals("NaN")) {
				runDate = "NaN";
			} else {
				runDate = ts.getRunDate();
			}
		} else if (runDate.compareTo(ts.getRunDate()) > 0) {
			if (ts.getRunDate().equals("NaN")) {
				runDate = "NaN";
			} else {
				runDate = ts.getRunDate();
			}
		}
		
		// Division by ZERO
		if (0 > noPassed || 0 == noTestCases) {
			noPassed = 0;
			passPct = 0.0;
		} else {
			passPct = ((double) noPassed / (double) noTestCases);
		}
		
		// Each time invoked we add one. And then we add the sum
		// from invocations in earlier calls.
		noSubTestSuites++;
		noSubTestSuites += ts.getNoSubTestSuites();
	}
	
	public void setRunIdx(int idx) {
		this.runIdx = idx;
	}
	
	public int getRunIdx() {
		return this.runIdx;
	}

	public void setNoPassed(int no) {
		this.noPassed = no;
	}
	
	public void setNoTestCases(int no) {
		this.noTestCases = no;
	}
	
	public int getNoNonPassing() {
		return this.noTestCases-this.noPassed;
	}
}
