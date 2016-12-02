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

public class Energizer extends AnimatedVisualObject
{
    protected Color color = Color.WHITE;
    protected static int diameter = 12;
    protected static int scoreValue = UIModelConsts.SCORE_ENERGIZER;
    
    private static boolean isEnergizerDisplayed = true;
    
    public static void toggleIsEnergizerDisplayed()
    {
        isEnergizerDisplayed = !isEnergizerDisplayed;
    }
            
    public void initialize()
    {
        super.initialize(xyCoarse);
    }
                
    public void render(Graphics g)
    {
        render(g, this.xyCoarse);
    }
    
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        /*
         * flashing
         */
        //clip(g, getXTopLeft(xyCoarse, diameter), 
        //    getYTopLeft(xyCoarse, diameter));;
        if (!isEnergizerDisplayed)
        {    
            // hide
            g.setColor(UIModelConsts.MAZE_BG_COLOR);    
        } 
        else
        {
            // show
            g.setColor(color);
        }
        g.fillOval(getXTopLeft(xyCoarse, diameter), 
            getYTopLeft(xyCoarse, diameter), 
            diameter, diameter);
        //g.setClip(null);
    }
    
    /*public void clip(Graphics g1, Graphics g2, XYCoarse xyCoarse)
    {
        clip(g1, xyCoarse);
        clip(g2, xyCoarse);
    }
    */
    
    public void clip(Graphics g, int x, int y)
    {
        g.clipRect(x, y, diameter, diameter); 
    }
    
    public static int getDiameter()
    {
        return diameter;
    }

    public int getScoreValue()
    {
        return scoreValue;
    }
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_ENERGIZER;
    }
    
    public Object clone()
    {
        return new Energizer();
    }
}