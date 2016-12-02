/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: XmlTextEncodingUtil.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.util;

/**
 * Utility class to encode special characters from strings. 
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class XmlTextEncodingUtil {
   
    /**
     * Function to replace special XML syntax characters
     * in the user string with XML placeholders.
     * @param userData the normal text string with special characters
     * @return the encoded string without special characters
     */
    public static String xmlEntityConvert(String userData) {
        StringBuffer xmlstring = new StringBuffer();
        char test;
        for (int i = 0; i < userData.length(); i++) {
            test = userData.charAt(i);
            if (test == '<') { /* '<' -> &lt; */
                xmlstring.append("&lt;");
            } else if (test == '>') { /* '>' -> &gt; */
                xmlstring.append("&gt;");
            } else if (test == '&') { /* '<' -> &amp; */
                xmlstring.append("&amp;");
            } else if (test == '\"') { /* '\"' -> &quot; */
                xmlstring.append("&quot;");
            } else if (test == '\'') { /* '\'' -> &apos; */
                xmlstring.append("&apos;");
            } else {
                xmlstring.append(test);
            }
        }
        return xmlstring.toString();
    }

}
