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
 * $Id: HtmlGen.java,v 1.24 2010/05/14 15:21:39 andnyb Exp $
 * =======================================================================
 */
package unitth.html.junit;

import unitth.core.UnitTH;
import unitth.html.HtmlGenUtils;
import unitth.junit.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * This class is responsible for generation of the HTML report pages.
 * 
 * @author Andreas Nyberg
 */
public class HtmlGen {

	protected static final String c_LF = "\r\n";
	private static final String c_TAB = "  ";

	protected final String HTML_MAIN_FRAME_FILE = "main.html";
	private final String HTML_TOP_FRAME_FILE = "top.html";
	protected final String HTMLMODULEOUTPUTFILE_PREFIX = "th-module-";
	protected final String HTMLMODULEOUTPUTFILE_SUFFIX = ".html";
	protected final String HTMLPACKAGEOUTPUTFILE_PREFIX = "th-package-";
	protected final String HTMLPACKAGEOUTPUTFILE_SUFFIX = ".html";
	protected final String IMAGE_SUFFIX = ".png";
	protected final String IMAGE_PASS_PREFIX = "mpr-";
	protected final String IMAGE_TC_PREFIX = "mtc-";
	protected final String IMAGE_FN_PREFIX = "mfn-";
	protected final String IMAGE_ET_PREFIX = "etg-";
	protected final String TOP_BGCOLOR = "BLACK";
	protected final int ICON_HEIGHT = 24;

	protected TestHistory th = null;
	protected boolean generateExecTimeGraphs = false;

	protected final String destDir;

	/**
	 * CTOR, sets the history to generate and sets a few of the fixed paths.
	 * 
	 * @param history
	 */
	public HtmlGen(TestHistory history, boolean generateExecTimeGraphs) {
		th = history;
		this.generateExecTimeGraphs = generateExecTimeGraphs;
		destDir = UnitTH.rootFolder;
		String IMGDIR = "img";
		String imgDir = destDir + File.separator + IMGDIR;
		String CSSDIR = "css";
		String cssDir = destDir + File.separator + CSSDIR;
	}

	/*
	 * Creates a string corresponding to a number of tabs.
	 * 
	 * @param noTabs The number of tabs to insert. @return
	 */
	protected String t(int noTabs) {
		String ret = "";
		for (int i = 0; i < noTabs; i++) {
			ret += c_TAB;
		}
		return ret;
	}

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Sets the report destination directory.
//	 *
//	 * @param dDir
//	 *            The report destination directory.
//	 */
//	public void setDestDir(String dDir) {
//		this.destDir = dDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Sets the image out put directory.
//	 *
//	 * @param iDir
//	 *            The image out put directory.
//	 */
//	public void setImgDir(String iDir) {
//		this.imgDir = iDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Sets the css out put directory.
//	 *
//	 * @param cDir
//	 *            The css out put directory.
//	 */
//	public void setCssDir(String cDir) {
//		this.cssDir = cDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

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
	void generateTopPage() {
	String fileName = destDir + File.separator + HTML_TOP_FRAME_FILE;
	BufferedWriter bwout = null;
	try {
	    bwout = createFile(fileName);
	    startPage(bwout, "Test History Top Frame");
	    bwout.write("<body bgcolor=\"" + TOP_BGCOLOR + "\">" + c_LF);

	    generateTopContents(bwout);

	    endBody(bwout);
	    endPage(bwout);	    
	} catch (IOException e) {
	    System.out
		    .println("An error occurred while writing to the output to file: "
			    + fileName
			    + " Possible fix: check file system permissions."
			    + " Possible fix: check available disk space.");
	    e.printStackTrace();
	} finally {
	    closeFile(bwout);
	}
    }
    
    /**
     * Convenience method for closing an open buffered writer to a file. If an
     * error occurs while closing the file, it will be caught and this method
     * shall return <code>false</code>.
     * 
     * @param writer
     *            The buffered writer to close.
     * @return <code>true</code> - only if the buffer was successfully closed
     */
	void closeFile(BufferedWriter writer) {
	try {
	    writer.flush();
	    writer.close();
	} catch (IOException e) {
	    System.out
		    .println("Could not close the buffer to the output file: "
			    + " Possible fix: check file system permissions."
			    + " Possible fix: make sure the properties are correct in the UnitTH properties file.");
	    e.printStackTrace();
	}
	}

    /**
     * Creates a new file given by the fileName parameter and returns a
     * BufferedWriter. If the file exists, it will be overwritten. The file
     * should be closed by calling {@link #closeFile(BufferedWriter)}.
     * 
     * @param fileName
     *            The name of the file to create.
     * @return A buffered writer to the newly-created file or <code>null</code>
     *         if an error occurred.
     */
    protected BufferedWriter createFile(String fileName) {
	File outFile = null;
	BufferedWriter bwout = null;
	try {
	    outFile = new File(fileName);
	    outFile.createNewFile();
	    bwout = new BufferedWriter(new FileWriter(outFile));
	} catch (NullPointerException e) {
	    System.out
		    .println("Could not create the output file: "
			    + " Possible fix: make sure the full file path is correct.");
	    e.printStackTrace();
	} catch (IOException e) {
	    System.out
		    .println("Could not create the output file: "
			    + outFile.getName()
			    + " Possible fix: check file system permissions."
			    + " Possible fix: make sure the properties are correct in the UnitTH properties file.");
	    e.printStackTrace();
	}
	return bwout;
    }
	
	/**
	 * Generates the contents in the top page of the report.
	 * 
	 * @param buf
	 *            The page buffer to append to.
	 */
	void generateTopContents(BufferedWriter buf) throws IOException {
		String backIcon = UnitTH.IMAGE_DIR + "/" + UnitTH.BACK_ICON;
		String unitthLogo = UnitTH.IMAGE_DIR + "/" + UnitTH.LOGO;
		buf.write(t(1)
				+ "<TABLE width=\"98%\" cellpadding=\"0\" cellspacing=\"0\">"
				+ c_LF);
		buf.write(t(2) + "<TR>" + c_LF);

		Date time = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM);
		String generationTime = df.format(time);

		buf.write(t(3) + "<TD align=\"left\" valign=\"middle\"><a href=\""
				+ HTML_MAIN_FRAME_FILE + "\" target=\"mainpage\"><img src=\""
				+ backIcon + "\" border=\"0\"></a></TD>" + c_LF);
		buf.write(t(3) + "<TD align=\"left\" valign=\"middle\">"
				+ "<b><span style=\"color:white\">Report generated: <br>"
				+ generationTime + "</span></b></TD>" + c_LF);
		buf.write(t(3) + "<TD align=\"right\" valign=\"middle\">"
				+ "<a href=\"http://junitth.sourceforge.net\" target=\"new\">"
				+ "<img src=\"" + unitthLogo + "\" border=\"0\" alt=\"UnitTH v"
				+ UnitTH.versionNumber + "\"></a></TD>" + c_LF);
		buf.write(t(2) + "</TR>" + c_LF);
		buf.write(t(1) + "</TABLE>" + c_LF);
	}

    /**
     * Generates the frames for the index page.
     */
	void generateMainFrames() {
		String HTML_MAIN = "index.html";
		String fileName = destDir + File.separator + HTML_MAIN;
	BufferedWriter bwout = null;
	try {
	    bwout = createFile(fileName);
	    startPage(bwout, "UnitTH - Test History");
		int TOP_PAGE_HEIGHT = 36;
		bwout.write("<frameset rows=\"" + TOP_PAGE_HEIGHT
				+ ",*\" frameborder=\"0\" border=\"0\" framespacing=\"0\">"
		    + c_LF);
	    bwout.write(t(1) + "<frame src=\"" + HTML_TOP_FRAME_FILE
		    + "\" name=\"menupage\" noresize scrolling=\"no\">" + c_LF);
	    bwout.write(t(1) + "<frame src=\"" + HTML_MAIN_FRAME_FILE
		    + "\" name=\"mainpage\" marginheight=\"20\">" + c_LF);
	    bwout.write("</frameset>" + c_LF);
	    bwout.write(HtmlGenUtils.createArgsDivTag());
	    endPage(bwout);
	} catch (IOException e) {
	    System.out
		    .println("An error occurred while writing to the output to file: "
			    + fileName
			    + " Possible fix: check file system permissions."
			    + " Possible fix: check available disk space.");
	    e.printStackTrace();
	} finally {
	    closeFile(bwout);
	}
    }

    /*
     * Generates the horizontal percentage bar used for all test runs, module
     * test runs and test case pass rates. The positive pass rate is represented
     * by a green bar and the negative is represented by a red bar.
     * 
     * @param buf The buffer to append to. @param pct The pass rate on which to
     * base the bar dimensions.
     */
    protected void generatePctBar(BufferedWriter buf, Double pct)
	    throws IOException {
	
	// I we cannot do the match we present a yellow bar.
	if (pct.toString().equals("NaN")) {
	    buf.write(t(7) + t(1) + "<TD class=\"prNan\"><img src=\"images/" + UnitTH.TRANS_IMAGE
		    + "\" width=\"" + 200 + "\" height=\"12\"/></TD>" + c_LF);
	    return;
	} 
	
	// Calculate width
	int redWidth = 200 - pct.intValue() * 2;
	int greenWidth = 200 - redWidth;
		
	if (200 == greenWidth || 200 == redWidth) {
	    String color = "prpass";
	    if (200 == greenWidth) {
		color = "prpass";
		} else {
		color = "prfail";
	    }
	    buf.write(t(7) + t(1) + "<TD class=\"" + color
		    + "\"><img src=\"images/" + UnitTH.TRANS_IMAGE
		    + "\" width=\"" + 200 + "\" height=\"12\"/></TD>" + c_LF);
	} else {
	    buf.write(t(7) + t(1) + "<TD class=\"prpass\"><img src=\"images/"
		    + UnitTH.TRANS_IMAGE + "\" width=\"" + greenWidth
		    + "\" height=\"12\"/></TD>" + c_LF);
	    buf.write(t(7) + t(1) + "<TD class=\"prfail\"><img src=\"images/"
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
	void startPage(BufferedWriter buf, String pageTitle) throws IOException {
	buf.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
		+ c_LF);
	buf.write("<html>" + c_LF);
	buf.write("<head>" + c_LF);
	buf.write(t(1)
		+ "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
		+ c_LF);
	buf.write(t(1) + "<title>" + pageTitle + "</title>" + c_LF);
	buf.write(t(1)
		+ "<LINK REL =\"stylesheet\" TYPE=\"text/css\" HREF=\"css/style.css\" TITLE=\"Style\">"
		+ c_LF);
	buf.write("</head>" + c_LF);
    }

	/*
	 * Outputs the BODY start tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void startBody(BufferedWriter buf) throws IOException {
		buf.write("<body>" + c_LF);
	}

	/*
	 * Outputs the BODY end tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void endBody(BufferedWriter buf) throws IOException {
		buf.write("</body>" + c_LF);
	}

	/*
	 * Outputs the HTML end tag.
	 * 
	 * @param buf The buffer to append to.
	 */
	protected void endPage(BufferedWriter buf) throws IOException {
		buf.write("</html>" + c_LF);
		buf.flush();
	}

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Returns the report destination directory.
//	 *
//	 * @return The report destination directory.
//	 */
//	public String getDestDir() {
//		return destDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Returns the images destination directory.
//	 *
//	 * @return The images destination directory.
//	 */
//	public String getImgDir() {
//		return imgDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

// --Commented out by Inspection START (01/01/15 19:30):
//	/**
//	 * Returns the css files destination directory.
//	 *
//	 * @return The css files destination directory.
//	 */
//	public String getCssDir() {
//		return cssDir;
//	}
// --Commented out by Inspection STOP (01/01/15 19:30)

	/*
	 * Returns the first tag in a HTML-link (a href) created from the run path
	 * stored when parsing the test runs.
	 * 
	 * @param runPath The relative path for a test run. @return The first tag in
	 * a HTML link.
	 */
	protected String getHtmlReportLink(TestRun tr, String linkText) {
		String link = null;
		if (UnitTH.useAbsPaths) {
			link = tr.getAbsolutePath();
		} else {
			link = tr.getRelativePath();
		}
		
		if (link.equals("")) {
			return linkText;
		} else {
			return "<a href=\"" + link + "\">" + linkText + "</a>";
		}
	}

	/**
	 * Returns the execution time unit to display on the execution time graph
	 * header.
	 * 
	 * @return The unit as String. "ms", "sec", "min", "hours"
	 */
	public String getExecutionTimeUnit(TestItemSummary tis) {
		double largest = 0.0;
		if (null == tis) {
			largest = th.getLargestExecutionTime();
		} else {
			largest = tis.getLargestExecutionTime();
		}

		if (largest <= 1.0) {
			return "ms";
		} else if (largest <= 120.0) {
			return "sec";
		} else if (largest <= 3600.0 * 2.0) {
			return "minutes";
		} else {
			return "hours";
		}
	}

	protected void generateMainPage() { /* To be overridden. */
	}

	protected void generateModuleRunPages() { /* To be overridden. */
	}

    /**
     * Generates the header for the the table listing all the test case
     * summaries.
     * 
     * @param buf
     *            The buffer to append to.
     */
    protected void generateTestCaseHeader(BufferedWriter buf)
	    throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeftAsc\"  abbr=\"input_text\">Test&nbsp;case&nbsp;name&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
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
		+ "<TH class=\"graphHeader\" abbr=\"float\">Ave&nbsp;ET*&nbsp;&nbsp;</TH>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\" abbr=\"percent\">TOTAL</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /**
     * Generates a row in the test case list on the module page. The row display
     * various statistics regarding the summarized executions for the unique
     * test case.
     * 
     * @param buf
     *            The buffer to append to. @param tcs The test case summary
     *            where to get the statistics.
     */
    protected void generateTestCaseItem(BufferedWriter buf, TestCaseSummary tcs)
	    throws IOException {
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" NOWRAP>"
		+ tcs.getTestCaseName() + "</TD>" + c_LF);
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

    /**
     * The information that describes the list displaying the test case pass
     * rate spread.
     * 
     * @param buf
     *            The buffer to append to.
     */
    protected void generateTestCaseSpreadInfo(BufferedWriter buf,
	    boolean isModule) throws IOException {
	buf.write("<a name=\"spread\"/><H3 class=\"title\">Test case verdict spread in this ");
	if (isModule) {
	    buf.write("module");
	} else {
	    buf.write("package");
	}
	buf.write("</H3>" + c_LF);
	buf.write("This list shows the test case verdicts in the order they occured during the test runs. If a regular HTML report has been generated click on a spread item to jump directly to the test report. Legend: (green=Pass, grey=No-run, red=Fail/Error)<br/><br/>"
		+ c_LF);
    }

    /*
     * This method generates the table header for the list displaying the test
     * case pass rates.
     * 
     * @param buf The buffer to append to.
     */
    protected void generateTestCaseSpreadHeader(BufferedWriter buf) throws IOException {

	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeaderLeft\" abbr=\"input_text\">Test&nbsp;case&nbsp;name&nbsp;&nbsp;&nbsp;</TD>"
		+ c_LF);
	buf.write(t(4)
		+ "<TH class=\"graphHeader\" abbr=\"number\">Runs&nbsp;&nbsp;</TH>"
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
		+ "<TH class=\"graphHeader\" align=\"left\" colspan=\"2\">SPREAD</TH>"
		+ c_LF);
	buf.write(t(3) + "</TR>" + c_LF);
    }

    /*
     * This method generates row in the table displaying the pass rate spreads
     * for individual test cases.
     * 
     * @param buf The buffer to append to. @param tcs The test case summary
     * where to get the information and spread.
     */
    protected void generateTestCaseSpreadItem(BufferedWriter buf,
	    TestCaseSummary tcs) throws IOException {
	buf.write(t(3) + "<TR>" + c_LF);
	buf.write(t(4) + "<TD class=\"graphItemLeft\" width=\"2*\" NOWRAP>"
		+ tcs.getTestCaseName() + "</TD>" + c_LF);
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

    /*
     * Generates a horizontal bar where all the runs the test module has been
     * executed in are represented by a little colored box. The box will be
     * colored according to the test verdict for the individual test case for a
     * specific run.
     * 
     * @param buf The buffer to append to. @param tcs The test case summary
     * where to get the spread.
     */
    protected void generateSpreadBar(BufferedWriter buf, TestCaseSummary tcs)
	    throws IOException {

	String cssClass = "";
	int noRuns = th.getNoRuns();
	for (int i = 1; i <= noRuns; i++) {
	    TestCaseVerdict tcv = tcs.getSpreadAt(i);
	    if (null == tcv) {
		cssClass = "norun";
	    } else if (TestCaseVerdict.e_PASS == tcv) {
		cssClass = "pass";
	    } else if (TestCaseVerdict.e_FAIL == tcv) {
		cssClass = "fail";
	    } else if (TestCaseVerdict.e_ERROR == tcv) {
		cssClass = "error";
	    } else if (TestCaseVerdict.e_IGNORED == tcv) {
		cssClass = "ignored";
	    }

	    buf.write(t(4));
	    buf.write(t(4));
		int SPREAD_WIDTH = 6;
		int SPREAD_HEIGHT = 6;
		buf.write("<TD class=\""
				+ cssClass
		    + "\" align=\"center\">"
		    + getHtmlReportLink(th.getTestRunByIdx(i),
			    "<img title=\"Run-" + i + "\" src=\"images/"
				    + UnitTH.TRANS_IMAGE
				    + "\" border=\"0\" width=\"" + SPREAD_WIDTH
				    + "\" height=\"" + SPREAD_HEIGHT + "\">")
		    + "</TD>" + c_LF);
	}
    }
}
