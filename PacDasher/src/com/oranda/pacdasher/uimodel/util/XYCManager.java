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
 
package com.oranda.pacdasher.uimodel.util;

import com.oranda.pacdasher.uimodel.Const;
 

/**
 * XYCManager - overall model for the UI, to which the UI listens
 * Singleton.
 */
 
public class XYCManager
{
    // the one true instance
    protected static XYCManager xycManager;
    protected static XYCoarse[][] xycArray;
    protected int xCoarseBoundary;
    protected int yCoarseBoundary;
    
    /*
     * Constructor is private because it's a singleton
     */
    private XYCManager()
    {                        
    }

    private XYCManager(int width, int height) 
    {

        this.xCoarseBoundary = width - 1;
        this.yCoarseBoundary = height - 1;
        Const.logger.fine("XYCManager boundaries " + xCoarseBoundary + 
            "," + yCoarseBoundary);
        xycArray = new XYCoarse[xCoarseBoundary+1][yCoarseBoundary+1];
        for (int i=0; i<=xCoarseBoundary; i++)
        {
            for (int j=0; j<=yCoarseBoundary; j++)
            {
                XYCoarse xyCoarse = new XYCoarse(i, j);
                xycArray[i][j] = xyCoarse;
            }
        }
    }    
    
    public XYCoarse createXYC(int xCoarse, int yCoarse)
    {
        if (xCoarse > xCoarseBoundary)
        {
            xCoarse = xCoarseBoundary;
        }
        if (xCoarse < 0)
        {
            xCoarse = 0;
        }
        if (yCoarse > yCoarseBoundary)
        {
            yCoarse = yCoarseBoundary;
        }
        if (yCoarse < 0)
        {
            yCoarse = 0;
        }
         
        return xycArray[xCoarse][yCoarse];
    }
    
    public static XYCManager getInstance()
    {
        if (xycManager == null) 
        {
            Const.logger.severe("ERROR: XYCManager not initialized");
        }
        return xycManager;
    }
    
    public static void initialize(int xCoarseBoundary, int yCoarseBoundary)
    {
        xycManager = new XYCManager(xCoarseBoundary, yCoarseBoundary);
    }
    
    public static boolean isInitialized()
    {
        return (xycManager != null);
    }
        
} 