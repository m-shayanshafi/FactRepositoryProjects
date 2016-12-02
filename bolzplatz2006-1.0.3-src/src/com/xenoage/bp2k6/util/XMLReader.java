/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.util;

import org.dom4j.Element;


/**
 * This class contains some helper functions
 * to read values out of XML files. They are
 * necessary, since dom4j returns <code>null</code>
 * if an element or attribute does not exist,
 * but this class returns - dependent on the type -
 * "" or 0.
 *
 * @author Andreas Wenger
 */
public class XMLReader
{

  /**
   * The encoding for all bp2k6 XML files.
   * Unicode (UTF-8, UTF-16, UTF-16LE, ...) did not
   * work, ISO/IEC 8859-15 did not work (no € Symbol),
   * now we use "windows-1252". It works.
   */
  public static final String encoding = "windows-1252";
  
  /* not needed
  /-**
   * @param parentElement   the parent element of the one to read
   * @param element         the name of the element to read
   * @return Element        the desired element, or, if not found,
   *                        an empty element with the name ""
   *-/
  public static Element readElement(Element parentElement, String element)
  {
    Element ret = parentElement.element(element);
    return ret != null ? ret : new DefaultElement("");
  } */

  public static String readAttributeValue(Element element, String name)
  {
    if (element == null) return "";
    String ret = element.attributeValue(name);
    return ret != null ? ret : "";
  }

  public static String readTextTrim(Element parentElement, String element)
  {
    if (parentElement == null) return "";
    String ret = parentElement.elementTextTrim(element);
    return ret != null ? ret : "";
  }

  public static String readTextTrim(Element element)
  {
    String ret = element.getTextTrim();
    return ret != null ? ret : "";
  }

  public static int readAttributeValueInt(Element element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Integer.parseInt(val);
    else
      return 0;
  }


  public static int readAttributeValueIntHex(Element element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
       return Integer.parseInt(val, 16);
    else
      return 0;
  }

  public static float readAttributeValueFloat(Element element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Float.parseFloat(val);
    else
      return 0;
  }

  public static boolean readAttributeValueBoolean(Element element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Boolean.parseBoolean(val);
    else
      return false;
  }

  public static float readElementValueFloat(Element element)
  {
    if (element != null)
      return Float.parseFloat(element.getText());
    else
      return 0;
  }


}
