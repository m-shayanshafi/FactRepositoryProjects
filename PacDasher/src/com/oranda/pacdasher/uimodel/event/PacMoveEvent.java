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
 
package com.oranda.pacdasher.uimodel.event;

import com.oranda.pacdasher.uimodel.IMobile;
import com.oranda.pacdasher.uimodel.util.DirectionCode;
import com.oranda.pacdasher.uimodel.util.XY;

public class PacMoveEvent
{
    // make this a singleton to save memory
    // Assuming only one pacDasher, one animation thread,
    // there is only one pacMoveEvent at a time
    private static PacMoveEvent pacMoveEvent = new PacMoveEvent();
    
    private XY xy;
    private DirectionCode directionCode;
    private IMobile iMobileSource;
    
    private PacMoveEvent()
    {
    }       
    
    public static PacMoveEvent getInstance(XY xy, DirectionCode directionCode, 
        IMobile iMobileSource)
    {
        pacMoveEvent.xy = xy;
        pacMoveEvent.directionCode = directionCode;
        pacMoveEvent.iMobileSource = iMobileSource;
        return pacMoveEvent;
    }
    
    public IMobile getSource()
    {
        return iMobileSource;
    }
    
    public XY getXy()
    {
        return xy;
    }
    
    public DirectionCode getDirectionCode()
    {
        return directionCode;
    }
    
}
