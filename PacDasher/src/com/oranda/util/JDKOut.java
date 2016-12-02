/**
 *  For explanation of this class, see below. 
 *  Copyright (c) 2003-2004 James McCabe. 
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package com.oranda.util;

import java.io.*;
import java.util.logging.*;
import javax.swing.*;

/**
 * Output using the Logging API new to J2SE 1.4
 
 * To use a log config file in the current dir, pass this on the java cmd line:
 * -Djava.util.logging.config.file=logging.properties
 * 
 * Default log file is in the JRE lib dir. 
 *
 * This class has a PROBLEM. The source of the log message is lost! JDK
 * logging normally prints the class and method name (except where there is
 * too much JITting).
 */
public class JDKOut implements IOutput
{   
    // Type of error
	private static String GLOBAL_LOGGER_NAME = "global";
	
	private static JDKOut jdkOut = new JDKOut();
	
	private Logger logger;
		
	public JDKOut()
	{
		logger = Logger.getLogger(GLOBAL_LOGGER_NAME);
	}
	
	public synchronized static JDKOut getInstance()
	{
	    if (jdkOut == null)
		{
			jdkOut = new JDKOut();
		}
		return jdkOut;
	}
	
	/**
	 * Short form of getInstance().
	 */
	public static JDKOut i()
	{
		return getInstance();
	}
	
    public boolean isDebugging()
    {
    	Level level = logger.getLevel();
		if (level.intValue() < Level.CONFIG.intValue())
		{
			return true;
		}
		else
		{
			return false;
		}
    }
	
	public void setDebugging(boolean isDebugging)
	{
		if (isDebugging)
		{
			logger.setLevel(Level.FINEST);
		}
		else
		{
			logger.setLevel(Level.INFO);
		}
	}
   
    public void debug(String message)
    {
        print(Output.DEBUG, message);
    }

    public void error(String message)
    {
        print(Output.ERROR, message);
    }

    public void error(String message, Throwable e)
    {
        print(Output.ERROR, message, e);
    }

   
    public void print(int typeOfOutput, String message)
    {
        print(typeOfOutput, message, null);
    }
    
    public void print(int typeOfOutput, final String message, Throwable e) 
    {
        String text = message;
        if (e != null)
        {
            try 
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(baos));
                text += "\n" + e.getMessage() + "\n" + baos.toString() + "\n";
                baos.close();
            }
            catch (IOException ioe) 
            {
                System.out.println("In Output.print() error converting "
                    + "exception to string. Exception was " + ioe.toString());
            }        
        }

        switch (typeOfOutput) {
             case NOTE:
                 logger.info(text);
				 break;
             case DEBUG: 
                 logger.fine(text);
                 break;
             case ERROR:
                 logger.severe(text);
                 break;
             case DIALOGERROR:
                 System.out.println("ERROR: " + text);
                 JOptionPane.showMessageDialog(null, message, 
                     "Application Error", JOptionPane.ERROR_MESSAGE); 
                 break;
            case DIALOGALERT: 
                 System.out.println("ALERT: " + text);
                 // overcome JOptionPane's modal (thread-blocking) behavior
                 Runnable dialog = new Runnable() {  
                     public void run() 
                     { 
                         JOptionPane.showMessageDialog(null, message, 
                             "ALERT", JOptionPane.INFORMATION_MESSAGE); 
                     } 
                 }; 
                 Thread dialogThread = new Thread(dialog);
                 dialogThread.start();
                 break;                 
             default:
                 System.out.println("invalid typeOfOutput int was "
                     + typeOfOutput + ", message was " + text);
         }    
    }
    
}
