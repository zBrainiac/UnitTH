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
 * $Id: TestCaseNumberGraphCreator.java,v 1.7 2010/05/20 21:20:19 andnyb Exp $
 * -----------------------------------------------------------------------
 *
 * =======================================================================
 */
package unitth.graphics.junit;

import java.awt.Color;

import unitth.junit.TestHistory;
import unitth.junit.TestModule;
import unitth.junit.TestPackage;
import unitth.junit.TestRun;

public class TestCaseNumberGraphCreator extends NumberGraphCreator {

	public TestCaseNumberGraphCreator(TestHistory history) {
		super(history);
		mainImage = "tc.png";
		filePrefix = "mtc-";
		color = Color.GREEN;
	}

	/* (non-Javadoc)
	 * @see unitth.graphics.NumberGraphCreator#getGraphNumbers(unitth.core.TestRun)
	 */
	protected int getGraphNumbers(TestRun tr) {
		return tr.getNoTestCases();
	}

	/* (non-Javadoc)
	 * @see unitth.graphics.NumberGraphCreator#getGraphNumbers(unitth.core.TestModule)
	 */
	protected int getGraphNumbers(TestModule tm) {
		return tm.getNoTestCases();
	}
	
	/* (non-Javadoc)
	 * @see unitth.graphics.NumberGraphCreator#getGraphNumbers(unitth.core.TestModule)
	 */
	protected int getGraphNumbers(TestPackage tp) {
		return tp.getNoTestCases();
	}
}
