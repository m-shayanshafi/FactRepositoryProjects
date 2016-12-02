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

public class GhostStrategyPinky extends GhostStrategy
{
        
    public GhostStrategyPinky(Ghost ghost)
    {
        super(ghost);
    }

    /*
     * Pinky is like Blinky at a distance
     * Otherwise she tries to move in front of PacDasher
     */
    public void tryMove(PosAndDirection posAndDirection,
        XY pacDasherXy, DirectionCode pacDasherDirectionCode,
        MoveAttempt moveAttempt)
    {
        XY xy = ghost.getXY();
        int verticalDistance = Math.abs(xy.getY() - pacDasherXy.getY());
        int horizontalDistance = Math.abs(xy.getX() - pacDasherXy.getX());
        
        int mazeWidth = Maze.DRAWABLE_WIDTH;
        int sumDistance = verticalDistance + horizontalDistance;
        int nearLength = UIModelConsts.Y_TILE_SIZE * 6;
        if (sumDistance > mazeWidth || sumDistance < nearLength ||
            !((xy.getX()%UIModelConsts.X_TILE_SIZE == 0 && ghost.isMovingHorizontally())
                || (xy.getY()%UIModelConsts.Y_TILE_SIZE == 0 && ghost.isMovingVertically())))
        {  
            super.tryMove(posAndDirection, pacDasherXy, null, moveAttempt);    
        }
        else
        {
            // try to move in front of PacDasher
            int xPacPredicted = pacDasherXy.getX();
            int yPacPredicted = pacDasherXy.getY();
            if (pacDasherDirectionCode.equals(DirectionCode.UP))
            {
                yPacPredicted -= nearLength;
            }
            else if (pacDasherDirectionCode.equals(DirectionCode.DOWN))
            {
                yPacPredicted += nearLength;
            }
            else if (pacDasherDirectionCode.equals(DirectionCode.LEFT))
            {
                xPacPredicted -= nearLength;
            }
            else if (pacDasherDirectionCode.equals(DirectionCode.RIGHT))
            {
                xPacPredicted += nearLength;
            }
            
            XY xyPacPredicted = new XY(xPacPredicted, yPacPredicted);
             
            tryMoveDirections(posAndDirection, (java.util.List) possibleDirections.clone(),
                xyPacPredicted, moveAttempt);
                        
        }
     }
}