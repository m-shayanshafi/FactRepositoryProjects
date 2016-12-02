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

import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

 
import java.awt.Toolkit;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerGameReady extends FrameRunner
{            
    public FrameRunnerGameReady() 
	{
	    //super();
		mazeRenderer = new MazeRendererGameReady(); 
	}
	
	protected void afterRenderChange()
	{
		// otherwise there is a pause on the first move
    	GUI.getInstance().exerciseKeyInput();
        
        long delayMillis = this.resourceMgr.getMillisecondLength(
                this.resourceMgr.openingSong);
        if (delayMillis == 0)
        {
            delayMillis = UIModelConsts.DELAY_GAME_READY;
        }
        stats.delay(delayMillis, true, false);
        stats.resetTime();
	}
    
    protected void changeStateIfNecessary()
    {
		AppEventStateManager.getInstance().setEventRan(AppEvents.PLAY);
        boolean loop = true;
        resourceMgr.setCurrentClip(resourceMgr.siren, loop);
    }
	
    protected void beforeRenderChange() 
    {
        boolean loop = false;
        resourceMgr.setCurrentClip(resourceMgr.openingSong, loop);
        GUI.getInstance().getLivesCanvas().resetFruitImages();
        showNextFruitImage();
        //resourceMgr.playCurrentClip();
    }   
}

