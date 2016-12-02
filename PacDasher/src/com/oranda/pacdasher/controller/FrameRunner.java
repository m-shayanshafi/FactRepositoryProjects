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
import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.util.Direction;
import com.oranda.pacdasher.uimodel.util.DirectionCode;
import com.oranda.pacdasher.uimodel.UIModel;

import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Image;

/*
 * FrameRunner is responsible for running a single frame of the animation.
 * This involves updating the UI model, and rendering it.
 * This abstract class implements the State pattern:
 * state-dependent behavior is implemented by subclasses.
 *
 *                
 *                States: START_STATE        until maze created
 *                        GAME_READY_STATE    until after DELAY_GAME_READY
 *                        NORMAL_STATE        until capture of ghost
 *                        CAPTURE_STATE    until after DELAY_CAPTURE
 *                        NORMAL_STATE        until after capture of PacDasher
 *                        READY_STATE        until after DELAY_READY
 *                        NORMAL_STATE        until isMazeFinished is true
 *                        BEFORE_LEVEL    until after DELAY_BEFORE_LEVEL
 *                        READY_STATE        until isGameOver is true
 *                        GAME_OVER_STATE    until DELAY_GAME_OVER
 *                        BETWEEN_GAMES_STATE until key pressed
 *                        GAME_READY_STATE see above
 *
 */
public abstract class FrameRunner
{	
	protected PerformanceStats stats;
	protected long frameInterval;
	
	// MazeRenderer is also subclassed for state
	protected MazeRenderer mazeRenderer;
    protected ResourceMgr resourceMgr = ResourceMgr.getInstance();
    
    public FrameRunner() 
	{
		//mazeRenderer = new MazeRenderer(); // does nothing
	}
    
    /**
     * Template method.
     */
	public final void run(PerformanceStats stats, long frameInterval)
	{		
		this.stats = stats;
		this.frameInterval = frameInterval;
        
        // at this stage, changes have been made to the model but
        // not rendered to the screen
		//Const.logger.fine("before beforeRenderChange()");
        beforeRenderChange();
		//Const.logger.fine("before paint()");
		paint();
		//Const.logger.fine("before getAndProcessInput()");
        //resourceMgr.playCurrentClip();
        getAndProcessInput();
		//Const.logger.fine("before afterRenderChange()");
		afterRenderChange();
		//Const.logger.fine("before changeStateIfNecessary()");
        changeStateIfNecessary(); // change state AGAIN
	}
    
    protected abstract void beforeRenderChange();
	protected abstract void afterRenderChange();
	
    /**
     * For changing the state AGAIN.
     * May be overridden to check some data and change the state if necessary.
     * State is generally changed by firing an event (setEventRan).
     */
    protected void changeStateIfNecessary() {}
    
	public void repaint()
	{
		paint();
	}
	
	protected void paint()
	{
	    GUI gui = GUI.getInstance();
        gui.getInfoCanvas().paint();
        gui.getMazeCanvas().paint(mazeRenderer);
        gui.getLivesCanvas().paint();
        gui.paintMenuCanvas();
		gui.refreshCanvas();
	}
	
	protected void getAndProcessInput()
    {
    	KeyEvent e = GUI.getInstance().getLastKeyEvent();
    	if (e != null)
		{
			processInput(e);
		}
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
        DirectionCode dirCode = Direction.getDirectionCode(keyCode);
        UIModel.getInstance().setDirectionCode(dirCode);
    }

    // utility method
    protected void showNextFruitImage()
    {
        Image img = UIModel.getInstance().getCurrentFruitImg();
        if (img != null)
        {
            GUI.getInstance().addFruitImage(img);
        }
    }
    

    protected void minimizeApp()
    {
        GUI.getInstance().minimize();
    }
}

