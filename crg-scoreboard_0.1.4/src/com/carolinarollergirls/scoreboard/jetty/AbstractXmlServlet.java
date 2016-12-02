package com.carolinarollergirls.scoreboard.jetty;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.text.*;

import javax.imageio.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.xml.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.file.*;
import com.carolinarollergirls.scoreboard.defaults.*;

public abstract class AbstractXmlServlet extends AbstractRegisterServlet
{
	protected void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			XmlListener listener = createXmlListener(request.getParameter("source"));
			String key = addRegisteredListener(listener);
			response.setContentType("text/xml");
			response.getWriter().print(editor.toString(editor.createDocument("Key", null, key)));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch ( TransformerException tE ) {
			response.getWriter().print(tE.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void getListenerInfo(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		Document doc = editor.createDocument();
		synchronized (clientMap) {
			Iterator listeners = clientMap.values().iterator();
			while (listeners.hasNext())
				((XmlListener)listeners.next()).addInfo(doc.getDocumentElement());
		}

		response.setContentType("text/xml");
		response.getWriter().print(editor.toString(doc));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void getFiles(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		Document doc = editor.createDocument();

		Element files = editor.addElement(doc.getDocumentElement(), "Files", "Saved");
		List list = AbstractScoreBoardFileIO.getFiles();
		for (int i=0; i<list.size(); i++) {
			File file = (File)list.get(i);
			Element fe = editor.addElement(files, "File", file.getName());
			editor.addElement(fe, "Name", null, file.getName());
			editor.addElement(fe, "Size", null, Long.toString(file.length()));
			editor.addElement(fe, "Date", "formatted", DateFormat.getInstance().format(new Date(file.lastModified())));
			editor.addElement(fe, "Date", "ms", Long.toString(file.lastModified()));
		}

		files = editor.addElement(doc.getDocumentElement(), "Files", "Saving");
		synchronized (this) {
//FIXME - change this when this servlet is updated to allow multiple concurrent save-to-files
			if (null != scoreBoardToXmlFile) {
				File file = scoreBoardToXmlFile.getFile();
				Element fe = editor.addElement(files, "File", file.getName());
				editor.addElement(fe, "Name", null, file.getName());
				editor.addElement(fe, "Size", null, Long.toString(file.length()));
				editor.addElement(fe, "Date", "formatted", DateFormat.getInstance().format(new Date(file.lastModified())));
				editor.addElement(fe, "Date", "ms", Long.toString(file.lastModified()));
			}
		}

		response.setContentType("text/xml");
		response.getWriter().print(editor.toString(doc));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean start = true;
		if (null != request.getParameter("start"))
			start = Boolean.parseBoolean(request.getParameter("start"));
		String filename = request.getParameter("filename");
		if (null == filename || "".equals(filename)) {
			response.getWriter().print("No filename specified");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		synchronized (this) {
//FIXME - add hashtable here, or in savetofile class, to handle multiple saves instead of a single instance here.
			if (start && (null == scoreBoardToXmlFile)) {
				scoreBoardToXmlFile = createScoreBoardToXmlFile(scoreBoardModel, filename);
				scoreBoardToXmlFile.start();
			}
			if (!start && (null != scoreBoardToXmlFile)) {
				scoreBoardToXmlFile.stop();
				scoreBoardToXmlFile = null;
			}
		}

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void load(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		boolean start = true;
		if (null != request.getParameter("start"))
			start = Boolean.parseBoolean(request.getParameter("start"));
		String filename = request.getParameter("filename");
		if (null == filename || "".equals(filename)) {
			response.getWriter().print("No filename specified");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		synchronized (this) {
//FIXME - add hashtable here, or in loadfromfile class, to handle multiple saves instead of a single instance here.
			if (start && (null == scoreBoardFromXmlFile)) {
				scoreBoardFromXmlFile = createScoreBoardFromXmlFile(scoreBoardModel, filename);
				scoreBoardFromXmlFile.start();
			}
			if (!start && (null != scoreBoardFromXmlFile)) {
				scoreBoardFromXmlFile.stop();
				scoreBoardFromXmlFile = null;
			}
		}

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		super.doPost(request, response);

		try {
			if ("/getListenerInfo".equals(request.getPathInfo()))
				getListenerInfo(request, response);

			if ("/getFiles".equals(request.getPathInfo()))
				getFiles(request, response);

			if ("/save".equals(request.getPathInfo()))
				save(request, response);

			if ("/load".equals(request.getPathInfo()))
				load(request, response);
		} catch ( Exception e ) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	protected XmlListener getXmlListenerForRequest(HttpServletRequest request) {
		return (XmlListener)getRegisteredListenerForRequest(request);
	}

	protected abstract XmlListener createXmlListener(String source);

	protected XmlListener createXmlListener() { return createXmlListener(""); }

	protected ScoreBoardToXmlFile createScoreBoardToXmlFile(ScoreBoardModel model, String filename) throws Exception {
		return new ScoreBoardToXmlFile(model, filename);
	}

	protected ScoreBoardFromXmlFile createScoreBoardFromXmlFile(ScoreBoardModel model, String filename) throws IOException {
		return new ScoreBoardFromXmlFile(model, filename);
	}

	protected XmlDocumentEditor editor = new XmlDocumentEditor();
	protected ScoreBoardXmlConverter converter = new ScoreBoardXmlConverter();

	protected ScoreBoardToXmlFile scoreBoardToXmlFile = null;
	protected ScoreBoardFromXmlFile scoreBoardFromXmlFile = null;

	protected class XmlListener extends RegisteredListener implements ScoreBoardListener
	{
		public void scoreBoardChange(ScoreBoardEvent event) {
			synchronized (getDocumentLock()) {
				event.accept(visitor);
				setEmpty(false);
			}
		}

		public Document getDocument() {
			synchronized (getDocumentLock()) {
				return visitor.toDocument();
			}
		}

		public Document resetDocument() {
			synchronized (getDocumentLock()) {
				Document document = visitor.toDocument();
				visitor.resetDocument();
				setEmpty(true);
				return document;
			}
		}

		public Object getDocumentLock() { return documentLock; }

		public boolean isEmpty() { return empty; }

		public void setEmpty(boolean e) { empty = e; }

		public void addInfo(Element node) {
			Element e = editor.addElement(node, "Listener", getKey());
			editor.addElement(e, "Source", null, getSource());
			editor.addElement(e, "LastRequestTime", null, Long.toString(new Date().getTime() - getLastRequestTime()));
		}

		protected Object documentLock = new Object();
		protected ScoreBoardEventXmlVisitor visitor = new ScoreBoardEventXmlVisitor();
		protected boolean empty = true;
	}
}
