/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: JDiveLogExceptionHandler.java
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
package net.sf.jdivelog.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jdivelog.gui.resources.Messages;

/**
 * Generic Exception Handler.
 * This Exception Handler will handle the Unhandled Exception during AWT-Event-Dispatching.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class JDiveLogExceptionHandler {
    
    private static final Logger LOGGER = Logger.getLogger(JDiveLogExceptionHandler.class.getName());
    
    private static MainWindow mainWindow;
    
    /**
     * @param mainWindow set this to have a parent Frame for the Message Dialog
     */
    public static void setMainWindow(MainWindow mainWindow) {
        JDiveLogExceptionHandler.mainWindow = mainWindow;
    }
    
    public JDiveLogExceptionHandler() {
        
    }
    
    /**
     * @param t Exception occured
     */
    public void handle(Throwable t) {
        LOGGER.log(Level.WARNING, "Exception caught!", t);
        if (t instanceof JDiveLogException) {
            JDiveLogException je = (JDiveLogException) t;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            je.printStackTrace(pw);
            new MessageDialog(mainWindow, je.getMessage(), je.getDetails(), sw.toString(), MessageDialog.MessageType.ERROR);
        } else {
            String mesg = Messages.getString("error.unknown");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            new MessageDialog(mainWindow, mesg, null, sw.toString(), MessageDialog.MessageType.ERROR);
        }
    }

}
