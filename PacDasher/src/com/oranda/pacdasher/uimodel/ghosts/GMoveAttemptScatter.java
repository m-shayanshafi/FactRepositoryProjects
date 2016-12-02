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

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.*;

import java.util.Random;

class GMoveAttemptScatter extends MoveAttempt
{
    protected static Random randomGenerator = new Random();
        
    private static MoveAttempt moveAttempt;
    private GMoveAttemptScatter() { }
    public static MoveAttempt getInstance() 
    { 
        if (moveAttempt == null)
        {
            moveAttempt = new GMoveAttemptScatter();
        }
        return moveAttempt; 
    }
            
    public DirectionCode propose(Ghost ghost, XY pacDasherXy, 
        java.util.List<DirectionCode> allowedDirections)
    {            
        DirectionCode desiredDirectionCode;
        DirectionCode curDirectionCode = ghost.getPosAndDirection().getCurDirectionCode();
        int r = randomGenerator.nextInt(allowedDirections.size());
         int r1 = randomGenerator.nextInt(allowedDirections.size() * 4);
            
        if (allowedDirections.contains(DirectionCode.LEFT)
            && !curDirectionCode.equals(DirectionCode.RIGHT))
        {
            desiredDirectionCode = DirectionCode.LEFT;
        }
        else if (allowedDirections.contains(DirectionCode.UP)
            && !curDirectionCode.equals(DirectionCode.DOWN)
            && r1 == 0)
        {
            desiredDirectionCode = DirectionCode.UP;
        }
        else if (allowedDirections.contains(DirectionCode.RIGHT) 
            && !curDirectionCode.equals(DirectionCode.LEFT))
        {
            desiredDirectionCode = DirectionCode.RIGHT;
        }
        else if (allowedDirections.contains(DirectionCode.DOWN)
            && !curDirectionCode.equals(DirectionCode.UP))
        {
            desiredDirectionCode = DirectionCode.DOWN;
        }
        else
        {
            desiredDirectionCode = allowedDirections.get(r);
        }
        
        //Const.logger.fine("scatter found " + desiredDirectionCode);         
        return desiredDirectionCode;
                                 
    }
    
}