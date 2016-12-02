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

/**
 * FileLogger - log to a file. Simple. Does not support concurrency.
 * Have to call FileLogger.open(filename) before 
 * doing any FileLogger.getInstance().debug calls, 
 * and MUST call FileLogger.close() at end (preferably in a finally block)
 * Overwrites any existing file of the same file name without warning.
 */
package com.oranda.util;

import java.io.*;


public class FileLogger
{   
    
    public static void main(String[] args)
    {
        try {
            FileLogger.open("pacdasher.log");
            FileLogger.getInstance().debug("test test debug message");
            FileLogger.getInstance().debug("test message 2");
        }
        catch (Throwable t)
        {
            Output.error("", t);
	    
        }    
        finally {
            FileLogger.close();
        }
    }
        
        
    // Type of error
    public final static int DEBUG = 0;
    public final static int NOTE = 1;
    public final static int ERROR = 2;
    
    private final static int HIGHESTTYPENUMBER = 2;
    
    protected final static String DEFAULT_FILE_NAME = "filelogger.log";
    
    protected final static boolean isDebugging = true;

    // Singleton instance
    private static FileLogger fileLogger;
         
    protected String filename;
    protected FileWriter fileWriter;
    
    public static boolean isDebugging()
    {
    	return isDebugging;
    }
    

    
    protected FileLogger() {}
    
    protected FileLogger(String filename) 
    {
        try 
	    { 
            File f = new File(filename);
			this.filename = filename;
            this.fileWriter = new FileWriter(f);
        }
        catch (IOException ioe)
        {
            Output.print(Output.ERROR, "Could not open file for logging " 
                + filename, ioe);
        }
    }
    
    public static FileLogger open(String filename) 
    {
        if (fileLogger == null) 
        {
            fileLogger = new FileLogger(filename); 
        } 
        else
        {
            Output.print(Output.ERROR, "FileLogger already initialized "
                + "with filename " + fileLogger.filename);
        }
        return fileLogger;
    }    
    
    public static void close() {
        FileLogger.getInstance().closeFile();
    }
    
    protected void closeFile() {
        try
        {
            fileWriter.close();
        }
        catch (IOException ioe)
        {
            Output.print(Output.ERROR, "Could not open file stream " 
                + filename, ioe);
        }
    }
    
    public synchronized static FileLogger getInstance()
    {
        if (fileLogger == null) 
        {
            Output.print(Output.ERROR, "FileLogger.open() was not "
                + "called. Using " + DEFAULT_FILE_NAME);
            return open(DEFAULT_FILE_NAME); 
        } 
        else
        {
            return fileLogger;
        }
    }
            
            
            
            
    public void debug(String message)
    {
        print(DEBUG, message);
    }

    public void error(String message)
    {
        print(ERROR, message);
    }

    public void error(String message, Throwable e)
    {
        print(ERROR, message, e);
    }

   
    protected void print(int typeOfOutput, String message)
    {
        print(typeOfOutput, message, null);
    }
    
    protected void print(int typeOfOutput, final String message, Throwable e) 
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
                    Output.error("In FileLogger.print() error converting "
                        + "exception to string. Exception was " + ioe.toString());
                }        
            }
    
            switch (typeOfOutput) {
                 case NOTE:
                     writeToStream("NOTE: " + text);
                 case DEBUG: 
                     if (isDebugging)
                     {
                         writeToStream("DEBUG: " + text);
                     }
                     break;
                 case ERROR:
                     writeToStream("ERROR: " + text);
                     break;   
                 default:
                     writeToStream("invalid typeOfOutput int was "
                         + typeOfOutput + ", message was " + text);
            }
            writeToStream("\n");  
    }
    
    protected void writeToStream(String text)
    {
		try
		{
			this.fileWriter.write(text);
		}
		catch (IOException ioe)
		{
			try
			{
				this.fileWriter = new FileWriter(this.filename);
			    fileWriter.write(text);
			}
			catch (IOException ioe2)
			{
				Output.error("Could not write to " + filename, ioe2);
			}
		}
	}
}
