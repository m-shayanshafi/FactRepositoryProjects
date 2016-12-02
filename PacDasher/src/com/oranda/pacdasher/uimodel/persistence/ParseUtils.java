/**
 *  PacDasher application. For explanation of this class, see below. 
 *  Copyright (c) 2003-2005 James McCabe. Email: code@oranda.com 
 *  http://www.oranda.com/java/pacdasher/
 * 
 *  PacDasher is free software under the Aladdin license (see license  
 *  directory). You are free to play, copy, distribute, and modify it
 *  except for commercial purposes. You may not sell this code, or
 *  compiled versions of it, or anything which incorporates either of these.
 * 
 */
package com.oranda.pacdasher.uimodel.persistence;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.pacdasher.uimodel.util.XYCManager;
import com.oranda.pacdasher.uimodel.util.XYCoarse;

/**
 * Utility methods for parsing Strings from the XML.
 */
public class ParseUtils
{
    private static XYCManager xycm; // singleton

    static
    {
        XYCManager.initialize(UIModelConsts.MAZE_WIDTH, 
                UIModelConsts.MAZE_HEIGHT);
        xycm = XYCManager.getInstance();
    }
    
    /**
     * @return a set of strings
     */
    static Set<String> tokenize(String str) throws ParseException
    {
        if (str == null) 
        {
            throw new ParseException("string to be parsed was null");
        }
        Set<String> setOfStrs = new HashSet<String>();
        try
        {        
            StringTokenizer st = new StringTokenizer(str);	
            while (st.hasMoreTokens()) 
            {         
                String strToken = st.nextToken();
                setOfStrs.add(strToken);
            }
        }
        catch (NoSuchElementException nsee) 
        {
            throw new ParseException(str, nsee);
        }
    	return setOfStrs;
    }

    /**
     * @return a set of strings
     */
    static List<String> tokenizeToList(String str) throws ParseException
    {
        if (str == null) 
        {
            throw new ParseException("string to be parsed was null");
        }
        List<String> strs = new ArrayList<String>();
        try
        {        
            StringTokenizer st = new StringTokenizer(str);	
            while (st.hasMoreTokens()) 
            {         
                String strToken = st.nextToken();
                strs.add(strToken);
            }
        }
        catch (NoSuchElementException nsee) 
        {
            throw new ParseException(str, nsee);
        }
    	return strs;
    }
    
    /**
     * @return a list of strings
     */
    static List<String> tokenizeToList(String str, String delim) 
    		throws ParseException
    {
        if (str == null) 
        {
            throw new ParseException("string to be parsed was null");
        }
        List<String> strs = new ArrayList<String>();
        try
        {        
            StringTokenizer st = new StringTokenizer(str, delim);	
            while (st.hasMoreTokens()) 
            {         
                String strToken = st.nextToken();
                strs.add(strToken);
            }
        }
        catch (NoSuchElementException nsee) 
        {
            throw new ParseException(str, nsee);
        }
    	return strs;
    }
    
    /**
     * @return a set of strings
     */
    static Set<String> tokenize(String str, String delim) 
    		throws ParseException
    {
        if (str == null) 
        {
            throw new ParseException("string to be parsed was null");
        }
        Set<String> setOfStrs = new HashSet<String>();
	    StringTokenizer st = new StringTokenizer(str.trim(), delim);	
    	try
    	{
    	    while (st.hasMoreTokens()) 
    	    {         
    	        String strToken = st.nextToken();
    	        setOfStrs.add(strToken);
    	    } 
    	}
        catch (NoSuchElementException nsee) 
        {
            throw new ParseException(str, nsee);
        }
    	return setOfStrs;
    }
    
    static Set<XYCoarse> parseStringsToXYCs(Set<String> setOfStrs)
    		throws ParseException
    {
        Set<XYCoarse> setOfXYCs = new HashSet<XYCoarse>();
        for (String strXYC : setOfStrs) 
        {
    	    StringTokenizer st = new StringTokenizer(strXYC, ",");	
    	    int xc = parseStringToInt(st.nextToken());
    	    int yc = parseStringToInt(st.nextToken());
            XYCoarse xyCoarse = ParseUtils.xycm.createXYC(xc, yc);
            setOfXYCs.add(xyCoarse);
        }
        return setOfXYCs;
    }

    static List<XYCoarse> parseStringsToXYCsList(List<String> strs)
	throws ParseException
	{
        List<XYCoarse> xycs = new ArrayList<XYCoarse>();
        for (String strXYC : strs) 
        {
            StringTokenizer st = new StringTokenizer(strXYC, ",");	
            int xc = parseStringToInt(st.nextToken());
            int yc = parseStringToInt(st.nextToken());
            XYCoarse xyCoarse = ParseUtils.xycm.createXYC(xc, yc);
            xycs.add(xyCoarse);
        }
        return xycs;
	}
    
    static Set<Integer> parseStringsToInts(Set<String> setOfStrs)
			throws ParseException
	{
        Set<Integer> setOfInts = new HashSet<Integer>();
        for (String strInt : setOfStrs) 
        {
            int theInt = parseStringToInt(strInt);
            setOfInts.add(new Integer(theInt));
        }
        return setOfInts;
	}
    
     static List<Class> parseStringsToClasses(List<String> setOfStrs)
			throws ParseException
	{
        List<Class> classes = new ArrayList<Class>();
        for (String strClass : setOfStrs) 
        {
            Class theClass = parseStringToClass(strClass);
            classes.add(theClass);
        }
        return classes;
	}
    
    static XYCoarse parseStringToXYC(String str) 
			throws ParseException
    {
        if (str == null || str.indexOf(",") == -1)
        {
            throw new ParseException("Could not parse an XYC out of " + str);
        }
	    StringTokenizer st = new StringTokenizer(str.trim(), ",");	
	    int xc = parseStringToInt(st.nextToken());
	    int yc = parseStringToInt(st.nextToken());
        XYCoarse xyCoarse = ParseUtils.xycm.createXYC(xc, yc);
        return xyCoarse;
    }
    
    static Color parseStringToColor(String str)
			throws ParseException
    {
        StringTokenizer st = new StringTokenizer(str.trim());
        int red = (new Integer(st.nextToken())).intValue();
        int green = (new Integer(st.nextToken())).intValue();
        int blue = (new Integer(st.nextToken())).intValue();
        return new Color(red, green, blue);
    }
    
    static int parseStringToInt(String str) throws ParseException
    {
        Integer i = null;
        try
        {
           i = new Integer(str.trim());
        }
        catch (NumberFormatException nfe)
        {
            throw new ParseException(str, nfe);
        }        
        return i.intValue();
    }
    
    static Class parseStringToClass(String className) 
    		throws ParseException
    {
        Class theClass = null;
        try 
        {
            theClass = Class.forName(className.trim());
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new ParseException(className, cnfe);
        }
        return theClass;
    }

    static boolean parseStringToBoolean(String str) 
    		throws ParseException
    {
        if ("true".equals(str))
        {
            return true;
        } 
        else if ("false".equals(str))
        {
            return false;
        }
        else 
        {
            throw new ParseException("Could not parse as boolean: " + str);
        }
    }
    
    static Set<XYCoarse> parseStringToXYCs(String str)
    		throws ParseException
    {
        return parseStringsToXYCs(tokenize(str));
    }
    
    static List<XYCoarse> parseStringToXYCsOrdered(String str)
			throws ParseException
    {
        return parseStringsToXYCsList(tokenizeToList(str));
    }
    
    static Set<Integer> parseStringToInts(String str)
    		throws ParseException
    {
        return parseStringsToInts(tokenize(str));
    }
    
    static List<Class> parseStringToClasses(String str) 
    		throws ParseException
    {
        return parseStringsToClasses(tokenizeToList(str, ","));
    }
}
