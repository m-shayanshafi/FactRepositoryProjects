package com.carolinarollergirls.scoreboard.jetty;

import java.io.*;
import java.text.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.*;
import java.awt.image.*;

import javax.imageio.*;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import org.xml.sax.*;

import com.carolinarollergirls.scoreboard.*;
import com.carolinarollergirls.scoreboard.xml.*;
import com.carolinarollergirls.scoreboard.event.*;
import com.carolinarollergirls.scoreboard.model.*;
import com.carolinarollergirls.scoreboard.file.*;
import com.carolinarollergirls.scoreboard.policy.*;
import com.carolinarollergirls.scoreboard.defaults.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.*;

public class XmlScoreBoardServlet extends AbstractXmlServlet
{
	public String getPath() { return "/XmlScoreBoard"; }

	protected void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		Document d = converter.toDocument(scoreBoardModel);
		String key = request.getParameter("document");
		Document sourceDocument = documentManager.getDefaultDocument();
		editor.mergeDocuments(d, sourceDocument);

		response.setContentType("text/xml");
		response.getWriter().print(editor.toString(d));
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		String key;
		XmlListener listener = null;
		synchronized (clientMap) {
			if ((null != (key = request.getParameter("key"))) && (null != (listener = (XmlListener)clientMap.get(key)))) {
				if (listener.isEmpty()) {
					response.setContentType("text/plain");
					response.setStatus(HttpServletResponse.SC_OK);
					return;
				}

				response.setContentType("text/xml");
				response.getWriter().print(editor.toString(listener.resetDocument()));
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}
	}

	protected void updateAllDocuments(Document document) {
		documentManager.mergeDocument(document);

		updateListenerDocuments(document);

// FIXME - once the superclass is changed to handle multiple save-to-file instances this needs to change to work with all instances
		try { scoreBoardToXmlFile.xmlDocumentChange(document); } catch ( NullPointerException npE ) { }
	}

	protected void updateListenerDocuments(Document document) {
		synchronized (clientMap) {
			Iterator listeners = clientMap.values().iterator();
			while (listeners.hasNext()) {
				try {
					XmlListener listener = (XmlListener)listeners.next();
					synchronized (listener.getDocumentLock()) {
						editor.mergeDocuments(listener.getDocument(), document);
						listener.setEmpty(false);
					}
				} catch ( ClassCastException ccE ) { }
			}		
		}
	}

	protected void reloadListeners(HttpServletRequest request, HttpServletResponse response) {
		updateListenerDocuments(editor.createDocument("Reload"));
		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void set(HttpServletRequest request, HttpServletResponse response) throws IOException,TransformerException {
		Document requestDocument = null;

		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				ServletFileUpload upload = new ServletFileUpload();

				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					if (!item.isFormField()) {
						InputStream stream = item.openStream();
						requestDocument = editor.toDocument(stream);
						stream.close();
						break;
					}
				}
			} else {
				requestDocument = editor.toDocument(request.getReader());
			}
		} catch ( FileUploadException fuE ) {
			response.getWriter().print(fuE.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} catch ( SAXException sE ) {
			response.getWriter().print(sE.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (null == requestDocument) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		/* This should clear the scoreboard to prepare for loading a new one */
		/* This does not work with continuous-save-to-file! */
		if (Boolean.parseBoolean(request.getParameter("clearScoreBoard"))) {
			reloadListeners(request, response);

			Document d = converter.toDocument(scoreBoardModel);
			documentManager.replaceDocument(d);
		}

		converter.processDocument(scoreBoardModel, requestDocument);

		updateAllDocuments(requestDocument);

		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		super.doPost(request, response);

		try {
			if ("/get".equals(request.getPathInfo()))
				get(request, response);
			else if ("/set".equals(request.getPathInfo()))
				set(request, response);
			else if ("/reloadViewers".equals(request.getPathInfo()))
				reloadListeners(request, response);
			else if (request.getPathInfo().endsWith(".xml"))
				getAll(request, response);
		} catch ( TransformerException tE ) {
			response.getWriter().print(tE.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected XmlListener createXmlListener(String source) {
		XmlListener listener = new XmlListener();
		listener.setSource(source);
		Document document = listener.getDocument();
		Document sourceDocument = documentManager.getDocument(source);
		editor.mergeDocuments(document, sourceDocument);
		listener.setEmpty(false);
		return listener;
	}

	public void setScoreBoardModel(ScoreBoardModel model) {
//FIXME - no events for policy adding/removing; need to implement that.
		model.addPolicyModel(new ScoreBoardHtmlIntermissionNamePolicy());		
		model.addPolicyModel(new ScoreBoardAdControlPolicy());		

		super.setScoreBoardModel(model);
		ScoreBoardListener listener = new ScoreBoardListener() {
				public void scoreBoardChange(ScoreBoardEvent event) {
					documentManager.mergeDocument(visitor.toDocument(event));
				}
				private ScoreBoardEventXmlVisitor visitor = new ScoreBoardEventXmlVisitor();
			};
		converter.processDocument(model, documentManager.getDefaultDocument());
		documentManager.mergeDocument(converter.toDocument(model));
		model.addScoreBoardListener(listener);
	}

	protected ScoreBoardToXmlFile createScoreBoardToXmlFile(ScoreBoardModel model, String filename) throws Exception {
		return new ScoreBoardToXmlFile(model, filename) {
			public boolean start() throws Exception {
				if (!super.start())
					return false;
				xmlDocumentChange(documentManager.getDefaultDocument());
				return true;
			}
		};
	}

	protected ScoreBoardFromXmlFile createScoreBoardFromXmlFile(ScoreBoardModel model, String filename) throws IOException {
		return new ScoreBoardFromXmlFile(model, filename) {
			protected void processDocument(ScoreBoardModel model, Document document) {
				super.processDocument(model, document);

				updateAllDocuments(document);
			}
		};
	}

	protected XmlDocumentManager documentManager = XmlDocumentManager.getInstance();

	// FIXME - don't much like this; we should not have page-specific stuff up here.
	public class ScoreBoardAdControlPolicy extends DefaultPolicyModel
	{
		public ScoreBoardAdControlPolicy() {
			super("scoreboard.html AdControlPolicy");
			setName(getId());

			Element sb = editor.getElement(documentManager.getDefaultDocument().getDocumentElement(), "ScoreBoard");
			Element sbpage = editor.getElement(sb, "Page", "scoreboard.html");

			Element ad_show_during_intermission = editor.getElement(sbpage, "AdsShowDuringIntermission");
			Element ad_use_lineup_clock = editor.getElement(sbpage, "AdsUseLineupClock");
			Element ad_random_order = editor.getElement(sbpage, "AdsRandomOrder");
			Element ad_display_seconds = editor.getElement(sbpage, "AdsDisplaySeconds");
			Element ad_auto_change = editor.getElement(sbpage, "AutoScoreBoardAdChange");

			if (null == editor.getContent(ad_show_during_intermission))
				editor.setContent(ad_show_during_intermission, String.valueOf(false));
			if (null == editor.getContent(ad_use_lineup_clock))
				editor.setContent(ad_use_lineup_clock, String.valueOf(true));
			if (null == editor.getContent(ad_random_order))
				editor.setContent(ad_random_order, String.valueOf(true));
			try {
				Integer.parseInt(editor.getContent(ad_display_seconds));
			} catch ( NumberFormatException nfE ) {
				editor.setContent(ad_display_seconds, String.valueOf(5));
			}
			if (null == editor.getContent(ad_auto_change))
				editor.setContent(ad_auto_change, String.valueOf(true));

			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, AD_SHOW_DURING_INTERMISSION, "Boolean", editor.getContent(ad_show_during_intermission)));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, AD_USE_LINEUP_CLOCK, "Boolean", editor.getContent(ad_use_lineup_clock)));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, AD_RANDOM_ORDER, "Boolean", editor.getContent(ad_random_order)));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, AD_DISPLAY_SECONDS, "Integer", editor.getContent(ad_display_seconds)));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, AD_AUTO_CHANGE, "Boolean", editor.getContent(ad_auto_change)));

			new DefaultPolicyParameterEventVisitor(getParameter(AD_SHOW_DURING_INTERMISSION)) {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					changePageElement("AdsShowDuringIntermission", value);					
				}
			};
			new DefaultPolicyParameterEventVisitor(getParameter(AD_USE_LINEUP_CLOCK)) {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					changePageElement("AdsUseLineupClock", value);
				}
			};
			new DefaultPolicyParameterEventVisitor(getParameter(AD_RANDOM_ORDER)) {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					changePageElement("AdsRandomOrder", value);
				}
			};
			new DefaultPolicyParameterEventVisitor(getParameter(AD_DISPLAY_SECONDS)) {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					changePageElement("AdsDisplaySeconds", value);
				}
			};
			new DefaultPolicyParameterEventVisitor(getParameter(AD_AUTO_CHANGE)) {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					changePageElement("AutoScoreBoardAdChange", value);
				}
			};
		}

		protected void changePageElement(String pageElementName, String pageElementValue) {
			Document doc = editor.createDocument("ScoreBoard");
			Element sb = editor.getElement(doc.getDocumentElement(), "ScoreBoard");
			Element page = editor.addElement(sb, "Page", "scoreboard.html");
			editor.addElement(page, pageElementName, null, pageElementValue);
			updateAllDocuments(doc);
		}

		public static final String AD_SHOW_DURING_INTERMISSION = "Show Ads During Intermission";
		public static final String AD_USE_LINEUP_CLOCK = "Ad Change Use Lineup Clock";
		public static final String AD_RANDOM_ORDER = "Show Ads in Random Order";
		public static final String AD_DISPLAY_SECONDS = "Ad Display Seconds";
		public static final String AD_AUTO_CHANGE = "Automatically Change Ad Image";
	}
	public class ScoreBoardHtmlIntermissionNamePolicy extends AbstractClockNumberChangePolicy
	{
		public ScoreBoardHtmlIntermissionNamePolicy() {
			super("scoreboard.html IntermissionNamePolicy");
			setName(getId());
			addClock(Clock.ID_INTERMISSION);

			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, INTERMISSION_1_NAME, "String", "Halftime"));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, INTERMISSION_2_NAME, "String", "Final"));
			addParameterModel(new DefaultPolicyModel.DefaultParameterModel(this, INTERMISSION_OTHER_NAME, "String", "Time To Derby"));

			final PolicyParameterEventVisitor ppeV = new DefaultPolicyParameterEventVisitor() {
				public void policyParameterValueChange(Policy.Parameter parameter, String value) {
					synchronized (changeLock) {
						Clock ic = scoreBoardModel.getClock(Clock.ID_INTERMISSION);
						clockNumberChange(ic, ic.getNumber());
					}
				}
			};
			PolicyParameterListener listener = new PolicyParameterListener() {
					public void policyParameterChange(PolicyParameterEvent event) {
						event.accept(ppeV);
					}
				};
			getParameter(INTERMISSION_1_NAME).addPolicyParameterListener(listener);
			getParameter(INTERMISSION_2_NAME).addPolicyParameterListener(listener);
			getParameter(INTERMISSION_OTHER_NAME).addPolicyParameterListener(listener);
		}

		public void setScoreBoardModel(ScoreBoardModel sbM) throws Exception {
			super.setScoreBoardModel(sbM);
			Clock ic = sbM.getClock(Clock.ID_INTERMISSION);
			clockNumberChange(ic, ic.getNumber());
		}

		public void clockNumberChange(Clock clock, int number) {
			Document doc = editor.createDocument("ScoreBoard");
			Element sb = editor.getElement(doc.getDocumentElement(), "ScoreBoard");
			Element page = editor.addElement(sb, "Page", "scoreboard.html");
			String name;
			switch (number) {
				case 1: name = getParameter(INTERMISSION_1_NAME).getValue(); break;
				case 2: name = getParameter(INTERMISSION_2_NAME).getValue(); break;
				default: name = getParameter(INTERMISSION_OTHER_NAME).getValue(); break;
			}
			editor.addElement(page, "IntermissionName", null, name);
			updateAllDocuments(doc);
		}

		public static final String INTERMISSION_1_NAME = "Intermission1";
		public static final String INTERMISSION_2_NAME = "Intermission2";
		public static final String INTERMISSION_OTHER_NAME = "IntermissionOther";
	}

}
