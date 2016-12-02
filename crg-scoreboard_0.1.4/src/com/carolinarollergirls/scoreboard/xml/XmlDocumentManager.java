package com.carolinarollergirls.scoreboard.xml;

import java.io.*;
import java.util.*;

import org.w3c.dom.*;

import com.carolinarollergirls.scoreboard.*;

public class XmlDocumentManager
{
	protected XmlDocumentManager() {
		loadDocuments();
	}

	public Document getDefaultDocument() { return defaultDocument; }

	public Document getDocument(String key) {
		Document d = documents.get(key);
		if (null == d)
			return getDefaultDocument();
		else
			return d;
	}

	public void mergeDocument(Document document) {
		synchronized (documents) {
			Iterator<Document> i = documents.values().iterator();
			while (i.hasNext()) {
				Document d = i.next();
				editor.mergeDocumentsRemoveElements(d, document);
			}
		}
		editor.mergeDocumentsRemoveElements(getDefaultDocument(), document);
	}

	public void replaceDocument(Document document) {
		synchronized (documents) {
			Iterator<String> i = documents.keySet().iterator();
			while (i.hasNext()) {
				Document d = editor.createDocument();
				editor.mergeDocumentsRemoveElements(d, document);
				documents.put(i.next(), d);
			}
		}
		defaultDocument = editor.createDocument();
		editor.mergeDocumentsRemoveElements(getDefaultDocument(), document);
	}

	public static synchronized XmlDocumentManager getInstance() {
		if (xmlDocumentManager == null)
			xmlDocumentManager = new XmlDocumentManager();

		return xmlDocumentManager;
	}

	protected void loadDocuments() {
		Enumeration properties = ScoreBoardManager.getProperties().propertyNames();

		synchronized (documents) {
			while (properties.hasMoreElements()) {
				String property = properties.nextElement().toString();
				if (!property.startsWith(DOC_KEY))
					continue;

				String key = property.replaceFirst(DOC_KEY+".", "");
				String value = ScoreBoardManager.getProperties().getProperty(property);

				InputStream is = getClass().getClassLoader().getResourceAsStream(value);

				if (null == is) {
					System.err.println("Could not find XML document " + value);
					continue;
				}

				Document doc;
				try {
					doc = editor.toDocument(is);
				} catch ( Exception e ) {
					System.err.println("Could not parse XML document ("+value+"): " + e.getMessage());
					continue;
				}

				if (key.equals(""))
					defaultDocument = doc;
				else
					documents.put(key, doc);
			}
		}

		if (null == defaultDocument) {
			InputStream is = getClass().getClassLoader().getResourceAsStream(DEFAULT_DOC_FILE);

			if (null == is)
				System.err.println("ERROR: Could not find default XML document " + DEFAULT_DOC_FILE);

			try {
				defaultDocument = editor.toDocument(is);
			} catch ( Exception e ) {
				System.err.println("ERROR: Could not parse default XML document ("+DEFAULT_DOC_FILE+"): " + e.getMessage());
			}
		}

		// If there really is no user-provided default doc, create an empty one
		if (null == defaultDocument)
			defaultDocument = editor.createDocument();
	}

	protected XmlDocumentEditor editor = XmlDocumentEditor.getInstance();
	protected HashMap<String,Document> documents = new HashMap<String,Document>();
	protected Document defaultDocument = null;

	private static XmlDocumentManager xmlDocumentManager = null;
	public static final String DOC_KEY = XmlDocumentManager.class.getName() + ".Document";
	public static final String DEFAULT_DOC_FILE = "crg.scoreboard.xml";
}
