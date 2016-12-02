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

import com.oranda.pacdasher.uimodel.event.IPacListener;
import com.oranda.pacdasher.uimodel.event.PacEvent;
import com.oranda.pacdasher.uimodel.event.PacScoreEvent;
import com.oranda.pacdasher.uimodel.event.PacMoveEvent;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.pacdasher.ResourceMgr;
import com.oranda.util.ResourceUtils;
import com.oranda.util.Str;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Representing the PacDasher sprite 
 */
public class PacDasher extends MobileVisualObject implements IPlayer
{
    protected Image curImg;
    
    protected int score = 0; 
    protected int numGhostsEaten = 0; // for this flight ghost state
    private static int highScore;
    
    protected int numLives = UIModelConsts.NUM_LIVES_INIT;
    protected int pacDyingFrameNum = -1; // -1 indicates pacDasher is not dying
    
    ArrayList<IPacListener> iPacListeners = new ArrayList<IPacListener>(3);

    ResourceMgr resources = ResourceMgr.getInstance();
    
    Image [] pacDyingSequence = { resources.pacDying0, resources.pacDying1,
                                  resources.pacDying2, resources.pacDying3,
                                  resources.pacDying4, resources.pacDying5 };
                                  
	public void initialize(XYCoarse xyCoarse)
	{
		super.initialize(xyCoarse);
        Const.logger.fine("Called super.initialize for PacDasher");
        curImg = this.resources.pacLeft; 
        setDirectionCode(DirectionCode.LEFT);
        // announce initial position
        fireMoved(posAndDirection.getXY());	
	}
	
    public void resetForGame()
    {                
        setNumLives(UIModelConsts.NUM_LIVES_INIT);
        setScore(0);
        reset();   
        Const.logger.fine("numLives: " + numLives);
    }
    
    public void reset()
    {
        resetAfterCapture();        
    }
    
    public void resetAfterCapture()
    {
        super.reset(); // reset direction
        pacDyingFrameNum = -1; // not dying
    }
    
    public DirectionCode getDirectionCode()
    {
        return posAndDirection.getCurDirectionCode();
    }
    
    public void addPacListener(IPacListener iPacListener)
    {
        iPacListeners.add(iPacListener);
    }
    
    /*
    public void clip(Graphics g1, Graphics g2, XYCoarse xyCoarse)
    {
        //paint(g);
        clip(g1, xyCoarse.getX(), xyCoarse.getY());
        clip(g2, xyCoarse.getX(), xyCoarse.getY());
    }
    */
    /*
    public void clip(Graphics g, int x, int y)
    {
        g.setClip(null);
        g.clipRect(x - 5, y - 5, UIModelConsts.X_PAC_DASHER_SIZE + 10, 
            UIModelConsts.Y_PAC_DASHER_SIZE + 10);
    }
    */
    public void render(Graphics g, XYCoarse XYCoarse) {};
    
    public static int getDiameter() 
    {
        return UIModelConsts.X_PAC_DASHER_SIZE;
    }
                
    public int getScore()
    {
        return score;
    }
                
    public int getHighScore()
    {
        return highScore;
    }
    
    public void setHighScore(int highScore)
    {
        this.highScore = highScore;
    }
    
    public int getNumLives()
    {
        return numLives;
    }

    public void setPacDyingFrame(int frameNum)
    {
        //Const.logger.fine("curImg set to pacDying frameNum " + frameNum);
        pacDyingFrameNum = frameNum;
    }
    
    public int getNumPacDyingFrames()
    {
        return pacDyingSequence.length;
    }
    private boolean isPacDasherDying()
    {
        return pacDyingFrameNum > -1 
                && pacDyingFrameNum < pacDyingSequence.length;        
    }
    
    public void render(Graphics g)
    {
        DirectionCode directionCode = posAndDirection.getCurDirectionCode();
        int movesPerTile = UIModelConsts.X_TILE_SIZE/UIModelConsts.MOVE_SIZE;
        if (isPacDasherDying())
        {
            // then PacDasher is in the captured (dying) state
            curImg = pacDyingSequence[this.pacDyingFrameNum];
        }        
        else 
        {
            if (UIModel.getInstance().getVirtualTime()%movesPerTile 
                    < movesPerTile/2 || directionCode == DirectionCode.STATIONARY)
            {
                // mouth open            
                Image img = getAppropriateImage(directionCode);
                if (img != null)
                {
                    curImg = img;
                }                
            } 
            else
            { 
                // mouth not open
                curImg = this.resources.pacDasher; 
            }
        }

        int xTopCorner = posAndDirection.getX() - UIModelConsts.X_TILE_SIZE;
        int yTopCorner = posAndDirection.getY() - UIModelConsts.Y_TILE_SIZE;
        //clip(g, xTopCorner, yTopCorner);
         
        drawImage(g, curImg, xTopCorner + 1, yTopCorner + 1, 
            UIModelConsts.X_PAC_DASHER_SIZE, UIModelConsts.Y_PAC_DASHER_SIZE);
    }

    
    
    public Image getAppropriateImage(DirectionCode directionCode)
    {
        if (directionCode == DirectionCode.DOWN)
        {
            return resources.pacDown;
        } 
        else if (directionCode == DirectionCode.UP)
        {
            return resources.pacUp;
        } 
        else if (directionCode == DirectionCode.LEFT)
        {
            return resources.pacLeft;
        } 
        else if (directionCode == DirectionCode.RIGHT)
        {
            return resources.pacRight;
        } 
        else
        {
            Const.logger.severe("Code not recognized: " + directionCode);
            return null;
        }                
    }    
    
    public PosAndDirection getPosAndDirection()
    {
        return posAndDirection;
    }
    
    public void move()
    {
        XY xy = posAndDirection.move();
        if (getDirectionCode() != DirectionCode.STATIONARY)
        {
            fireMoved(xy);
        }
    }    
    
    public void addToScore(int scoreValue)
    {
        //Const.logger.fine("addToScore " + scoreValue);
        setScore(this.score + scoreValue);
        
    }
    
    public void setScore(int score)
    {
        this.score = score;
        if (score > highScore)
        {
            this.highScore = score;
        }
        fireScoreChanged();
    }
    
    public void setNumLives(int numLives)
    {
        this.numLives = numLives;
        fireLivesChanged();
        UIModel.getInstance().resetVirtualTime();
    }
    
    public void gainedPower()
    {
        resetNumGhostsEaten();
        fireGainedPower();
    }
    
    public void loseLife()
    {
        //Const.logger.fine("");
        setNumLives(this.numLives - 1);
        //Const.logger.fine("numLives " + numLives);
    }
    
    
    private void fireScoreChanged()
    {
        for (IPacListener listener : iPacListeners)  
        {
            //Const.logger.fine("" + score);
            listener.pacScoreChanged(new PacScoreEvent(this));
        }
    }        
    
    private void fireMoved(XY xy) 
    {
        // assume that listeners do not deregister themselves during
        // the execution of this method
        for (IPacListener listener : iPacListeners)   
        {
            listener.pacDasherMoved(PacMoveEvent.getInstance(xy, 
                posAndDirection.getCurDirectionCode(),this));
        }
    }
    
    private void fireGainedPower() 
    {

        // assume that listeners do not deregister themselves during
        // the execution of this method
        for (IPacListener listener : iPacListeners)   
        {
            listener.pacDasherGainedPower();
        }
    }
    
    private void fireLivesChanged() 
    {
        // assume that listeners do not deregister themselves during
        // the execution of this method
        for (IPacListener listener : iPacListeners)  
        {
            listener.pacDasherLivesChanged(new PacEvent(this));
        }
    }

    public void setDirectionCode(DirectionCode directionCode)
    {
    	if (posAndDirection != null)
    	{
            posAndDirection.setDesiredDirectionCode(directionCode);
        }
    }        
   
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_PAC_DASHER;
    }
    
    public int getNumGhostsEaten()
    {
        return this.numGhostsEaten;
    }
    
    public void incNumGhostsEaten()
    {
        this.numGhostsEaten++;
    }
    
    public void resetNumGhostsEaten()
    {
        this.numGhostsEaten = 0;
    }
    
}