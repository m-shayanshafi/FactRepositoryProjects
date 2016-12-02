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

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerStart extends FrameRunner
{
    public FrameRunnerStart() 
	{
	    //super();
		mazeRenderer = new MazeRenderer(); // default does nothing
	}

	protected void paint()
	{
	    // do nothing 
	}
	
	protected void afterRenderChange()
	{
        GUI.getInstance().requestFocus();
	}
	
    protected void changeStateIfNecessary()
    {
        GUI.getInstance().clearScreen();
        GUI.getInstance().clearLastKeyEvent();
	    Animation.logInitialPerfAnalysis();
		AppEventStateManager.getInstance().setEventRan(
		        AppEvents.SPLASH_SCREEN);
    }
    
    protected void beforeRenderChange() 
    {       

    }
    
}

