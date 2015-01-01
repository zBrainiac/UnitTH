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
 * =======================================================================
 * $Id: HtmlGen.java,v 1.24 2010/05/14 15:21:39 andnyb Exp $
 * =======================================================================
 */
package unitth.html;

import unitth.core.UnitTH;

public class HtmlGenUtils {
	public static String createArgsDivTag() {
		String ret = "<div class=\"hidden\">\n";
		ret += "Properties" + "\n"
				+ "-+--------------------------------------------------+-\n";
		ret += "unitth.generate.exectimegraphs="
				+ UnitTH.generateExecTimeGraphs + "\n";
		ret += "unitth.html.report.path=" + UnitTH.reportPath + "\n";
		ret += "unitth.report.dir=" + UnitTH.rootFolder + "\n";
		ret += "unitth.xml.report.filter=" + UnitTH.xmlReportFilter + "\n";
		ret += "unitth.use.absolute.paths=" + UnitTH.useAbsPaths + "\n";
		ret += "\nInput arguments" + "\n"
				+ "-+--------------------------------------------------+-\n";
		
		// All input args - if any
		if (UnitTH.inputArgs != null) {
			for (String s : UnitTH.inputArgs) {
				ret += s + "\n";
			}
		}
		ret += "\n</div>";
		return ret;
	}
	
	/**
	 * Calculates the number of zeroes to pad an index with to make all indexes
	 * the same width.
	 * 
	 * @param idx
	 *            The index to base the calculation on.
	 * @return The padded string.
	 */
	public static String calculateRunIdxString(int noRuns, int idx) {
		String noRunsStr = String.valueOf(noRuns);
		String idxStr = String.valueOf(idx);

		int diff = noRunsStr.length() - idxStr.length();
		String retStr = "";
		for (int i = 0; i < diff; i++) {
			retStr += "0";
		}
		return retStr + idxStr;
	}
	
	public static String getTrendImage(String value, boolean isPositive) {
		String trendDownImg = UnitTH.IMAGE_DIR + "/" + UnitTH.TREND_DOWN;
		String trendSameImg = UnitTH.IMAGE_DIR + "/" + UnitTH.TREND_SAME;
		String trendUpImg = UnitTH.IMAGE_DIR + "/" + UnitTH.TREND_UP;
		String trendUpNegImg = UnitTH.IMAGE_DIR + "/" + UnitTH.TREND_UP_NEGATIVE; // Red
		String trendDownNegImg = UnitTH.IMAGE_DIR + "/" + UnitTH.TREND_DOWN_NEGATIVE; // Green

		if (isPositive) { 
			if (value.startsWith("+")) {
				return trendUpImg;
			} else if (value.startsWith("-")) {
				return trendDownImg;
			} else {
				return trendSameImg;
			}
		} else {
			if (value.startsWith("+")) {
				return trendUpNegImg;
			} else if (value.startsWith("-")) {
				return trendDownNegImg;
			} else {
				return trendSameImg;
			}
		}
	}
}
