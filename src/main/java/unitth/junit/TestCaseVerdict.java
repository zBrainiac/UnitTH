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
 * $Id: TestCaseVerdict.java,v 1.2 2010/01/03 23:32:40 andnyb Exp $
 * -----------------------------------------------------------------------
 * 
 * =======================================================================
 */

package unitth.junit;

/**
 * This is the enum that represents the different final states for a
 * <code>TestCase</code> object.
 * 
 * @author andnyb
 */
public enum TestCaseVerdict {
	e_PASS, e_FAIL, e_ERROR, e_NORUN, e_IGNORED;

	/**
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		switch (this) {
		case e_PASS:
			return "PASS";
		case e_FAIL:
			return "FAIL";
		case e_ERROR:
			return "ERROR";
		case e_IGNORED:
			return "IGNORED";
		default:
			return "NORUN";
		}
	}
}
