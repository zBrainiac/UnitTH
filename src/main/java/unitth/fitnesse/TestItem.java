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

public class TestItem {
	
	protected String name = "undef";
	protected String runDate = "NaN";
	
	protected int noAssertions = 0;
	protected int noRights = 0;
	protected int noWrongs = 0;
	protected int noIgnores = 0;
	protected int noExceptions = 0;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRunDate(String date) {
		runDate = TestItemUtils.fixFitNesseDateFormat(date);
	}
	
	public String getRunDate() {
		return runDate;
	}
	
	public String getName() {
		if (name == null) {
			return "module-not-defined";
		}

		return name;
	}

	public int getNoAssertions() {
		return noAssertions;
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
	
	public void setNoAssertions(int noAssertions) {
		this.noAssertions = noAssertions;
	}
	
	public long getRunDateAsLong() {
		return TestItemUtils.runDateToLong(runDate);
	}
}
