package com.carolinarollergirls.scoreboard.file;

import java.io.*;
import java.util.*;
import java.nio.charset.*;

import javax.xml.parsers.*;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.xml.*;

import org.w3c.dom.*;

public class ScoreBoardToXmlFile extends ScoreBoardToFile implements ScoreBoardListener
{
	public ScoreBoardToXmlFile(ScoreBoard sb, String filename) throws Exception {
		super(filename.endsWith(".xml") ? filename.substring(0, filename.length()-4) : filename, "xml");

		scoreBoard = sb;

		transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	}

	public boolean start() throws Exception {
		synchronized (this) {
			if (!running) {
				printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), Charset.forName("UTF-8"))));
				streamResult = new StreamResult(printWriter);
				// I would rather use programmatic means to do this,
				// but I can't find anything that allows manually creating a toplevel element
				// and then streaming in subelements (and then eventually closing the element/doc)
				printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				printWriter.println("<ScoreBoardStream version=\"0.1.4\">");
				scoreBoard.addScoreBoardListener(this);
				running = true;
			}
		}

		return running;
	}

	public boolean stop() throws Exception {
		synchronized (this) {
			if (running) {
				running = false;
				scoreBoard.removeScoreBoardListener(this);
				synchronized (transformer) {
					printWriter.println("</ScoreBoardStream>");
					printWriter.flush();
					printWriter.close();
					streamResult = null;
				}
			}
		}

		return !running;
	}

	public boolean isRunning() { return running; }

	public void xmlDocumentChange(Document d) {
		synchronized (transformer) {
			if (running) {
				DOMSource source = new DOMSource(d.getDocumentElement());
				try {
					transformer.transform(source, streamResult);
				} catch ( TransformerException tE ) {
					System.err.println("Could not transform DOM document to XML stream! : " + tE.getMessage());
					tE.printStackTrace();
				}
			}
		}
	}

	public void scoreBoardChange(ScoreBoardEvent event) {
		xmlDocumentChange(visitor.toDocument(event));
	}

	protected boolean running = false;
	protected ScoreBoard scoreBoard = null;
	protected Transformer transformer = null;
	protected PrintWriter printWriter = null;
	protected StreamResult streamResult = null;
	protected ScoreBoardEventXmlVisitor visitor = new ScoreBoardEventXmlVisitor();
}
