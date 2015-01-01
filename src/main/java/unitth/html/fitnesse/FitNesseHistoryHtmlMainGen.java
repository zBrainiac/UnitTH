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
package unitth.html.fitnesse;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import unitth.fitnesse.RootTestSuiteSummary;
import unitth.fitnesse.TestCaseSummary;
import unitth.fitnesse.TestHistory;
import unitth.fitnesse.TestSuiteSummary;

public class FitNesseHistoryHtmlMainGen extends FitNesseHistoryHtmlGen {

	public FitNesseHistoryHtmlMainGen(TestHistory history,
			boolean generateExecTimeGraphs) {
		super(history, generateExecTimeGraphs);
	}

	public void generateHtmlHistory() {
		super.generateHtmlHistory();
		generateMainPage();
	}

	protected void generateMainPage() {
		// Create the string to write to the file.
		StringBuffer out = new StringBuffer();
		startPageMain(out, "Test History Main Frame - FitNesse reports");
		startBody(out);
		generateMainAnchors(out);
		out.append("<TABLE width=\"90%\"><TR><TD><!-- START main contents -->" + c_LF);
		generateMainRunSummary(out);
		out.append("<br>" + c_LF);
		generateMainRootTestSuiteInfo(out);
		generateMainRootTestSuiteList(out);

		generateMainTestSuiteListLists(out);
		
		out.append("<br>" + c_LF); 
		out.append("</TD></TR></TABLE><!-- END main contents -->" + c_LF);
		endBody(out);
		endPage(out);

		writeFile(destDir + File.separator + HTML_MAIN_FRAME_FILE, out);

		out = null;
	}

	protected void generateMainTestSuiteListLists(StringBuffer out) {

		out.append("<a name=\"testsuites\"/></a>" + c_LF);
		Collection<RootTestSuiteSummary> c = th.getRootTestSuiteSummaries().values();
		Iterator<RootTestSuiteSummary> iter = c.iterator();
		while (iter.hasNext()) {
			RootTestSuiteSummary rtss = iter.next();
			if (null != rtss) {
				out.append("<br>" + c_LF);
				generateMainTestSuiteListInfo(out, rtss.getName());
				
				// Are there any sub test suites?
				if (th.hasSubTestSuiteSummaries(rtss.getName(), rtss.getName())) {
					generateMainTestSuiteListList(out, rtss.getName());
				}
			}
		}
	}

	protected void generateMainTestSuiteListList(StringBuffer buf,
			String rtsName) {
		
		Collection<TestSuiteSummary> c = th.getTestSuiteSummaries(rtsName).values();
		Iterator<TestSuiteSummary> iter = c.iterator();

		buf
				.append(t(1)
						+ "<TABLE id='testsuite_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
						+ c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateMainTestSuiteListHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);

		while (iter.hasNext()) {
			TestSuiteSummary tss = iter.next();
			if (null != tss) {
				if (tss.getName().contains(rtsName) && !tss.getName().equalsIgnoreCase(rtsName)){
					generateMainTestSuiteListItem(buf, tss);
				}
			}
		}
		
		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}
	
	protected void generateMainTestSuiteTestCaseItem(StringBuffer buf,
			TestCaseSummary tcs, String testSuiteName) {
		generateTestCaseItem(buf, tcs, testSuiteName);
	}
	
	protected void generateMainTestSuiteTestCaseInfo(StringBuffer buf,
			String name) {
		buf.append("<H4 class=\"title\">" + name + "</H4>" + c_LF);
		buf
		.append("Insert slim pass rate image.<br/><br/>"
				+ c_LF);
	}

	protected void generateMainTestSuiteListInfo(StringBuffer buf,
			String name) {
		buf.append("<H4 class=\"title\"> Test suite: " + name + "</H4>" + c_LF);
		buf
		.append("TODO Insert slim pass rate image.<br/><br/>"
				+ c_LF);
	}
	
	private void generateMainAnchors(StringBuffer out) {
		out.append("<TABLE class=\"mainAnchors\" width=\"100%\">" + c_LF);
		out.append(t(1) + "<TR>" + c_LF);
		out.append(t(2) + "<TD>");
		out.append("</TABLE>" + c_LF);
	}

	private void generateMainRunSummary(StringBuffer buf) {
		buf.append(t(1) + "<TABLE class=\"topcontainer\" cellspacing=\"4\">"
				+ c_LF);
		buf.append(t(2) + "<TR>" + c_LF);
		buf.append(t(3) + "<TD valign=\"top\">" + c_LF);
		buf.append(t(4) + "<TABLE class=\"sum\" cellspacing=\"4\">" + c_LF);
		buf
				.append(t(5)
						+ "<TR><TD class=\"sumhdr\" colspan=\"2\">Test&nbsp;history&nbsp;overview</TD></TR>"
						+ c_LF);

		buf
				.append(t(5)
						+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;test&nbsp;suites&nbsp;</TD><TD class=\"summid\"> "
						+ th.getNoTestSuites() + " </TD></TR>" + c_LF);
		buf
				.append(t(5)
						+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;test&nbsp;cases&nbsp;</TD><TD> "
						+ th.getNoUniqueTestCases() + " </TD></TR>" + c_LF);
		buf
				.append(t(5)
						+ "<TR><TD class=\"sumbottom\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD class=\"sumbottom\"><b>"
						+ th.getAvePassPct() + "% </b></TD></TR>" + c_LF);
		buf.append(t(4) + "</TABLE>" + c_LF);
		buf.append(t(3) + "</TD>" + c_LF);
		buf.append(t(2) + "</TR>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	private void generateMainRootTestSuiteInfo(StringBuffer buf) {
		buf
				.append("<a name=\"testsuites\"/><H3 class=\"title\">Root test suites</H3>"
						+ c_LF);
		buf.append("This list shows the summary of all executed root test suites. A root test suite is a suite that has been specificly executed with FitNesse equivalent to clicking the \"Suite\" button in the FitNesse GUI. A root test suite can include any number of sub test suites. <b>As</b> = Asserts, <b>Ri</b> = Rights, <b>Wr</b> = Wrongs, <b>Ig</b> = Ignores, <b>Ex</b> = Exceptions<br/><br/>" + c_LF);
	}

	private void generateMainRootTestSuiteList(StringBuffer buf) {
		buf
				.append(t(1)
						+ "<TABLE id='root_testsuite_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
						+ c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateMainRootTestSuiteHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);
		generateMainRootTestSuiteItems(buf);
		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	private void generateMainRootTestSuiteHeader(StringBuffer buf) {

		buf.append(t(3) + "<TR>" + c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Test&nbsp;suite&nbsp;name</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Tests&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Exec.&nbsp;Tests&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">As&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ri&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Wr&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ig&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ex&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
						+ c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	
	private void generateMainTestSuiteListHeader(StringBuffer buf) {

		buf.append(t(3) + "<TR>" + c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Test&nbsp;suite&nbsp;name</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Tests&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Exec.&nbsp;Tests&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">As&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ri&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Wr&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ig&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Ex&nbsp;&nbsp;</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
						+ c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}
	
	/**
	 * Running through all root test suites.
	 * @param buf, The HTML buffer.
	 */
	private void generateMainRootTestSuiteItems(StringBuffer buf) {

		Collection<RootTestSuiteSummary> c = th.getRootTestSuiteSummaries().values();
		Iterator<RootTestSuiteSummary> iter = c.iterator();
		while (iter.hasNext()) {
			RootTestSuiteSummary rtss = iter.next();
			if (null != rtss) {
				generateMainRootTestSuiteListItem(buf, rtss);
			}
		}
	}

	private void generateMainRootTestSuiteListItem(StringBuffer buf,
			RootTestSuiteSummary rtss) {

		// Create the link to the module run page
		String clickableLinkFileName = HTMLTESTSUITEOUTPUTFILE_PREFIX
				+ rtss.getName() + HTMLTESTSUITEOUTPUTFILE_SUFFIX;
		String link = "<a href=\"" + clickableLinkFileName + "\">"
				+ rtss.getName() + "</a>";

		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link
				+ "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoRuns() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoUniqueTestCases() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoExecuted() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoPassed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoFailed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoAssertions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoRights() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoWrongs() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoIgnores() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ rtss.getNoExceptions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
				+ rtss.getPassPct() + "%</TD>" + c_LF);

		buf.append(t(4) + "<TD class=\"graphBar\">" + c_LF);
		buf
				.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">"
						+ c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR class=\"bar\">" + c_LF);
		generatePctBar(buf, rtss.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}
	
	private void generateMainTestSuiteListItem(StringBuffer buf,
			TestSuiteSummary tss) {

		// Create the link to the module run page
		String clickableLinkFileName = HTMLTESTSUITEOUTPUTFILE_PREFIX
				+ tss.getName() + HTMLTESTSUITEOUTPUTFILE_SUFFIX;
		String link = "<a href=\"" + clickableLinkFileName + "\">"
				+ tss.getName() + "</a>";

		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link
				+ "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoRuns() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoUniqueTestCases() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoExecuted() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoPassed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoFailed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoAssertions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoRights() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoWrongs() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoIgnores() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoExceptions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
				+ tss.getPassPct() + "%</TD>" + c_LF);

		buf.append(t(4) + "<TD class=\"graphBar\">" + c_LF);
		buf
				.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">"
						+ c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR class=\"bar\">" + c_LF);
		generatePctBar(buf, tss.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}
}
