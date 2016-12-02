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
 
package com.oranda.pacdasher.controller;

import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

/*
 * Subclass of FrameRunner for a particular state.
 * This plays a dying tune, and runs a little dying animation during it.
 */
public class FrameRunnerPacCaptured extends FrameRunner
{
    private int pacDyingFrameNum = 0;
    private static long delayMillis = -1;
    
    public FrameRunnerPacCaptured() 
	{
	    //super();
		mazeRenderer = new MazeRendererPacCaptured();
		// do not initialize delayMillis here because it depends on the 
		// UIModel being initialized
	}
	
    private void initializeDelayMillis() 
    {
        if (delayMillis == -1)
        {
            int numPacDyingFrames = UIModel.getInstance().getNumPacDyingFrames();
            long stateDelayMillis = this.resourceMgr.getMillisecondLength(
                    this.resourceMgr.pacmanDies);
            if (stateDelayMillis == 0)
            {
                stateDelayMillis = UIModelConsts.DELAY_PAC_CAPTURE;
            }        
            this.delayMillis = stateDelayMillis / numPacDyingFrames;
        }
    }
    
    protected void beforeRenderChange()
    {
        initializeDelayMillis();
        UIModel.getInstance().setPacDyingFrame(this.pacDyingFrameNum);
    }
    
	protected void afterRenderChange()
	{
        //Const.logger.fine("pacDyingFrameNum" + this.pacDyingFrameNum);
        if (this.pacDyingFrameNum == 0)
        {
            boolean loop = false;
            this.resourceMgr.setCurrentClip(this.resourceMgr.pacmanDies, loop);        
        }
        //Const.logger.fine("delay " + delayMillis);
        try { Thread.currentThread().sleep(delayMillis); }
        catch (InterruptedException ie) {};
        this.pacDyingFrameNum++;
	}
    
    protected void changeStateIfNecessary()
    {                   
        if (this.pacDyingFrameNum >= 6) // it is the end of the dying animation
        {
            this.pacDyingFrameNum = 0; // reset for next dying animation
            // loseLife() may call the GAME_OVER event as a side-effect
            boolean gameOver = UIModel.getInstance().loseLife(); 
            if (!gameOver) 
            {
                //Const.logger.fine("After dying, setting to ready");
                stats.resetTime();
                AppEventStateManager.getInstance().setEventRan(AppEvents.READY); 
            }
        }
    }    
}

