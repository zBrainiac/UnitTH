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
 * $Id: ReportParser.java,v 1.7 2010/05/12 23:42:30 andnyb Exp $
 * -----------------------------------------------------------------------
 * 090208 | Nyberg  | Fixed the naming of test cases when a class name is
 *                  | not provided. Also fixed the setting of a test run
 *                  | time stamp from a file creation date when no test
 *                  | run time stamp has been provided.
 * =======================================================================
 */
package unitth.junit;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import unitth.core.ReportParser;
import unitth.core.UnitTH;
import unitth.core.UnitTHException;
import unitth.jenkins.JenkinsReportParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is responsible for handling all the parsing of all JUnit files. It
 * extends the <code>DefaultHandler</code> and the methods needed for parsing
 * the XML in the JUnit reports.
 * 
 * @author andnyb
 */
public class JUnitReportParser extends ReportParser {

	/** The SAX parser factory used for reading in the order files. */
	private static SAXParserFactory saxFactory = SAXParserFactory.newInstance();
	/** The SAXParser instance used for all the parsing. */
	private SAXParser saxp = null;

	private final String c_XML_TAG_TESTSUITE = "testsuite";
	private final String c_XML_TAG_TESTCASE = "testcase";

	private TestModule currentTestModule = null;
	private TestCase currentTestCase = null;
	private TestHistory history = null;
	private HashMap<String, TestPackage> parsedTestPackages = null;

	/**
	 * The ReportParser Ctor, creates a new parser and initializes the test
	 * history to put all the parsed contents into.
	 */
	public JUnitReportParser() {
		try {
			saxp = saxFactory.newSAXParser();
			history = new TestHistory();
		} catch (SAXException t) {
			System.err.println("Could not create SAX parser... "+t.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.println("Unknown exception... "+t.getMessage());
		}
	}

	/**
	 * This method only bothers about the few elements that are needed for the
	 * collection of statistics, Testsuite, Testcase.
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String sName, String qName,
			Attributes attrs) throws SAXException {

		String eName = sName; // element name

		// We do not know which name was used, simple or qualified
		if ("".equals(eName)) {
			eName = qName; // not namespace-aware
		}

		String c_XML_TAG_FAILURE = "failure";
		String c_XML_TAG_ERROR = "error";
		String c_XML_TAG_PROPERTY = "property";
		if (c_XML_TAG_TESTSUITE.equals(eName)) {

			currentTestModule = new TestModule();
			String c_XML_TAG_TIMESTAMP = "timestamp";
			if (attrs.getIndex(c_XML_TAG_TIMESTAMP) != -1) {
				currentTestModule.setDate(attrs.getValue(c_XML_TAG_TIMESTAMP));
			}

			String c_XML_TAG_ERRORS = "errors";
			currentTestModule.setNoErrors(attrs.getValue(c_XML_TAG_ERRORS));
			String c_XML_TAG_FAILURES = "failures";
			currentTestModule.setNoFailures(attrs.getValue(c_XML_TAG_FAILURES));
			String c_XML_TAG_SKIPPED = "skipped";
			currentTestModule.setNoIgnored(attrs.getValue(c_XML_TAG_SKIPPED));
			String c_XML_TAG_TESTS = "tests";
			currentTestModule.setNoTestCases(attrs.getValue(c_XML_TAG_TESTS)); // Will be overwritten if the class has been ignored
			String c_XML_TAG_TIME = "time";
			currentTestModule.setExecutionTime(attrs.getValue(c_XML_TAG_TIME));
			String c_XML_TAG_NAME = "name";
			currentTestModule.setName(attrs.getValue(c_XML_TAG_NAME));

			if (UnitTH.c_DBG) {
				System.out.println("The attributes of the testsuite element.");
				System.out.println("errors: " + attrs.getValue("errors"));
				System.out.println("failures: " + attrs.getValue("failures"));
				System.out.println("name: " + attrs.getValue("name"));
				System.out.println("tests: " + attrs.getValue("tests"));
				System.out.println("time: " + attrs.getValue("time"));
				System.out.println("timestamp: " + attrs.getValue("timestamp")
						+ "\n");
			}
		} else if (c_XML_TAG_PROPERTY.equals(eName)) {
			String c_XML_TAG_PROPERTY_NAME = "name";
			String str = attrs.getValue(c_XML_TAG_PROPERTY_NAME);
			if (null != str) {
				String c_XML_TAG_LAUNCH_TIMESTAMP = "launch.timestamp";
				if (str.equals(c_XML_TAG_LAUNCH_TIMESTAMP)
						&& null == currentTestModule.getExecutionDate()) {
					String c_XML_TAG_PROPERTY_VALUE = "value";
					currentTestModule.setDate(attrs
							.getValue(c_XML_TAG_PROPERTY_VALUE));
				}
			}
		} else if (c_XML_TAG_TESTCASE.equals(eName)) {
			if (!attrs.getValue("name").equalsIgnoreCase("initializationError")) {
				currentTestCase = new TestCase();
				currentTestCase.setName(attrs.getValue("name"));
				// If a class name has not been provided for the test case the
				// test
				// module name can be used instead.
				currentTestCase
						.setClassName(attrs.getValue("classname") != null ? attrs
								.getValue("classname")
								: currentTestModule.getName());
				currentTestCase.setModuleName(currentTestModule.getName());
				currentTestCase.setExecutionTime(attrs.getValue("time"));
			}
		} else if (c_XML_TAG_FAILURE.equals(eName)) {
			currentTestCase.setVerdict(TestCaseVerdict.e_FAIL);
		} else if (c_XML_TAG_ERROR.equals(eName)) {
			currentTestCase.setVerdict(TestCaseVerdict.e_ERROR);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {

		String eName = sName; // element name

		// We do not know which name was used, simple or qualified
		if ("".equals(eName)) {
			eName = qName; // not namespace-aware
		}
		String c_XML_TAG_IGNORE = "skipped";
		if (c_XML_TAG_TESTSUITE.equals(eName)) {
			// void
		} else if (c_XML_TAG_TESTCASE.equals(eName)) {
			if (null != currentTestCase) {
				currentTestModule.addTestCase(currentTestCase);
			}
		} else if (c_XML_TAG_IGNORE.equals(eName)) {
			if (null != currentTestCase) {
				currentTestCase.setVerdict(TestCaseVerdict.e_IGNORED);
			}
			
			// Entire class has been ignored
			if (currentTestCase != null ? currentTestCase.getName().equals(currentTestModule.getName()) : false) {
				currentTestModule.setAsIgnored();
				currentTestModule.setNoTestCases("0"); // TODO, valid fix or not
			}
		}
	}

	/**
	 * This method calls the SAXParser parse method.
	 * 
	 * @param file
	 *            The file to be parsed.
	 * @throws UnitTHException
	 */
	private void parse(File file) throws UnitTHException {
		try {
			saxp.parse(file, this);
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			System.err.println("IO errors occured...");
			throw new UnitTHException("IO errors occured...");
		} catch (SAXException saxe) {
			throw new UnitTHException("SAXException ..."+saxe.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unknown exception...");
			throw new UnitTHException("Unknown exception...");
		}
	}

	/**
	 * Initiates the parsing of all runs. The method also sets an index for the
	 * run.
	 * 
	 * @param testRunFiles
	 *            A list of files to be parsed.
	 * @param dirs
	 *            A list of folders where the files to e parsed can be found.
	 * @return The filled <code>RunHistory</code> data structure.
	 */
	public TestHistory parseRuns(ArrayList<ArrayList<File>> testRunFiles,
			ArrayList<String> dirs) {

		int currentRunIdx = 1;
		for (ArrayList<File> files : testRunFiles) {
			try {
				TestRun currentTestRun = new TestRun();
				parsedTestPackages = new HashMap<String, TestPackage>();
				// Populate the current test run object and return
				// the relative path to the parsed files
				parseFiles(files);
				// -1 since the runs are in an array starting at ZERO
				currentTestRun.setRunPath(dirs.get(currentRunIdx - 1));
				currentTestRun.addTestPackages(parsedTestPackages);
				history.addTestRun(currentTestRun);

				currentTestRun = null;
				parsedTestPackages = null;
			} catch (UnitTHException jthe) {
				jthe.printStackTrace();
			}
			currentRunIdx++;
		}
		return history;
	}

	/**
	 * Calls parseFile for each of the files in a run.
	 * 
	 * @see JenkinsReportParser.parseFile
	 * 
	 * @param files
	 *            The files to be parsed
	 */
	private void parseFiles(ArrayList<File> files) {
		for (File f : files) {
			parseFile(f);
		}
	}

	/**
	 * Parses a file and adds the statistics to a <code>TestPackage</code>.
	 * 
	 * @param file
	 *            The file to be parsed.
	 */
	private void parseFile(File file) {

		if (UnitTH.c_DBG) {
			System.out.println("Parsing file: " + file.getName());
		}
		try {
			parse(file);

			// Check if anything has been parsed at all.
			if (currentTestModule==null) {
				System.err.println("The file '"+file.getAbsolutePath()+"' did not get parsed sucessfully. Expected a JUnit report XML-file.");
				return;
			}
			
			// If there are no time stamps included in the report format the
			// last modification time of the parsed file will be used.
			if (null == currentTestModule.getExecutionDate()) {
				long fileLastModified = file.lastModified();
				currentTestModule.setDate(fileLastModified);
			}

			addToPackage(currentTestModule);
			if (UnitTH.c_DBG) {
				System.out.println("Module: " + currentTestModule.getName()
						+ " added to run: " + currentTestModule.toString());
			}

			// currentTestRun.addTestPackage(parsedTestPackages);
			currentTestModule = null;
		} catch (UnitTHException jte) {
			// If we end up here the test module will never be added
			System.err
					.println("Test module never added to test run since the parsed file ("
							+ file.getAbsolutePath() + ") contained errors.");
			System.err
			.println(jte.getMessage());
		}
	}

	public void addToPackage(TestModule tm) {

		String packageName = tm.getPackageName();

		TestPackage tp = parsedTestPackages.get(packageName);

		// Package does not exist, creating it.
		if (tp == null) {
			tp = new TestPackage();
			tp.setName(packageName);
			parsedTestPackages.put(packageName, tp);
		}
		tp.addTestModule(tm);
	}
}

/* eof */
