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
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.util.*;

public class GhostStrategyClyde extends GhostStrategy
{
        
    public GhostStrategyClyde(Ghost ghost)
    {
        super(ghost);
    }

    /*
     * Clyde is like Blinky but dumber
     */
    public void tryMove(PosAndDirection posAndDirection,
        XY pacDasherXy, DirectionCode pacDasherDirectionCode,
        MoveAttempt moveAttempt)
    {
        XY xy = (XY) ghost.getXY().clone();
        if ((xy.getX()%(UIModelConsts.X_TILE_SIZE * 7) < 2 && ghost.isMovingHorizontally())
            || (xy.getY()%(UIModelConsts.Y_TILE_SIZE * 7) < 2 && ghost.isMovingVertically()))
        {
            //Const.logger.fine("move() xy " + xy);
            //resetPossibleDirections();
            // if at a possible turning point, decide upon a new direction
            tryMoveDirections(posAndDirection,
                (java.util.List) possibleDirections.clone(),
                pacDasherXy, moveAttempt);
        }
        else 
        {            
            XY newXy = posAndDirection.move();

            if (newXy.equals(xy))
            {
                // move unsuccessful  
                // next line includes posAndDirection.move()              
                tryMoveDirections(posAndDirection,
                    (java.util.List) possibleDirections.clone(),
                    pacDasherXy, moveAttempt);    
            }
        }    
    }
}