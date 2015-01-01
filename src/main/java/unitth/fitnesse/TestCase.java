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


public class TestCase extends TestItem {
	
	private boolean isGreen = false;
	
	// TestItem.name is the test case name. 
	// This is only for the test suite name.
	private String testSuiteName = "undef";
	private String historyLink = "";
	
	public TestCase() {
		// Nothing to initialize...
	}
	
	public boolean isPassed() {
		return isGreen;
	}
	
	public int getRights() {
		return noRights;
	}

	public void setRights(String rights) {
		this.noRights = Integer.parseInt(rights.trim());
	}

	public int getWrongs() {
		return noWrongs;
	}

	public void setWrongs(String wrongs) {
		this.noWrongs = Integer.parseInt(wrongs.trim());
	}

	public int getIgnores() {
		return noIgnores;
	}

	public void setIgnores(String ignores) {
		this.noIgnores = Integer.parseInt(ignores.trim());
	}

	public int getExceptions() {
		return noExceptions;
	}

	public void setExceptions(String exceptions) {
		this.noExceptions = Integer.parseInt(exceptions.trim());
	}

	public String toString() {
		return "+- TestCase -----------------------------+\n" 
				+ "  Name: "+ testSuiteName+"."+name + "\n" 
				+ "  RunDate: " + runDate + "\n" 
				+ "  History link: " + historyLink + "\n"
				+ "  Pass: " + isGreen + "\n"
				+ "  Assertions: " + getNoAssertions() + "\n"
				+ "  Right: " + noRights + "\n" 
				+ "  Wrong: " + noWrongs + "\n"
				+ "  Ignored: " + noIgnores + "\n" 
				+ "  Exceptions: " + noExceptions + "\n";
	}

	protected void calcStats() {
		if (noExceptions == 0 && noIgnores == 0 && noWrongs == 0 && noRights > 0) {
			isGreen = true;
		}
		noAssertions = noRights + noWrongs + noExceptions;
	}
	
	public String getTestSuiteName() {
		return testSuiteName;
	}
	
	public void setTestSuiteName(String name) {
		testSuiteName = name;
	}
	
	public void setTestCaseName(String name) {
		this.name = name;
	}
	
	public String getQualifiedName() {
		return testSuiteName+"."+name;
	}
	
	public void setNameFromFullName(String name) {
		int idx = name.lastIndexOf(".");
		this.name = name.substring(idx+1);
		this.testSuiteName = name.substring(0, idx);
	}
	
	public void setHistoryLink(String link) {
		historyLink = link;
	}
}
