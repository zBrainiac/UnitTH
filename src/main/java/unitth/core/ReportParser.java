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
 */
package unitth.core;

import java.io.File;
import java.util.ArrayList;

import org.xml.sax.helpers.DefaultHandler;

import unitth.junit.TestHistory;

public abstract class ReportParser extends DefaultHandler {

	public abstract TestHistory parseRuns(ArrayList<ArrayList<File>> testRunFiles,
			ArrayList<String> dirs);
}
