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
 * $Id: TestItem.java,v 1.5 2010/05/14 15:21:39 andnyb Exp $
 * -----------------------------------------------------------------------
 * 
 * =======================================================================
 */

package unitth.junit;

import unitth.core.TestItemUtils;

/**
 * This is the super class for all test item classes. It provides some common
 * functionality and variables.
 * 
 * @author andnyb
 */
public class TestItem {

	protected static final String UNKNOWN_TIME_STAMP = "unknown, broken run";
	
	protected String name = "";
	protected double executionTime = 0.000;
	protected String runDate = null;
	protected double passPct = 0.0;

	/**
	 * This method sets the execution time parsed from a string.
	 * 
	 * @param s
	 *            The string to parse into a double.
	 */
	public void setExecutionTime(String s) {
		try {
			executionTime = Double.parseDouble(s);
		} catch (NumberFormatException nfe) {
			System.err.println("Could not parse the execution time " + s
					+ " setting the execution time to 0.000");
			executionTime = 0.000;
		}
	}

	/**
	 * This method returns the execution time as a string.
	 * 
	 * @return The stringified execution time.
	 */
	public String getExecutionTime() {
		return TestItemUtils.executionTimeToString(executionTime);
	}

	/**
	 * This method returns the execution time as a double.
	 * 
	 * @return The execution time.
	 */
	public double getExecutionTimeDouble() {
		return executionTime;
	}

	/**
	 * Returns the name of the test item, qualified test case name, test module
	 * name or simply the string Run+index for the test run objects.
	 * 
	 * @return The name of the test item.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the test item name.
	 * 
	 * @param s
	 *            The name to be set.
	 */
	public void setName(String s) {
		name = s;
	}
	
	/**
	 * Strips the last part of the qualified name to get the package name.
	 * @return The package part ripped from the full name.
	 */
	public String getPackageName() {
		int liof = name.lastIndexOf(".");
		if (-1 == liof) {
			return "not-in-package";
		}
		return name.substring(0, liof);
	}
	
	/**
	 * This method calculates a long value from a stringified date. The method
	 * is primarily used for calculating trends based on time stamps.
	 * 
	 * @return The run date converted to long.
	 */
	public long getRunDateAsLong() {
		return TestItemUtils.runDateToLong(runDate);
	}
	
	/**
	 * Returns the stringified pass percentage.
	 * 
	 * @return The pass percentage as string.
	 */
	public String getPassPct() {
		return TestItemUtils.passPctToString(passPct);
	}
}

/* eof */
