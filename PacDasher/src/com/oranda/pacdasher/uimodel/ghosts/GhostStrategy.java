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
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GhostStrategy
{
    protected static ArrayList<DirectionCode> possibleDirections;
        
    protected Ghost ghost;
    protected Random randomGenerator;
    protected int virtualTimeBeganReturning;
    private GhostState ghostState;
    
    static
    {
        possibleDirections = new ArrayList<DirectionCode>(4);
        possibleDirections.add(DirectionCode.UP);
        possibleDirections.add(DirectionCode.LEFT);
        possibleDirections.add(DirectionCode.DOWN);
        possibleDirections.add(DirectionCode.RIGHT);
    }
    
    public GhostStrategy(Ghost ghost)
    {
        this.ghost = ghost;        
        this.randomGenerator = new Random();
        ghostState = GhostState.NORMAL_GHOST_STATE;
    }

    public void move(PosAndDirection posAndDirection,
        XY pacDasherXy, DirectionCode pacDasherDirectionCode)
    {  
        if (ghostState == GhostState.FLIGHT_GHOST_STATE)
        {            
             /*
              // if flight time is up, restore to normal
             if (getVirtualTimeSinceFlight() > Ghost.TIME_FLIGHT_TOTAL)
             {
                 setStateNormalDueToTimeout();
             }
             // halve speed in flight state
             else 
             */
             // halve the speed of fleeing ghosts
             if (UIModel.getVirtualTime()%2 == 1)
             {
                 return; // i.e. don't move
             }
        }
        
        if (ghostState == GhostState.RETURNING_GHOST_STATE)
        {
             // if flight time is up, restore to normal
             if (getVirtualTimeSinceReturning() > Ghost.TIME_RETURNING)
             {
                 Const.logger.fine("setting back to normal, "
                        + "virtualTimeSinceReturning is " 
                        + getVirtualTimeSinceReturning());
                 setGhostState(GhostState.NORMAL_GHOST_STATE);
             }
             else
             {
                 return; // i.e. don't move
             }
        } 
        
        // slow down ghosts at the side exits to be faithful
        if (ghostState == GhostState.NORMAL_GHOST_STATE 
                || ghostState == GhostState.SCATTER_GHOST_STATE) 
        {
            if (UIModel.getVirtualTime()%2 == 1)
            {
                int xc = posAndDirection.nearestXCoarse();
                // Const.logger.fine("ghost " + ghost.getClass().getName() 
                //         + " xc: " + xc);
                int leftXCBoundary = 0;
                int rightXCBoundary = UIModelConsts.MAZE_WIDTH;
                if (xc < leftXCBoundary + 2 || xc > rightXCBoundary - 4) 
                {                    
                    return; // i.e. don't move  
                }
            }
        }
        
        // better to do this polymorphically using the State pattern    
        MoveAttempt moveAttempt;
        if (ghostState == GhostState.NORMAL_GHOST_STATE)
        {
            moveAttempt = GMoveAttemptNormal.getInstance();
        }
        else if (ghostState == GhostState.SCATTER_GHOST_STATE)
        {
            moveAttempt = GMoveAttemptScatter.getInstance();
        }
        else if (ghostState == GhostState.RETURNING_GHOST_STATE)
        {
            moveAttempt = GMoveAttemptReturning.getInstance();
        }
        else //if (ghostState == GhostState.FLIGHT_GHOST_STATE)
        {
            moveAttempt = GMoveAttemptFlight.getInstance();
        }
        
        tryMove(posAndDirection, pacDasherXy, 
            pacDasherDirectionCode, moveAttempt);

        //Const.logger.fine("GhostStrategy: after move " + posAndDirection.getXY());
    }

    public void tryMove(PosAndDirection posAndDirection,
        XY pacDasherXy, DirectionCode pacDasherDirectionCode,
        MoveAttempt moveAttempt)
    {
        XY xy = (XY) ghost.getXY().clone();

        if ((xy.getX()%UIModelConsts.X_TILE_SIZE == 0 && ghost.isMovingHorizontally())
            || (xy.getY()%UIModelConsts.Y_TILE_SIZE == 0 && ghost.isMovingVertically()))
        {
            //Const.logger.fine("move() xy " + xy);
            //resetPossibleDirections();
            // if at a possible turning point, decide upon a new direction
            tryMoveDirections(posAndDirection,
                (java.util.List) possibleDirections.clone(),
               pacDasherXy, moveAttempt);
            //XY newXy = posAndDirection.move();    
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

    /* 
     * Calls itself recursively until there is a move
     */
    public void tryMoveDirections(PosAndDirection posAndDirection,
        java.util.List allowedDirections, 
        XY pacDasherXy, MoveAttempt moveAttempt)
    {
                  
        if (allowedDirections.isEmpty())
        {
            return; // allow escape from infinite recursion
        }
        XY xy = (XY) ghost.getXY().clone();
        DirectionCode desiredDirectionCode 
                = moveAttempt.propose(ghost, pacDasherXy, allowedDirections);             
        posAndDirection.setDesiredDirectionCode(desiredDirectionCode);
        XY newXy = posAndDirection.move();     
        if (xy.equals(newXy))
        {
            // move unsuccessful
            allowedDirections.remove(desiredDirectionCode); 
            tryMoveDirections(posAndDirection, allowedDirections, 
                pacDasherXy, moveAttempt);
        }
    }
    
    /*
     * Ghosts have different returning times.
     */
    protected int getVirtualTimeBeganReturning()
    {
        return virtualTimeBeganReturning;
    }
    
    protected int getVirtualTimeSinceReturning()
    {
        return UIModel.getVirtualTime() 
            - getVirtualTimeBeganReturning();
    }
              
    public void markVirtualTimeBeganReturning()
    {
        virtualTimeBeganReturning = UIModel.getVirtualTime();
    }
    
    public GhostState getGhostState()
    {
        return this.ghostState;
    }
    
    public void setGhostState(GhostState ghostState)
    {
        //Const.logger.fine("ghostState: " + ghostState);
        this.ghostState = ghostState;
        
        if (ghostState == GhostState.RETURNING_GHOST_STATE)
        {
            markVirtualTimeBeganReturning();
        }
    }
}
 