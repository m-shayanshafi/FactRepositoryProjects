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

public class Gate extends VisualObject 
{
    private static Color color = Color.GREEN; // default
    protected static int diameter = UIModelConsts.X_TILE_SIZE;
            
    public Gate()
    {
    }
    
    public static void setColor(Color color)
    {
    	Gate.color = color;
    }
    
    public void render(Graphics g, XYCoarse XYCoarse)
    {
        g.setColor(color);
        g.fillRect(getXTopLeft(XYCoarse, 0), 
            getYTopLeft(XYCoarse, 0) + 5, 
            diameter + 2, 2);
    }
    
    public static int getDiameter()
    {
        return diameter;
    }

    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_GATE;
    }
    
    public Object clone()
    {
        return new Gate();
    }
}