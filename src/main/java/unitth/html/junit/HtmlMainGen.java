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
 *   along with UnitTH and if not, see <http://www.gnu.org/licenses/>.
 *
 * =======================================================================
 * $Id: HtmlMainGen.java,v 1.11 2010/05/14 15:21:39 andnyb Exp $
 * -----------------------------------------------------------------------
 * =======================================================================
 */
package unitth.html.junit;

import unitth.core.UnitTH;
import unitth.html.HtmlGenUtils;
import unitth.junit.TestHistory;
import unitth.junit.TestModuleSummary;
import unitth.junit.TestPackageSummary;
import unitth.junit.TestRun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class is responsible for generation of the HTML report pages. This class
 * specifically generates the main page contents.
 * 
 * @author Andreas Nyberg
 */
public class HtmlMainGen extends HtmlGen {

    /**
     * CTOR, sets the history to generate and sets a few of the fixed paths.
     * 
     * @param history
     */
    public HtmlMainGen(TestHistory history, boolean generateExecTimeGraphs) {
	super(history, generateExecTimeGraphs);
    }

    /**
     * The main entry point for the HTML generation.
     */
    public void generateHtmlHistory() {
	super.generateHtmlHistory();
	generateMainPage();
    }

    /**
     * Generates the main history page.
     */
    protected void generateMainPage() {
	String fileName = destDir + File.separator + HTML_MAIN_FRAME_FILE;
	try {
	    // Create the file to which to write
	    BufferedWriter bwout = createFile(fileName);
	    startPageMain(bwout, "Test History Main Frame");
	    startBody(bwout);
	    generateMainAnchors(bwout);
	    bwout.write("<TABLE width=\"90%\"><TR><TD>" + c_LF);
	    generateMainRunSummary(bwout);
	    bwout.write("<br>" + c_LF);
/**        generateMainRunInfo(bwout);  */
/**     generateMainRunList(bwout);  */
/**     bwout.write("<br>" + c_LF);  */
		generateMainPackageInfo(bwout);
	    generateMainPackageList(bwout);
	    bwout.write("<br>" + c_LF);
	    generateMainModuleInfo(bwout);
	    generateMainModuleList(bwout);
	    bwout.write("<br>" + c_LF);
	    generateMainModuleTotals(bwout);
	    bwout.write("</TD></TR></TABLE>" + c_LF);
	    endBody(bwout);
	    endPage(bwout);
	} catch (IOException e) {
	    System.out
		    .println("An error occurred while writing to the output to file: "
			    + fileName
			    + " Possible fix: check file system permissions."
			    + " Possible fix: check available disk space.");
	    e.printStackTrace();
	}
    }

    /**
     * Generates a link bar on the main page to make it possible to navigate to
     * the correct part on the page.
     * 
     * @param out
     *            The buffer to append to.
     */
    private void generateMainAnchors(BufferedWriter out) throws IOException {
	out.write("<TABLE class=\"mainAnchors\" width=\"100%\">" + c_LF);
	out.write(t(1) + "<TR>" + c_LF);
	out.write(t(2) + "<TD>");
	out.write("<CENTER><a href=\"#runs\">RUNS</a>");
	out.write("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#packages\">PACKAGES</a>");
	out.write("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#modules\">MODULES</a>");
	out.write("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#totals\">TOTALS</a></CENTER>");
	out.write("</TD>" + c_LF);
	out.write(t(1) + "</TR>" + c_LF);
	out.write("</TABLE>" + c_LF);
    }

    /*
     * Generates the main page run summary.
     * 
     * @param buf The buffer to append the page contents to.
     */
    private void generateMainRunSummary(BufferedWriter buf) throws IOException {
	buf.write(t(1) + "<TABLE class=\"topcontainer\" cellspacing=\"4\">"
		+ c_LF);
	buf.write(t(2) + "<TR>" + c_LF);
	buf.write(t(3) + "<TD valign=\"top\">" + c_LF);
	buf.write(t(4)
		+ "<TABLE id=\"summary\" class=\"sum\" cellspacing=\"4\">"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" colspan=\"2\">Test&nbsp;history&nbsp;overview</TD></TR>"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;runs&nbsp;</TD><TD id=\"mainSmryRuns\" class=\"summid\">"
		+ th.getNoRuns() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Last&nbsp;run&nbsp;</TD><TD id=\"mainSmryLastRun\" class=\"summid\"> "
		+ th.getLastRun() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;packages&nbsp;</TD><TD id=\"mainSmryNoPks\" class=\"summid\"> "
		+ th.getNoPackages() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;unique&nbsp;test&nbsp;modules/classes&nbsp;</TD><TD id=\"mainSmryNoTms\" class=\"summid\"> "
		+ th.getNoUniqueModules() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;unique&nbsp;test&nbsp;cases&nbsp;</TD><TD id=\"mainSmryNoTcs\"> "
		+ th.getNoUniqueTestCases() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;ignored&nbsp;test&nbsp;cases&nbsp;</TD><TD id=\"mainSmryNoIgnored\"> "
		+ th.getNoIgnoredTestCases() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Best&nbsp;run&nbsp;</TD><TD id=\"mainSmryBest\" class=\"summid\"> "
		+ th.getBestRun() + "% </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Worst&nbsp;run&nbsp;</TD><TD id=\"mainSmryWorst\" class=\"summid\"> "
		+ th.getWorstRun() + "% </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumbottom\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD id=\"mainSmryAve\" class=\"sumbottom\"><b>"
		+ th.getAvePassPct() + "% </b></TD></TR>" + c_LF);
	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);

	//
	// Images
	//
	buf.write(t(3) + "<TD VALIGN=\"top\" rowspan=\"2\">" + c_LF);
	buf.write(t(4) + "<TABLE class=\"imageholder\" cellspacing=\"4\">"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD valign=\"top\" class=\"sumhdr\" align=\"center\">Pass&nbsp;rates&nbsp;-&nbsp;all&nbsp;runs</TD></TR>"
		+ c_LF);
	buf.write(t(5) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(6) + "<TR><TD><img src=\"./images/"
		+ UnitTH.TEST_HISTORY_GRAPH_IMAGE_NAME + "\"/></TD></TR>"
		+ c_LF);

	buf.write(t(5) + "</TABLE></TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" valign=\"top\" align=\"center\">Number&nbsp;of&nbsp;test&nbsp;cases</TD></TR>"
		+ c_LF);
	buf.write(t(5) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(6) + "<TR><TD><img src=\"./images/"
		+ UnitTH.TEST_CASE_NUMBERS_GRAPH_IMAGE_NAME + "\"/></TD></TR>"
		+ c_LF);
	buf.write(t(5) + "</TABLE></TD></TR>" + c_LF);

	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);

	buf.write(t(3) + "<TD VALIGN=\"top\" rowspan=\"2\">" + c_LF);
	buf.write(t(4) + "<TABLE class=\"imageholder\" cellspacing=\"4\">"
		+ c_LF);

	// Only draw the execution time graph if the flag is set in the unitth
	// properties
	if (generateExecTimeGraphs) {
	    buf.write(t(5)
		    + "<TR><TD valign=\"top\" class=\"sumhdr\" align=\"center\">Execution&nbsp;times&nbsp;("
		    + getExecutionTimeUnit(null)
		    + ")&nbsp;-&nbsp;all&nbsp;runs</TD></TR>" + c_LF);
	    buf.write(t(5) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	    buf.write(t(6) + "<TR><TD><img src=\"./images/"
		    + UnitTH.EXEC_TIME_NUMBERS_GRAPH_IMAGE_NAME
		    + "\"/></TD></TR>" + c_LF);
	    buf.write(t(5) + "</TABLE></TD></TR>" + c_LF);
	}

	buf.write(t(5)
		+ "<TR><TD valign=\"top\" class=\"sumhdr\" align=\"center\">Non&nbsp;passing&nbsp;test&nbsp;cases</TD></TR>"
		+ c_LF);

	buf.write(t(6) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(7) + "<TR><TD><img src=\"./images/"
		+ UnitTH.EXEC_FAILURE_NUMBERS_GRAPH_IMAGE_NAME
		+ "\"/></TD></TR>" + c_LF);
	buf.write(t(6) + "</TABLE></TD></TR>" + c_LF);

	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);
	buf.write(t(2) + "</TR>" + c_LF);

	//
	// Trends
	//
	String selectedTrend = "";

	buf.write(t(2) + "<TR>" + c_LF);
	buf.write(t(3) + "<TD valign=\"top\">" + c_LF);

	buf.write(t(4) + "<TABLE id=\"trends\" class=\"sum\">" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" colspan=\"3\">Trends</TD></TR>"
		+ c_LF);

	selectedTrend = HtmlGenUtils
		.getTrendImage(th.getPrTrendLastRun(), true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;run&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getPrTrendLastRun()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getPrTrendLast5Runs(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;5&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getPrTrendLast5Runs()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getPrTrendLast10Runs(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;10&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getPrTrendLast10Runs()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getPrTrendLast3Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getPrTrendLast3Days()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getPrTrendLast7Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getPrTrendLast7Days()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getTcTrendLast3Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getTcTrendLast3Days()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getTcTrendLast7Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"sumid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getTcTrendLast7Days()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getFnTrendLast3Days(),
		false);
	buf.write(t(5)
		+ "<TR><TD class=\"sumid\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ th.getFnTrendLast3Days()
		+ "</TD><TD align=\"right\" valign=\"middle\">" + "<IMG src=\""
		+ selectedTrend + "\" height=\"" + UnitTH.ICON_HEIGHT
		+ "\" width=\"" + UnitTH.ICON_WIDTH + "\">" + "</TD></TR>"
		+ c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(th.getFnTrendLast7Days(),
		false);
	buf.write(t(5)
		+ "<TR><TD class=\"sumbottom\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrendBottom\">"
		+ th.getFnTrendLast7Days()
		+ "</TD><TD class=\"prTrendBottom\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);
	buf.write(t(2) + "</TR>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the information to be placed between the summary and the list
     * of executed runs.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainRunInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"runs\"/><H3 class=\"title\">Runs</H3>" + c_LF);
	buf.write("For individual runs and if regular HTML test reports have been generated click on the run for it to open."
		+ c_LF);
	buf.write("<br><br>" + c_LF);
    }

    /*
     * Generates the information to be placed between the list of packages and
     * the list of executed modules.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainModuleInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"modules\"/><H3 class=\"title\">Test modules</H3>"
		+ c_LF);
	buf.write("This list shows the average pass rate for test objects/modules."
		+ " For individual module executions click on the module name.<br/><br/>"
		+ c_LF);
    }

    /*
     * Generates the information to be placed between the list of runs and the
     * list of executed packages.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainPackageInfo(BufferedWriter buf) throws IOException {
        buf.write("<a name=\"packages\"/><H3 class=\"title\">Test packages</H3>" + c_LF);
        buf.write("This list shows the statistics and average pass rate for test packages. The stats are recursive including statistics from sub packages."
                + " For individual package executions click on the package name. Only test cases that have been executed are counted, ignored test cases and classes are not counted.<br/><br/>"
                + c_LF);
    }

    /*
     * Generates the list of runs on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainRunList(BufferedWriter buf) throws IOException {
	buf.write(t(1)
		+ "<TABLE id='run_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateMainRunHeader(buf, true);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generateMainRunItems(buf);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the header for the list of runs on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainRunHeader(BufferedWriter buf, boolean isMainRunList)
	    throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	if (isMainRunList) {
	    buf.write(t(4)
		    + "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Run</TH>"
		    + c_LF);
	} else {
	    buf.write(t(4)
		    + "<TH class=\"graphHeaderLeft\" abbr=\"input_text\">Run</TH>"
		    + c_LF);
	}
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"input_text\">Path</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Tests&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Error&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Ignored&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"date\" NOWRAP>Exec&nbsp;date</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"float\">Exec&nbsp;time&nbsp;(sec)&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * Generates all the run items in the list of test runs on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainRunItems(BufferedWriter buf) throws IOException {
	int idx = th.getNoRuns();
	for (TestRun tr : th.getRuns()) {
	    generateMainRunItem(buf, tr, idx--);
	}
    }

    /*
     * Generates a run item in the list of test runs on the main page.
     * 
     * @param buf The buffer to append to. @param tr The test run to generate
     * information for. @param idx The index of the test run.
     */
    private void generateMainRunItem(BufferedWriter buf, TestRun tr, int idx)
	    throws IOException {

	String idxStr = HtmlGenUtils.calculateRunIdxString(th.getNoRuns(), idx);

	String runName = getHtmlReportLink(tr, "Run-" + idxStr);
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + runName
		+ "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItem\" NOWRAP>" + tr.getChosenPath()
		+ "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getNoTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
		+ tr.getExecutionDate() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tr.getExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
		+ tr.getPassPct() + "%</TD>" + c_LF);

	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);

	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);
	buf.write(t(7) + "<TR class=\"bar\">" + c_LF);
	generatePctBar(buf, tr.getPassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);

	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * Generates the list of executed test modules in all run son the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainModuleList(BufferedWriter buf) throws IOException {
	buf.write(t(1)
		+ "<TABLE id='module_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateMainModuleHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generateMainModuleItems(buf);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the list of executed test modules in all run son the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainPackageList(BufferedWriter buf) throws IOException {
	buf.write(t(1)
		+ "<TABLE id='package_table' class=\"sortable\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateMainPackageHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generateMainPackageItems(buf);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the header for the list of executed modules on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainModuleHeader(BufferedWriter buf)
	    throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Module&nbsp;name</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Tests&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Exec.&nbsp;Tests&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Error&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Ignored&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"float\">Exec&nbsp;time&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * Generates the header for the list of executed packages on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainPackageHeader(BufferedWriter buf)
	    throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Package&nbsp;name</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Test&nbsp;Modules&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;Tests&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Exec.&nbsp;Tests&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Pass&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Error&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Fail&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Ignored&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"float\">Exec&nbsp;time&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * Generates the module items in the list of test modules on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainModuleItems(BufferedWriter buf) throws IOException {
	Collection<TestModuleSummary> c = th.getTestModuleSummaries().values();
	Iterator<TestModuleSummary> iter = c.iterator();
	while (iter.hasNext()) {
	    TestModuleSummary tms = iter.next();
	    if (null != tms) {
		generateMainModuleItem(buf, tms);
	    }
	}
    }

    /*
     * Generates the package items in the list of test modules on the main page.
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainPackageItems(BufferedWriter buf)
	    throws IOException {

	Collection<TestPackageSummary> c = th.getTestPackageSummaries()
		.values();
	Iterator<TestPackageSummary> iter = c.iterator();
	while (iter.hasNext()) {
	    TestPackageSummary tps = iter.next();
	    if (null != tps) {
		generateMainPackageItem(buf, tps);
	    }
	}
    }

    /*
     * Generates a module run item in the list of module executions on all
     * module test history pages.
     * 
     * @param buf The buffer to append to. @param tm The test module summary
     * where to retrieve all information.
     */
    private void generateMainModuleItem(BufferedWriter buf,
	    TestModuleSummary tms) throws IOException {

	// Create the link to the module run page
	String clickableLinkFileName = HTMLMODULEOUTPUTFILE_PREFIX
		+ tms.getName() + HTMLMODULEOUTPUTFILE_SUFFIX;
	String link = "<a href=\"" + clickableLinkFileName + "\">"
		+ tms.getName() + "</a>";

	// String pathToReport
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link + "</TD>"
		+ c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoRuns() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoUniqueTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tms.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"left\">"
		+ tms.getExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
		+ tms.getPassPct() + "%</TD>" + c_LF);

	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);

	buf.write(t(7) + "<TR class=\"bar\">" + c_LF);
	generatePctBar(buf, tms.getPassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * Generates the test module summarized executions. All test modules or all
     * runs (same thing) are added together and displayed in a two rows table
     * (header + the statistics).
     * 
     * @param buf The buffer to append to.
     */
    private void generateMainModuleTotals(BufferedWriter buf)
	    throws IOException {
	buf.write("<a name=\"totals\"/><H3 class=\"title\">Test module execution totals</H3>"
		+ c_LF);
	buf.write("This is a summary of all executed test in all runs.<br><br>"
		+ c_LF);
	buf.write(t(1) + "<TABLE name=\"totals_table\" cellspacing=\"0\" cellpadding=\"2\">" + c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateMainRunHeader(buf, false);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>TOTALS</TD>"
		+ c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">NA</TD>"
		+ c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ th.getNoExecutedTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ th.getNoPassedTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ th.getNoErrorTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ th.getNoFailedTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
	        + th.getNoIgnoredTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
		+ "NA" + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ th.getTotalExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\"><B>"
		+ th.getAvePassPct() + "%</B></TD>" + c_LF);

	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);

	buf.write(t(7) + "<TR class=\"bar\">" + c_LF);
	generatePctBar(buf, th.getAvePassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the index.html page where the test history framed layout will
     * be defined.
     * 
     * @param buf The buffer to append to. @param pageTitle The title name for
     * this page.
     */
    protected void startPageMain(BufferedWriter buf, String pageTitle)
	    throws IOException {
	buf.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
		+ c_LF);
	buf.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" >" + c_LF);
	buf.write("<head>" + c_LF);
	buf.write(t(1)
		+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
		+ c_LF);
	buf.write(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
	buf.write(t(1)
		+ "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"css/style.css\" TITLE=\"Style\"/>"
		+ c_LF);

	//
	// JavaScript component where the tables to be sorted are listed.
	//
	buf.write(t(1) + "<script src=\"" + UnitTH.JS_DIR + "/"
		+ UnitTH.SORT_SCRIPT + "\" type=\"text/javascript\"></script>"
		+ c_LF);
	buf.write(t(1) + "<script type=\"text/javascript\">" + c_LF);
	buf.write(t(2) + "function sortTables() {" + c_LF);
	buf.write(t(3) + "var runTable = new TableSort;" + c_LF);
	buf.write(t(3) + "var packageTable = new TableSort;" + c_LF);
	buf.write(t(3) + "var moduleTable = new TableSort;" + c_LF);
	buf.write(t(3) + "runTable.init(\"run_table\");" + c_LF);
	buf.write(t(3) + "packageTable.init(\"package_table\");" + c_LF);
	buf.write(t(3) + "moduleTable.init(\"module_table\");" + c_LF);
	buf.write(t(2) + "}" + c_LF);
	buf.write(t(2) + "window.onload = sortTables;" + c_LF);
	buf.write(t(1) + "</script>" + c_LF);

	buf.write("</head>" + c_LF);
    }

    private void generateMainPackageItem(BufferedWriter buf,
	    TestPackageSummary tps) throws IOException {

	// Create the link to the module run page
	String clickableLinkFileName = HTMLPACKAGEOUTPUTFILE_PREFIX
		+ tps.getName() + HTMLPACKAGEOUTPUTFILE_SUFFIX;
	String link = "<a href=\"" + clickableLinkFileName + "\">"
		+ tps.getName() + "</a>";

	// String pathToReport
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link + "</TD>"
		+ c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoRuns() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoUniqueTestModules() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoUniqueTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tps.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"left\">"
		+ tps.getExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
		+ tps.getPassPct() + "%</TD>" + c_LF);

	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);

	buf.write(t(7) + "<TR class=\"bar\">" + c_LF);
	generatePctBar(buf, tps.getPassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }
}
