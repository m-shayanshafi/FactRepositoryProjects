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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.uimodel.ghosts.*;
import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.visualobjects.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * Keeps track of overlapping collections of sprites.
 */
public class MazeAnimatedObjects
{        
    /**
     * ghostCollection is a subset of mobileList is a subset of animatedList.   
     */    
    private GhostCollection ghostCollection;
    private ArrayList<MobileVisualObject> mobileList;
    private ArrayList<AnimatedVisualObject> animatedList;
    private Score sampleScore = new Score();
    private Fruit sampleFruit = new Cherry();
    private VisualObjectFactory voFactory = new VisualObjectFactory();
    
    private static int energizerRenderCount; // = 0
    
	MazeAnimatedObjects()
    {
     	mobileList = new ArrayList<MobileVisualObject>();
     	animatedList = new ArrayList<AnimatedVisualObject>();
     	ghostCollection = GhostCollection.getInstance();
    }

    public void changeGhostStateMaybe()
    {
        ghostCollection.changeGhostStateMaybe();
    }
    
    public List getAnimatedList()
    {
        return animatedList;
    }    
    
    public List getMobileList()
    {
        return animatedList;
    }
    
    public GhostCollection getGhostCollection()
    {
        return ghostCollection;
    }
        
    List getGhostList()
    {
        return ghostCollection.getGhostList();
    }              
    
    GhostState getGeneralGhostState()
    {
        return ghostCollection.getGeneralGhostState();
    }
    
    void setGeneralGhostState(GhostState ghostState)
    {
        ghostCollection.setGeneralGhostState(ghostState);
    }
    
    void constructAnimateds(Set<XYCoarse> xycs, Class visualObjectClass)
	{
		for (XYCoarse xyc : xycs)
        {
             int xCoarse = xyc.getX();
             int yCoarse = xyc.getY();
             XYCManager xycm = XYCManager.getInstance();
             XYCoarse xyCoarse = xycm.createXYC(xCoarse, yCoarse);
             AnimatedVisualObject avo = (AnimatedVisualObject) 
                    voFactory.construct(visualObjectClass);
             Const.logger.fine("about to init " + visualObjectClass);
             avo.initialize(xyCoarse);
             if (avo.getTypeID() != sampleFruit.getTypeID())
             {
                 animatedList.add(avo);       
             }
        }
	}
    
    void move()
    {
        // the general ghost state might have timed out in some way
        changeGhostStateMaybe();
        for (MobileVisualObject mvo : mobileList)
        {
            mvo.moveAndSetDirtyArea();
        }
    }
    
    void initializeLists(PacDasher pacDasher)
    {        
        this.ghostCollection.clear();
        this.animatedList.add(pacDasher);
        for (AnimatedVisualObject avo : animatedList)
        {
            if (avo instanceof MobileVisualObject)
            {
             	this.mobileList.add((MobileVisualObject) avo);
             	if (avo instanceof Ghost)
                {
                    this.ghostCollection.add((Ghost) avo);
                }
            }
        }       
    }
    
        
    boolean checkMobileCollision(PacDasher pacDasher)
    {
        int xPacCoreRadius = UIModelConsts.X_PAC_DASHER_SIZE / 4;
        int yPacCoreRadius = UIModelConsts.Y_PAC_DASHER_SIZE / 4;
        int pacLeft = getXAdjusted(pacDasher.getXY().getX() - xPacCoreRadius);
        int pacRight = pacLeft + xPacCoreRadius * 2;
        int pacTop = getYAdjusted(pacDasher.getXY().getY() - yPacCoreRadius);
        int pacBottom = pacTop + xPacCoreRadius * 2;
        
        int xGCoreRadius = UIModelConsts.GHOST_DIAMETER/4;
        int yGCoreRadius = UIModelConsts.GHOST_DIAMETER/4;
        
        List<Ghost> ghostList = ghostCollection.getGhostList();
        for (Ghost ghost : ghostList)
        {
            int gLeft = getXAdjusted(ghost.getXY().getX() - xGCoreRadius);
            int gRight = gLeft + xGCoreRadius * 2;
            int gTop = getYAdjusted(ghost.getXY().getY() - yGCoreRadius);
            int gBottom = gTop + xGCoreRadius * 2;
                
            //Const.logger.fine("Collision? "
            //            + "gLeft,gRight: " + gLeft + "," + gRight + ","
            //            + "pacLeft,pacRight: " + pacLeft + "," + pacRight); 
            
            if (gLeft < pacRight && gRight > pacLeft
                && gBottom > pacTop && gTop < pacBottom)
            {
                //Const.logger.fine("Collision! "
                //        + "gLeft,gRight: " + gLeft + "," + gRight + ","
                //        + "pacLeft,pacRight: " + pacLeft + "," + pacRight); 
                fireMobileCollision(pacDasher, ghost);  
                return true;
            }
        }
        return false;
    }   
    
    private void fireMobileCollision(PacDasher pacDasher, Ghost ghost)
    {
        GhostState ghostState = ghost.getGhostState();
        //Const.logger.fine("collision, ghost state is " + ghostState);
        //Const.logger.fine("ghost posAndDirection " + ghost.getPosAndDirection());
        if (ghostState == GhostState.NORMAL_GHOST_STATE
                || ghostState ==  GhostState.SCATTER_GHOST_STATE)
        {
            UIModel.getInstance().pacDasherCaptured();
        }
        else if (ghostState == GhostState.FLIGHT_GHOST_STATE)
        {
            int creditGained = ghost.giveCredit(pacDasher);
            XY captureXY = (XY) (ghost.getXY().clone());
            // the Capture Frame Runner must remember to remove this object
            createScoreVOGhost(captureXY, creditGained);
            
            UIModel.getInstance().setCaptureEventRan();
            ghost.reactToCapture();
        }
        else // returning state
        {
            // do nothing
        }
    }
    
    void adjustModel(PacDasher pacDasher, 
                     Collection<XYCoarse> coordsAVOsToRemove,
                     boolean isMazeFinished)
    {
        removeTempVOsIfExpired();
        
        if (coordsAVOsToRemove != null)
        {
            for (XYCoarse xyc : coordsAVOsToRemove)
            {
                removeAnimated(xyc);
            }
        }
        
        // has PacDasher run into a Ghost
        if (!isMazeFinished)
        {
            // has PacDasher eaten all the dots
            checkMobileCollision(pacDasher);
        }
    }
    
    /**
     * Remove the non-mobile animated visual object, whose position is specified
     * by the given coarse xy coordinate.
     */
    void removeAnimated(XYCoarse xyCoarse)
    {       
        // a bit of a hack -- loop through the objects and figure out which one
        // to remove
        
        // don't use the new for loop -- need the iterator to do the remove()
        // otherwise a ConcurrentModificationException is thrown
        java.util.Iterator i = animatedList.iterator();
        while (i.hasNext())
        {
            AnimatedVisualObject avo = (AnimatedVisualObject) i.next();
            if (!(avo instanceof MobileVisualObject))
            {
                XYCoarse xyCoarseAnimated = avo.getXYCoarse();                
                if (xyCoarse.equals(xyCoarseAnimated))
                {
                    Const.logger.fine("removing xyCoarse: " + xyCoarse + "; " 
                          + "xyCoarseAnimated: " + xyCoarseAnimated);
                    i.remove();
                    break;
                }
            }
        }
        Const.logger.fine("animatedList: " + animatedList);
    }
     
    void addFruit(Fruit fruit)
    {
        if (fruit != null && !animatedList.contains(fruit))
        {
            animatedList.add(fruit);
        }
    }
    
    void removeFruit(Fruit fruit)
    {
        animatedList.remove(fruit);
    }
    
    void createScoreVOGhost(XY xy, int score)
    {
        Score scoreVisualObject = new Score();
        scoreVisualObject.initialize(xy, score, Color.CYAN);
        animatedList.add(scoreVisualObject);
    }
    
    void createScoreVOFruit(XY xy, int score)
    {
        Score scoreVisualObject = new Score();
        scoreVisualObject.initialize(xy, score, Color.YELLOW);
        animatedList.add(scoreVisualObject);
        Const.logger.fine("added score to animatedList: " + score);
    }
    
    void removeTempVisualObjects()
    {
        java.util.Iterator i = animatedList.iterator();
        while (i.hasNext())
        {
            AnimatedVisualObject avo = (AnimatedVisualObject) i.next();
            // so far the only temporary object is Score
            if (avo.getTypeID() == sampleScore.getTypeID())
            {
             	i.remove();
            }
        }    
    }
    
    void removeTempVOsIfExpired()
    {
        java.util.Iterator i = animatedList.iterator();
        while (i.hasNext())
        {
            AnimatedVisualObject avo = (AnimatedVisualObject) i.next();
            // so far the only temporary object is Score
            if (avo.getTypeID() == sampleScore.getTypeID() 
                    && ((Score) avo).isExpired())
            {
             	i.remove();
            }
        }   
    }
    
    void resetMobiles()
    {
        for (MobileVisualObject mvo : mobileList)
        {
            //Const.logger.fine("Resetting " + mvo);
            mvo.reset();
        }
    }
    
    
    public int getXAdjusted(int x)
    {
        if (x > 0) 
        {
            return x;
        }
        else
        {
            return x + UIModelConsts.WRAP_ADJUST_X;
        }
    }
        
    public int getYAdjusted(int y)
    {
        if (y > 0) 
        {
            return y;
        }
        else
        {
            return y + UIModelConsts.WRAP_ADJUST_Y;
        }
    }
        
    void renderAnimated(Graphics g)
    {
        energizerRenderCount++;   
        if (energizerRenderCount%16 < 1)
        {
            Energizer.toggleIsEnergizerDisplayed();
        }
        for (AnimatedVisualObject avo : animatedList)
        {           
            avo.render(g);
        }
    }
    
    public String toString()
    {
        return animatedList.toString();
    }
    
    public Object clone()
    {              
        MazeAnimatedObjects clone = new MazeAnimatedObjects();
        // the ghosts and pacDasher remain when a new game starts, 
        // but some eaten energizers may need to be restored later
        clone.ghostCollection = this.ghostCollection;
        clone.mobileList = this.mobileList;
        clone.animatedList = (ArrayList) this.animatedList.clone();
        return clone;
    }        
}