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
 * $Id:$
 * -----------------------------------------------------------------------
 * yymmdd | author | comment
 * =======================================================================
 */
package unitth.fitnesse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import unitth.core.TestItemUtils;
import unitth.core.UnitTH;
import unitth.core.UnitTHException;

public class FitNesseReportParser extends DefaultHandler {

	private final String c_XML_TAG_TEST_REFERENCE = "pageHistoryReference";
	private final String c_XML_TAG_TEST_HISTORY = "pageHistoryLink";
	private final String c_XML_TAG_TEST_NAME = "name";
	private final String c_XML_TAG_TEST_DATE = "date";
	private final String c_XML_TAG_TEST_RIGHT = "right";
	private final String c_XML_TAG_TEST_WRONG = "wrong";
	private final String c_XML_TAG_TEST_IGNORES = "ignores";
	private final String c_XML_TAG_TEST_EXCEPTIONS = "exceptions";
	private final String c_XML_TAG_TEST_SUITE_END = "suiteResults";

	/** The SAX parser factory used for reading in the order files. */
	private static SAXParserFactory saxFactory = SAXParserFactory.newInstance();
	/** The SAXParser instance used for all the parsing. */
	private SAXParser saxp = null;

	private String currentElement = "";
	private RootTestSuite currentTestSuite = null;
	private TestRun currentTestRun = null;
	private TestCase currentTestCase = null;
	private TestCase previousTestCase = null;
	private TestHistory history = null;
	private int currentRunIdx = 0;
	private File currentFile = null;
	private boolean inTestCase = false;

	public FitNesseReportParser() {
		try {
			saxp = saxFactory.newSAXParser();
			history = new TestHistory();
		} catch (SAXException t) {
			t.printStackTrace();
			System.err.println("SAX parsing errors...");
			return;
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.println("Unknown exception...");
			return;
		}
	}

	public void startElement(String namespaceURI, String sName, String qName,
			Attributes attrs) throws SAXException {

		currentElement = sName; // element name

		// We do not know which name was used, simple or qualified
		if ("".equals(currentElement)) {
			currentElement = qName; // not namespace-aware
		}

		if (c_XML_TAG_TEST_NAME.equalsIgnoreCase(currentElement)) {
			currentTestCase = new TestCase();
		} else if (c_XML_TAG_TEST_REFERENCE.equalsIgnoreCase(currentElement)) {
			inTestCase = true;
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		// TODO, optimization lots of new String.
		if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_NAME)) {
			currentTestCase.setNameFromFullName(new String(ch, start, length));

			// 1) Check if this is the first test in the file, then the test
			// suite name needs to be set.
			// 2) The root test suite name needs to be set to the shortest
			// commonality from the last dot. If the test suite names differ
			// we get the shortest match ending with a dot.
			if (currentTestSuite.getName().equalsIgnoreCase("undef")) {
				currentTestSuite.setNameFromTestCaseName(new String(ch, start,
						length));
			} else if (!currentTestSuite.getName().equalsIgnoreCase(
					(TestItemUtils
							.getFitNesseTestSuiteFromTestCaseName(new String(
									ch, start, length))))) {
				String newTestSuiteName = TestItemUtils.getShortestCommonality(
						currentTestSuite.getName(), new String(ch, start,
								length));
				currentTestSuite.setName(newTestSuiteName);
			}
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_DATE)) {
			currentTestCase.setRunDate(new String(ch, start, length));
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_RIGHT)
				&& inTestCase) {
			currentTestCase.setRights(new String(ch, start, length));
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_WRONG)
				&& inTestCase) {
			currentTestCase.setWrongs(new String(ch, start, length));
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_IGNORES)
				&& inTestCase) {
			currentTestCase.setIgnores(new String(ch, start, length));
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_EXCEPTIONS)
				&& inTestCase) {
			currentTestCase.setExceptions(new String(ch, start, length));
		} else if (currentElement.equalsIgnoreCase(c_XML_TAG_TEST_HISTORY)
				&& inTestCase) {
			currentTestCase.setHistoryLink(new String(ch, start, length));
		}
	}

	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {
		String eName = sName; // element name

		// We do not know which name was used, simple or qualified
		if ("".equals(eName)) {
			eName = qName; // not namespace-aware
		}

		// Test case name
		if (c_XML_TAG_TEST_NAME.equalsIgnoreCase(eName)) {
			if (null != currentTestCase) {
				currentTestRun.addTestCase(currentTestCase);
			}
		} else if (c_XML_TAG_TEST_SUITE_END.equalsIgnoreCase(eName)) {
			if (null != currentTestCase) {
				previousTestCase = null;
			}
		} else if (c_XML_TAG_TEST_REFERENCE.equals(eName)) {
			inTestCase = false;
			previousTestCase = currentTestCase;
		}
		// Resetting to avoid having to deal with white spaces
		// elsewhere in the parsed file.
		currentElement = "";
	}

	public TestHistory parseRuns(ArrayList<ArrayList<File>> testRunFiles) {
		currentRunIdx = 1; // Counter for the directories list.
		for (ArrayList<File> files : testRunFiles) {
			currentTestSuite = new RootTestSuite();
			parseFiles(files);
			currentTestRun.setRunIndex(currentRunIdx);
			currentRunIdx++;
			history.addRootTestSuite(currentTestSuite);
			currentTestSuite = null;
		}
		return history;
	}

	private void parseFiles(ArrayList<File> files) {
		for (File f : files) {
			currentTestRun = new TestRun();
			parseFile(f);
			try {
				currentTestRun.setFileName(f.getCanonicalPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentTestSuite.addTestRun(currentTestRun);
		}
	}

	private void parseFile(File file) {

		if (UnitTH.c_DBG) {
			System.out.println("Parsing file: " + file.getName());
		}
		try {
			currentFile = file;
			parse(file);
		} catch (UnitTHException jte) {
			// If we end up here the test run will never be added
			System.err
					.println("Test run was never added to test run since the parsed file ("
							+ file.getName() + ") contained errors.");
		}
	}

	private void parse(File file) throws UnitTHException {
		try {
			saxp.parse(file, this);
		} catch (IOException ioe) {
			System.err.println("IO errors occured...");
			throw new UnitTHException("IO errors occured...");
		} catch (SAXException saxe) {
			System.err.println("SAX parsing errors... omitting test module");
			throw new UnitTHException("SAX parsing errors...");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unknown exception...");
			throw new UnitTHException("Unknown exception...");
		}
	}
}
