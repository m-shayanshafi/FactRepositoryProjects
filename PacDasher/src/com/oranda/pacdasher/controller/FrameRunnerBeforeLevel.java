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
 */
public class FrameRunnerBeforeLevel extends FrameRunner
{
    public FrameRunnerBeforeLevel() 
	{
	    //super();
		mazeRenderer = new MazeRenderer(); // do nothing 
	}
	
	protected void afterRenderChange()
	{		
        this.resourceMgr.setCurrentClip(null, false);
	    UIModel.getInstance().goToNewLevel();
        stats.delay(UIModelConsts.DELAY_BEFORE_LEVEL, true, false);
        GUI.getInstance().clearLastKeyEvent();
	}
    
    protected void changeStateIfNecessary()
    {
        showNextFruitImage();
		AppEventStateManager.getInstance().setEventRan(AppEvents.READY); 
    }
    
    protected void beforeRenderChange() {}

    // disable painting
    protected void paint()
	{
	}
    
}

