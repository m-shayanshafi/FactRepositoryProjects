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

/*
 * Inky likes to try the directions: Right, Left, Down, Up in order
 * except when he is near to PacDasher - then he is like Blinky
 */
public class GhostStrategyInky extends GhostStrategy
{
        
    public GhostStrategyInky(Ghost ghost)
    {
        super(ghost);
    }
    
    public void tryMove(PosAndDirection posAndDirection,
        XY pacDasherXy, DirectionCode pacDasherDirectionCode,
        MoveAttempt moveAttempt)
    {
        XY xy = ghost.getXY();
        int verticalDistance = Math.abs(xy.getY() - pacDasherXy.getY());
        int horizontalDistance = Math.abs(xy.getX() - pacDasherXy.getX());
        
        int mazeWidth = Maze.DRAWABLE_WIDTH;
        int sumDistance = verticalDistance + horizontalDistance;
        int nearLength = UIModelConsts.Y_TILE_SIZE * 16;
        if (sumDistance < nearLength)
        {  
            super.tryMove(posAndDirection, pacDasherXy, 
                pacDasherDirectionCode, moveAttempt);    
        }
        else
        {    
            if (getGhostState() == GhostState.NORMAL_GHOST_STATE)
            {
                moveAttempt = new NormalMoveAttemptInky();
            }
            tryMoveDirections(posAndDirection,
                (java.util.List) possibleDirections.clone(),
                pacDasherXy, moveAttempt);                    
        }
     }
     


    /*
     * Default algorithm for Blinky supposed to be
     * 1. Always try to close the horizontal or vertical distance
     *    between himself or PacDasher, whichever is shorter.
     * (2. If distances equal, use priority order UP, LEFT, DOWN, RIGHT)
     */    
    class NormalMoveAttemptInky extends MoveAttempt
    {
        public DirectionCode propose(Ghost ghost, XY pacDasherXy, 
                java.util.List<DirectionCode> allowedDirections)
        {
            DirectionCode desiredDirectionCode;
            XY xy = ghost.getXY();
              
            DirectionCode curDirectionCode = ghost.getPosAndDirection().getCurDirectionCode();

            
            if (allowedDirections.contains(DirectionCode.UP)
                && !curDirectionCode.equals(DirectionCode.DOWN))
            {
                desiredDirectionCode = DirectionCode.UP;
            }
            else if (allowedDirections.contains(DirectionCode.LEFT)
                && !curDirectionCode.equals(DirectionCode.RIGHT))
            {
                desiredDirectionCode = DirectionCode.LEFT;
            }
            else if (allowedDirections.contains(DirectionCode.DOWN)
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
     
     
}