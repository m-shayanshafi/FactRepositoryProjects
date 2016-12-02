/**
 *  For explanation of this class, see below. 
 *  Copyright (c) 2000 James McCabe. 
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

/**
 * Output - methods logging activity and reporting errors,
 * to the terminal
 */
package com.oranda.util;

import java.io.*;
import javax.swing.*;


public class Output
{   
    // Type of error
    public final static int DEBUG = 0;
    public final static int NOTE = 1;
    public final static int ERROR = 2;
    public final static int DIALOGERROR = 3;
    public final static int DIALOGALERT = 4;
    
    private final static int HIGHESTTYPENUMBER = 4;
    
    private static boolean isDebugging = true;
    
    public static boolean isDebugging()
    {
    	return isDebugging;
    }
	
	public static void setDebugging(boolean isDebugging)
	{
		Output.isDebugging = isDebugging;
	}
   
    public static void debug(String message)
    {
        print(Output.DEBUG, message);
    }

    public static void debug(Object o)
    {
        debug(o.toString());
    }
    
    public static void error(String message)
    {
        print(Output.ERROR, message);
    }

    public static void error(String message, Throwable e)
    {
        print(Output.ERROR, message, e);
    }

   
    public static void print(int typeOfOutput, String message)
    {
        print(typeOfOutput, message, null);
    }
    
    public static void print(int typeOfOutput, final String message, Throwable e) 
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
                 System.out.println("NOTE: " + text);
                 break;
             case DEBUG: 
                 if (isDebugging)
                 {
                     System.out.println("DEBUG: " + text);
                 }
                 break;
             case ERROR:
                 System.out.println("ERROR: " + text);
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
