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
import java.lang.reflect.*;

import org.w3c.dom.*;

/**
 * IhmSettingsXML saves/loads every public member (int, double, boolean, String)
 * into a XML file. It also groups settings and descriptions. All descriptions
 * have to be String and a member which ends with "_DOC" (DESCRIPTION END).
 * <P>
 * Example:
 * <P>
 * public final int MAX_COUNT = 10;
 * <P>
 * public final String MAX_COUNT_DOC = "That's the max count setting";
 * <P>
 * 
 * 
 * @author Bernhard von Gunten
 * @created September, 2004
 */
public class IhmSettingsXML implements Serializable {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 3258692113242732080L;

  /** The ending for a description member */
  private static final String DESCRIPTION_END = "_DOC";

  /**
   * Returns true, if this member field shall be saved
   * 
   * @param field
   * @return boolean if field shall be saved
   */
  private boolean saveField(Field field) {
    // Do not save documentation fields
    if (field.getName().endsWith(DESCRIPTION_END)) {
      return false;
    }

    // Do only save public members
    if (!field.toString().startsWith("public")) {
      return false;
    }

    return true;
  }

  /**
   * Reads member fields from XML file.
   * 
   * @param fileToRead
   *          File to read
   * @throws Exception
   */
  public void readSettingsFromXml(File fileToRead) throws Exception {
    Document doc = ToolsXML.loadXmlFile(fileToRead);
    Element root = doc.getDocumentElement();
    NodeList settingElements = root.getElementsByTagName("SETTING");

    int size = settingElements.getLength();

    for (int i = 0; i < size; i++) {
      Element setting = (Element) settingElements.item(i);
      String varName = setting.getAttribute("NAME");
      String description = setting.getAttribute("DESCRIPTION");
      String value = setting.getAttribute("VALUE");

      // Set the description (could be missing, just try)
      try {
        Field fieldDescription = getClass().getField(varName + DESCRIPTION_END);
        fieldDescription.set(this, description);
      } catch (Exception ignored) {
      }

      // Set the filed
      Field field = getClass().getField(varName);

      if (setting.getAttribute("TYPE").equalsIgnoreCase("int")) {
        setIntValue(field, value);
      } else if (setting.getAttribute("TYPE").equalsIgnoreCase("double")) {
        setDoubleValue(field, value);
      } else if (setting.getAttribute("TYPE").equalsIgnoreCase("boolean")) {
        setBooleanValue(field, value);
      } else if (setting.getAttribute("TYPE").equalsIgnoreCase("java.lang.string")) {
        setStringValue(field, value);
      }

    }

  }

  private void setIntValue(Field field, String value) throws Exception {
    int val = Integer.valueOf(value).intValue();
    field.setInt(this, val);
  }

  private void setDoubleValue(Field field, String value) throws Exception {
    double val = Double.valueOf(value).doubleValue();
    field.setDouble(this, val);
  }

  private void setBooleanValue(Field field, String value) throws Exception {
    boolean val = Boolean.valueOf(value).booleanValue();
    field.setBoolean(this, val);
  }

  private void setStringValue(Field field, String value) throws Exception {
    field.set(this, value);
  }

  /**
   * Writes members to XML file
   * 
   * @param fileToWrite
   * @throws Exception
   */
  public void writeSettingsToXml(File fileToWrite) throws Exception {

    Document document = ToolsXML.createXmlDocument();
    Element root = document.createElement("SETTINGS");

    Field[] fields = getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      if (this.saveField(fields[i])) {
        if (fields[i].getType().getName().equalsIgnoreCase("int")) {
          appendIntToXml(root, fields[i]);
        } else if (fields[i].getType().getName().equalsIgnoreCase("double")) {
          appendDoubleToXml(root, fields[i]);
        } else if (fields[i].getType().getName().equalsIgnoreCase("boolean")) {
          appendBooleanToXml(root, fields[i]);
        } else if (fields[i].getType().getName().equalsIgnoreCase("java.lang.string")) {
          appendStringToXml(root, fields[i]);
        }
      }
    }

    document.appendChild(root);
    ToolsXML.saveXmlFile(fileToWrite, document);

    return;
  }

  private void appendIntToXml(Element element, Field field) throws Exception {
    String fieldName = field.getName();
    String fieldValue = String.valueOf(field.getInt(this));
    String fieldDescription = getFieldDescription(fieldName);
    String fieldType = "int";
    createXmlElement(element, fieldType, fieldName, fieldValue, fieldDescription);
  }

  private void appendDoubleToXml(Element element, Field field) throws Exception {
    String fieldName = field.getName();
    String fieldValue = String.valueOf(field.getDouble(this));
    String fieldDescription = getFieldDescription(fieldName);
    String fieldType = "double";
    createXmlElement(element, fieldType, fieldName, fieldValue, fieldDescription);
  }

  private void appendBooleanToXml(Element element, Field field) throws Exception {
    String fieldName = field.getName();
    String fieldValue = String.valueOf(field.getBoolean(this));
    String fieldDescription = getFieldDescription(fieldName);
    String fieldType = "boolean";
    createXmlElement(element, fieldType, fieldName, fieldValue, fieldDescription);
  }

  private void appendStringToXml(Element element, Field field) throws Exception {
    String fieldName = field.getName();
    String fieldValue = (String) field.get(this);
    String fieldDescription = getFieldDescription(fieldName);
    String fieldType = "java.lang.string";
    createXmlElement(element, fieldType, fieldName, fieldValue, fieldDescription);
  }

  private void createXmlElement(Element element, String type, String name, String value, String description) {
    Element settingElement = element.getOwnerDocument().createElement("SETTING");
    settingElement.setAttribute("NAME", name);
    settingElement.setAttribute("VALUE", value);
    settingElement.setAttribute("DESCRIPTION", description);
    settingElement.setAttribute("TYPE", type);
    element.appendChild(settingElement);
  }

  private String getFieldDescription(String prefix) {
    try {
      String varName = prefix + DESCRIPTION_END;
      Field field = getClass().getField(varName);
      return (String) field.get(this);
    } catch (Exception err) {
      return "";
    }
  }

}
