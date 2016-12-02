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
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

 

import java.awt.Toolkit;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerReady extends FrameRunner
{

    public FrameRunnerReady() 
	{
	    //super();
		mazeRenderer = new MazeRendererReady(); 
	}
		
	protected void afterRenderChange()
	{
		Const.logger.fine("");
        stats.delay(UIModelConsts.DELAY_READY, true, false);
        stats.resetTime();
        GUI.getInstance().requestFocus();
	}
    
    protected void changeStateIfNecessary()
    {
       // boolean loop = true;
        //resourceMgr.setCurrentClip(resourceMgr.siren, loop);
        //Const.logger.fine("setting current clip to siren");
		AppEventStateManager.getInstance().setEventRan(AppEvents.PLAY);
    }
    
    protected void beforeRenderChange() 
    {
        GUI.getInstance().clearLastKeyEvent();
    }
    
}

