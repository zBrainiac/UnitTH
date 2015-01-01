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
package unitth.core;

public abstract class RunHistory {

	// Abstract methods
	public abstract void calcStats();
	//public abstract int getLargestNumberOfTcs(String s);
	//public abstract double getLargestExecutionTime(String s);
	
	// To be overridden
	//public TreeSet<unitth.junit.TestRun> getJUnitRuns() { /* left void */ return null; }
	//public TreeSet<unitth.fitnesse.TestRun> getFitNesseRuns(String testSuiteName) { /* left void */ return null; }
}
