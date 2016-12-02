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

public class XY
{
    protected int x, y;
    
    public XY(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public boolean equals(Object o)
    {    
        XY xy = (XY) o;
        return (x == xy.x && y == xy.y);
    }
        
    public int hashCode()
    {
        long bits = java.lang.Double.doubleToLongBits(x);
        bits ^= java.lang.Double.doubleToLongBits(y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));        
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    // for object reuse
    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public String toString()
    {
        return "x=" + x + ",y=" + y;
    }
    
    public Object clone()
    {
        return new XY(x, y);
    } 
}