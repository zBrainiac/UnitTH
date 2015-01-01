/*
 *    This file is part of UnitTH.
 *
 *   UnitTH is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   JUnitTH is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with UnitTH. If not, see <http://www.gnu.org/licenses/>.
 *
 * =======================================================================
 * $Id: UnitTHException.java,v 1.3 2010/05/14 15:21:39 andnyb Exp $
 * -----------------------------------------------------------------------
 * 
 * =======================================================================
 */

package unitth.core;

/**
 * This is the exception class for JUnitTH specific exceptions.
 * 
 * @author andnyb
 */
public class UnitTHException extends Exception {

	static final long serialVersionUID = 3L;

	String msg = "undef";

	/**
	 * Ctor for the UnitTH exception, takes a message as in parameter.
	 * 
	 * @param msg
	 *            An message for this exception.
	 */
	// FIXME, there needs to be support for handling multiple messages. 
	public UnitTHException(String... msg) {
		if (null != msg[0]) {
			this.msg = msg[0];
		}
	}
	
	public String getMessage() {
		return msg;
	}
}

/* eof */
