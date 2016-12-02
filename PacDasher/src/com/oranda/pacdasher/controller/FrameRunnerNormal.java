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


import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerNormal extends FrameRunner
{

    public FrameRunnerNormal() 
	{
	    //super();
		mazeRenderer = new MazeRendererAnimated(); 
	}
	
    protected void beforeRenderChange()
    {        
        boolean loop = true;
        boolean ghostCaptureState 
                = UIModel.getInstance().getGhostCaptureState();
        if (!ghostCaptureState)
        {
            resourceMgr.setCurrentClipIfFree(resourceMgr.siren, loop);
        }
        else
        {
            resourceMgr.setCurrentClipIfFree(resourceMgr.sirenLow, loop);
        }
    }
    
	
	protected void afterRenderChange()
	{
		 UIModel.getInstance().move();
	     stats.delay(this.frameInterval, false, true); 
	}
    
    protected void changeStateIfNecessary()
    {
        if (UIModel.getInstance().isMazeFinished())
        {
            if (UIModel.getInstance().moreLevelsToGo())
            {
                paint();
                AppEventStateManager.getInstance().setEventRan(AppEvents.BEFORE_LEVEL);
            }
            else
            {
                AppEventStateManager.getInstance().setEventRan(AppEvents.GAME_OVER);  
            }
        }
    }
    
    
}

