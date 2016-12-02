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
import com.oranda.pacdasher.uimodel.event.*;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;

import java.util.*;

/**
 * Keeps track of overlapping collections of sprites.
 */
public class GhostCollection implements IPacListener
{    
    private static GhostCollection ghostCollection;
    
    private ArrayList<Ghost> ghostList;
    private GhostState generalGhostState = GhostState.NORMAL_GHOST_STATE;    
    private int virtualTimeEnteredFlight;
    
    private GhostCollection()
    {
        this.ghostList = new ArrayList(4);
    }

    /**
     * Assume for speed: Only one thread will ever access.
     */
    public static GhostCollection getInstance()
    {
        if (GhostCollection.ghostCollection == null) 
        {
            Const.logger.fine("calling new GhostCollection()");
            GhostCollection.ghostCollection = new GhostCollection();
        }
        return GhostCollection.ghostCollection;
    }
    
    
    public List<Ghost> getGhostList()
    {
        return ghostList;
    }              
    
    public void changeGhostStateMaybe()
    {
        if (generalGhostState == GhostState.FLIGHT_GHOST_STATE)
        {            
             // if flight time is up, restore to normal
             if (getVirtualTimeSinceFlight() > Ghost.TIME_FLIGHT_TOTAL)
             {
                 setStateNormalDueToTimeout();
             }
             // halve speed in flight state
             else if (UIModel.getVirtualTime()%2 == 1)
             {
                 return; // i.e. don't move
             }
        } 
        // every once in a while enter scatter state
        if (generalGhostState == GhostState.NORMAL_GHOST_STATE 
                && UIModel.getVirtualTime()%Ghost.PERIOD_SCATTER 
                <= Ghost.TIME_SCATTER)
        {
            setGeneralGhostState(GhostState.SCATTER_GHOST_STATE);
        }
        else if (generalGhostState == GhostState.SCATTER_GHOST_STATE 
                && UIModel.getVirtualTime()%Ghost.PERIOD_SCATTER 
                > Ghost.TIME_SCATTER)
        {
            setGeneralGhostState(GhostState.NORMAL_GHOST_STATE);
        }
    }
    
    public void setGeneralGhostState(GhostState ghostState)
    {
        Const.logger.fine("Setting the general ghost state");
        this.generalGhostState = ghostState;
        for (Ghost ghost: ghostList)
        {
            ghost.setGhostState(ghostState);
        }
        Const.logger.fine("General state was set to?" + this.generalGhostState);
        if (this.generalGhostState == GhostState.FLIGHT_GHOST_STATE)
        {
            Const.logger.fine("General state was set to flight state");
            UIModel.getInstance().setClipForGhostsFlight();
            markVirtualTimeEnteredFlight();
        }
    }
    
    public GhostState getGeneralGhostState()
    {        
        return this.generalGhostState;
    }
    
    /**
     * Ghosts always enter flight at the same time.
     * Hence methods dealing with flight are static.
     */
    private int getVirtualTimeEnteredFlight()
    {
        return virtualTimeEnteredFlight;
    }
    
    public int getVirtualTimeSinceFlight()
    {
        return UIModel.getVirtualTime() - getVirtualTimeEnteredFlight();
    }

    public void markVirtualTimeEnteredFlight()
    {
        Const.logger.fine("virtualTime: " + UIModel.getVirtualTime());
        virtualTimeEnteredFlight = UIModel.getVirtualTime();
    }
    
    /**
     * Set the state for all ghosts back to normal because of a
     * general timeout.
     */
    private void setStateNormalDueToTimeout()
    {
        Const.logger.fine("setting back to normal, "
                + "virtualTimeSinceFlight is " 
                + getVirtualTimeSinceFlight());
        UIModel.getInstance().setClipForGhostsNormal();
        setGeneralGhostState(GhostState.NORMAL_GHOST_STATE);
    }
    
    public void add(Ghost ghost)
    {
        ghostList.add(ghost);
    }
            
    public void clear() 
    {
    	ghostList.clear();
    }
    
    public void pacDasherGainedPower() 
    {
        setGeneralGhostState(GhostState.FLIGHT_GHOST_STATE);
    }
    
    public void pacDasherMoved(PacMoveEvent pme) 
    {
        for (Ghost ghost: ghostList)
        {
            ghost.pacDasherMoved(pme);
        }
    };
    
    public void pacDasherLivesChanged(PacEvent pe) {};
    public void pacScoreChanged(PacScoreEvent pse) {};

    
    public Object clone()
    {        
        GhostCollection clone = new GhostCollection();
        // like pacDasher, the ghosts remain when a new game starts 
        clone.ghostList = this.ghostList;
        return clone;
    }        
}
