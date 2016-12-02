/**
 *  PacDasher application. For explanation of this class, see below. 
 *  Copyright (c) 2004 James McCabe. Email: code@oranda.com 
 *  http://www.oranda.com/java/pacdasher/
 * 
 *  PacDasher is free software under the Aladdin license (see license  
 *  directory). You are free to play, copy, distribute, and modify it
 *  except for commercial purposes. You may not sell this code, or
 *  compiled versions of it, or anything which incorporates either of these.
 * 
 */
 
package com.oranda.pacdasher.uimodel.ghosts;

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.*;

import java.util.Random;

class GMoveAttemptReturning extends MoveAttempt
{
    protected static Random randomGenerator = new Random();
    
    private static MoveAttempt moveAttempt;
    private GMoveAttemptReturning() { }
    public static MoveAttempt getInstance() 
    { 
        if (moveAttempt == null)
        {
            moveAttempt = new GMoveAttemptReturning();
        }
        return moveAttempt; 
    }
            
    public DirectionCode propose(Ghost ghost, XY pacXy, 
            java.util.List<DirectionCode> allowedDirections)
    {
        return allowedDirections.get(0);
        /*
         DirectionCode desiredDirectionCode;
        XY xy = ghost.getXY();
        XY initPosXy = ghost.getInitPosXY();
        
        int up = xy.getY() - initPosXy.getY();
        int left = xy.getX() - initPosXy.getX(); 
        int down = initPosXy.getY() - xy.getY();
        int right = initPosXy.getX() - xy.getX();
        
        DirectionCode curDirectionCode = ghost.getPosAndDirection().getCurDirectionCode();
        
        if (allowedDirections.contains(DirectionCode.UP)
            && up <= left && up <= down && up < right)
        {
            desiredDirectionCode = DirectionCode.UP;
        }
        else if (allowedDirections.contains(DirectionCode.LEFT)
            && left <= down && left <= right)
        {
            desiredDirectionCode = DirectionCode.LEFT;
        }
        else if (allowedDirections.contains(DirectionCode.DOWN)
            && down <= right)
        {
            desiredDirectionCode = DirectionCode.DOWN;
        }
        else if (allowedDirections.contains(DirectionCode.RIGHT) )
        {
            desiredDirectionCode = DirectionCode.RIGHT;
        }
        else
        {
            int r = randomGenerator.nextInt(allowedDirections.size());
            desiredDirectionCode = allowedDirections.get(r);
        }
        Const.logger.fine("Allowed directions: " + allowedDirections + "..."
                + "Desired direction: " + desiredDirectionCode);
        return desiredDirectionCode;   
        */                      
    }
   
}    