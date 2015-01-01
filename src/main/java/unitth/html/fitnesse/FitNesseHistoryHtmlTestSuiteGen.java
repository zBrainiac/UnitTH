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
import java.util.TreeMap;

import unitth.core.UnitTH;
import unitth.fitnesse.RootTestSuite;
import unitth.fitnesse.TestCaseSummary;
import unitth.fitnesse.TestHistory;
import unitth.fitnesse.TestRun;
import unitth.fitnesse.TestSuite;
import unitth.fitnesse.TestSuiteSummary;
import unitth.html.HtmlGenUtils;

public class FitNesseHistoryHtmlTestSuiteGen extends FitNesseHistoryHtmlGen {

	public FitNesseHistoryHtmlTestSuiteGen(TestHistory history,
			boolean generateExecTimeGraphs) {
		super(history, generateExecTimeGraphs);
	}

	public void generateHtmlHistory(String rootTestSuiteName) {
		super.generateHtmlHistory();

		Object[] testSuiteNames = th.getUniqueTestSuites();

		for (Object testSuiteName : testSuiteNames) {
			testSuiteName = ((String)testSuiteName).substring(((String)testSuiteName).indexOf(":")+1);
			if (((String) testSuiteName).contains(rootTestSuiteName)) {
				generateTestSuitePage(rootTestSuiteName, (String) testSuiteName);
			}
		}
	}

	protected void generateTestSuitePage(String rootTestSuiteName,
			String testSuiteName) {
		
		TestSuiteSummary tss = th.getTestSuiteSummary(rootTestSuiteName, testSuiteName); 
		if (tss != null) {
			// Create the string to write to the file.
			StringBuffer out = new StringBuffer();
			startPageTestSuite(out, "UnitTH - FitNesse Test Suite - "
					+ testSuiteName);
			startBody(out);
			generateTestSuiteAnchors(out, rootTestSuiteName, testSuiteName);
			out.append("<TABLE width=\"90%\"><TR><TD>" + c_LF);
			generateTestSuiteRunSummary(out, tss);
			out.append("<br>" + c_LF);
			generateTestSuiteTestRunInfo(out);
			generateTestSuiteTestRunList(out, rootTestSuiteName, testSuiteName);
			out.append("<br>" + c_LF);
	
			if (th.hasSubTestSuiteSummaries(rootTestSuiteName, testSuiteName) == true) {
				generateTestSuiteTestSuiteInfo(out);
				generateTestSuiteTestSuiteList(out, rootTestSuiteName,
						testSuiteName);
				out.append("<br>" + c_LF);
			}
	
			generateTestSuiteTestCaseInfo(out);
			generateTestSuiteTestCaseList(out, rootTestSuiteName, testSuiteName);
			out.append("<br>" + c_LF);
			generateTestSuiteTestCaseSpreadInfo(out);
			generateTestSuiteTestCaseSpreadList(out, rootTestSuiteName,
					testSuiteName);
			out.append("<br>" + c_LF);
			generateTestSuiteTestSuiteTotals(out, tss);
			out.append("</TD></TR></TABLE>" + c_LF);
			endBody(out);
			endPage(out);
	
			writeFile(destDir + File.separator + HTMLTESTSUITEOUTPUTFILE_PREFIX
					+ testSuiteName + HTMLTESTSUITEOUTPUTFILE_SUFFIX, out);
			out = null;
		}
	}

	private void generateTestSuiteTestRunList(StringBuffer buf,
			String rootTestSuiteName, String testSuiteName) {
		RootTestSuite rts = th.getRootTestSuite(rootTestSuiteName);

		if (rts == null) {
			return;
		}
		buf.append(t(1)
				+ "<TABLE id=\"run_table\" cellspacing=\"0\" cellpadding=\"2\">"
				+ c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateTestSuiteTestRunHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);

		if (null != rts) {
			int idx = rts.getTestRuns().size();
			for (TestRun tr : rts.getTestRuns()) {
				if (null != tr) {
					TestSuite ts = tr.getTestSuite(testSuiteName);

					// Only print rows where the suite has been included in the
					// run.
					if (null != ts) {
						generateTestSuiteTestRunItem(buf, idx, ts);
					}
				}
				idx--;
			}
		}

		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	private void generateTestSuiteTestRunHeader(StringBuffer buf) {

		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Run</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Tests&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">As&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ri&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Wr&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ig&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ex&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" NOWRAP abbr=\"date\">Exec&nbsp;date</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"float\">Pass&nbsp;%&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
				+ c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	private void generateTestSuiteTestRunItem(StringBuffer buf, int idx,
			TestSuite ts) {

		String idxStr = HtmlGenUtils.calculateRunIdxString(th.getNoRuns(), idx);
		String runName = "Run-" + idxStr;

		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + runName
				+ "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ ts.getNoTestCases() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ ts.getNoPassed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ ts.getNoFailed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ ts.getNoAssertions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ ts.getNoRights() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
				+ ts.getNoWrongs() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
				+ ts.getNoIgnores() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
				+ ts.getNoExceptions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
				+ ts.getRunDate() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
				+ ts.getPassPct() + "%</TD>" + c_LF);

		buf.append(t(4) + "<TD class=\"graphBar\">" + c_LF);
		buf.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR>" + c_LF);
		generatePctBar(buf, ts.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	private void generateTestSuiteTestRunInfo(StringBuffer buf) {
		buf.append("<a name=\"testruns\"/><H3 class=\"title\">Test suite runs</H3>"
				+ c_LF);
		buf.append("This list shows the test suite runs.  <b>As</b> = Asserts, <b>Ri</b> = Rights, <b>Wr</b> = Wrongs, <b>Ig</b> = Ignores, <b>Ex</b> = Exceptions<br/><br/>"
				+ c_LF);
	}

	private void generateTestSuiteTestCaseInfo(StringBuffer buf) {
		buf.append("<a name=\"testcases\"/><H3 class=\"title\">Test cases</H3>"
				+ c_LF);
		buf.append("This list shows the summary of all executed test cases. <b>As</b> = Asserts, <b>Ri</b> = Rights, <b>Wr</b> = Wrongs, <b>Ig</b> = Ignores, <b>Ex</b> = Exceptions<br/><br/>"
				+ c_LF);
	}

	private void generateTestSuiteTestCaseSpreadInfo(StringBuffer buf) {
		buf.append("<a name=\"spread\"/><H3 class=\"title\">Test case spread</H3>"
				+ c_LF);
		buf.append("This list shows the pass rate spread for all executed test cases. <br/><br/>"
				+ c_LF);
	}

	private void generateTestSuiteAnchors(StringBuffer out, String rtsName,
			String testSuiteName) {
		out.append("<TABLE class=\"mainAnchors\" width=\"100%\">" + c_LF);
		out.append(t(1) + "<TR>" + c_LF);
		out.append(t(2) + "<TD>");
		out.append("<CENTER><a href=\"#testruns\">RUNS</a>");
		if (th.hasSubTestSuiteSummaries(rtsName, testSuiteName) == true) {
			out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#testsuites\">TEST SUITES</a>");
		}
		out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#testcases\">TEST CASES (ALL)</a>");
		out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#spread\">SPREAD (ALL)</a>");
		out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#testcaseslocal\">TEST CASES (LOCAL)</a>");

		out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#spreadlocal\">SPREAD (LOCAL)</a>");
		out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#totals\">TOTALS</a></CENTER>");
		out.append("</TD>" + c_LF);
		out.append(t(1) + "</TR>" + c_LF);
		out.append("</TABLE>" + c_LF);
	}

	private void generateTestSuiteRunSummary(StringBuffer buf,
			TestSuiteSummary tss) {
		buf.append("<H1 class=\"title\">TEST SUITE</H1>" + c_LF);
		buf.append("<H2 class=\"title\">" + tss.getName() + "</H2>" + c_LF);
		buf.append(t(1) + "<TABLE class=\"topcontainer\" cellspacing=\"4\">"
				+ c_LF);
		generateSummarySection(buf, tss);
		generateImageSection(buf, tss);
		generateTrendSection(buf, tss);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	protected void generateSummarySection(StringBuffer buf, TestSuiteSummary tss) {
		buf.append(t(2) + "<TR>" + c_LF);
		buf.append(t(3) + "<TD valign=\"top\">" + c_LF);
		buf.append(t(4) + "<TABLE id=\"summary\" class=\"sum\" cellspacing=\"4\">" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"sumhdr\" colspan=\"2\">Test&nbsp;history&nbsp;overview</TD></TR>"
				+ c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;runs&nbsp;</TD><TD class=\"summid\"> "
				+ tss.getNoRuns() + " </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;sub&nbsp;test&nbsp;suites&nbsp;</TD><TD class=\"summid\"> "
				+ tss.getNoSubTestSuites() + " </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;test&nbsp;cases&nbsp;</TD><TD> "
				+ tss.getNoUniqueTestCases() + " </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD>"
				+ tss.getPassPct() + "% </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Best&nbsp;run&nbsp;</TD><TD>"
				+ tss.getBestRun() + "% </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Worst&nbsp;run&nbsp;</TD><TD>"
				+ tss.getWorstRun() + "% </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD>"
				+ tss.getPassPct() + "% </TD></TR>" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"sumbottom\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD class=\"sumbottom\"><b>"
				+ tss.getAveragePassRate() + "% </b></TD></TR>" + c_LF);
		buf.append(t(4) + "</TABLE>" + c_LF);
		buf.append(t(3) + "</TD>" + c_LF);
		buf.append(t(2) + "</TR>" + c_LF);
	}

	protected void generateImageSection(StringBuffer buf, TestSuiteSummary tss) {
		// TODO, implement image section
	}

	protected void generateTrendSection(StringBuffer buf, TestSuiteSummary tss) {
		String selectedTrend = "";
		buf.append(t(2) + "<TR>" + c_LF);
		buf.append(t(3) + "<TD valign=\"top\">" + c_LF);

		buf.append(t(4) + "<TABLE id=\"trends\" class=\"sum\">" + c_LF);
		buf.append(t(5)
				+ "<TR><TD class=\"sumhdr\" colspan=\"3\">Trends</TD></TR>"
				+ c_LF);

		String trendOutput = tss.getPrTrendLastRun();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;run&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getPrTrendLast5Runs();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;5&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getPrTrendLast10Runs();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;10&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getPrTrendLast3Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getPrTrendLast7Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getTcTrendLast3Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"summid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getTcTrendLast7Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, true);
		buf.append(t(5)
				+ "<TR><TD class=\"sumid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getFnTrendLast3Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, false);
		buf.append(t(5)
				+ "<TR><TD class=\"sumid\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
				+ trendOutput + "</TD><TD align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);

		trendOutput = tss.getFnTrendLast7Days();
		selectedTrend = HtmlGenUtils.getTrendImage(trendOutput, false);
		buf.append(t(5)
				+ "<TR><TD class=\"sumbottom\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrendBottom\">"
				+ trendOutput
				+ "</TD><TD class=\"prTrendBottom\" align=\"right\" valign=\"middle\">"
				+ "<IMG src=\"" + selectedTrend + "\" height=\""
				+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
				+ "\">" + "</TD></TR>" + c_LF);
		buf.append(t(4) + "</TABLE>" + c_LF);
		buf.append(t(3) + "</TD>" + c_LF);
		buf.append(t(2) + "</TR>" + c_LF);
	}

	protected void generateTestSuiteTestCaseList(StringBuffer buf,
			String rtsName, String testSuiteName) {
		Collection<TestCaseSummary> c = th.getTestCaseSummaries(rtsName)
				.values();
		Iterator<TestCaseSummary> iter = c.iterator();

		buf.append(t(1)
				+ "<TABLE id='testcase_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
				+ c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateTestSuiteTestCaseHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);

		while (iter.hasNext()) {
			TestCaseSummary tcs = iter.next();

			if (tcs.getTestSuiteName().contains(testSuiteName)) {
				generateTestSuiteTestCaseItem(buf, tcs, testSuiteName);
			}
		}

		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);

	}

	protected void generateTestSuiteTestCaseSpreadList(StringBuffer buf,
			String rootTestSuiteName, String testSuiteName) {
		
		TreeMap<String, TestCaseSummary> tcSmries = th
				.getTestCaseSummaries(rootTestSuiteName);
	
		if (tcSmries != null) {
			Collection<TestCaseSummary> c = tcSmries.values();
			Iterator<TestCaseSummary> iter = c.iterator();

			buf.append(t(1)
					+ "<TABLE id='spread_table' cellspacing=\"0\" cellpadding=\"2\">"
					+ c_LF);
			buf.append(t(2) + "<THEAD>" + c_LF);
			generateTestCaseSpreadHeader(buf);
			buf.append(t(2) + "</THEAD>" + c_LF);
			buf.append(t(2) + "<TBODY>" + c_LF);

			while (iter.hasNext()) {
				TestCaseSummary tcs = iter.next();

				if (tcs.getTestSuiteName().contains(testSuiteName)) {
					generateTestCaseSpreadItem(buf, tcs,
							th.getRootTestSuite(rootTestSuiteName)
									.getTestRuns().size(), testSuiteName);
				}
			}

			buf.append(t(2) + "</TBODY>" + c_LF);
			buf.append(t(1) + "</TABLE>" + c_LF);
		}
	}

	protected void generateTestSuiteTestCaseHeader(StringBuffer buf) {
		generateTestCaseHeader(buf);
	}

	protected void generateTestSuiteTestCaseItem(StringBuffer buf,
			TestCaseSummary tcs, String testSuiteName) {
		generateTestCaseItem(buf, tcs, testSuiteName);
	}

	private void generateTestSuiteTestSuiteTotals(StringBuffer buf,
			TestSuiteSummary tss) {
		buf.append("<a name=\"totals\"/><H3 class=\"title\">Test suite execution totals</H3>"
				+ c_LF);
		buf.append("This is a summary of all executed test in all runs and all test suites.<br><br>"
				+ c_LF);
		buf.append(t(1) + "<TABLE id=\"testsuite_totals\" cellspacing=\"0\" cellpadding=\"2\">" + c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateTestCaseHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);
		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>TOTALS</TD>"
				+ c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tss.getNoRuns() + "</TD>" + c_LF);
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
		buf.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR class=\"bar\">" + c_LF);
		generatePctBar(buf, tss.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	protected void generateTestSuiteTestSuiteInfo(StringBuffer buf) {
		buf.append("<a name=\"testsuites\"/><H3 class=\"title\">Test suites</H3>"
				+ c_LF);
		buf.append("This list shows all the sub test suites for this test suite. All the numbers are calculated based on sub test suites and tests. <br/><br/>"
				+ c_LF);
	}

	protected void generateTestSuiteTestSuiteList(StringBuffer buf,
			String rtsName, String testSuiteName) {

		Collection<TestSuiteSummary> c = th.getTestSuiteSummaries(rtsName)
				.values();
		Iterator<TestSuiteSummary> iter = c.iterator();

		buf.append(t(1)
				+ "<TABLE id='testsuite_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
				+ c_LF);
		buf.append(t(2) + "<THEAD>" + c_LF);
		generateTestSuiteTestSuiteListHeader(buf);
		buf.append(t(2) + "</THEAD>" + c_LF);
		buf.append(t(2) + "<TBODY>" + c_LF);

		while (iter.hasNext()) {
			TestSuiteSummary tss = iter.next();
			if (null != tss) {
				if (tss.getName().contains(testSuiteName)
						&& !tss.getName().equalsIgnoreCase(testSuiteName)) {
					generateTestSuiteTestSuiteListItem(buf, tss);
				}
			}
		}

		buf.append(t(2) + "</TBODY>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	private void generateTestSuiteTestSuiteListHeader(StringBuffer buf) {

		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Test&nbsp;suite&nbsp;name</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Tests&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Exec.&nbsp;Tests&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">As&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ri&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Wr&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ig&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" abbr=\"number\">Ex&nbsp;&nbsp;</TH>"
				+ c_LF);
		buf.append(t(4)
				+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
				+ c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	private void generateTestSuiteTestSuiteListItem(StringBuffer buf,
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
		buf.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR class=\"bar\">" + c_LF);
		generatePctBar(buf, tss.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	protected void startPageTestSuite(StringBuffer buf, String pageTitle) {
		buf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ c_LF);
		buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" >" + c_LF);
		buf.append("<head>" + c_LF);
		buf.append(t(1)
				+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ c_LF);
		buf.append(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
		buf.append(t(1)
				+ "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"css/style.css\" TITLE=\"Style\"/>"
				+ c_LF);

		//
		// JavaScript component where the tables to be sorted are listed.
		//
		buf.append(t(1) + "<script src=\"" + UnitTH.JS_DIR + "/"
				+ UnitTH.SORT_SCRIPT + "\" type=\"text/javascript\"></script>"
				+ c_LF);
		buf.append(t(1) + "<script type=\"text/javascript\">" + c_LF);
		buf.append(t(2) + "function sortTables() {" + c_LF);
		buf.append(t(3) + "var runTable = new TableSort;" + c_LF);
		buf.append(t(3) + "var testSuiteTable = new TableSort;" + c_LF);
		buf.append(t(3) + "var testCaseTable = new TableSort;" + c_LF);
		buf.append(t(3) + "var spreadTable = new TableSort;" + c_LF);
		buf.append(t(3) + "runTable.init(\"run_table\");" + c_LF);
		buf.append(t(3) + "testSuiteTable.init(\"testsuite_table\");" + c_LF);
		buf.append(t(3) + "testCaseTable.init(\"testcase_table\");" + c_LF);
		buf.append(t(3) + "spreadTable.init(\"spread_table\");" + c_LF);
		buf.append(t(2) + "}" + c_LF);
		buf.append(t(2) + "window.onload = sortTables;" + c_LF);
		buf.append(t(1) + "</script>" + c_LF);

		buf.append("</head>" + c_LF);
	}
}
