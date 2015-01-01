package unitth.core;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FileCheckingParser extends DefaultHandler {

	private final String c_XML_TAG_FITNESSE_TESTSUITE = "suiteResults";
	private final String c_XML_TAG_TEST_REFERENCE = "pageHistoryReference";
	
	/** The SAX parser factory used for reading in the order files. */
	private static SAXParserFactory saxFactory = SAXParserFactory.newInstance();
	/** The SAXParser instance used for all the parsing. */
	private SAXParser saxp = null;
	
	public boolean suiteResults = false;
	public boolean testCasesExist = false;
	
	public FileCheckingParser() {
		try {
			saxp = saxFactory.newSAXParser();
		} catch (Throwable t) {
			t.printStackTrace();
			System.err.println("Unknown exception...");
			return;
		}
	}
	
	public void startElement(String namespaceURI, String sName, String qName,
			Attributes attrs) throws SAXException {

		String currentElement = sName; // element name

		// We do not know which name was used, simple or qualified
		if ("".equals(currentElement)) {
			currentElement = qName; // not namespace-aware
		}

		// We can only check one thing here, otherwise we will overwrite the verdict
		// in cases where the tag does not match.
		if (c_XML_TAG_FITNESSE_TESTSUITE.equalsIgnoreCase(currentElement)) {
			suiteResults = true;
		}
		if (c_XML_TAG_TEST_REFERENCE.equalsIgnoreCase(currentElement)) {
			testCasesExist = true;
		}
	}
	
	public boolean parse(File file) {
		suiteResults = false;
		testCasesExist = false;
		try {
			saxp.parse(file, this);
		} catch (Exception e) {
			// Silent treatment of thrown exceptions. A thrown exception
			// must be interpreted as a non valid file. 
		}
		if (suiteResults && testCasesExist) {
			return true;
		} else {
			return false;
		}
	}
}
