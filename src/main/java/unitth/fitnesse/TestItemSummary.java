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

import unitth.core.TestItemUtils;

public abstract class TestItemSummary {

	protected String name = "";
	protected int noRuns = 0;
	protected int noPassed = 0;
	protected int noFailed = 0;
	protected int noAssertions = 0;
	protected int noRights = 0;
	protected int noWrongs = 0;
	protected int noIgnores = 0;
	protected int noExceptions = 0;
	protected double passPct = 0.0;
	protected int noExecuted = 0;
	protected int highestRunIdx = 0;
	
	public String getPassPct() {
		return TestItemUtils.passPctToString(getPassPctDouble());
	}

	public double getPassPctDouble() {
		return passPct * 100.0;
	}
	
	public String getName() {
		if (name == null) {
			return "module-not-defined";
		}

		return name;
	}
	
	public int getNoRuns() {
		return noRuns;
	}

	public void setNoRuns(int noRuns) {
		this.noRuns = noRuns;
	}

	public int getNoPassed() {
		return noPassed;
	}

	public void setNoPassed(int noPassed) {
		this.noPassed = noPassed;
	}

	public int getNoFailed() {
		return noFailed;
	}

	public void setNoFailed(int noFailed) {
		this.noFailed = noFailed;
	}

	public int getNoAssertions() {
		return noAssertions;
	}

	public void setNoAssertions(int noAssertions) {
		this.noAssertions = noAssertions;
	}

	public int getNoRights() {
		return noRights;
	}

	public void setNoRights(int noRights) {
		this.noRights = noRights;
	}

	public int getNoWrongs() {
		return noWrongs;
	}

	public void setNoWrongs(int noWrongs) {
		this.noWrongs = noWrongs;
	}

	public int getNoIgnores() {
		return noIgnores;
	}

	public void setNoIgnores(int noIgnores) {
		this.noIgnores = noIgnores;
	}

	public int getNoExceptions() {
		return noExceptions;
	}

	public void setNoExceptions(int noExceptions) {
		this.noExceptions = noExceptions;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNoFailures() {
		return noFailed;
	}
	
	public void setNoExecuted(int no) {
		noExecuted = no;
	}
	
	public int getNoExecuted() {
		return noExecuted;
	}
	
	//
	// Trends
	//
	/**
	 * This method returns the test case trend for the last day.
	 * 
	 * @return The change in number of test cases.
	 */
	// FIXME, JUnit
	public String getTcTrendLastDay() {
		return getTcTrend(1 * 24 * 60 * 60 * 1000);
	}

	/**
	 * This method returns the test case trend for the last 3 days.
	 * 
	 * @return The change in number of test cases.
	 */
	// FIXME, JUnit
	public String getTcTrendLast3Days() {
		return getTcTrend(3 * 24 * 60 * 60 * 1000);
	}

	/**
	 * This method returns the test case trend for the last 7 days.
	 * 
	 * @return The change in number of test cases.
	 */
	// FIXME, JUnit
	public String getTcTrendLast7Days() {
		return getTcTrend(7 * 24 * 60 * 60 * 1000);
	}

	/**
	 * This method returns the failure trend for the last day.
	 * 
	 * @return The change in number of failure.
	 */
	public String getFnTrendLastDay() {
		return getFnTrend(1 * 24 * 60 * 60 * 1000);
	}

	/**
	 * This method returns the failure trend for the last 3 days.
	 * 
	 * @return The change in number of failures.
	 */
	public String getFnTrendLast3Days() {
		return getFnTrend(3 * 24 * 60 * 60 * 1000);
	}

	/**
	 * This method returns the failure trend for the last 7 days.
	 * 
	 * @return The change in number of failures.
	 */
	public String getFnTrendLast7Days() {
		return getFnTrend(7 * 24 * 60 * 60 * 1000);
	}
	
	public String getPrTrendLastRun() {
		if (noRuns < 2) {
			return "NA";
		} else {
			return getPrDiff(highestRunIdx, 1);
		}
	}
	
	public String getPrTrendLast3Days() {
		return getPrTrend(3 * 24 * 60 * 60 * 1000);
	}

	public String getPrTrendLast7Days() {
		return getPrTrend(7 * 24 * 60 * 60 * 1000);
	}

	public String getPrTrendLast5Runs() {
		if (noRuns < 5) {
			return "NA";
		} else {
			return getPrDiff(highestRunIdx, 4);
		}
	}

	public String getPrTrendLast10Runs() {
		if (noRuns < 10) {
			return "NA";
		} else {
			return getPrDiff(highestRunIdx, 9);
		}
	}
	
	public int getHighestRunIdx() {
		return highestRunIdx;
	}

	protected abstract String getFnTrend(long trendInterval);
	protected abstract String getTcTrend(long trendInterval);
	protected abstract String getPrTrend(long trendInterval);
	protected abstract String getPrDiff(int runs, int idx);
}
