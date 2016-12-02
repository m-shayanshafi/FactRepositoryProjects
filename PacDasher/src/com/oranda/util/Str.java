/**
 * Copyright (c) James McCabe
 */

package com.oranda.util;

import java.io.*;

public class Str
{
	public static String getStackTraceAsStr(Throwable t)
	{
		String text = "";
    	try 
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            text += "\n" + t.getMessage() + "\n" + baos.toString() + "\n";
            baos.close();
        }
        catch (IOException ioe) 
        {
            System.out.println("In Util.getStackTraceAsString() "
            		+ "error converting exception to string. "
            		+ "Exception was " + ioe.toString());
        }
        return text;
    }
    
    /**
     * Shorten something like "com.oranda.util.Str" to "Str"
     */
    public static String abbrevFQClassname(String fqClassname)
    {
        int index = fqClassname.lastIndexOf('.') + 1;
        if (index < 0 || index >= fqClassname.length())
        {
            return fqClassname;
        }
        else
        {    
            return fqClassname.substring(index);
        }
    }
}