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
 

import java.util.HashSet;

/**
 * PosAndDirection
 * 
 */
 
public class PosAndDirection implements Cloneable
{
    
    private final static int mazeWidth = Maze.DRAWABLE_WIDTH;
    private final static int mazeHeight = Maze.DRAWABLE_HEIGHT;
        
    /*
     * There are two systems of coordinates:
     * one nearer to the graphics, pixel-based: x,y 
     * and one nearer to the model, tile-based: 
     * xCoarse, and yCoarse
     */
    private XY xy = new XY(-1, -1);
    private XYCoarse xyCoarse = new XYCoarse(-1, -1);
    
    // save last x and y matching exactly a coarse coordinate
    //private XY xyLast;
    
    private Direction direction;

    private static int increment = UIModelConsts.MOVE_SIZE;
    
    private MobileVisualObject mvo;
        
    public PosAndDirection(MobileVisualObject mvo,
        XYCoarse xyCoarseInit, Direction direction)
    throws IllegalArgumentException
    {
        setPos(xyCoarseInit);
        if (direction == null)
        {
            throw new IllegalArgumentException("direction");
        }
        this.direction = direction;    
        this.mvo = mvo;
    }
    
    public Object clone()
    {
        Direction clonedDirection = (Direction) direction.clone();
        return new PosAndDirection(mvo, xyCoarse, clonedDirection);
    }
    
    public void setPos(XYCoarse xyCoarse)
    {
        this.xyCoarse = xyCoarse;
        this.xy = translateXY(xyCoarse);

        //xLast = x;
        //yLast = y;
    }
    
    public XY translateXY(XYCoarse xyCoarse)
    {
        int x = translateX(xyCoarse.getX());
        int y = translateY(xyCoarse.getY());
        return new XY(x, y);
    }
    
    public int translateX(int xCoarse)
    {
        return xCoarse * UIModelConsts.X_TILE_SIZE;
    }
    public int translateY(int yCoarse)
    {
        return yCoarse * UIModelConsts.Y_TILE_SIZE;
    }    
        
    public int translateXCoarse(int x)
    {
        return x/UIModelConsts.X_TILE_SIZE;
    }
    
    public int translateYCoarse(int y)
    {
        return y/UIModelConsts.Y_TILE_SIZE;
    }
    
    public int nearestXCoarse()
    {
        return translateXCoarse(getX());
    }
    
    public int nearestYCoarse()
    {
        return translateYCoarse(getY());
    }
    
    public XYCoarse nearestXYCoarse()
    {
        return XYCManager.getInstance().createXYC(nearestXCoarse(), nearestYCoarse());
    }
    
    public XY move()
    {
        //Const.logger.fine("Trying to move 1st " + direction.getDirectionCode());
        if (move(direction.getDesiredDirectionCode()))
        {
            //Const.logger.fine("curDirectionCode " + direction.getCurDirectionCode());
            direction.setCurDirectionToDesired();
        }
        else if (move(direction.getCurDirectionCode()))
        {
            //Const.logger.fine("curDirectionCode");
        }
        else
        {

        }
        
        return xy;
    }
    
    boolean move(DirectionCode directionCode)
    {
        //Const.logger.fine("xCoarse " + xCoarse 
        //    + ", yCoarse " + yCoarse);
        
        int xNext, yNext;
        if (directionCode == DirectionCode.RIGHT)
        { 
            if (!isRightBlocked())
            {
                // first implement wraparound
               
                if (getX() > mazeWidth + UIModelConsts.DEFAULT_SPRITE_SIZE / 2)
                {
                    setX(getX() - UIModelConsts.WRAP_ADJUST_X);
                }
                
                // mainline
                setX(getX() + increment);
                return true;
            }

        }
        else if (directionCode == DirectionCode.LEFT)
        {
            if (!isLeftBlocked())
            {
                // first implement wraparound
                if (getX() < 0 - UIModelConsts.DEFAULT_SPRITE_SIZE / 2)
                {
                    setX(getX() + UIModelConsts.WRAP_ADJUST_X);
                }
                
                // main line
                setX(getX() - increment);
                return true;
            }
        }
        else if (directionCode == DirectionCode.DOWN)
        {
            if (!isDownBlocked())
            {
                // first implement wraparound
                if (getY() > mazeHeight + UIModelConsts.DEFAULT_SPRITE_SIZE / 2)
                {
                    setY(getY() - UIModelConsts.WRAP_ADJUST_Y);
                }                
                setY(getY() + increment);
                return true;
            }
        }
        else if (directionCode == DirectionCode.UP)
        {
            if (!isUpBlocked())
            {
                // first implement wraparound
                if (getY() < 0 - UIModelConsts.DEFAULT_SPRITE_SIZE / 2)
                {
                    setY(getY() + UIModelConsts.WRAP_ADJUST_Y);
                }
                
                setY(getY() - increment);
                return true;
            }
        }
        return false;
    }

    public boolean isStationary()
    {
        return (direction.getCurDirectionCode() == DirectionCode.STATIONARY);
    }
    
    public DirectionCode getCurDirectionCode()
    {
        return direction.getCurDirectionCode();
    }
    
    public DirectionCode getDesiredDirectionCode()
    {
        return direction.getDesiredDirectionCode();
    }
    
    public DirectionCode getPrevDirectionCode()
    {
        return direction.getPrevDirectionCode();
    }
    
    public void setDesiredDirectionCode(DirectionCode desiredDirectionCode)
    {
        direction.setDesiredDirectionCode(desiredDirectionCode);
    }
    
    public Direction getDirection()
    {
        return direction;
    }
    
    public DirectionCode getDirectionCode()
    {
        return direction.getCurDirectionCode();
    }

    public int getX()
    {
        return xy.getX();
    }
        
    public int getY()
    {
        return xy.getY();
    }

    
    public void setX(int x)
    {
        xy.setX(x);
    }
    
    public void setY(int y)
    {
        xy.setY(y);
    }
    
    /*
    public XYCoarse getXYCoarse()
    {
        return xyCoarse;
    }*/
    
    public XY getXY()
    {
        return xy;
    }
    
    /*
     * These methods check if the PacDasher is blocked in
     * the direction currently requested. If PacDasher is
     * exactly placed on a square in the coarse coordinate
     * system, it is only necessary to check one other square;
     * however, if PacDasher is between tiles (more likely),
     * it's necessary to check two squares - hence the extraCondition.
     */
    
    public boolean isRightBlocked()
    {
        return isXBlocked(getX() + UIModelConsts.X_TILE_SIZE, getY());
    }

    public boolean isLeftBlocked()
    {
        return isXBlocked(getX() - 2 * UIModelConsts.X_TILE_SIZE, getY());
    }

    public boolean isDownBlocked()
    {
        return isYBlocked(getX(), getY() + UIModelConsts.Y_TILE_SIZE);
    }

    public boolean isUpBlocked()
    {
        return isYBlocked(getX(), getY() - 2 * UIModelConsts.Y_TILE_SIZE);
    }            
    
    public boolean isXBlocked(int xPossibleWall, int y)
    {
        int xCoarse = translateXCoarse(xPossibleWall);
        int yCoarse = translateYCoarse(y);
            
        if (xPossibleWall%UIModelConsts.X_TILE_SIZE >= increment)
        {
            // walls only occur in multiples of X_TILE_SIZE
            return false;
        }
        else
        {

            
            
            // check if two or three squares to the side are blocked
            // depending on whether PacDasher is between tiles
            boolean extraCondition = false;
            if (y%UIModelConsts.Y_TILE_SIZE >= increment)
            {
                extraCondition = isBlocked(xCoarse, yCoarse + 1);
            }
            
            //Const.logger.fine("isXblocked? " + xCoarse + "," + yCoarse
            //    + isBlocked(xCoarse, yCoarse - 1) 
            //   + isBlocked(xCoarse, yCoarse)
            //   + extraCondition);
            return isBlocked(xCoarse, yCoarse - 1) 
                || isBlocked(xCoarse, yCoarse) || extraCondition;
        }
    }        
    
    public boolean isYBlocked(int x, int yPossibleWall)
    {
        int xCoarse = translateXCoarse(x);
        int yCoarse = translateYCoarse(yPossibleWall);

        if (x > mazeWidth || x < 0)
        {
            //Const.logger.fine("isYBlocked? " + mazeWidth + " " + x);

            // boundary case (for horizontal wraparound)
            return true;
        }
        if (yPossibleWall%UIModelConsts.Y_TILE_SIZE >= increment)
        {
            // walls only occur in multiples of Y_TILE_SIZE
            return false;
        }
        else
        {

            // check if two or three squares vertically are blocked
            // depending on whether PacDasher is between tiles
            boolean extraCondition = false;
            if (x%UIModelConsts.X_TILE_SIZE >= increment)
            {
                extraCondition = isBlocked(xCoarse + 1, yCoarse);
            }
                
            return isBlocked(xCoarse - 1, yCoarse) 
                || isBlocked(xCoarse, yCoarse) || extraCondition;
        }
    }            
    
    public boolean isBlocked(int xCoarse, int yCoarse)
    {    
        boolean isBlocked 
            = UIModel.getInstance().getMaze().isBlocked(mvo, xCoarse, yCoarse);
        //Const.logger.fine("isBlocked " + isBlocked + " "
        //    + xCoarse + " " + yCoarse);
        return isBlocked;
    }
    
    public String toString()
    {
        return xy + " " + direction;
    }
} 