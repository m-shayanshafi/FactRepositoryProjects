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

public class DirectionCode implements Cloneable
{
    private char value;
    
    protected DirectionCode(char v)
    {
        value = v;
    }
    

    
    protected Object clone()
    {
        return new DirectionCode(value);
    }
    
    public boolean equals(Object o)
    {    
        DirectionCode dirCode = (DirectionCode) o;            
        return (value == dirCode.getValue());
    }
    
    private char getValue()
    {
        return value;
    }    
    
    public final static DirectionCode STATIONARY = new DirectionCode('.');
    public final static DirectionCode DOWN = new DirectionCode('D');
    public final static DirectionCode UP = new DirectionCode('U');
    public final static DirectionCode LEFT = new DirectionCode('L');
    public final static DirectionCode RIGHT = new DirectionCode('R');

    public String toString()
    {
        return Character.toString(value);
    }
    
}