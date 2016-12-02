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
 
package com.oranda.pacdasher.uimodel.ghosts;

import com.oranda.pacdasher.uimodel.util.*;

import java.util.Random;

class GMoveAttemptFlight extends MoveAttempt
{
    protected static Random randomGenerator = new Random();
    
    private static MoveAttempt moveAttempt;
    private GMoveAttemptFlight() { }
    public static MoveAttempt getInstance() 
    { 
        if (moveAttempt == null)
        {
            moveAttempt = new GMoveAttemptFlight();
        }
        return moveAttempt; 
    }
            
    public DirectionCode propose(Ghost ghost, XY pacDasherXy, 
        java.util.List<DirectionCode> allowedDirections)
    {
        DirectionCode desiredDirectionCode;
        XY xy = ghost.getXY();
 
        int up = xy.getY() - pacDasherXy.getY();
        int left = xy.getX() - pacDasherXy.getX(); 
        int down = pacDasherXy.getY() - xy.getY();
        int right = pacDasherXy.getX() - xy.getX();
        
        
        DirectionCode curDirectionCode = ghost.getPosAndDirection().getCurDirectionCode();
        
        if (allowedDirections.contains(DirectionCode.UP)
            && up <= left && up <= down && up < right 
            && !curDirectionCode.equals(DirectionCode.DOWN))
        {
            desiredDirectionCode = DirectionCode.UP;
        }
        else if (allowedDirections.contains(DirectionCode.LEFT)
            && left <= down && left <= right 
            && !curDirectionCode.equals(DirectionCode.RIGHT))
        {
            desiredDirectionCode = DirectionCode.LEFT;
        }
        else if (allowedDirections.contains(DirectionCode.DOWN)
            && down <= right 
            && !curDirectionCode.equals(DirectionCode.UP))
        {
            desiredDirectionCode = DirectionCode.DOWN;
        }
        else if (allowedDirections.contains(DirectionCode.RIGHT) 
            && !curDirectionCode.equals(DirectionCode.LEFT))
        {
            desiredDirectionCode = DirectionCode.RIGHT;
        }
        else
        {
            int r = randomGenerator.nextInt(allowedDirections.size());
            desiredDirectionCode = allowedDirections.get(r);
        }
                   
        return desiredDirectionCode;
                                 
    }
}    