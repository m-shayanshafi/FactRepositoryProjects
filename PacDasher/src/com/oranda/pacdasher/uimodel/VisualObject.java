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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.uimodel.util.*;
 

import java.awt.Graphics;
import java.lang.reflect.Constructor;
import java.util.List;

/*
 * A VisualObject is something which is initialized with a
 * coarse xy coordinate and shown on the screen. A sprite, basically.
 */
public abstract class VisualObject implements Cloneable
{    
    public static int getDiameter()
    {
        // default
        return UIModelConsts.X_TILE_SIZE;
    }
        
    public abstract void render(Graphics g, XYCoarse xyCoarse);

    public String toString()
    {
        return getClass().getName();
    }
    
    // classes should override if isNeighborly is true
    public void render(Graphics g, XYCoarse xyCoarse, 
        List neighbors)
    {
        // default
        render(g, xyCoarse);
    }
    
    // select classes may override
    public boolean isNeighborly()
    {
        return false;
    }
    
    protected VisualObject() {}
    
    public static int getX(XYCoarse xyCoarse)
    {
        return xyCoarse.getX() * UIModelConsts.X_TILE_SIZE;
    }
    
    public static int getY(XYCoarse xyCoarse)
    {
        return xyCoarse.getY() * UIModelConsts.Y_TILE_SIZE;
    }
    
    public static int getXTopLeft(XYCoarse xyCoarse, int diameter)
    {
        return getX(xyCoarse) - diameter/2;
    }
    
    public static int getYTopLeft(XYCoarse xyCoarse, int diameter)
    {
        return getY(xyCoarse) - diameter/2;
    }

    // select classes may override
    public int getScoreValue()        
    {
        return 0;
    }
    
    public int giveCredit(PacDasher pacDasher)
    {
        int credit = getScoreValue();
        pacDasher.addToScore(credit);
        return credit;
    }
    
    /*
     * Whether to render on the intersection of grid lines
     * or on a block.
     */
    public boolean isIntersectionBased()
    {
        // default
        return false;
    }
    
    // for efficiency use rather than instanceof
    public abstract int getTypeID();
    
    public abstract Object clone();
    
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        VisualObject vo = (VisualObject) o;
        return vo.getTypeID() == this.getTypeID();
    }
}