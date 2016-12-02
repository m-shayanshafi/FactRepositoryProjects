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
import java.util.ArrayList;

public abstract class MobileVisualObject extends AnimatedVisualObject 
implements IMobile
{
    protected PosAndDirection posAndDirection;
    protected PosAndDirection initPosAndDirection;
    
    protected DirtyArea dirtyArea = new DirtyArea();    
    
    public MobileVisualObject()
    {
    }

    public void initialize(XYCoarse xyCoarse)
    {

    	super.initialize(xyCoarse);
        
    	Direction direction = new Direction(DirectionCode.LEFT);
        this.posAndDirection = new PosAndDirection(this, xyCoarse, direction);
        this.initPosAndDirection = (PosAndDirection) posAndDirection.clone();
        Const.logger.fine("Initialized posAndDirection to " + this.posAndDirection);
    }
    
    public void reset()
    {
        this.posAndDirection = (PosAndDirection) initPosAndDirection.clone();
        //Const.logger.fine("Reset posAndDirection to " + this.posAndDirection);
    }
             
    public static int getDiameter() 
    {
        // default
        return UIModelConsts.DEFAULT_SPRITE_SIZE;
    }    
    
    public void render(Graphics g) {
        // should not be called
        Const.logger.fine("");
    }
    
    public void render(Graphics g, XYCoarse xyCoarse)
    {
        setPos(xyCoarse);
        this.render(g);
    }

    public void setPos(XYCoarse xyCoarse)
    {
        if (posAndDirection == null)
        {
            String msg = "posAndDirection null for " + getTypeID();
        	Const.logger.severe(msg);
        	throw new RuntimeException(msg);
        }
        else
        {
        	posAndDirection.setPos(xyCoarse);
        }
    }
    
    public void setInitPos(XYCoarse xyCoarse)
    {        
        posAndDirection.setPos(xyCoarse);
        this.initPosAndDirection = (PosAndDirection) posAndDirection.clone();
    }
    
    public XY getInitPosXY()
    {
        return this.initPosAndDirection.getXY();
    }
    
    public XY getXY()
    {
        return posAndDirection.getXY();        
    }
    
    public XYCoarse getXYCoarse()
    {
        return posAndDirection.nearestXYCoarse();
    }
        
    public DirectionCode getDirectionCode()
    {
        return posAndDirection.getCurDirectionCode();
    }

    public boolean isMovingHorizontally()
    {
        return getDirectionCode() == DirectionCode.RIGHT
            || getDirectionCode() == DirectionCode.LEFT;
    }

    public boolean isMovingVertically()
    {
        return getDirectionCode() == DirectionCode.UP
            || getDirectionCode() == DirectionCode.DOWN;
    }    
    
    protected abstract void move();
    
    public void moveAndSetDirtyArea()
    {
        move();
        setDirtyArea();
    }
        
        
/*
    public void clip(Graphics g1, int x, int y)
    {
    }
*/
    public int getXTopLeft(XYCoarse xyCoarse)
    {        
        return (xyCoarse.getX() - 1) * (UIModelConsts.X_TILE_SIZE);
    }
    
    public int getYTopLeft(XYCoarse xyCoarse)
    {
        return (xyCoarse.getY() - 1) * (UIModelConsts.Y_TILE_SIZE);
    }
    
    // like Rectangle.contains() but Rectangles are garbage
    public boolean envelopes(int xLeft, int xRight, int yTop, int yBottom)
    {
        int thisXLeft = getXLeft();
        int thisXRight = getXRight();
        int thisYTop = getYTop();
        int thisYBottom = getYBottom();
        
        if (xLeft >= thisXLeft - 2 && xRight <= thisXRight + 2
            && yTop >= thisYTop - 2 && yBottom <= thisYBottom + 2)
        {
            return true;
        }
        return false;
    }
    
    public int getXLeft()
    {
        return getXY().getX() - getDiameter()/2;
    }
    
    public int getXRight()
    {
        return getXY().getX() + getDiameter()/2;
    }
    
    public int getYTop()
    {
        return getXY().getY() - getDiameter()/2;
    }
    
    public int getYBottom()
    {
        return getXY().getY() + getDiameter()/2;
    }


    public void setDirtyArea()
    {
    	//this.dirtyArea.addMVOArea(this);

        XYCManager xycm = XYCManager.getInstance();
        XYCoarse xyCoarse = getXYCoarse();
        int xCoarse = xyCoarse.getX();
        int yCoarse = xyCoarse.getY();
        XYCoarse xycLeftTop = null;
        XYCoarse xycRightBottom = null;

        xycLeftTop = xycm.createXYC(xCoarse - 2, yCoarse - 2);
        xycRightBottom = xycm.createXYC(xCoarse + 2, yCoarse + 2);
              
        /*
        DirectionCode dirCode = getDirectionCode();
        if (dirCode == DirectionCode.LEFT)
        {
            xycLeftTop = xycm.createXYC(xCoarse - 1, yCoarse - 1);
            xycRightBottom = xycm.createXYC(xCoarse + 2, yCoarse);
        }
        else if (dirCode == DirectionCode.RIGHT)
        {
            xycLeftTop = xycm.createXYC(xCoarse - 2, yCoarse - 1);
            xycRightBottom = xycm.createXYC(xCoarse + 1, yCoarse);
        
        }
        else if (dirCode == DirectionCode.UP)
        {
            xycLeftTop = xycm.createXYC(xCoarse - 1, yCoarse - 1);
            xycRightBottom = xycm.createXYC(xCoarse, yCoarse + 2);
        
        }
        else if (dirCode == DirectionCode.DOWN)
        {
            xycLeftTop = xycm.createXYC(xCoarse - 1, yCoarse - 2);
            xycRightBottom = xycm.createXYC(xCoarse, yCoarse + 1);
        
    	}
    	*/
        this.dirtyArea.set(xycLeftTop, xycRightBottom);
        
    }
    
    
    public DirtyArea getDirtyArea()
    {
        if (dirtyArea == null)
        {
            setDirtyArea();
        }
        return dirtyArea;
    }
    
    public abstract int getTypeID();
    
    public Object clone()
    {
        return this;
    }
    
    public String toString()
    {
        return getClass().getName() + " " + posAndDirection;
    }
    

}    