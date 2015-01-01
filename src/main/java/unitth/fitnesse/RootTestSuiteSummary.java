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

import java.util.TreeSet;

/*
 * Collect a statement for all runs included in this test suite.
 */
public class RootTestSuiteSummary extends TestItemSummary {

	private TreeSet<String> noUniqueTestCases = new TreeSet<String>();

	private double bestRun = 0.0;
	private double worstRun = 1.0;
	
	public RootTestSuiteSummary(RootTestSuite rts) {
		name = rts.getName(); // Full name
		noRuns = rts.getTestRuns().size();
		noExecuted = rts.getNoTestCases();
		noPassed = rts.getNoPassed(); 
		noFailed = rts.getNoFailed();
		noAssertions = rts.getNoAssertions();
		noRights = rts.getNoRights();
		noWrongs = rts.getNoWrongs();
		noIgnores = rts.getNoIgnores();
		noExceptions = rts.getNoExceptions();
		
		passPct = (double)noPassed/(double)noExecuted;
		
		// Determine best and worst run
		double passRate = rts.getPassPctDouble();
		if (passRate > bestRun) {
			bestRun = passRate;
		}
		if (passRate < worstRun) {
			worstRun = passRate;
		}
		
		// Getting all unique test case names
		for (TestRun tr : rts.getTestRuns()) {
			for(String tcName : tr.getTestCases().keySet()) {
				noUniqueTestCases.add(tcName);
			}
		}
	}
	
	public void increment(RootTestSuite rts) {
		noRuns += rts.getTestRuns().size();
		noExecuted += rts.getNoTestCases();
		noPassed += rts.getNoPassed(); 
		noFailed += rts.getNoFailed();
		noAssertions += rts.getNoAssertions();
		noRights += rts.getNoRights();
		noWrongs += rts.getNoWrongs();
		noIgnores += rts.getNoIgnores();
		noExceptions += rts.getNoExceptions();
		
		passPct = (double)noPassed/(double)noExecuted;
		
		// Determine best and worst run
		double passRate = rts.getPassPctDouble();
		if (passRate > bestRun) {
			bestRun = passRate;
		}
		if (passRate < worstRun) {
			worstRun = passRate;
		}
		
		// Getting all unique test case names
		for (TestRun tr : rts.getTestRuns()) {
			for(String tcName : tr.getTestCases().keySet()) {
				noUniqueTestCases.add(tcName);
			}
		}
	}
	
	public int getNoUniqueTestCases() {
		return noUniqueTestCases.size();
	}

	public String toString() {
		String ret = "\n+- Root Test Suite Summary -----------------------------+\n"
			+ "  Name: " + name + "\n"
			+ "  No runs: "+ noRuns +"\n"
			+ "  No unique test cases: "+ noUniqueTestCases.size() +"\n"
			+ "  Best run: "+(bestRun*100.0)+"\n"
			+ "  Worst run: "+(worstRun*100.0)+"\n"
			+ "  Average: "+getPassPct()+"\n"
			+ "  Executed: "+ noExecuted + "\n"
			+ "  Passed: "+ noPassed + "\n"
			+ "  Failed: "+ (noExecuted-noPassed) + "\n"
			+ "  Asserts: "+ noAssertions + "\n"
			+ "  Rights: "+ noRights + "\n"
			+ "  Wrongs: "+ noWrongs + "\n"
			+ "  Ignores: "+ noIgnores + "\n"
			+ "  Exceptions: "+ noExceptions + "\n";
		
		return ret;
	}
	
	protected String getFnTrend(long trendInterval) { return ""; }
	protected String getTcTrend(long trendInterval) { return ""; }
	protected String getPrTrend(long trendInterval) { return ""; }
	protected String getPrDiff(int runs, int idx) { return ""; }
}
