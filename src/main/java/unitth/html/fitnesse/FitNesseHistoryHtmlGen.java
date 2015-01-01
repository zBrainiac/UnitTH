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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import unitth.core.UnitTH;
import unitth.fitnesse.TestCaseSummary;
import unitth.fitnesse.TestHistory;


public class FitNesseHistoryHtmlGen {

	protected static final String c_LF = "\r\n";
	protected static final String c_TAB = "  ";

	protected final String IMGDIR = "img";
	protected final String CSSDIR = "css";
	protected final String HTML_MAIN_FRAME_FILE = "main.html";
	protected final String HTML_TOP_FRAME_FILE = "top.html";
	protected final String HTML_MAIN = "index.html";
	protected final String HTMLTESTSUITEOUTPUTFILE_PREFIX = "fitnesse-testsuite-";
	protected final String HTMLTESTSUITEOUTPUTFILE_SUFFIX = ".html";
	protected final String IMAGE_SUFFIX = ".png";
	protected final String IMAGE_PASS_PREFIX = "tspr-";
	protected final String IMAGE_NUMBERS_PREFIX = "tsnu-";
	protected final String IMAGE_ASSERTIONS_PREFIX = "tsas-";
	protected final String IMAGE_EXECUTIONTIME_PREFIX = "tset-";
	protected final String IMAGE_RIGHTS_PREFIX = "tsri-";
	protected final String IMAGE_WRONGS_PREFIX = "tswr-";
	protected final String IMAGE_INGORES_PREFIX = "tsig-";
	protected final String IMAGE_EXCEPTIONS_PREFIX = "tsex-";
	protected final int TOP_PAGE_HEIGHT = 36;
	protected final String TOP_BGCOLOR = "BLACK";
	protected final int ICON_HEIGHT = 24;
	protected final int ICON_WIDTH = 24;
	protected final int SPREAD_HEIGHT = 6;
	protected final int SPREAD_WIDTH = 6;

	protected TestHistory th = null;
	protected boolean generateExecTimeGraphs = false;

	protected String destDir;
	protected String imgDir;
	protected String cssDir;

	/**
	 * CTOR, sets the history to generate and sets a few of the fixed paths.
	 * 
	 * @param history
	 */
	public FitNesseHistoryHtmlGen(TestHistory history,
			boolean generateExecTimeGraphs) {
		th = history;
		this.generateExecTimeGraphs = generateExecTimeGraphs;
		destDir = UnitTH.rootFolder;
		imgDir = destDir + File.separator + IMGDIR;
		cssDir = destDir + File.separator + CSSDIR;
	}

	/**
	 * Creates a string corresponding to a number of tabs.
	 * 
	 * @param noTabs The number of tabs to insert. @return
	 */
	//TODO, Move to html common package
	protected String t(int noTabs) {
		String ret = "";
		for (int i = 0; i < noTabs; i++) {
			ret += c_TAB;
		}
		return ret;
	}

	/**
	 * Sets the report destination directory.
	 * 
	 * @param dDir
	 *            The report destination directory.
	 */
	public void setDestDir(String dDir) {
		this.destDir = dDir;
	}

	/**
	 * Sets the image out put directory.
	 * 
	 * @param iDir
	 *            The image out put directory.
	 */
	public void setImgDir(String iDir) {
		this.imgDir = iDir;
	}

	/**
	 * Sets the css out put directory.
	 * 
	 * @param cDir
	 *            The css out put directory.
	 */
	public void setCssDir(String cDir) {
		this.cssDir = cDir;
	}

	/**
	 * The main entry point for the HTML generation.
	 */
	public void generateHtmlHistory() {
		generateTopPage();
		generateMainFrames();
	}

	/**
	 * Generates the top page in the framed history output.
	 */
	public void generateTopPage() {
		StringBuffer out = new StringBuffer();
		startPage(out, "Test History Top Frame");
		out.append("<body bgcolor=\"" + TOP_BGCOLOR + "\">" + c_LF);

		generateTopContents(out);

		endBody(out);
		endPage(out);
		writeFile(destDir + File.separator + HTML_TOP_FRAME_FILE, out);
		out = null;
	}

	/**
	 * Generates the contents in the top page of the report.
	 * 
	 * @param buf
	 *            The page buffer to append to.
	 */
	public void generateTopContents(StringBuffer buf) {
		String backIcon = UnitTH.IMAGE_DIR + "/" + UnitTH.BACK_ICON;
		String unitthLogo = UnitTH.IMAGE_DIR + "/" + UnitTH.LOGO;
		buf.append(t(1)
				+ "<TABLE width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">"
				+ c_LF);
		buf.append(t(2) + "<TR>" + c_LF);

		Date time = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);
		String generationTime = df.format(time);

		buf.append(t(3) + "<TD align=\"left\" valign=\"middle\"><a href=\""
				+ HTML_MAIN_FRAME_FILE + "\" target=\"mainpage\"><img src=\""
				+ backIcon + "\" border=\"0\"></a></TD>" + c_LF);
		buf.append(t(3) + "<TD align=\"left\" valign=\"middle\">"
				+ "<b><span style=\"color:white\">Report generated: <br>"
				+ generationTime + "</span></b></TD>" + c_LF);
		buf.append(t(3) + "<TD align=\"right\" valign=\"middle\">"
				+ "<a href=\"http://junitth.sourceforge.net\" target=\"new\">"
				+ "<img src=\"" + unitthLogo + "\" border=\"0\" alt=\"UnitTH v"
				+ UnitTH.versionNumber + "\"></a></TD>" + c_LF);
		buf.append(t(2) + "</TR>" + c_LF);
		buf.append(t(1) + "</TABLE>" + c_LF);
	}

	/**
	 * Generates the frames for the index page.
	 */
	public void generateMainFrames() {
		StringBuffer out = new StringBuffer();
		startPage(out, "UnitTH - Test History");
		out.append("<frameset rows=\"" + TOP_PAGE_HEIGHT
				+ ",*\" frameborder=\"0\" border=\"0\" framespacing=\"0\">"
				+ c_LF);
		out.append(t(1) + "<frame src=\"" + HTML_TOP_FRAME_FILE
				+ "\" name=\"menupage\" noresize scrolling=\"no\">" + c_LF);
		out.append(t(1) + "<frame src=\"" + HTML_MAIN_FRAME_FILE
				+ "\" name=\"mainpage\" marginheight=\"20\">" + c_LF);
		out.append("</frameset>" + c_LF);
		endPage(out);
		writeFile(destDir + File.separator + HTML_MAIN, out);
		out = null;
	}

	/*
	 * Generates the horizontal percentage bar used for all test runs, module
	 * test runs and test case pass rates. The positive pass rate is represented
	 * by a green bar and the negative is represented by a red bar.
	 * 
	 * @param buf The buffer to append to. @param pct The pass rate on which to
	 * base the bar dimensions.
	 */
	protected void generatePctBar(StringBuffer buf, Double pct) {
		// Calculate width
		int redWidth = 200 - pct.intValue() * 2;
		int greenWidth = 200 - redWidth;
		if (200 == greenWidth || 200 == redWidth) {
			String color = "prpass";
			if (200 == greenWidth) {
				color = "prpass";
			} else if (200 == redWidth) {
				color = "prfail";
			}
			buf.append(t(7) + t(1) + "<TD class=\"" + color
					+ "\"><img src=\"images/" + UnitTH.TRANS_IMAGE
					+ "\" width=\"" + 200 + "\" height=\"12\"/></TD>" + c_LF);
		} else {
			buf.append(t(7) + t(1) + "<TD class=\"prpass\"><img src=\"images/"
					+ UnitTH.TRANS_IMAGE + "\" width=\"" + greenWidth
					+ "\" height=\"12\"/></TD>" + c_LF);
			buf.append(t(7) + t(1) + "<TD class=\"prfail\"><img src=\"images/"
					+ UnitTH.TRANS_IMAGE + "\" width=\"" + redWidth
					+ "\" height=\"12\"/></TD>" + c_LF);
		}
	}

	/*
	 * Generates the index.html page where the test history framed layout will
	 * be defined.
	 * 
	 * @param buf The buffer to append to. @param pageTitle The title name for
	 * this page.
	 */
	protected void startPage(StringBuffer buf, String pageTitle) {
		buf
				.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
						+ c_LF);
		buf.append("<html>" + c_LF);
		buf.append("<head>" + c_LF);
		buf
				.append(t(1)
						+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
						+ c_LF);
		buf.append(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
		buf
				.append(t(1)
						+ "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"css/style.css\" TITLE=\"Style\">"
						+ c_LF);
		buf.append("</head>" + c_LF);
	}

	/*
	 * Outputs the BODY start tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void startBody(StringBuffer buf) {
		buf.append("<body>" + c_LF);
	}

	/*
	 * Outputs the BODY end tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void endBody(StringBuffer buf) {
		buf.append("</body>" + c_LF);
	}

	/*
	 * Outputs the HTML end tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void endPage(StringBuffer buf) {
		buf.append("</html>" + c_LF);
	}

	/**
	 * Returns the report destination directory.
	 * 
	 * @return The report destination directory.
	 */
	public String getDestDir() {
		return destDir;
	}

	/**
	 * Returns the images destination directory.
	 * 
	 * @return The images destination directory.
	 */
	public String getImgDir() {
		return imgDir;
	}

	/**
	 * Returns the css files destination directory.
	 * 
	 * @return The css files destination directory.
	 */
	public String getCssDir() {
		return cssDir;
	}

	/*
	 * Writes a buffer to a file given by the fileName parameter.
	 * 
	 * @param fileName The name of the file to be created. @param out The buffer
	 * that shall end up as a HTMl file.
	 */
	protected void writeFile(String fileName, StringBuffer out) {
		File outFile = null;
		try {
			outFile = new File(fileName);
			outFile.createNewFile();

			BufferedWriter bwout = new BufferedWriter(new FileWriter(outFile));
			bwout.write(out.toString());
			bwout.close();
		} catch (IOException e) {
			System.out
					.println("Could not create and write the output to file: "
							+ outFile.getName()
							+ " Possible fix: check file system permisons."
							+ " Possible fix: make sure the properties are correct in the unitth.properties file.");
			e.printStackTrace();
		}
	}

	protected void generateMainPage() { /* To be overridden. */
	}

	protected void generateModuleRunPages() { /* To be overridden. */
	}

	/**
	 * The information that describes the list displaying the test case pass
	 * rate spread.
	 * 
	 * @param buf
	 *            The buffer to append to.
	 */
	protected void generateTestCaseSpreadInfo(StringBuffer buf, boolean isModule) {
		buf
				.append("<a name=\"spread\"/><H3 class=\"title\">Test case verdict spread in this ");
		if (isModule) {
			buf.append("module");
		} else {
			buf.append("package");
		}
		buf.append("</H3>" + c_LF);
		buf
				.append("This list shows the test case verdicts in the order they occured during the test runs. If a regular HTML report has been generated click on a spread item to jump directly to the test report. Legend: (green=Pass, grey=No-run, red=Fail/Error)<br/><br/>"
						+ c_LF);
	}

	/*
	 * This method generates row in the table displaying the pass rate spreads
	 * for individual test cases.
	 * 
	 * @param buf The buffer to append to. @param tcs The test case summary
	 * where to get the information and spread.
	 */
	protected void generateTestCaseSpreadItem(StringBuffer buf,
			TestCaseSummary tcs, int noRuns, String testSuiteName) {
		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" width=\"2*\" NOWRAP>"
				+ tcs.getTestSuitePlusTestCaseName(testSuiteName) + "</TD>" + c_LF);
		buf.append(t(4)
				+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
				+ tcs.getNoRuns() + "</TD>" + c_LF);
		buf.append(t(4)
				+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
				+ tcs.getNoPassed() + "</TD>" + c_LF);
		buf.append(t(4)
				+ "<TD class=\"graphPercent\" align=\"center\" width=\"1*\">"
				+ tcs.getNoFailures() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphBar\" width=\"40*\">" + c_LF);
		buf
				.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">"
						+ c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);
		buf.append(t(7) + "<TR>" + c_LF);
		generateSpreadBar(buf, tcs, noRuns);
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}

	private void generateSpreadBar(StringBuffer buf, TestCaseSummary tcs, int noRuns) {

		String cssClass = "";
		for (int i = 1; i <= noRuns; i++) {
			Integer tcv = tcs.getSpreadAt(i);
			
			if (tcv == null) {
				cssClass = "norun";
			} else if (tcv == 1) {
				cssClass = "pass";
			} else if (tcv == -1) {
				cssClass = "fail";
			} 

			buf.append(t(4));
			buf.append(t(4));
			buf.append("<TD class=\""
					+ cssClass
					+ "\" align=\"center\">"
					+ "<img title=\"Run-" 
					+ i + "\" src=\"images/"
					+ UnitTH.TRANS_IMAGE
					+ "\" border=\"0\" width=\"" + SPREAD_WIDTH
					+ "\" height=\"" + SPREAD_HEIGHT + "\">"
					+ "</TD>" + c_LF);
		}
	}
	
	protected void startPageMain(StringBuffer buf, String pageTitle) {
		buf
				.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
						+ c_LF);
		buf.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" >" + c_LF);
		buf.append("<head>" + c_LF);
		buf
				.append(t(1)
						+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
						+ c_LF);
		buf.append(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
		buf
				.append(t(1)
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
		buf.append(t(3) + "var rootTestSuiteTable = new TableSort;" + c_LF);
		buf.append(t(3) + "var testSuiteTable = new TableSort;" + c_LF);
		buf.append(t(3) + "rootTestSuiteTable.init(\"root_testsuite_table\");" + c_LF);
		buf.append(t(3) + "testSuiteTable.init(\"testsuite_table\");" + c_LF);
		buf.append(t(2) + "}" + c_LF);
		buf.append(t(2) + "window.onload = sortTables;" + c_LF);
		buf.append(t(1) + "</script>" + c_LF);

		buf.append("</head>" + c_LF);
	}
	
	protected void generateTestCaseHeader(StringBuffer buf) {
		buf.append(t(3) + "<TR>" + c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeaderLeftAsc\" abbr=\"input_text\">Test&nbsp;case&nbsp;name</TH>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
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

	protected void generateTestCaseItem(StringBuffer buf,
			TestCaseSummary tcs, String testSuiteName) {
		buf.append(t(3) + "<TR>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>" + tcs.getTestSuitePlusTestCaseName(testSuiteName)
				+ "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoRuns() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoPassed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoFailed() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoAssertions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoRights() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoWrongs() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoIgnores() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphPercent\" align=\"center\">"
				+ tcs.getNoExceptions() + "</TD>" + c_LF);
		buf.append(t(4) + "<TD class=\"graphBarLeft\" align=\"right\">"
				+ tcs.getPassPct() + "%</TD>" + c_LF);

		buf.append(t(4) + "<TD class=\"graphBar\">" + c_LF);
		buf
				.append(t(5) + "<TABLE class=\"barGraph\" cellspacing=\"0\">"
						+ c_LF);
		buf.append(t(6) + "<TBODY>" + c_LF);

		buf.append(t(7) + "<TR class=\"bar\">" + c_LF);
		generatePctBar(buf, tcs.getPassPctDouble());
		buf.append(t(7) + "</TR>" + c_LF);
		buf.append(t(6) + "</TBODY>" + c_LF);
		buf.append(t(5) + "</TABLE>" + c_LF);
		buf.append(t(4) + "</TD>" + c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}
	
	protected void generateTestCaseSpreadHeader(StringBuffer buf) {

		buf.append(t(3) + "<TR>" + c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeaderLeft\" abbr=\"input_text\">Test&nbsp;case&nbsp;name&nbsp;&nbsp;&nbsp;</TD>"
						+ c_LF);
		buf
				.append(t(4)
						+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
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
						+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\">SPREAD</TH>"
						+ c_LF);
		buf.append(t(3) + "</TR>" + c_LF);
	}
}
