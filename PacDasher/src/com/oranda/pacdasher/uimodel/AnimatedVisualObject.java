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
import java.awt.Image;
import java.util.ArrayList;

/**
 * Essentially, a sprite. 
 * "A sprite is any object that moves or interacts in a game."
 */
public abstract class AnimatedVisualObject extends VisualObject 
implements IAnimated
{
    
    protected XYCoarse xyCoarse;
    
    public AnimatedVisualObject()
    {
    }
    
    public void initialize(XYCoarse xyCoarse)
    {
    	this.xyCoarse = xyCoarse;
    }
    
    public XYCoarse getXYCoarse()
    {
    	return this.xyCoarse;
    }
    
    public abstract void render(Graphics g);

    public abstract int getTypeID();
    
    // utility method
    public void drawImage(Graphics g, Image curImg, 
        int xTopCorner, int yTopCorner, int width, int height)
    {          
        g.drawImage(curImg, 
                xTopCorner, yTopCorner, width, height, 
                null);               
    }
    
    public abstract Object clone();
    
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        AnimatedVisualObject vo = (AnimatedVisualObject) o;
        return vo.getTypeID() == this.getTypeID()
                && vo.xyCoarse.equals(this.xyCoarse);
    }
}    