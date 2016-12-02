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

public class Wall extends VisualObject
{
    protected static Color color = Color.BLUE;
    
    protected static int xTileSize = UIModelConsts.X_TILE_SIZE;
    protected static int yTileSize = UIModelConsts.Y_TILE_SIZE;
    protected static int diameter = xTileSize;
    
    public Wall()
    {
    }
    
    public static void setColor(Color color)
    {
    	Wall.color = color;
    }
                
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        g.setColor(color);
        g.fillRect(getXTopLeft(xyCoarse, 0), 
            getYTopLeft(xyCoarse, 0), 
            diameter, diameter);
    }
    

    public void render(Graphics g, XYCoarse xyCoarse, List neighbors)
    {
        g.setColor(color);
        //Const.logger.fine("xyCoarse " + xyCoarse + ", neighbors" + neighbors);

        if (neighbors.size() == 4) 
        {
            //render(g, xyCoarse);    
        }
        else if (neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.DOWN)
            && neighbors.contains(DirectionCode.RIGHT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize);        
        }
        else if (neighbors.contains(DirectionCode.UP)
            && neighbors.contains(DirectionCode.DOWN) 
            && neighbors.contains(DirectionCode.LEFT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize);
        }
        else if (neighbors.contains(DirectionCode.RIGHT)
            && neighbors.contains(DirectionCode.LEFT) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0));
        }
        else if (neighbors.contains(DirectionCode.RIGHT)
            && neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.LEFT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);    
        } 
        else if (neighbors.size() == 2 
            && neighbors.contains(DirectionCode.RIGHT) 
            && neighbors.contains(DirectionCode.UP))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                180,                             
                90);
                
            // across
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize/2 - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);    
                
            // vertical
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize/2 + 1);
        }
        else if (neighbors.size() == 2 
            && neighbors.contains(DirectionCode.RIGHT) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                90,                             
                90);
                
            // across
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize/2,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0));    
                
            // vertical
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize/2,
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize);
        }
        else if (neighbors.size() == 2 
            && neighbors.contains(DirectionCode.LEFT) 
            && neighbors.contains(DirectionCode.UP))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                270,                             
                90);
                
            // across
            g.drawLine(getXTopLeft(xyCoarse, 0) - 4,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize/2 + 4,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);    
                
            // vertical
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) - 4,
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize/2 + 4);
                
        }
        else if (neighbors.size() == 2 
            && neighbors.contains(DirectionCode.LEFT) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                0,                             
                90);
                
            // across
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize/2,
                getYTopLeft(xyCoarse, 0));    
                
            // down
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0)  + yTileSize/2 - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize);            
        }
        else if (neighbors.contains(DirectionCode.RIGHT) 
            && neighbors.contains(DirectionCode.LEFT))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0));
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);
        }
        else if (neighbors.contains(DirectionCode.UP) 
            && neighbors.contains(DirectionCode.DOWN))
        {
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize);
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize);
        }
        else if (neighbors.size() == 1
            && neighbors.contains(DirectionCode.RIGHT))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                90,                             
                180);
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize/2 - 1,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0));
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize/2 - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);        
        }
        else if (neighbors.contains(DirectionCode.LEFT))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0) - 2,                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                90,                             
                -180);
            g.drawLine(getXTopLeft(xyCoarse, 0) - 2,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize/2,
                getYTopLeft(xyCoarse, 0));
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize/2 + 2,
                getYTopLeft(xyCoarse, 0) + yTileSize - 1);    
        }
        else if (neighbors.contains(DirectionCode.UP))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0) - 2,                             
                xTileSize,
                yTileSize,
                0,                             
                -180);
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize/2);
            g.drawLine(getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0),
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize/2 + 2);
        }
        else if (neighbors.contains(DirectionCode.DOWN))
        {
            g.drawArc(getXTopLeft(xyCoarse, 0),                          
                getYTopLeft(xyCoarse, 0),                             
                xTileSize,
                yTileSize,
                180,                             
                -180);
            g.drawLine(getXTopLeft(xyCoarse, 0)  + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize/2 - 1,
                getXTopLeft(xyCoarse, 0) + xTileSize - 1,
                getYTopLeft(xyCoarse, 0) + yTileSize);
            g.drawLine(getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0)  + yTileSize/2 - 1,
                getXTopLeft(xyCoarse, 0),
                getYTopLeft(xyCoarse, 0) + yTileSize);
        }    
        else
        {
            // do nothing
            //render(g, xyCoarse);
        }
    }    
    
    public static int getDiameter()
    {
        return diameter;
    }

    public boolean isNeighborly()        
    {
        return true;
    }
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_BOX_WALL;
    }
        
    public Object clone()
    {
        return new Wall();
    }
    

}