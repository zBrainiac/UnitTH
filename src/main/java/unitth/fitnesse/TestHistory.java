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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import unitth.core.RunHistory;
import unitth.core.TestItemUtils;

public class TestHistory extends RunHistory {

	// Layer for all test suite objects
	private TreeSet<RootTestSuite> rootTestSuites = null;
	private TreeSet<RootTestSuite> sortedRootTestSuites = null;

	private int noUniqueTestCases = 0;
	private double worstRun = 100.0;
	private double bestRun = 0.0;
	private double averagePassRate = 0.0;
	private int largestNumberOfTcs = 0;
	private int largestNumberOfNonPass = 0; // In a test run
	private int noAssertions = 0;
	private int noRights = 0;
	private int noWrongs = 0;
	private int noIgnores = 0;
	private int noExceptions = 0;
	private int noRuns = 0;
	private int noPassed = 0;
	private int noFailed = 0;

	/*
	 * This <code>TreeMap</code> stores a number of summaries for all the test
	 * cases found in the FitNesse reports. These summaries are created while
	 * running through all the saved statistics. The reason for using a summary
	 * object is to avoid having to parse all the stored statistics every time
	 * some statistics regarding a module needs to be fetched.
	 */
	private TreeMap<String, RootTestSuiteSummary> rootTestSuiteSummaries = new TreeMap<String, RootTestSuiteSummary>();
	
	public void calcStats() {

		double sumOfPassRates = 0.0;
		sortedRootTestSuites = new TreeSet<RootTestSuite>();

		// Looping through all the basic calculations all the way down
		// to the test case level.
		for (RootTestSuite rts : rootTestSuites) {
			rts.calcStats();

			double passRateHolder = rts.getPassPctDouble();

			// Get worst and best pass rates
			if (passRateHolder > bestRun) {
				bestRun = passRateHolder;
			}
			if (passRateHolder < worstRun) {
				worstRun = passRateHolder;
			}
			sumOfPassRates += passRateHolder;

			// Largest number of test cases
			int noTestCasesHolder = rts.getNoTestCases();
			if (noTestCasesHolder > largestNumberOfTcs) {
				largestNumberOfTcs = noTestCasesHolder;
			}

			int noFailuresHolder = rts.getNoFailed();
			if (noFailuresHolder > largestNumberOfNonPass) {
				largestNumberOfNonPass = noFailuresHolder;
			}

			noAssertions += rts.getNoAssertions();
			noRights += rts.getNoRights();
			noWrongs += rts.getNoWrongs();
			noIgnores += rts.getNoIgnores();
			noExceptions += rts.getNoExceptions();

			sortedRootTestSuites.add(rts); // Added in sorted order.
		}

		// 
		// ROOT TEST SUITE SUMMARIES
		// 
		for (RootTestSuite rts : sortedRootTestSuites) {

			if (rootTestSuiteSummaries.containsKey(rts.getName())) {
				rootTestSuiteSummaries.get(rts.getName()).increment(rts);
			} else {
				rootTestSuiteSummaries.put(rts.getName(), new RootTestSuiteSummary(rts));
			}

			//
			// TEST RUNS
			//
			int index = rts.getTestRuns().size();
			for (TestRun tr : rts.getTestRuns()) {
				tr.setRunIndex(index);
				rts.setRunIdx(index);
				
				//
				// TEST SUITES
				//
				Collection<TestSuite> c0 = tr.getTestSuites().values();
				Iterator<TestSuite> iter0 = c0.iterator();
				
				while (iter0.hasNext()) {
					TestSuite ts = iter0.next();
					ts.setRunIdx(index);
					rts.addTestSuiteSummary(ts);
				}
				
				//
				// TEST CASES
				//
				Collection<TestCase> c1 = tr.getTestCases().values();
				Iterator<TestCase> iter1 = c1.iterator();

				while (iter1.hasNext()) {

					TestCase tc = iter1.next();
					rts.addTestCaseSummary(tc, index);
				}
				index--;
			}

			noRuns += rts.getNoRuns();
			noPassed += rts.getNoPassed();
			noFailed += rts.getNoFailed();
			noUniqueTestCases += rts.getNoUniqueTestCases();
			
			// Get the 
			if (rts.getTestSuiteSummary(rts.getName()) != null) {
				rts.getTestSuiteSummary(rts.getName()).setNoSubTestSuites(rts.getNoSubTestSuites(rts.getName()));
				rts.getTestSuiteSummary(rts.getName()).setNoTestCases(rts.getNoUniqueTestCases());
			}
		}
		averagePassRate = sumOfPassRates / sortedRootTestSuites.size();
		rootTestSuites = null;
		rootTestSuites = sortedRootTestSuites;
	}

	public TestHistory() {
		rootTestSuites = new TreeSet<RootTestSuite>();
	}

	public void addRootTestSuite(RootTestSuite ts) {
		rootTestSuites.add(ts);
	}

	public String toString() {
		String ret = "+- FitNesse TestHistory -----------------------------+\n"
				+ "  No root test suites: " + rootTestSuites.size() + "\n";
		if (rootTestSuites.size() > 0) {
			ret += "\n";
		}

		for (RootTestSuite ts : rootTestSuites) {
			ret += ts.toString() + "\n";
		}

		ret += "+= Test Summaries ===================================+";
		
		Collection<RootTestSuiteSummary> c = rootTestSuiteSummaries.values();
		Iterator<RootTestSuiteSummary> iter = c.iterator();
		while (iter.hasNext()) {
			RootTestSuiteSummary tcs = iter.next();
			ret += tcs.toString();
		}

		for (RootTestSuite rts : rootTestSuites) {
			ret += "-+- Summaries in RTS "+rts.getName()+" ------------------------+-\n";
			
			Collection<TestSuiteSummary> c3 = rts.getTestSuiteSummaries().values();
			Iterator<TestSuiteSummary> iter3 = c3.iterator();
			while (iter3.hasNext()) {
				TestSuiteSummary tcs = iter3.next();
				ret += tcs.toString();
			}
			
			Collection<TestCaseSummary> c2 = rts.getTestCaseSummaries().values();
			Iterator<TestCaseSummary> iter2 = c2.iterator();
			while (iter2.hasNext()) {
				TestCaseSummary tcs = iter2.next();
				ret += tcs.toString();
			}
		}
		return ret;
	}

	public int getNoTestSuites() {
		return rootTestSuiteSummaries.size();
	}

	public String getAvePassPct() {
		return TestItemUtils.passPctToString(getAvePassPctDouble());
	}

	public double getAvePassPctDouble() {
		return averagePassRate;
	}
	
	public int getNoUniqueTestCases() {
		return noUniqueTestCases;
	}

	

	public TreeMap<String, RootTestSuiteSummary> getRootTestSuiteSummaries() {
		return rootTestSuiteSummaries;
	}

	public RootTestSuiteSummary getRootTestSuiteSummary(String name) {
		return rootTestSuiteSummaries.get(name);
	}

	// Unique test suite names needs to be unique over all root test
	// suites.
	public Object[] getUniqueTestSuites() {
		
		ArrayList<Object> list = new ArrayList<Object>();
		for (RootTestSuite rts : rootTestSuites) {
			// For each - key add the RTS name.
			Set<String> set = rts.getTestSuiteSummaries().keySet();
			for (String s : set) {
				list.add(rts.getName()+":"+s);
			}
		}
		return list.toArray();
	}

	public RootTestSuite getRootTestSuite(String name) {

		for (RootTestSuite rts : sortedRootTestSuites) {
			if (rts.getName().equalsIgnoreCase(name)) {
				return rts;
			}
		}
		return null;
	}
		
	public int getNoRuns() {
		return noRuns;
	}

	public int getNoPassed() {
		return noPassed;
	}

	public int getNoFailed() {
		return noFailed;
	}

	public int getNoAssertions() {
		return noAssertions;
	}

	public int getNoRights() {
		return noRights;
	}

	public int getNoWrongs() {
		return noWrongs;
	}

	public int getNoIgnores() {
		return noIgnores;
	}

	public int getNoExceptions() {
		return noExceptions;
	}

	public String getPassPct() {
		return TestItemUtils.passPctToString(averagePassRate);
	}

	public double getPassPctDouble() {
		return averagePassRate;
	}

	/**
	 * Gets the test runs in the FitNesse history.
	 * 
	 * @return
	 */
	public TreeSet<TestRun> getFitNesseRuns(String testSuiteName) {

		TreeSet<TestRun> runs = new TreeSet<TestRun>();

		// Iterate x Iterate
		for (RootTestSuite ts : sortedRootTestSuites) {
			if (ts.getName().equalsIgnoreCase(testSuiteName)) {

				// Collection<TestRun> c = ts.getTestRuns();
				runs.addAll(ts.getTestRuns());
			}
		}
		return runs;
	}
	
	public int getLargestNumberOfTcs(String testSuiteName) {
		
		// Iterate x Iterate
		for (RootTestSuite ts : sortedRootTestSuites) {
			if (ts.getName().equalsIgnoreCase(testSuiteName)) {
				return ts.getNoTestCases();
			}
		}
		return 0;
	}
	
	public boolean hasSubTestSuiteSummaries(String rtsName, String testSuiteName) {
		for( RootTestSuite rts : rootTestSuites ) {
			if (rts.getName().equalsIgnoreCase(rtsName)) {
				Collection<TestSuiteSummary> tsColl = rts.getTestSuiteSummaries().values();
				Iterator<TestSuiteSummary> iter = tsColl.iterator();
				while (iter.hasNext()) {
					TestSuiteSummary tss = iter.next();
					if (null != tss) {
						if (tss.getName().contains(testSuiteName+".") && !tss.getName().equalsIgnoreCase(testSuiteName)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public TreeSet<RootTestSuite> getRootTestSuites() {
		return sortedRootTestSuites;
	}
	
	public TreeMap<String, TestSuiteSummary> getTestSuiteSummaries(String testSuiteName) {
		for (RootTestSuite rts : rootTestSuites) {
			if (rts.getName().equalsIgnoreCase(testSuiteName)) {
				return rts.getTestSuiteSummaries();
			}
		}
		return null;
	}
	
	public TestSuiteSummary getTestSuiteSummary(String rtsName, String testSuiteName) {
		for (RootTestSuite rts : rootTestSuites) {
			if (rts.getName().equalsIgnoreCase(rtsName)) {
				return rts.getTestSuiteSummary(testSuiteName);
			}
		}
		return null;
	}
	
	public TreeMap<String, TestCaseSummary> getTestCaseSummaries(String rootTestSuiteName) {
		for (RootTestSuite rts : rootTestSuites) {
			if (rts.getName().equalsIgnoreCase(rootTestSuiteName)) {
				return rts.getTestCaseSummaries();
			}
		}
		return null;
	}
	
	public TestCaseSummary getTestCaseSummary(String rtsName, String testSuiteName) {
		for (RootTestSuite rts : rootTestSuites) {
			if (rts.getName().equalsIgnoreCase(rtsName)) {
				return rts.getTestCaseSummary(testSuiteName);
			}
		}
		return null;
	}
}