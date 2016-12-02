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

import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.event.IPacListener;
import com.oranda.pacdasher.uimodel.event.PacEvent;
import com.oranda.pacdasher.uimodel.event.PacScoreEvent;
import com.oranda.pacdasher.uimodel.event.PacMoveEvent;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.util.ResourceUtils;
import com.oranda.util.Str;

import java.awt.Graphics;
import java.awt.Image;

public class Ghost extends MobileVisualObject
{
    protected static final int TIME_FLIGHT_NORMAL = 300;
    protected static final int TIME_FLIGHT_TOTAL = 450;
    protected static final int TIME_SCATTER = 300;
    protected static final int PERIOD_SCATTER = 1500;    
    protected static final int TIME_RETURNING = 250;

    protected XYCoarse xyCoarse;
    protected XY pacDasherXy;
    protected DirectionCode pacDasherDirectionCode;
    protected GhostStrategy ghostStrategy;
    
    protected ResourceMgr resources = ResourceMgr.getInstance();

    protected Image iFlight =  resources.gFlight;
    protected Image iFlightWhite = resources.gFlightWhite;
    protected Image iReturningLeft = resources.gEyesLeft;
    protected Image iReturningRight = resources.gEyesRight;
    protected Image iRight, iLeft, iUp, iDown; // set in subclasses
    
    protected Image curImg;
        
    protected static final int scoreValue = 200;   
    
    public Ghost()
    {
    }

    public void initialize(XYCoarse xyCoarseInit)
    {        
        super.initialize(xyCoarseInit);
        
        // default until set
        this.pacDasherXy = new XY(0, 0); 
        this.pacDasherDirectionCode = DirectionCode.LEFT;
        // default
        this.ghostStrategy = new GhostStrategyBlinky(this);
    }
    
    public GhostState getGhostState()
    {
        return ghostStrategy.getGhostState();
    }
    
    public void setGhostState(GhostState ghostState)
    {
        ghostStrategy.setGhostState(ghostState);
    }
    
    public Image getAppropriateImage(DirectionCode directionCode)
    {
        int virtualTimeSinceFlight
                = GhostCollection.getInstance().getVirtualTimeSinceFlight();

        // it would be better to do this polymorphically
        if (ghostStrategy.getGhostState() == GhostState.FLIGHT_GHOST_STATE
            && virtualTimeSinceFlight > TIME_FLIGHT_NORMAL
            && virtualTimeSinceFlight%16 > 8)
        {
            return iFlightWhite;
        }
        else if (ghostStrategy.getGhostState() == GhostState.FLIGHT_GHOST_STATE)
        {
            return iFlight;
        }
        else if (ghostStrategy.getGhostState() 
                == GhostState.RETURNING_GHOST_STATE
                && virtualTimeSinceFlight%16 > 8)
        {
            return iReturningLeft;
        }
        else if (ghostStrategy.getGhostState() 
                == GhostState.RETURNING_GHOST_STATE)
        {
            return iReturningRight;
        }
        else if (directionCode == DirectionCode.DOWN)
        {
            return iDown;
        } 
        else if (directionCode == DirectionCode.UP)
        {
            return iUp;
        } 
        else if (directionCode == DirectionCode.LEFT)
        {
            return iLeft;
        } 
        else if (directionCode == DirectionCode.RIGHT)
        {
            return iRight;
        } 
        else
        {
            return null;
        }                
    }
    
    public PosAndDirection getPosAndDirection()
    {
        return this.posAndDirection;
    }
    
    public void setPosAndDirection(PosAndDirection posAndDirection)
    {
        this.posAndDirection = (PosAndDirection) posAndDirection.clone();
        //init();
    }
    
    public void reset()
    {
        super.reset();
        ghostStrategy.setGhostState(GhostState.NORMAL_GHOST_STATE);
        //Const.logger.fine("posAndDirection after " + posAndDirection);
        //ghostStrategy.setPosAndDirection(posAndDirection);    
    }
    
    public void reactToCapture()
    {
        super.reset(); // go back to initial position
        ghostStrategy.setGhostState(GhostState.RETURNING_GHOST_STATE);
    }
    
    public void render(Graphics g)
    {
        int xTopCorner = posAndDirection.getX() - UIModelConsts.X_TILE_SIZE;
        int yTopCorner = posAndDirection.getY() - UIModelConsts.Y_TILE_SIZE;
        //clip(g, xTopCorner, yTopCorner);
        
        Image img = getAppropriateImage(posAndDirection.getCurDirectionCode());
        if (img != null)
        {
            curImg = img;
        }        
        
        drawImage(g, curImg, xTopCorner, yTopCorner,
            UIModelConsts.GHOST_DIAMETER, UIModelConsts.GHOST_DIAMETER);
    }

    public void move()
    {
        ghostStrategy.move(this.posAndDirection, 
                this.pacDasherXy, 
                this.pacDasherDirectionCode);
    }
        
    public void pacDasherMoved(PacMoveEvent pme) 
    {
        XY xy = pme.getXy();
        this.pacDasherXy = xy;    
        this.pacDasherDirectionCode = pme.getDirectionCode(); 
    }
    
    public void pacDasherLivesChanged(PacEvent pe)
    {
        setPosAndDirection(initPosAndDirection);
    }
    
    public int getScoreValue()
    {
        return this.scoreValue;
    }
    
    /**
     * The 2nd ghost eaten during scatter mode is twice the
     * value of the 1st; the 3rd is twice the value of the 2nd;
     * etc.
     */
    public int giveCredit(PacDasher pacDasher)
    {
        pacDasher.incNumGhostsEaten();
        int numGhostsEaten = pacDasher.getNumGhostsEaten();
        int realScoreValue = (int) ((double) getScoreValue() *
                Math.pow(2, (double) (numGhostsEaten - 1)));
        pacDasher.addToScore(realScoreValue);
        return realScoreValue;
    }
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_GHOST;
    }
    
}