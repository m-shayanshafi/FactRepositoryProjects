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
import java.util.*;
import java.util.logging.*;

public class IhmLoggingConsoleFormatter extends java.util.logging.Formatter {

  Date dat = new Date();

  private Object args[] = new Object[1];

  // Line separator string. This is the value of the line.separator
  // property at the moment that the SimpleFormatter was created.
  private String lineSeparator = System.getProperty("line.separator");

  /**
   * Format the given LogRecord.
   * 
   * @param record
   *          the log record to be formatted.
   * @return a formatted log record
   */
  public synchronized String format(LogRecord record) {
    StringBuffer sb = new StringBuffer();
    // Minimize memory allocations here.
    dat.setTime(record.getMillis());
    args[0] = dat;

    sb.append("[");
    sb.append(record.getLevel().getName());
    sb.append("] ");

    String message = formatMessage(record);

    sb.append(message);

    sb.append(lineSeparator);

    if (record.getThrown() != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        record.getThrown().printStackTrace(pw);
        pw.close();
        sb.append(sw.toString());
      } catch (Exception ex) {
      }
    }
    return sb.toString();
  }
}
