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
 
package com.oranda.pacdasher.uimodel.visualobjects;

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.*;

import java.awt.Color;
import java.awt.Graphics;

public class Dot extends VisualObject
{
    private Color color = Color.WHITE;
    private static final int diameter = 4;
    private static final int scoreValue = 10;
    
    public Dot()
    {
        super();
    }
        
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        // now draw the dot
        g.setColor(color);
        g.fillRect(getXTopLeft(xyCoarse, diameter), 
            getYTopLeft(xyCoarse, diameter), 
            diameter, diameter);
    }
    
    public static int getDiameter()
    {
        return diameter;
    }

    public int getScoreValue()
    {
        return scoreValue;
    }
    
    public boolean isIntersectionBased()
    {
        return true;
    }
    
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_DOT;
    }
    
    public Object clone()
    {
        return new Dot();
    }
}