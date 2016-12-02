package com.carolinarollergirls.scoreboard.xml;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;

import org.xml.sax.*;

public class XmlDocumentEditor
{
	public XmlDocumentEditor() {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch ( Exception e ) {
			System.err.println("Could not create DocumentBuilder : " + e.getMessage());
		}

		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
		} catch ( Exception e ) {
			System.err.println("Could not create Transformer : " + e.getMessage());
		}
	}

	public Document newDocument() {
		synchronized (documentBuilder) {
			return documentBuilder.newDocument();
		}
	}

	public Document createDocument() {
		return createDocument(null, null, null);
	}

	public Document createDocument(String element) {
		return createDocument(element, null, null);
	}

	public Document createDocument(String element, String id) {
		return createDocument(element, id, null);
	}

	public Document createDocument(String element, String id, String value) {
		Document document = newDocument();
		Element e = addElement(document, "document");
		addSystemTime(document);
		if (null != element)
			addElement(e, element, id, value);
		return document;
	}

	public Element addElement(Node parent, String name) {
		return addElement(parent, name, null, null);
	}

	public Element addElement(Node parent, String name, String id) {
		return addElement(parent, name, id, null);
	}

	public Element addElement(Node parent, String name, String id, String content) {
		synchronized (parent) {
			Document d = parent.getOwnerDocument();
			if (null == d)
				d = (Document)parent;
			Element element = d.createElement(name);
			if (null != id && !"".equals(id))
				element.setAttribute("Id", id);
			parent.appendChild(element);
			setContent(element, content);
			return element;
		}
	}

	public Element setElement(Node parent, String name) {
		return setElement(parent, name, null, null);
	}

	public Element setElement(Node parent, String name, String id) {
		return setElement(parent, name, id, null);
	}

	public Element setElement(Node parent, String name, String id, String content) {
		Element element = getElement(parent, name, id);
		setContent(element, content);
		return element;
	}

	public Element getElement(Node parent, String name) {
		return getElement(parent, name, null);
	}

	public Element getElement(Node parent, String name, String id) {
		synchronized (parent) {
			Node n = parent.getFirstChild();
			while (null != n) {
				try {
					Element element = (Element)n;
					boolean sameName = element.getNodeName().equals(name);
					String elementId = element.getAttribute("Id");
					boolean noId = null == id || "".equals(id);
					boolean noElementId = null == elementId || "".equals(elementId);
					boolean noIds = noId && noElementId;
					boolean sameIds = (null != id && id.equals(elementId));
					if (sameName && (noIds || sameIds))
						return element;
				} catch ( Exception e ) {
				}
				n = n.getNextSibling();
			}
		}

		return addElement(parent, name, id, null);
	}

	public void removeContent(Element e) {
		if (null == e)
			return;

		synchronized (e) {
			try { e.removeAttribute("empty"); } catch (Exception err) { }

			Node node = e.getFirstChild();
			Node textNode = null;
			while (null != node) {
				Node n = node;
				node = node.getNextSibling();

				if (n.getNodeType() == Node.TEXT_NODE)
					e.removeChild(n);
			}
		}
	}

	public void setContent(Element e, String content) {
		if (null == e || null == content)
			return;

		synchronized (e) {
			removeContent(e);

			if ("".equals(content))
				e.setAttribute("empty", "true");
			else
				e.appendChild(e.getOwnerDocument().createTextNode(content));
		}
	}

	public String getContent(Element e) {
		if (null == e)
			return null;

		synchronized (e) {
			String c = null;

			Node n = e.getFirstChild();
			while (n != null) {
				if (n.getNodeType() == Node.TEXT_NODE) {
					c = n.getNodeValue();
					break;
				}

				n = n.getNextSibling();
			}

			if ((null == c) || ("".equals(c)))
				return (e.getAttribute("empty").equals("true") ? "" : null);
			else
				return c;
		}
	}

	public void addSystemTime(Document doc) {
		String oldSystemTime = doc.getDocumentElement().getAttribute("SystemTime");
		if (oldSystemTime == null || oldSystemTime.equals(""))
			setSystemTime(doc);
	}

	public void setSystemTime(Document doc) {
		doc.getDocumentElement().setAttribute("SystemTime", Long.toString(new Date().getTime()));
	}

	public long getSystemTime(Document doc) throws NumberFormatException {
		return Long.parseLong(doc.getDocumentElement().getAttribute("SystemTime"));
	}

	public Document toDocument(InputStream stream) throws SAXException,IOException {
		Document document;
		synchronized (documentBuilder) {
			document = documentBuilder.parse(stream);
		}
		addSystemTime(document);
		return document;
	}

	public Document toDocument(Reader reader) throws TransformerException {
		Document d = newDocument();
		DOMResult result = new DOMResult(d);
		StreamSource source = new StreamSource(reader);
		synchronized (transformer) {
			transformer.transform(source, result);
		}
		addSystemTime(d);
		return d;
	}

	public Document toDocument(String s) throws TransformerException {
		StringReader stringReader = new StringReader(s);
		return toDocument(stringReader);
	}

	public String toString(Document d) throws TransformerException {
		DOMSource source = new DOMSource(d);
		StringWriter stringWriter = new StringWriter();
		StreamResult result = new StreamResult(stringWriter);
		synchronized (transformer) {
			transformer.transform(source, result);
		}
		stringWriter.write("\n");
		return stringWriter.toString();	
	}

	public void mergeDocuments(Document to, Document from) {
		mergeElements(to.getDocumentElement(), from.getDocumentElement(), true, null, true, false);
	}

	public void mergeDocumentsRemoveElements(Document to, Document from) {
		mergeElements(to.getDocumentElement(), from.getDocumentElement(), true, null, true, true);
	}

	public void mergeDocumentAttributes(Document to, Document from) {
		mergeElements(to.getDocumentElement(), from.getDocumentElement(), true, null, false, false);
	}

	public void mergeDocumentAttributes(Document to, Document from, String attrName) {
		mergeElements(to.getDocumentElement(), from.getDocumentElement(), true, attrName, false, false);
	}

	public void mergeDocumentContent(Document to, Document from) {
		mergeElements(to.getDocumentElement(), from.getDocumentElement(), false, null, true, false);
	}

	public void mergeElements(Element to, Element from, boolean mergeAttributes, String attrName, boolean mergeContent, boolean removeElements) {
		/* Remove any nodes with "remove" attribute if removeElements is true */
		if (from.getAttribute("remove").equals("true") && removeElements) {
			to.getParentNode().removeChild(to);
			return;
		}

		if (mergeAttributes) {
			NamedNodeMap attrs = from.getAttributes();
			for (int i=0; i<attrs.getLength(); i++) {
				Node attr = attrs.item(i);
				if (attrName == null || attrName.equals(attr.getNodeName()))
					to.setAttribute(attr.getNodeName(), attr.getNodeValue());
			}
		}

		/* Don't merge the "add" attribute */
		if (to.getAttribute("add").equals("true"))
			to.removeAttribute("add");

		if (mergeContent)
			setContent(to, getContent(from));

		NodeList list = from.getChildNodes();
		for (int i=0; i<list.getLength(); i++) {
			Node n = list.item(i);
			String name = n.getNodeName();
			String value = n.getNodeValue();
			switch (n.getNodeType()) {
			case Node.ELEMENT_NODE:
				try {
					Element e = (Element)n;
					mergeElements(getElement(to, name, e.getAttribute("Id")), e, mergeAttributes, attrName, mergeContent, removeElements);
				} catch ( Exception e ) { }
			}
		}
	}

	public static XmlDocumentEditor getInstance() { return xmlDocumentEditor; }

	protected DocumentBuilder documentBuilder;
	protected Transformer transformer;

	private static XmlDocumentEditor xmlDocumentEditor = new XmlDocumentEditor();
}
