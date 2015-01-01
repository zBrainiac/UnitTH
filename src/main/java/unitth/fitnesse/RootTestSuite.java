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
import java.util.TreeMap;
import java.util.TreeSet;

import unitth.core.TestItemUtils;
import unitth.core.UnitTHException;

public class RootTestSuite extends TestItem implements
		Comparable<RootTestSuite> {

	private TreeSet<TestRun> testRuns = null;

	private double bestRun = 0.0;
	private double worstRun = 100.0;
	private int noPassed = 0;
	private int noTestCases = 0;
	private int runIdx = 0;

	private TreeMap<String, TestSuiteSummary> testSuiteSummaries = new TreeMap<String, TestSuiteSummary>();
	private TreeMap<String, TestCaseSummary> testCaseSummaries = new TreeMap<String, TestCaseSummary>();

	public RootTestSuite() {
		testRuns = new TreeSet<TestRun>();
	}

	public int getNoRuns() {
		return testRuns.size();
	}

	public void addTestRun(TestRun tr) {
		testRuns.add(tr);
	}

	public TreeSet<TestRun> getTestRuns() {
		return testRuns;
	}

	public double getPassPctDouble() {
		return ((double) noPassed / (double) noTestCases) * 100.0;
	}

	public String getPassPct() {
		return TestItemUtils.passPctToString(getPassPctDouble());
	}

	public int getNoPassed() {
		return noPassed;
	}

	public String toString() {
		String ret = "+- RootTestSuite -----------------------------+\n"
				+ "  Name: "
				+ name
				+ "\n"
				+ "  Best run: "
				+ bestRun
				+ "\n"
				+ "  Worst run: "
				+ worstRun
				+ "\n"
				+ "  Average: "
				+ getPassPctDouble()
				+ "\n"
				+ "  Test cases: "
				+ noTestCases
				+ "\n"
				+ "  Passed: "
				+ noPassed
				+ "\n"
				+ "  Failed: "
				+ (noTestCases - noPassed)
				+ "\n"
				+ "  Asserts: "
				+ noAssertions
				+ "\n"
				+ "  Rights: "
				+ noRights
				+ "\n"
				+ "  Wrongs: "
				+ noWrongs
				+ "\n"
				+ "  Ignores: "
				+ noIgnores
				+ "\n"
				+ "  Exceptions: "
				+ noExceptions
				+ "\n"
				+ "  No test runs: " + testRuns.size() + "\n";
		if (testRuns.size() > 0) {
			ret += "\n";
			for (TestRun tr : testRuns) {
				ret += tr.toString() + "\n";
			}
		}
		return ret;
	}

	public void calcStats() {
		for (TestRun tr : testRuns) {
			tr.setRootTestSuiteName(name);
			tr.calcStats();

			double passPct = tr.getPassPctDouble();
			if (bestRun < passPct) {
				bestRun = passPct;
			}
			if (worstRun > passPct) {
				worstRun = passPct;
			}

			noPassed += tr.getNoPassed();
			noTestCases += tr.getNoTestCases();
			noAssertions += tr.getSumAssertions();
			noRights += tr.getSumRights();
			noWrongs += tr.getSumWrongs();
			noIgnores += tr.getSumIgnores();
			noExceptions += tr.getSumExceptions();
		}
		
		// All calculated, now the testRun list needs to be
		// sorted.
		TreeSet<TestRun> sortedTestRuns = new TreeSet<TestRun>();
		for (TestRun tr : testRuns) {
			sortedTestRuns.add(tr);
		}
		testRuns.clear();
		testRuns = sortedTestRuns;
	}

	/**
	 * Returns the number of unique test cases.
	 * 
	 * @return no of unique test cases.
	 */
	public int getNoTestCases() {
		return noTestCases;
	}

	public int getNoFailed() {
		return this.noTestCases - this.noPassed;
	}

	/**
	 * Strips and sets the test suite part of the full FitNesse test case name
	 * in the attribute name.
	 * 
	 * @param name
	 *            The full FitNesse test case name.
	 */
	public void setNameFromTestCaseName(String s) {
		this.name = TestItemUtils.getFitNesseTestSuiteFromTestCaseName(s);
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(RootTestSuite rts) {
		return name.compareTo(rts.name);
	}

	public HashMap<String, TestCase> getTestCases() {
		// Run through all test runs to get all unique test cases.
		HashMap<String, TestCase> tests = new HashMap<String, TestCase>();
		for (TestRun tr : this.testRuns) {
			tests.putAll(tr.getTestCases());
		}
		return tests;
	}

	public void setRunIdx(int idx) {
		this.runIdx = idx;
	}

	public int getRunIdx() {
		return this.runIdx;
	}

	public TreeMap<String, TestCaseSummary> getTestCaseSummaries() {
		return testCaseSummaries;
	}

	public TreeMap<String, TestCaseSummary> getTestCaseSummaries(
			String testSuiteName) {

		TreeMap<String, TestCaseSummary> filteredTestCaseSummaries = new TreeMap<String, TestCaseSummary>();

		Collection<TestCaseSummary> c = testCaseSummaries.values();
		Iterator<TestCaseSummary> iter = c.iterator();

		while (iter.hasNext()) {
			TestCaseSummary tcs = iter.next();

			if (null != tcs && tcs.getTestSuiteName().startsWith(testSuiteName)) {
				filteredTestCaseSummaries.put(tcs.getQualifiedName(), tcs);
			}
		}
		return filteredTestCaseSummaries;
	}

	public TestSuiteSummary getTestSuiteSummary(String name) {
		return testSuiteSummaries.get(name);
	}

	public TreeMap<String, TestSuiteSummary> getTestSuiteSummaries() {
		return testSuiteSummaries;
	}

	public TestCaseSummary getTestCaseSummary(String name) {
		return testCaseSummaries.get(name);
	}
	
	public void addTestSuiteSummary(TestSuite ts) {
		try {
			if (testSuiteSummaries.containsKey(ts.getName())) {
				testSuiteSummaries.get(ts.getName()).increment(ts);
			} else {
				testSuiteSummaries.put(ts.getName(), new TestSuiteSummary(ts));
			}
		} catch (Exception e) {
			System.err
					.print("Trying to add, test suite stats to a list created for another test case name.");
			e.printStackTrace(System.err);
		}
	}

	public void addTestCaseSummary(TestCase tc, int index) {
		try {
			if (testCaseSummaries.containsKey(tc.getQualifiedName())) {
				testCaseSummaries.get(tc.getQualifiedName()).increment(tc,
						index);
			} else {
				testCaseSummaries.put(tc.getQualifiedName(),
						new TestCaseSummary(tc, index));
			}
		} catch (UnitTHException jthe) {
			System.err
					.print("Trying to add, test case stats to a list created for another test case name.");
			jthe.printStackTrace(System.err);
		}
	}
	
	public int getNoUniqueTestCases() {
		return testCaseSummaries.size();
	}
	
	public int getNoSubTestSuites() {
		return this.testSuiteSummaries.size();
	}
	
	public int getNoSubTestSuites(String rtsName) {
		int ctr = 0;
		Collection<TestSuiteSummary> c = testSuiteSummaries.values();
		Iterator<TestSuiteSummary> i = c.iterator();
		while (i.hasNext()) {
			TestSuiteSummary tss = i.next();
			if (!rtsName.contains(tss.getName())) { 
				ctr++;
			}
		}
		return ctr;
	}
}
