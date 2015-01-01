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

import java.util.HashMap;
import java.util.TreeSet;

import unitth.core.TestItemUtils;
import unitth.core.UnitTHException;

/*
 * Collect a statement for all runs included in this test suite.
 */
public class TestSuiteSummary extends TestItemSummary {

	private TreeSet<String> noUniqueTestCases = new TreeSet<String>();

	private double bestRun = 0.0;
	private double worstRun = 100.0;
	private int noSubTestSuites = 0;
	private int noTestCases = 0;
	// Needed to be able to find the correct test suites when calculating
	// trends. Test suites can exist many times but the RTS differs.
	private String rootTestSuite = "undef";

	private HashMap<Integer, TestSuite> spread = null;

	public TestSuiteSummary(TestSuite ts) {
		name = ts.getName();
		noRuns++;
		noExecuted = ts.getNoTestCases();
		noPassed = ts.getNoPassed();
		noFailed = ts.getNoFailed();
		noAssertions = ts.getNoAssertions();
		noRights = ts.getNoRights();
		noWrongs = ts.getNoWrongs();
		noIgnores = ts.getNoIgnores();
		noExceptions = ts.getNoExceptions();

		passPct = (double) noPassed / (double) noExecuted;

		// Determine best and worst run
		double passRate = ts.getPassPctDouble();
		if (passRate > bestRun) {
			bestRun = passRate;
		}
		if (passRate < worstRun) {
			worstRun = passRate;
		}

		if (highestRunIdx < ts.getRunIdx()) {
			highestRunIdx = ts.getRunIdx();
		}

		spread = new HashMap<Integer, TestSuite>();
		spread.put(ts.getRunIdx(), ts);
		noUniqueTestCases.addAll(ts.getTestCases().keySet());
	}

	public TestSuiteSummary(RootTestSuite rts) {
		name = rts.getName();
		rootTestSuite = rts.getName();
		noExecuted = rts.getNoTestCases();
		noPassed = rts.getNoPassed();
		noFailed = rts.getNoFailed();
		noAssertions = rts.getNoAssertions();
		noRights = rts.getNoRights();
		noWrongs = rts.getNoWrongs();
		noIgnores = rts.getNoIgnores();
		noExceptions = rts.getNoExceptions();

		passPct = (double) noPassed / (double) noExecuted;

		// Determine best and worst run
		double passRate = rts.getPassPctDouble();
		if (passRate > bestRun) {
			bestRun = passRate;
		}
		if (passRate < worstRun) {
			worstRun = passRate;
		}

		if (highestRunIdx < rts.getRunIdx()) {
			highestRunIdx = rts.getRunIdx();
		}

		spread = new HashMap<Integer, TestSuite>();
		spread.put(rts.getRunIdx(), convert(rts));
		noUniqueTestCases.addAll(rts.getTestCases().keySet());
	}

	public void increment(TestSuite ts) {
		if (ts.name.equalsIgnoreCase(name)) {
			noRuns++;
			noExecuted += ts.getNoTestCases();
			noPassed += ts.getNoPassed();
			noFailed += ts.getNoFailed();
			noAssertions += ts.getNoAssertions();
			noRights += ts.getNoRights();
			noWrongs += ts.getNoWrongs();
			noIgnores += ts.getNoIgnores();
			noExceptions += ts.getNoExceptions();

			passPct = (double) noPassed / (double) noExecuted;

			// Determine best and worst run
			double passRate = ts.getPassPctDouble();
			if (passRate > bestRun) {
				bestRun = passRate;
			}
			if (passRate < worstRun) {
				worstRun = passRate;
			}

			if (highestRunIdx < ts.getRunIdx()) {
				highestRunIdx = ts.getRunIdx();
			}

			spread.put(ts.getRunIdx(), ts);
			noUniqueTestCases.addAll(ts.getTestCases().keySet());
		}
	}

	public void increment(RootTestSuite rts) {
		if (rts.getName().equalsIgnoreCase(name)) {
			noExecuted += rts.getNoTestCases();
			noPassed += rts.getNoPassed();
			noFailed += rts.getNoFailed();
			noAssertions += rts.getNoAssertions();
			noRights += rts.getNoRights();
			noWrongs += rts.getNoWrongs();
			noIgnores += rts.getNoIgnores();
			noExceptions += rts.getNoExceptions();

			passPct = (double) noPassed / (double) noExecuted;

			// Determine best and worst run
			double passRate = rts.getPassPctDouble();
			if (passRate > bestRun) {
				bestRun = passRate;
			}
			if (passRate < worstRun) {
				worstRun = passRate;
			}

			if (highestRunIdx < rts.getRunIdx()) {
				highestRunIdx = rts.getRunIdx();
			}

			spread.put(rts.getRunIdx(), convert(rts));
			noUniqueTestCases.addAll(rts.getTestCases().keySet());
		}
	}

	public int getNoUniqueTestCases() {
		if (noTestCases != 0) {
			return noTestCases;
		}
		return noUniqueTestCases.size();
	}

	public String toString() {
		String ret = "+- Test Suite Summary -----------------------------+\n"
				+ "  Name: "
				+ name
				+ "\n"
				+ "  No runs: "
				+ noRuns
				+ "\n"
				+ "  No unique test cases: "
				+ noUniqueTestCases.size()
				+ "\n"
				+ "  Best run: "
				+ bestRun
				+ "\n"
				+ "  Worst run: "
				+ (worstRun * 100.0)
				+ "\n"
				+ "  Average: "
				+ getPassPct()
				+ "\n"
				+ "  Executed: "
				+ noExecuted
				+ "\n"
				+ "  Passed: "
				+ noPassed
				+ "\n"
				+ "  Failed: "
				+ (noExecuted - noPassed)
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
				+ noExceptions + "\n";

		return ret;
	}

	public void addFrom(TestSuiteSummary tss) {

		noExecuted += tss.getNoExecuted();
		noPassed += tss.getNoPassed();
		noFailed += tss.getNoFailed();
		noAssertions += tss.getNoAssertions();
		noRights += tss.getNoRights();
		noWrongs += tss.getNoWrongs();
		noIgnores += tss.getNoIgnores();
		noExceptions += tss.getNoExceptions();

		passPct = (double) noPassed / (double) noExecuted;

		// Determine best and worst run
		double passRate = tss.getPassPctDouble();
		if (passRate > bestRun) {
			bestRun = passRate;
		}
		if (passRate < worstRun) {
			worstRun = passRate;
		}

		noUniqueTestCases.addAll(tss.noUniqueTestCases);
		
		// Each time invoked we add one. And then we add the sum
		// from invocations in earlier calls.
		noSubTestSuites++;
		noSubTestSuites += tss.getNoSubTestSuites();
	}

	public int getNoSubTestSuites() {
		return this.noSubTestSuites;
	}

	public double getWorstRunDouble() {
		return worstRun;
	}

	public double getBestRunDouble() {
		return bestRun;
	}

	public String getWorstRun() {
		return TestItemUtils.passPctToString(getWorstRunDouble());
	}

	public String getBestRun() {
		return TestItemUtils.passPctToString(getBestRunDouble());
	}

	public double getAveragePassRateDouble() {
		return passPct * 100;
	}

	public String getAveragePassRate() {
		return TestItemUtils.passPctToString(getAveragePassRateDouble());
	}

	/**
	 * Gets the first run larger that has a time stamp larger than the interval.
	 * Starting at the back since the runs are ordered with the highest and
	 * latest index belonging the last executed test run.
	 * 
	 * @param trendInterval
	 *            In milliseconds
	 * @return The found TestSuite if any, else null.
	 */
	public TestSuite getTrendTestRun(long trendInterval) {
		long currentTime = System.currentTimeMillis();
		long breakPoint = currentTime - trendInterval;

		// It might be necessary to run through all runs in case
		// there might be an occasion where the modules are not sorted.

		for (int i = this.noRuns; i > 0; i--) {
		//for (int i = 0; i<this.noRuns; i++) {
			TestSuite ts = spread.get(i);

			if (ts != null) {
				long runTimeStamp = ts.getRunDateAsLong();

				if (breakPoint > runTimeStamp) {
					return ts;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the test case difference between two test runs indicated by the
	 * trend interval.
	 * 
	 * @param trendInterval
	 *            In milliseconds, indicates the run to find.
	 * @return A string indicating the test case trend, +N, -N, 0 or NA if no
	 *         matching run was found.
	 */
	public String getTcTrend(long trendInterval) {
		TestSuite matchingTs = getTrendTestRun(trendInterval);
		if (null != matchingTs) {

			TestSuite tsAtEnd = spread.get(highestRunIdx);
			if (null != tsAtEnd) {
				int diff = spread.get(highestRunIdx).getNoTestCases()
						- matchingTs.getNoTestCases();

				String ret = Integer.toString(diff);
				if (0 < diff) {
					ret = "+" + ret;
				}
				return ret;
			}
		}
		return "NA";
	}

	/**
	 * Gets the test case fail difference between two test runs indicated by the
	 * trend interval.
	 * 
	 * @param trendInterval
	 *            In milliseconds, indicates the run to find.
	 * @return A string indicating the test case trend, +N, -N, 0 or NA if no
	 *         matching run was found.
	 */
	public String getFnTrend(long trendInterval) {
		TestSuite matchingTs = getTrendTestRun(trendInterval);
		if (null != matchingTs) {
			TestSuite tsAtEnd = spread.get(highestRunIdx);
			if (null != tsAtEnd) {
				int diff = spread.get(highestRunIdx).getNoNonPassing()
						- matchingTs.getNoNonPassing();

				String ret = Integer.toString(diff);
				if (0 < diff) {
					ret = "+" + ret;
				}
				return ret;
			}
		}
		return "NA";
	}

	/**
	 * Gets the test case pass difference between two test runs indicated by the
	 * trend interval.
	 * 
	 * @param trendInterval
	 *            In milliseconds, indicates the run to find.
	 * @return A string indicating the test case trend, +N, -N, 0 or NA if no
	 *         matching run was found.
	 */
	public String getPrTrend(long trendInterval) {
		TestSuite matchingTs = getTrendTestRun(trendInterval);
		if (null != matchingTs) {
			TestSuite tsAtEnd = spread.get(highestRunIdx);
			if (null != tsAtEnd) {
				double diff = Double.parseDouble(spread.get(highestRunIdx).getPassPct())
						- Double.parseDouble(matchingTs.getPassPct());

				String ret = TestItemUtils.passPctToString(diff) + "%";
				if (0 < diff) {
					ret = "+" + ret;
				}
				return ret;
			}
		}
		return "NA";
	}

	/**
	 * Gets the pass rate difference between two test runs indicated by the
	 * trend no runs and index.
	 * 
	 * @param trendInterval
	 *            In milliseconds, indicates the run to find.
	 * @return A string indicating the test case trend, +N, -N, 0 or NA if no
	 *         matching run was found.
	 */
	public String getPrDiff(int last, int idx) {

		// There is no guarantee that there are run indexes 1-N.
		int key = 0;
		try {
			key = getSpreadKey(idx);
		} catch (UnitTHException uthe) {
			return "NA";
		}
		
		TestSuite tpLast = spread.get(last);
		TestSuite tpFirst = spread.get(key);

		// Do not return any diff if this module was not part of the last run.
		if (null == tpLast || null == tpFirst) {
			return "NA";
		}
		double diff = Double.parseDouble(tpLast.getPassPct())
				- Double.parseDouble(tpFirst.getPassPct());
		String ret = TestItemUtils.passPctToString(diff) + "%";
		if (0 < diff) {
			ret = "+" + ret;
		}
		return ret;
	}

	public int getSpreadKey(int idx) throws UnitTHException {
		Object[] keys = spread.keySet().toArray();
		int keyIdx = keys.length - idx - 1;
		if (keyIdx < keys.length && keyIdx >= 0) {
			return (Integer) keys[keyIdx];
		} else {
			throw new UnitTHException(
					"Failed on: keyIdx < keys.length && keyIdx >= 0");
		}
	}

	/*
	 * Simply converts a RootTestSuite to a TestSuite. Used when adding test
	 * suites to the spread.
	 */
	private TestSuite convert(RootTestSuite rts) {
		TestSuite ts = new TestSuite();
		ts.setNoPassed(rts.getNoPassed());
		ts.setNoTestCases(rts.getNoTestCases());
		ts.setPassPct(rts.getPassPctDouble());
		ts.setRunIdx(rts.getRunIdx());
		ts.setTestCases(rts.getTestCases());
		ts.setName(rts.getName());
		ts.setNoAssertions(rts.getNoAssertions());
		ts.setNoExceptions(rts.getNoExceptions());
		ts.setNoIgnores(rts.getNoIgnores());
		ts.setNoRights(rts.getNoRights());
		ts.setNoWrongs(rts.getNoWrongs());
		return ts;
	}

	public String getRTSName() {
		return rootTestSuite;
	}
	
	public void setNoSubTestSuites(int noTestSuites) {
		this.noSubTestSuites = noTestSuites;
	}
	
	public void setNoTestCases(int noTestCases) {
		this.noTestCases = noTestCases;
	}
}
