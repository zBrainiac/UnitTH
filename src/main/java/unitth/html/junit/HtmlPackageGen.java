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
package unitth.html.junit;

import unitth.core.UnitTH;
import unitth.html.HtmlGenUtils;
import unitth.junit.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class HtmlPackageGen extends HtmlGen {

    /**
     * Ctor for the package pages.
     * 
     * @param history
     *            The test history where to get and calculate the stats from.
     * @param generateExecTimeGraphs
     *            Flag that indicates that execution time graphs shall be
     *            generated.
     */
    public HtmlPackageGen(TestHistory history, boolean generateExecTimeGraphs) {
	super(history, generateExecTimeGraphs);
    }

    public void generateHtmlHistory() {
	generatePackageRunPages();
    }

    /**
     * Entry point for the method generating all the module history pages.
     */
    protected void generatePackageRunPages() {
	Object[] packageNames = th.getUniquePackages();

	for (Object packageName : packageNames) {
	    generatePackageRunPage((String) packageName);
	}
    }

    /**
     * Generates a package history page for the named package.
     * 
     * @param packageName
     *            The name of the package for the page to be generated.
     */
    private void generatePackageRunPage(String packageName) {
	// Create the string to write to the file.
	String fileName = destDir + File.separator
		+ HTMLPACKAGEOUTPUTFILE_PREFIX + packageName
		+ HTMLPACKAGEOUTPUTFILE_SUFFIX;
	try {
	    // Create the file to which to write
	    BufferedWriter out = createFile(fileName);
	    startPagePackage(out, "UnitTH - " + packageName);
	    startBody(out);

	    generatePackageAnchors(out, packageName);
	    out.append("<TABLE width=\"90%\"><TR><TD>" + c_LF);
	    generatePackagePackageSummary(out,
		    th.getTestPackageSummary(packageName));
	    generatePackageRunInfo(out);
	    generatePackageRunList(out, packageName);

	    // Only generate this if there are sub packages. FIXME
		if (th.hasSubPackageSummaries(packageName)) {
			generatePackagePackageInfo(out);
    		generatePackagePackageList(out, packageName);
	    }
	    generatePackageModuleInfo(out);
	    generatePackageModuleList(out, packageName);
	    generatePackageTestCaseInfo(out);
	    generatePackageTestCaseList(out, packageName);
	    generateTestCaseSpreadInfo(out, false);
	    generatePackageTestCaseSpreadList(out, packageName);
	    out.append("</TD></TR></TABLE>" + c_LF);

	    endBody(out);
	    endPage(out);

	} catch (IOException e) {
	    System.out
		    .println("An error occurred while writing to the output to file: "
			    + fileName
			    + " Possible fix: check file system permissions."
			    + " Possible fix: check available disk space.");
	    e.printStackTrace();
	}
    }

    /*
     * Generates the index.html page where the test history framed layout will
     * be defined.
     * 
     * @param buf The buffer to append to. @param pageTitle The title name for
     * this page.
     */
    protected void startPagePackage(BufferedWriter buf, String pageTitle) throws IOException {
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
	buf.write(t(3) + "var testCaseTable = new TableSort;" + c_LF);
	buf.write(t(3) + "var spreadTable = new TableSort;" + c_LF);
	buf.write(t(3) + "runTable.init(\"run_table\");" + c_LF);
	buf.write(t(3) + "packageTable.init(\"package_table\");" + c_LF);
	buf.write(t(3) + "moduleTable.init(\"module_table\");" + c_LF);
	buf.write(t(3) + "testCaseTable.init(\"testcase_table\");" + c_LF);
	buf.write(t(3) + "spreadTable.init(\"spread_table\");" + c_LF);
	buf.write(t(2) + "}" + c_LF);
	buf.write(t(2) + "window.onload = sortTables;" + c_LF);
	buf.write(t(1) + "</script>" + c_LF);

	buf.write("</head>" + c_LF);
    }

    /**
     * Generates a link bar on every page to make it possible to navigate to the
     * correct part on the page.
     * 
     * @param out
     *            The buffer to append to.
     */
    private void generatePackageAnchors(BufferedWriter out, String packageName) throws IOException {
	out.append("<TABLE class=\"mainAnchors\" width=\"100%\">" + c_LF);
		out.append(t(1)).append("<TR>").append(c_LF);
		out.append(t(2)).append("<TD>");
	out.append("<CENTER><a href=\"#runs\">RUNS</a>");
		if (th.hasSubPackageSummaries(packageName)) {
	    out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#subpackages\">SUB PACKAGES</a>");
	}
	out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#testmodules\">TEST MODULES</a>");
	out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#testcases\">TEST CASES</a>");
	out.append("&nbsp;&nbsp;||&nbsp;&nbsp;<a href=\"#spread\">TEST CASE SPREAD</a></CENTER>");
	out.append("</TD>" + c_LF);
		out.append(t(1)).append("</TR>").append(c_LF);
	out.append("</TABLE>" + c_LF);
    }

    /**
     * Generates the summary for a specific test module.
     * 
     * @param buf
     *            The buffer to append to. @param tms The test module summary to
     *            get the statistics from.
     */
    private void generatePackagePackageSummary(BufferedWriter buf,
	    TestPackageSummary tps) throws IOException {
	buf.write("<H1 class=\"title\">PACKAGE</H1>" + c_LF);
	buf.write("<H2 class=\"title\">" + tps.getName() + "</H2>" + c_LF);
	buf.write(t(1) + "<TABLE class=\"topcontainer\" cellspacing=\"6\">"
		+ c_LF);
	buf.write(t(2) + "<TR>" + c_LF);
	buf.write(t(3) + "<TD valign=\"top\">" + c_LF);
	buf.write(t(4)
		+ "<TABLE id=\"summary\" class=\"sum\" cellspacing=\"4\">"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" colspan=\"2\">Test&nbsp;module&nbsp;history&nbsp;overview</TD><TD>"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;runs&nbsp;</TD><TD id=\"mainSmryRuns\" class=\"summid\"> "
		+ tps.getNoRuns() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;test&nbsp;modules/classes&nbsp;</TD><TD id=\"mainSmryNoTms\" class=\"summid\"> "
		+ tps.getNoUniqueTestModules() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Number&nbsp;of&nbsp;unique&nbsp;test&nbsp;cases&nbsp;</TD><TD id=\"mainSmryNoTcs\" class=\"summid\"> "
		+ tps.getNoUniqueTestCases() + " </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Best&nbsp;run&nbsp;</TD><TD id=\"mainSmryBest\" class=\"summid\"> "
		+ tps.getBestRun() + "% </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Worst&nbsp;run&nbsp;</TD><TD id=\"mainSmryWorst\" class=\"summid\"> "
		+ tps.getWorstRun() + "% </TD></TR>" + c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumbottom\">&nbsp;Average&nbsp;pass&nbsp;rate&nbsp;</TD><TD id=\"mainSmryAve\" class=\"sumbottom\"> <b>"
		+ tps.getPassPct() + "% </b></TD></TR>" + c_LF);
	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);

	//
	// Images
	//
	buf.write(t(3) + "<TD VALIGN=\"top\" rowspan=\"2\">" + c_LF);
	buf.write(t(4) + "<TABLE class=\"imageholder\" cellspacing=\"4\">"
		+ c_LF);
	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" align=\"center\">Pass&nbsp;rates&nbsp;-&nbsp;all&nbsp;runs</TD></TR>"
		+ c_LF);

	buf.write(t(6) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(5) + "<TR><TD><img src=\"./images/" + IMAGE_PASS_PREFIX
		+ tps.getName().replace('.', '-') + IMAGE_SUFFIX
		+ "\"></TD></TR>" + c_LF);
	buf.write(t(6) + "</TABLE></TD></TR>" + c_LF);

	buf.write(t(5)
		+ "<TR><TD class=\"sumhdr\" align=\"center\">Number&nbsp;of&nbsp;test&nbsp;cases&nbsp;-&nbsp;run&nbsp;by&nbsp;run</TD></TR>"
		+ c_LF);

	buf.write(t(6) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(5) + "<TR><TD><img src=\"./images/" + IMAGE_TC_PREFIX
		+ tps.getName().replace('.', '-') + IMAGE_SUFFIX
		+ "\"></TD></TR>" + c_LF);
	buf.write(t(6) + "</TABLE></TD></TR>" + c_LF);

	buf.write(t(4) + "</TABLE>" + c_LF);
	buf.write(t(3) + "</TD>" + c_LF);

	buf.write(t(3) + "<TD VALIGN=\"top\" rowspan=\"2\">" + c_LF);
	buf.write(t(4) + "<TABLE class=\"imageholder\"  cellspacing=\"4\">"
		+ c_LF);

	// Only draw the execution time graph if the flag is set in the unitth
	// properties
	if (generateExecTimeGraphs) {
	    buf.write(t(5)
		    + "<TR><TD valign=\"top\" class=\"sumhdr\" align=\"center\">Execution&nbsp;times&nbsp;("
		    + getExecutionTimeUnit(tps)
		    + ")&nbsp;-&nbsp;all&nbsp;runs</TD></TR>" + c_LF);

	    buf.write(t(6) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	    buf.write(t(5) + "<TR><TD><img src=\"./images/" + IMAGE_ET_PREFIX
		    + tps.getName().replace('.', '-') + IMAGE_SUFFIX
		    + "\"></TD></TR>" + c_LF);
	    buf.write(t(6) + "</TABLE></TD></TR>" + c_LF);
	}

	buf.write(t(5)
		+ "<TR><TD valign=\"top\" class=\"sumhdr\" align=\"center\">Non&nbsp;passing&nbsp;test&nbsp;cases</TD></TR>"
		+ c_LF);

	buf.write(t(6) + "<TR><TD><TABLE cellpadding=\"4\">" + c_LF);
	buf.write(t(5) + "<TR><TD><img src=\"./images/" + IMAGE_FN_PREFIX
		+ tps.getName().replace('.', '-') + IMAGE_SUFFIX
		+ "\"></TD></TR>" + c_LF);
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
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getPrTrendLastRun(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;run&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getPrTrendLastRun()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getPrTrendLast5Runs(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;5&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getPrTrendLast5Runs()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getPrTrendLast10Runs(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;10&nbsp;runs&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getPrTrendLast10Runs()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getPrTrendLast3Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getPrTrendLast3Days()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getPrTrendLast7Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Pass&nbsp;rate&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getPrTrendLast7Days()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getTcTrendLast3Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"summid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getTcTrendLast3Days()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getTcTrendLast7Days(),
		true);
	buf.write(t(5)
		+ "<TR><TD class=\"sumid\">&nbsp;Test&nbsp;case&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getTcTrendLast7Days()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getFnTrendLast3Days(),
		false);
	buf.write(t(5)
		+ "<TR><TD class=\"sumid\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;3&nbsp;days&nbsp;</TD><TD class=\"prTrend\">"
		+ tps.getFnTrendLast3Days()
		+ "</TD><TD class=\"prTrend\" align=\"right\" valign=\"middle\">"
		+ "<IMG src=\"" + selectedTrend + "\" height=\""
		+ UnitTH.ICON_HEIGHT + "\" width=\"" + UnitTH.ICON_WIDTH
		+ "\">" + "</TD></TR>" + c_LF);
	selectedTrend = HtmlGenUtils.getTrendImage(tps.getFnTrendLast7Days(),
		false);
	buf.write(t(5)
		+ "<TR><TD class=\"sumbottom\">&nbsp;Failure&nbsp;trend&nbsp;last&nbsp;7&nbsp;days&nbsp;</TD><TD class=\"prTrendBottom\">"
		+ tps.getFnTrendLast7Days()
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
     * Generates the informational text preceding the test module run pass rate
     * list.
     * 
     * @param buf The buffer to append to.
     */
    private void generatePackageRunInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"runs\"/><H3 class=\"title\">Package runs</H3>"
		+ c_LF);
	buf.write("If regular HTML test reports have been generated click on the run to open the report.<br/><br/>"
		+ c_LF);
    }

    /**
     * Generates the list of modules in all runs and each row in the table
     * represents a summary of all executions for the specific module.
     * 
     * @param buf
     *            The buffer to append to. @param moduleName The name of the
     *            module to get the statistics from.
     */
    private void generatePackageRunList(BufferedWriter buf, String packageName) throws IOException {
	buf.write(t(1)
		+ "<TABLE id=\"run_table\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generatePackageRunHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generatePackageRunItems(buf, packageName);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /**
     * Generates the header for the list of module runs.
     * 
     * @param buf
     *            The buffer to append to.
     */
    private void generatePackageRunHeader(BufferedWriter buf) throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Run</TH>"
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
		+ "<TH class=\"graphHeader\" NOWRAP abbr=\"date\">Exec&nbsp;date</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"float\">Exec&nbsp;time&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /**
     * This method creates the list of runs for a specific package.
     * 
     * @param buf
     *            The buffer to append to. @param moduleName The name of the
     *            module whose runs to generate a list for.
     */
    private void generatePackageRunItems(BufferedWriter buf, String packageName) throws IOException {
	int idx = th.getRuns().size();
	for (TestRun tr : th.getRuns()) {
	    TestPackage tp = tr.getTestPackages().get(packageName);
	    if (null != tp) {
		generatePackageRunItem(buf, tp, idx, tr);
	    }
	    idx--;
	}
    }

    /*
     * Generates a row in the test module history list.
     * 
     * @param buf The buffer to append to. @param tm The test module who's
     * statistics to print out. @param idx The index to display for this run.
     * 
     * @param runPath The path to the parsed files for this run.
     */
    private void generatePackageRunItem(BufferedWriter buf, TestPackage tm,
	    int idx, TestRun tr) throws IOException {

	String idxStr = HtmlGenUtils.calculateRunIdxString(th.getNoRuns(), idx);

	String runName = getHtmlReportLink(tr, "Run-" + idxStr);

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + runName
		+ "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getNoTestCases() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\" NOWRAP>"
		+ tm.getExecutionDate() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tm.getExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
		+ tm.getPassPct() + "%</TD>" + c_LF);

	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);

	buf.write(t(7) + "<TR>" + c_LF);
	generatePctBar(buf, tm.getPassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /**
     * Generates the informational text preceding the test module run pass rate
     * list.
     * 
     * @param buf
     *            The buffer to append to.
     */
    private void generatePackageModuleInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"testmodules\"/><H3 class=\"title\">Module runs in this package</H3>"
		+ c_LF);
    }

    /**
     * Generates the informational text preceding the test module run pass rate
     * list.
     * 
     * @param buf
     *            The buffer to append to.
     */
    private void generatePackagePackageInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"subpackages\"/><H3 class=\"title\">Sub packages in this package</H3>"
		+ c_LF);
    }

    /**
     * Generates the list of modules in all runs and each row in the table
     * represents a summary of all executions for the specific module.
     * 
     * @param buf
     *            The buffer to append to. @param moduleName The name of the
     *            module to get the statistics from.
     */
    private void generatePackageModuleList(BufferedWriter buf, String packageName) throws IOException {
	buf.write(t(1)
		+ "<TABLE id=\"module_table\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generatePackageModuleHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generatePackageModuleItems(buf, packageName);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    private void generatePackagePackageList(BufferedWriter buf, String packageName) throws IOException {
	buf.write(t(1)
		+ "<TABLE id=\"package_table\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generatePackagePackageHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generatePackagePackageItems(buf, packageName);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /**
     * Generates the header for the list of module runs.
     * 
     * @param buf
     *            The buffer to append to.
     */
    private void generatePackageModuleHeader(BufferedWriter buf) throws IOException {

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

    private void generatePackagePackageHeader(BufferedWriter buf) throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Package&nbsp;name</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Unique&nbsp;test&nbsp;classes&nbsp;&nbsp;</TH>"
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
     * Generates a row in the test module history list.
     * 
     * @param buf The buffer to append to. @param tm The test module who's
     * statistics to print out. @param idx The index to display for this run.
     * 
     * @param runPath The path to the parsed files for this run.
     */
    private void generatePackageModuleItem(BufferedWriter buf,
	    TestModuleSummary tms) throws IOException {

	// Create the link to the module run page
	String clickableLinkFileName = HTMLMODULEOUTPUTFILE_PREFIX
		+ tms.getName() + HTMLMODULEOUTPUTFILE_SUFFIX;
	String link = "<a href=\"" + clickableLinkFileName + "\">"
		+ tms.getName() + "</a>";

	// String pathToReport
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link
		+ "</TD>" + c_LF);
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

    private void generatePackagePackageItem(BufferedWriter buf,
	    TestPackageSummary tps) throws IOException {

	// Create the link to the module run page
	String clickableLinkFileName = HTMLPACKAGEOUTPUTFILE_PREFIX
		+ tps.getName() + HTMLPACKAGEOUTPUTFILE_SUFFIX;
	String link = "<a href=\"" + clickableLinkFileName + "\">"
		+ tps.getName() + "</a>";

	// String pathToReport
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + link
		+ "</TD>" + c_LF);
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

    /**
     * This method creates the list of runs for a specific module.
     * 
     * @param buf
     *            The buffer to append to.
     * @param moduleName
     *            The name of the module whose runs to generate a list for.
     */
    private void generatePackageModuleItems(BufferedWriter buf, String packageName) throws IOException {
	Collection<TestModuleSummary> c = th.getTestModuleSummaries().values();
	Iterator<TestModuleSummary> iter = c.iterator();
	while (iter.hasNext()) {
	    TestModuleSummary tms = iter.next();
	    if (null != tms && tms.getPackageName().contains(packageName)) {
		generatePackageModuleItem(buf, tms);
	    }
	}
    }

    private void generatePackagePackageItems(BufferedWriter buf,
	    String packageName) throws IOException {
	Collection<TestPackageSummary> c = th.getTestPackageSummaries()
		.values();
	Iterator<TestPackageSummary> iter = c.iterator();

	while (iter.hasNext()) {
	    TestPackageSummary tps = iter.next();
	    if (null != tps) {
		if (tps.getName().contains(packageName + ".")) {
		    generatePackagePackageItem(buf, tps);
		}
	    }
	}
    }

    /**
     * Generates the informational text preceding the test case pass rate list.
     * 
     * @param buf
     *            The buffer to append to.
     */
    private void generatePackageTestCaseInfo(BufferedWriter buf) throws IOException {
	buf.write("<a name=\"testcases\"/><H3 class=\"title\">Test cases in this package</H3>"
		+ c_LF);
	buf.write("This list shows the average pass rate and execution times for all test cases in this test module.<br/><br/>"
		+ c_LF);
    }

    /**
     * Generates the list representing all test cases in a test module and all
     * its associated statistics.
     * 
     * @param buf
     *            The buffer to append to. @param moduleName The name of the
     *            module where all the test cases shall be retrieved.
     */
    private void generatePackageTestCaseList(BufferedWriter buf,
	    String packageName) throws IOException {
	buf.write(t(1)
		+ "<TABLE id=\"testcase_table\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateTestCaseHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generatePackageTestCaseItems(buf, packageName);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /**
     * Generates the list of test case items in the test cases list on the
     * module page.
     * 
     * @param buf
     *            The buffer to append to. @param moduleName The name of the
     *            module where all the test cases shall be retrieved.
     */
    private void generatePackageTestCaseItems(BufferedWriter buf,
	    String packageName) throws IOException {
	Collection<TestCaseSummary> c = th.getTestCaseSummaries().values();
	Iterator<TestCaseSummary> iter = c.iterator();
	while (iter.hasNext()) {
	    TestCaseSummary tcs = iter.next();
	    if (null != tcs && tcs.getPackageName().contains(packageName)) {
		generatePackageTestCaseItem(buf, tcs, packageName);
	    }
	}
    }

    /*
     * Generates the list that displays the pass rate spread for individual test
     * cases.
     * 
     * @param buf The buffer to append to. @param moduleName The name of the
     * module who's test cases to list.
     */
    private void generatePackageTestCaseSpreadList(BufferedWriter buf,
	    String packageName) throws IOException {
	buf.write(t(1)
		+ "<TABLE id=\"spread_table\" cellspacing=\"0\" cellpadding=\"2\">"
		+ c_LF);
	buf.write(t(2) + "<THEAD>" + c_LF);
	generateTestCaseSpreadHeader(buf);
	buf.write(t(2) + "</THEAD>" + c_LF);
	buf.write(t(2) + "<TBODY>" + c_LF);
	generatePackageTestCaseSpreadItems(buf, packageName);
	buf.write(t(2) + "</TBODY>" + c_LF);
	buf.write(t(1) + "</TABLE>" + c_LF);
    }

    /*
     * Generates the list of items displaying the test case spread for
     * individual test cases.
     * 
     * @param buf The buffer to append to. @param moduleName The name of the
     * module where to get the test cases.
     */
    private void generatePackageTestCaseSpreadItems(BufferedWriter buf,
	    String packageName) throws IOException {
	Collection<TestCaseSummary> c = th.getTestCaseSummaries().values();
	Iterator<TestCaseSummary> iter = c.iterator();
	while (iter.hasNext()) {
	    TestCaseSummary tcs = iter.next();
	    if (null != tcs && tcs.getPackageName().contains(packageName)) {
		generatePackageTestCaseSpreadItem(buf, tcs, packageName);
	    }
	}
    }

    protected void generatePackageTestCaseItem(BufferedWriter buf,
	    TestCaseSummary tcs, String packageName) throws IOException {
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>"
		+ tcs.getClassPlusTestCaseName(packageName) + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getNoRuns() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
		+ tcs.getExecutionTime() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
		+ tcs.getPassPct() + "%</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBar\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);

	buf.write(t(7) + "<TR>" + c_LF);
	generatePctBar(buf, tcs.getPassPctDouble());
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    protected void generatePackageTestCaseSpreadItem(BufferedWriter buf,
	    TestCaseSummary tcs, String packageName) throws IOException {
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" width=\"2*\" NOWRAP>"
		+ tcs.getClassPlusTestCaseName(packageName) + "</TD>" + c_LF);
	buf.write(t(4)
		+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
		+ tcs.getNoRuns() + "</TD>" + c_LF);
	buf.write(t(4)
		+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
		+ tcs.getNoPassed() + "</TD>" + c_LF);
	buf.write(t(4)
		+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
		+ tcs.getNoErrors() + "</TD>" + c_LF);
	buf.write(t(4)
		+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
		+ tcs.getNoFailures() + "</TD>" + c_LF);
	buf.write(t(4)
		+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
		+ tcs.getNoIgnored() + "</TD>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphBar\" width=\"40*\">" + c_LF);
	buf.write(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">" + c_LF);
	buf.write(t(6) + "<TBODY>" + c_LF);
	buf.write(t(7) + "<TR>" + c_LF);
	generateSpreadBar(buf, tcs);
	buf.write(t(7) + "</TR>" + c_LF);
	buf.write(t(6) + "</TBODY>" + c_LF);
	buf.write(t(5) + "</TABLE>" + c_LF);
	buf.write(t(4) + "</TD>" + c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }
}

/* EOF */
