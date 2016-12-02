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

/*
 * just like XY bu use only for coarse (tile-based) coordinates
 */
public class XYCoarse extends XY
{
    public XYCoarse(int x, int y)
    {
        super(x, y);
    }
    
    public XY translateToXY()
    {
        return new XY(x * UIModelConsts.X_TILE_SIZE,
                      y * UIModelConsts.Y_TILE_SIZE);
    }
    
    // these are less memory intensive than translateToXY
    public int getXTranslated() 
    {
        return x * UIModelConsts.X_TILE_SIZE;
    }
    
    public int getYTranslated() 
    {
        return y * UIModelConsts.Y_TILE_SIZE;
    }    
    
    public Object clone()
    {
        return new XYCoarse(x, y);
    } 
    
    public boolean equals(final Object o)
    {    
        //if (xyCoarse == null) return false;
        XYCoarse xyCoarse = (XYCoarse) o;
        //Const.logger.fine( x + "=" + XYCoarse.getX()
        //    + "? " + y + "=" + XYCoarse.getY());            
        return (x == xyCoarse.x && y == xyCoarse.y);
    }
    

    
    public boolean near(XYCoarse xyCoarse, int distance)
    {
        if (Math.abs(xyCoarse.x - x) <= distance)
        {
            return true;
        }
        return false;
    }

}