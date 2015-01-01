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

import unitth.core.UnitTHException;

public class TestCaseSummary extends TestItemSummary {

	private String testSuiteName = "UNDEF";

	// <Run, Outcome> Outcome 1=pass 0=fail
	private HashMap<Integer, Integer> spread = null;

	public TestCaseSummary(TestCase tc, int index) {
		name = tc.getName();
		testSuiteName = tc.getTestSuiteName();
		noRuns = 1;
		if (tc.isPassed()) {
			noPassed = 1;
			passPct = 1.0;
		} else {
			noFailed = 1;
			passPct = 0.0;
		}

		noAssertions = tc.getNoAssertions();
		noRights = tc.getNoRights();
		noWrongs = tc.getNoWrongs();
		noIgnores = tc.getNoIgnores();
		noExceptions = tc.getNoExceptions();

		spread = new HashMap<Integer, Integer>();
		if (tc.isPassed()) {
			addRunIdx(index, 1);
		} else {
			addRunIdx(index, -1);
		}
	}

	public void increment(TestCase tc, int index) throws UnitTHException {
		if (name.equalsIgnoreCase(tc.getName())
				&& testSuiteName.equalsIgnoreCase(tc.getTestSuiteName())) {
			noRuns++;
			if (tc.isPassed()) {
				noPassed++;
			} else {
				noFailed++;
			}

			noAssertions += tc.getNoAssertions();
			noRights += tc.getNoRights();
			noWrongs += tc.getNoWrongs();
			noIgnores += tc.getNoIgnores();
			noExceptions += tc.getNoExceptions();

			passPct = ((double) noPassed / (double) (noPassed + noFailed));

			if (tc.isPassed()) {
				addRunIdx(index, 1);
			} else {
				addRunIdx(index, -1);
			}

		} else {
			throw new UnitTHException(
					"This test case has a different class name than expected!");
		}
	}

	public String getTestCaseName() {
		return name;
	}
	
	public String getQualifiedName() {
		return testSuiteName+"."+name;
	}

	private void addRunIdx(int idx, Integer verdict) {
		spread.put(idx, verdict);
	}

	public String getTestSuiteName() {
		return testSuiteName;
	}

	public Integer getSpreadAt(int idx) {
		if (spread.containsKey(idx)) {
			return spread.get(idx);
		} else {
			return null;
		}
	}

	public String toString() {
		String ret = "+- Test Case Summary -----------------------------+\n"
				+ "  Name: " + name + "\n" 
				+ "  Test Suite Name: " + testSuiteName + "\n"
				+ "  Qualified Name: " + getQualifiedName() + "\n" 
				+ "  No runs: " + noRuns + "\n"
				+ "  No asserts: " + noAssertions + "\n" 
				+ "  Pass pct %: " + getPassPct() + "\n" 
				+ "  Pass pct double: "	+ getPassPctDouble() + "\n";
		return ret;
	}
	
	public String getTestSuitePlusTestCaseName(String tsName) {
		// Ripping everything but the last part of the qualified name
		String strippedClassName = "UNDEF";
		String ret = null;
		if (this.testSuiteName.startsWith(tsName) && !tsName.equals("")
				&& !tsName.equals("UNDEF")) {
			if (testSuiteName.equalsIgnoreCase(tsName)) {
				ret = this.name;
			} else {
				strippedClassName = this.testSuiteName.replaceFirst(tsName
						+ ".", "");
				ret = strippedClassName + "." + this.name;
			}
		} else {
			ret = this.testSuiteName+"."+this.name;
		}
		return ret;
	}
	
	protected String getFnTrend(long trendInterval) { return ""; }
	protected String getTcTrend(long trendInterval) { return ""; }
	protected String getPrTrend(long trendInterval) { return ""; }
	protected String getPrDiff(int runs, int idx) { return ""; }
}
