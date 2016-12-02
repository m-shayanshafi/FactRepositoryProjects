package com.carolinarollergirls.scoreboard.file;

import java.io.*;
import java.util.*;
import java.text.*;
import java.nio.charset.*;

import javax.xml.transform.*;
import javax.xml.stream.*;

import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.xml.*;

public class ScoreBoardFromXmlFile extends ScoreBoardFromFile
{
	public ScoreBoardFromXmlFile(ScoreBoardModel sbm, String filename) throws IOException {
		super(filename);

		scoreBoardModel = sbm;
	}

	protected void processDocument(ScoreBoardModel model, Document document) {
		if (isRealtime())
			converter.processDocumentRealtime(model, document);
		else
			converter.processDocument(model, document);
	}

	public boolean isRealtime() { return realtime; }
	public void setRealtime(boolean r) {
		if (r)
			converter.reset();

		realtime = r;
	}

	public void start() {
		converter.reset();
		super.start();
	}

	public void stop() {
		super.stop();
		converter.reset();
	}

	protected String xmlHeader = null;
	protected void process(char[] chars, int length) {
		if ((null == chars) && (-1 < xmlBuffer.indexOf("<document"))) {
			try {
				processDocument(scoreBoardModel, editor.toDocument(xmlBuffer.toString()));
			} catch ( TransformerException tE ) {
				// The end of the file may not have a complete XML document if the save wasn't stopped before shutdown, so ignore transformer errors on the last one
			}

			return;
		}

		// FIXME - all this DOM-based individual element processing should just be replaced by a stream parser (i.e. add or convert ScoreBoardXmlConverter to stream)
		// NOTE - this doesn't check for, or enforce presence of, the XML declaration nor the top level ScoreBoardStream element.  It just jumps from <document to <document
		xmlBuffer.append(chars, 0, length);
		// HACK - grab the doc xml declaration to use later for each individual doc (element).  See above about needing to use stream parsing instead.
		if (null == xmlHeader)
			xmlHeader = xmlBuffer.substring(xmlBuffer.indexOf("<?xml"), xmlBuffer.indexOf("?>")+2);
		int docEnd;
		while (0 < (docEnd = xmlBuffer.indexOf("</document>"))) {
			docEnd += "</document>".length();
			String doc = xmlHeader + xmlBuffer.substring(xmlBuffer.indexOf("<document"), docEnd);
			xmlBuffer.delete(0, docEnd);
			try {
				processDocument(scoreBoardModel, editor.toDocument(doc));
			} catch ( TransformerException tE ) {
				System.err.println("TransformerException converting document : "+tE.getMessage());
				System.err.println("Document :");
				System.err.println(doc);
			}
		}
	}


	protected StringBuffer xmlBuffer = new StringBuffer();
	protected ScoreBoardModel scoreBoardModel = null;

	protected boolean realtime = true;

	protected RealtimeScoreBoardXmlConverter converter = new RealtimeScoreBoardXmlConverter();
	protected XmlDocumentEditor editor = new XmlDocumentEditor();
}
