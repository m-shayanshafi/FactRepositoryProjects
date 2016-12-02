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

import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

 

import java.awt.event.KeyEvent;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerBetweenGames extends FrameRunner
{
    public boolean readyForNextState = false;
    
    public FrameRunnerBetweenGames() 
	{
	    // Use the same renderer as GameOver for now
		mazeRenderer = new MazeRendererGameOver(); 
	}

	/*
	 * Override default behavior of responding to keys. This is
     * called only if e is not null.
	 *
	 * @param e assumed not null
	 */
    protected void processInput(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        String keyText = KeyEvent.getKeyText(keyCode);
		Const.logger.fine("" + keyText);
        if (keyCode == KeyEvent.VK_ESCAPE)
        {
            PacDasherMain.exit();
        }
        else if (keyCode == KeyEvent.VK_M)
        {
            minimizeApp();
            GUI.getInstance().clearLastKeyEvent();
        }
        else
        {
            UIModel.getInstance().resetVirtualTime();
            readyForNextState = true;
        }
    }
	
	protected void afterRenderChange()
	{
		//Const.logger.fine("FrameRunnerBetweenGames.runSpecific()");
        // This delay is short, meaning that processInput runs frequently
        // stats.delay(UIModelConsts.DELAY_BETWEEN_GAMES_INPUT, true);
	}	
    
    protected void changeStateIfNecessary()
    {
        if (readyForNextState)
        {
            readyForNextState = false;
            AppEventStateManager.getInstance().setEventRan(
                    AppEvents.STARTING_PACDASHER); 
        }
    }
    
    protected void beforeRenderChange() {}
    
}

