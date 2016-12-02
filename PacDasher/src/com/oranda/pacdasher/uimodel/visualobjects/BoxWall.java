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
import java.util.List;

public class BoxWall extends Wall
{   
	protected static Color color = Color.BLUE;
	     
    public BoxWall()
    {
    }
    
    public static void setColor(Color color)
    {
    	BoxWall.color = color;
    }
                
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        g.setColor(color);
        g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + UIModelConsts.X_TILE_SIZE,
                getYTopLeft(xyCoarse, 0) + 4);
        g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + UIModelConsts.Y_TILE_SIZE,
                getYTopLeft(xyCoarse, 0) + 8);
    }
    
    public void render(Graphics g, XYCoarse xyCoarse, List neighbors)
    {
        g.setColor(color);
        //Const.logger.fine("xyCoarse " + xyCoarse + ", neighbors" + neighbors);

        if (neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.RIGHT))
        {
            
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 16,
                getYTopLeft(xyCoarse, 0) + 4);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + 16,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 4);

        }
        else if (neighbors.contains(DirectionCode.DOWN) 
            && neighbors.contains(DirectionCode.RIGHT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + 16,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 16,
                getYTopLeft(xyCoarse, 0) + 4);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 16);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 16);
                
        }
        else if (neighbors.contains(DirectionCode.LEFT) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 4);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 16);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 16,
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 8);
                
        }
        else if (neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.LEFT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 4,
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 4);
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + 8,
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 0,
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + 8);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + 4);        
        }
        else if (neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + 4,
                getYTopLeft(xyCoarse, 0) + UIModelConsts.X_TILE_SIZE);
            g.drawLine(getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + 8,
                getYTopLeft(xyCoarse, 0) + UIModelConsts.X_TILE_SIZE);
        }
        else
        {
            render(g, xyCoarse);
        }
    
    }    
    
    public static int getDiameter()
    {
        return diameter;
    }
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_BOX_WALL;
    }
    
    public Object clone()
    {
        return new BoxWall();
    }
}