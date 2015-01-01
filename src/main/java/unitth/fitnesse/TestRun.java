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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import unitth.core.CustomStringLengthComparator;
import unitth.core.TestItemUtils;

public class TestRun implements Comparable<TestRun> {

	private String rootTestSuiteName = null;
	private String runDate = "NaN";
	private int runIndex = 0;
	private int sumAsserts = 0;
	private int sumRights = 0;
	private int sumWrongs = 0;
	private int sumIgnores = 0;
	private int sumExceptions = 0;
	private int noPassed = 0;
	private int noExecuted = 0;
	private int noFailed = 0;
	private String fileName = "undef";

	private HashMap<String, TestSuite> testSuites = null;

	public TestRun() {
		testSuites = new HashMap<String, TestSuite>();
	}

	public void setRootTestSuiteName(String name) {
		this.rootTestSuiteName = name;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public int getSumAssertions() {
		return sumRights + sumWrongs + sumIgnores + sumExceptions;
	}

	public int getSumRights() {
		return sumRights;
	}

	public void setSumRights(int sumRights) {
		this.sumRights = sumRights;
	}

	public int getSumWrongs() {
		return sumWrongs;
	}

	public void setSumWrongs(int sumWrongs) {
		this.sumWrongs = sumWrongs;
	}

	public int getSumIgnores() {
		return sumIgnores;
	}

	public void setSumIgnores(int sumIgnores) {
		this.sumIgnores = sumIgnores;
	}

	public int getSumExceptions() {
		return sumExceptions;
	}

	public void setSumExceptions(int sumExceptions) {
		this.sumExceptions = sumExceptions;
	}

	public int getNoExecuted() {
		return noExecuted;
	}

	public void setNoExecuted(int noExecuted) {
		this.noExecuted = noExecuted;
	}

	public int getNoFailed() {
		return noFailed;
	}

	public void setNoFailed(int noFailed) {
		this.noFailed = noFailed;
	}

	public int getRunIndex() {
		return runIndex;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public void setNoPassed(int noPassed) {
		this.noPassed = noPassed;
	}

	/**
	 * Comparator 
	 * 1) Time stamp
	 * 2) Root test suite
	 * @param The <code>TestRun</code> compare with.
	 * @return -1, 0, 1 according to Comparator iface spec
	 */
	public int compareTo(TestRun tr) {
		if (tr != null ) {
			if (runDate != null && tr.getRunDate() != null) {
				if (!runDate.equalsIgnoreCase(tr.getRunDate())) {
					return tr.getRunDate().compareTo(runDate);
				}
			}
			if (this.rootTestSuiteName != null && tr.getRootTestSuiteName() != null) {
				if (!rootTestSuiteName.equalsIgnoreCase(tr.getRootTestSuiteName())) {
					return tr.getRootTestSuiteName().compareTo(rootTestSuiteName);
				}
			}
		}
		return -1;
	}

	public void setRunIndex(int index) {
		runIndex = index;
	}

	public String getRunDate() {
		return runDate;
	}

	public String getRootTestSuiteName() {
		return rootTestSuiteName;
	}

	public double getPassPctDouble() {
		return ((double) noPassed / (double) noExecuted) * 100.0;
	}

	public String getPassPct() {
		return TestItemUtils.passPctToString(getPassPctDouble());
	}

	public int getNoTestCases() {
		return this.noExecuted;
	}

	public int getNoPassed() {
		return this.noPassed;
	}

	public String toString() {
		String ret = "+- TestRun -----------------------------+\n"
				+ "  Run index: "
				+ runIndex
				+ "\n"
				+ "  Run date: "
				+ runDate
				+ "\n"
				+ "  File name: "
				+ fileName
				+ "\n"
				+ "  Pass %: "
				+ getPassPct()
				+ "\n"
				+ "  Executed: "
				+ noExecuted
				+ "\n"
				+ "  Passed: "
				+ noPassed
				+ "\n"
				+ "  Failed: "
				+ noFailed
				+ "\n"
				+ "  Asserts: "
				+ sumAsserts
				+ "\n"
				+ "  Rights: "
				+ sumRights
				+ "\n"
				+ "  Wrongs: "
				+ sumWrongs
				+ "\n"
				+ "  Ignores: "
				+ sumIgnores
				+ "\n"
				+ "  Exceptions: "
				+ sumExceptions
				+ "\n"
				+ "  No test suites in this run: "
				+ testSuites.size() + "\n";

		if (testSuites.size() > 0) {
			ret += "\n";
			Collection<TestSuite> c = testSuites.values();
			Iterator<TestSuite> iter = c.iterator();
			while (iter.hasNext()) {
				TestSuite ts = iter.next();
				ret += ts.toString() + "\n";
			}
		}
		return ret;
	}

	public void calcStats() {

		Collection<TestSuite> c = testSuites.values();
		Iterator<TestSuite> iter = c.iterator();

		while (iter.hasNext()) {
			TestSuite ts = iter.next();
			ts.calcStats();

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

			sumRights += ts.getRights();
			sumWrongs += ts.getWrongs();
			sumIgnores += ts.getIgnores();
			sumExceptions += ts.getExceptions();
			sumAsserts += ts.getNoAssertions();
			noExecuted += ts.getNoTestCases();
			noPassed += ts.getNoPassed();
			noFailed += ts.getNoFailed();
		}
		
		//
		// Test suites are stored flat - lets fake a tree structure. All
		// stats shall be recursive. We are incrementing all test suites
		// as the stats has already been calculated.
		//
		// 1) Get a list of sorted keys.
		// 2) For every package where there is a sub match in name we increment.
		//
		String[] testSuiteNames = testSuites.keySet().toArray(new String[0]);
		Arrays.sort(testSuiteNames, new CustomStringLengthComparator());

		for (String s1 : testSuiteNames) {
			for (String s2 : testSuiteNames) {
				// We do not want to increment the same
				if (!s1.equalsIgnoreCase(s2)) {
					if (s2.length() >= rootTestSuiteName.length()) {
						// Trim s1 one level!
						if (s1.contains(".")) {
							String parent = s1.substring(0, s1.lastIndexOf("."));
							if (parent.equalsIgnoreCase(s2)) {
								testSuites.get(s2).addFrom(testSuites.get(s1));
							}
						}
					}
				}
			}
		}
	}

	public HashMap<String, TestSuite> getTestSuites() {
		return testSuites;
	}

	/**
	 * Add test case to existing test suite or create new test suite if it does
	 * not already exist.
	 * 
	 * @param tc
	 *            The test case to be added.
	 */
	public void addTestCase(TestCase tc) {
		if (testSuites.containsKey(tc.getTestSuiteName())) {
			testSuites.get(tc.getTestSuiteName()).addTestCase(tc);
		} else {
			TestSuite ts = new TestSuite();
			ts.setName(tc.getTestSuiteName());
			ts.addTestCase(tc);
			testSuites.put(tc.getTestSuiteName(), ts);
		}

		// All parent nodes needs to be added.
		if (tc.getTestSuiteName().contains(".")) {
			String parentName = tc.getTestSuiteName().substring(0,
					tc.getTestSuiteName().lastIndexOf("."));
			while (true) {
				if (!testSuites.containsKey(parentName)) {
					TestSuite ts = new TestSuite();
					ts.setName(parentName);
					testSuites.put(parentName, ts);
				}
				if (parentName.contains(".")) {
					parentName = parentName.substring(0,
							parentName.lastIndexOf("."));
				} else {
					return;
				}
			}
		}
	}

	public void addTestSuite(TestSuite ts) {
		testSuites.put(ts.getName(), ts);
	}

	/**
	 * Returns a test suite that matches the name given.
	 * 
	 * @param name
	 *            Name of the test suite to find.
	 * @return The looked for test suite or null if not found.
	 */
	public TestSuite getTestSuite(String name) {
		return testSuites.get(name);
	}

	/**
	 * Returns all test cases in all test suites.
	 * 
	 * @return All test cases in all test suites or an empty HashMap if there
	 *         are no test cases.
	 */
	public HashMap<String, TestCase> getTestCases() {
		HashMap<String, TestCase> items = new HashMap<String, TestCase>();
		Collection<TestSuite> tsc = testSuites.values();
		Iterator<TestSuite> tsi = tsc.iterator();

		while (tsi.hasNext()) {
			TestSuite ts = tsi.next();
			items.putAll(ts.getTestCases());
		}
		return items;
	}
}
