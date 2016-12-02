/************************************************************* 
 * 
 * Ice Hockey Manager 
 * ================== 
 * 
 * Copyright (C) by the IHM Team (see doc/credits.txt) 
 * 
 * This program is released under the GPL (see doc/gpl.txt) 
 * 
 * Further informations: http://www.icehockeymanager.org  
 * 
 *************************************************************/ 
  
package org.icehockeymanager.ihm.lib;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Tools XML contains some usefull "xml" functions.
 * 
 * @author Bernhard von Gunten <bvg@users.sourceforge.net>
 */
public class ToolsXML {

  public static final String SIMPLE_DATE_FORMAT = "dd.MM.yyyy";

  /**
   * Description of the Method
   * 
   * @param file
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
  public static Document loadXmlFile(File file) throws Exception {
    try {
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
      return domBuilder.parse(file);
    } catch (Exception e) {
      throw e;
    }
  }

  public static Element getFirstSubElement(Element parent, String name) {
    NodeList list = parent.getElementsByTagName(name);
    return (Element) list.item(0);
  }

  /**
   * Description of the Method
   * 
   * @param file
   *          Description of the Parameter
   * @param doc
   *          Description of the Parameter
   * @exception Exception
   *              Exception
   */
  public static void saveXmlFile(File file, Document doc) throws Exception {
    try {
      // Tools.writeAsciiFile(file, xmlToString(doc));
      // Save the document to the disk file
      TransformerFactory tranFactory = TransformerFactory.newInstance();
      Transformer aTransformer = tranFactory.newTransformer();

      Source src = new DOMSource(doc);
      Result dest = new StreamResult(file);
      aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
      aTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
      aTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      aTransformer.transform(src, dest);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Description of the Method
   * 
   * @param xmlString
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
  public static Document stringToXml(String xmlString) throws Exception {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      InputSource inputSource = new InputSource(new StringReader(xmlString));
      return factory.newDocumentBuilder().parse(inputSource);
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Description of the Method
   * 
   * @param doc
   *          Description of the Parameter
   * @return Return Value
   * @exception Exception
   *              Exception
   */
  public static String xmlToString(Document doc) throws Exception {
    try {
      Source source = new DOMSource(doc);

      StringWriter stringWriter = new StringWriter();
      Result result = new StreamResult(stringWriter);

      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, result);

      return stringWriter.toString();
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * Description of the Method
   * 
   * @return Return Value
   * @exception Exception
   *              Exception
   */
  public static Document createXmlDocument() throws Exception {
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

    } catch (Exception e) {
      throw e;
    }
  }

  public static String convertSimpleCalendar(Calendar day) {
    SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    return df.format(day.getTime());
  }

  public static Calendar getSimpleCalendarAttribute(Element element, String key) throws Exception {
    String value = element.getAttribute(key);
    SimpleDateFormat df = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    Date date = df.parse(value);
    Calendar result = Calendar.getInstance();
    result.setTime(date);
    return result;
  }

  public static int getIntAttribute(Element element, String key) {
    String value = element.getAttribute(key);
    return Integer.valueOf(value).intValue();
  }

  public static double getDoubleAttribute(Element element, String key) {
    String value = element.getAttribute(key);
    return Double.valueOf(value).doubleValue();
  }

  public static boolean getBooleanAttribute(Element element, String key) {
    String value = element.getAttribute(key);
    return Boolean.valueOf(value).booleanValue();
  }

  public static String getAttribute(Element element, String key) {
    return element.getAttribute(key);
  }

}
