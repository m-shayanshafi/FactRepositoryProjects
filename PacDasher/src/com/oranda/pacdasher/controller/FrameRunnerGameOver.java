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

 

import java.awt.event.KeyEvent;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerGameOver extends FrameRunner
{

    public FrameRunnerGameOver() 
	{
	    //super();
		mazeRenderer = new MazeRendererGameOver(); 
	}
	
	protected void afterRenderChange()
	{
		//Const.logger.fine("FrameRunnerGameOver.runSpecific()"); 
        this.resourceMgr.setCurrentClip(null, false);
        stats.delay(UIModelConsts.DELAY_GAME_OVER, true, false);
	}	
    
    protected void changeStateIfNecessary()
    {
        GUI.getInstance().clearLastKeyEvent();
		AppEventStateManager.getInstance().setEventRan(AppEvents.BETWEEN_GAMES); 
    }
    
    protected void beforeRenderChange() {}
    
}

