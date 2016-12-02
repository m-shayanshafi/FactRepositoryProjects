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

import java.awt.event.KeyEvent;


/* 
 * This class keeps track of the current direction
 * and desired direction, which depends on the state of 
 * the keys.
 */
public class Direction implements Cloneable
{
    DirectionCode curDirectionCode;
    DirectionCode desiredDirectionCode;
    DirectionCode prevDirectionCode;
    
    public Direction()
    {
        // default
        this(DirectionCode.LEFT);
    }
    
    public Direction(DirectionCode directionCode)
    {
        this.curDirectionCode = directionCode;
        this.desiredDirectionCode = this.curDirectionCode;
        this.prevDirectionCode = this.curDirectionCode;
    }    
    
    public DirectionCode getCurDirectionCode()
    {
        return curDirectionCode;
    }
    
    public DirectionCode getDesiredDirectionCode()
    {
        return desiredDirectionCode;
    }

    public DirectionCode getPrevDirectionCode()
    {
        return prevDirectionCode;
    }
    
    public void setDesiredDirectionCode(DirectionCode desiredDirectionCode)
    {
        this.desiredDirectionCode = desiredDirectionCode;
    }
    
    
    public void setCurDirectionToDesired()
    {
        if (!curDirectionCode.equals(desiredDirectionCode))
        {
            prevDirectionCode = (DirectionCode) curDirectionCode.clone();
            curDirectionCode = (DirectionCode) desiredDirectionCode;
        }
    }
    
        
    public static DirectionCode getDirectionCode(int keyCode) 
        throws IllegalArgumentException
    {
        switch (keyCode)
        {
            case KeyEvent.VK_DOWN:
                return DirectionCode.DOWN;
            case KeyEvent.VK_UP:
                return DirectionCode.UP;
            case KeyEvent.VK_LEFT:
                return DirectionCode.LEFT;
            case KeyEvent.VK_RIGHT:
                return DirectionCode.RIGHT;
            default:
                return DirectionCode.STATIONARY;
        }
    }    

    public String toString()
    {
        return "current: " + this.curDirectionCode.toString()
                + ";  desired: " + this.desiredDirectionCode.toString()
                + "; prev: " + this.prevDirectionCode.toString();
    }
        
    public Object clone()
    {
        return new Direction(this.curDirectionCode);
    }         
}