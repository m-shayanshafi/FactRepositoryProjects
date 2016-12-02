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
import java.awt.Image;
import java.awt.Graphics;

public class Background extends VisualObject
{
    private Color color = UIModelConsts.MAZE_BG_COLOR;
    protected static int diameter = UIModelConsts.X_TILE_SIZE;
    protected Image iBackground;
    
    public Background()
    {
        super();
        /* Read background image? */
    }
        
    public void render(Graphics g, XYCoarse xyCoarse)
    {
    	/*g.clearRect(getXTopLeft(xyCoarse, 0), 
            getYTopLeft(xyCoarse, 0), 
            diameter, diameter);
    	*/
        g.setColor(color);
        g.fillRect(getXTopLeft(xyCoarse, 0), 
            getYTopLeft(xyCoarse, 0), 
            diameter, diameter);
        
        /*g.drawImage(iBackground, 
            getXTopLeft(xyCoarse, 0), getYTopLeft(xyCoarse, 0), 
            diameter, diameter, null);*/

    }
    
    public static int getDiameter()
    {
        return diameter;
    }
     
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_BACKGROUND;
    }
    
    public Object clone()
    {
        return new Background();
    }
}