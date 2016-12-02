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

import java.awt.event.KeyEvent;

import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.Direction;
import com.oranda.pacdasher.uimodel.util.DirectionCode;

/*
 * Subclass of FrameRunner for a particular state.
 */
public class FrameRunnerSplashScreen extends FrameRunner
{
    private boolean isReady = false;
    private boolean isGameToBeStarted = true;
    
    public FrameRunnerSplashScreen() 
	{
	}

	protected void paint()
	{	    
	    GUI gui = GUI.getInstance();
        gui.getSplashCanvas().paint();
		gui.refreshCanvas();
	}
	
	/*
	 * @param e assumed not null
	 */
    protected void processInput(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE)
        {
            PacDasherMain.exit();
        } 
        else if (keyCode == KeyEvent.VK_M)
        {
            minimizeApp();
            GUI.getInstance().clearLastKeyEvent();
        }
        else if (keyCode == KeyEvent.VK_1)
        {
            isReady = true;
        }
    }
    
	protected void afterRenderChange()
	{
		if (this.isGameToBeStarted)
		{
		    /*
		     * The following call is not run in a separate thread. So if the 
		     * user presses a key while it is executing, the request will 
		     * simply be stored and run at processInput() time.
		     * To keep a responsive system therefore, startNewGame()
		     * should execute quickly.
		     */ 
			UIModel.getInstance().startNewGame();
			
			this.isGameToBeStarted = false;
		}
	}
	
    protected void changeStateIfNecessary()
    {
		//Const.logger.fine("FrameRunnerSplashScreen before event GAME_READY");
        if (isReady)
        {
            AppEventStateManager.getInstance().setEventRan(AppEvents.GAME_READY);
            isReady = false;
            String time = AppEventStateManager.getInstance().getTimeBetweenEvents(
                    AppEvents.ENDED_LOADING_XML, AppEvents.GAME_READY);
            Const.fileLogger.fine("Millis from XML load ended " +
                    "to game ready was " + time);
            GUI.getInstance().startGame();
            this.isGameToBeStarted = true; // reset for the next game
        }
    }
    
    protected void beforeRenderChange() 
    {       
    }
}

