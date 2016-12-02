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

import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.XYCoarse;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

import java.awt.*;

/**
 * 
 */
public class Strawberry extends Fruit
{    
    private static int diameter = UIModelConsts.DEFAULT_SPRITE_SIZE;
    protected static int scoreValue = UIModelConsts.SCORE_STRAWBERRY;
     
    public Strawberry()
    {
        setImage(ResourceMgr.getInstance().fStrawberry); 
    }
        
    public void render(Graphics g)
    {
        render(g, this.xyCoarse);
    }    
    
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        int xTopCorner = getXTopLeft(xyCoarse, diameter);
        int yTopCorner = getYTopLeft(xyCoarse, diameter); 
        drawImage(g, this.img, xTopCorner, yTopCorner, diameter, diameter);
    }
    
    public int getScoreValue()
    {
        return scoreValue;
    }
    
    public Object clone()
    {
        Strawberry clone = new Strawberry();
        clone.initialize(this.getXYCoarse());
        return clone;
    }
}
