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

import java.awt.Toolkit;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerCapture extends FrameRunner
{

    public FrameRunnerCapture() 
	{
	    //super();
		mazeRenderer = new MazeRendererAnimated();
	}
	
	protected void afterRenderChange()
	{
        boolean loop = false;
        this.resourceMgr.setCurrentClip(this.resourceMgr.eatingGhost, loop);
        long delayMillis 
                = resourceMgr.getMillisecondLength(resourceMgr.eatingGhost);
		if (delayMillis == 0)
        {
            delayMillis = UIModelConsts.DELAY_CAPTURE;
        }
        stats.delay(delayMillis, true, false);
        stats.resetTime();
	}
    
    protected void changeStateIfNecessary()
    {   
        boolean loop = true;
        this.resourceMgr.setCurrentClip(this.resourceMgr.sirenLow, loop);        
        UIModel.getInstance().removeTempVisualObjects();
		AppEventStateManager.getInstance().setEventRan(AppEvents.PLAY); 
    }
    
    protected void beforeRenderChange() {}
    
}

