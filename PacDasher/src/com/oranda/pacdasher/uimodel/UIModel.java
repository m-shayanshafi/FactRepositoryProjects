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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.controller.AppEvents;
import com.oranda.pacdasher.controller.AppEventStateManager;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.ghosts.*;
import com.oranda.pacdasher.uimodel.persistence.IMazeAccessor;
import com.oranda.pacdasher.uimodel.persistence.IUIModelReader;
import com.oranda.pacdasher.uimodel.persistence.MazeAccessor;
import com.oranda.pacdasher.uimodel.persistence.ParseException;
import com.oranda.pacdasher.uimodel.persistence.UIModelReaderJDOM;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.pacdasher.uimodel.visualobjects.Fruit;
import com.oranda.util.Str;

import java.awt.Image;
import java.util.List;

/*
 * The UIModel contains the existence and positions
 * of all visual objects in the Maze: PacDasher, ghosts, 
 * dots, etc. 
 * Singleton.
 */ 
public class UIModel
{
    // the one true instance
    protected static UIModel uiModel;
    
    protected Maze maze;
    protected int level = 0;
    
    //protected int xOffset;
    //protected int yOffset;
    
    protected static int virtualTime; // starts at 0
    
    private PacDasher pacDasher;
    protected static Direction pacDirection;
    private IMazeAccessor mazeAccessor;
            
    private ResourceMgr rMgr = ResourceMgr.getInstance();
    
    private GameInfo gameInfo;
    
    /*
     * Constructor is private because it's a singleton
     */
    private UIModel()
    {   
		XYCManager.initialize(UIModelConsts.MAZE_WIDTH, 
                UIModelConsts.MAZE_HEIGHT);
        this.pacDasher = new PacDasher();
        
	    AppEventStateManager.getInstance().setEventRan(
				AppEvents.STARTED_LOADING_XML);
	    String time
     			= AppEventStateManager.getInstance().getTimeBetweenEvents(
     			        AppEvents.STARTING_PACDASHER, 
     			        AppEvents.STARTED_LOADING_XML);
	    Const.fileLogger.fine("Millis from "  
	            + AppEvents.STARTING_PACDASHER + " to " 
	            + AppEvents.STARTED_LOADING_XML + " was " + time);
	    Const.logger.config( "Started reading XML"); 
        IUIModelReader uiModelReader = new UIModelReaderJDOM();
        this.mazeAccessor = new MazeAccessor(uiModelReader);
        
        try
        {		        
            this.gameInfo = uiModelReader.parseForGameInfo();
        } 
        catch (ParseException pe)
        {
            Const.logger.severe("Could not get game info from XML " 
                    + Str.getStackTraceAsStr(pe));
        }        
    }
    
    public boolean isMazeFinished()
    {
        return maze.isMazeFinished();
    }
    
    public boolean isGameOver()
    {
        //Const.logger.fine("numLives: " + pacDasher.getNumLives());
        return pacDasher.getNumLives() <= 0;
    }
    
    public int getFrameInterval()
    {
        int framesPerSecond = this.gameInfo.getFramesPerSecond(level);
        return 1000 / framesPerSecond;
    }
    
	public boolean checkGameOver()
	{
		boolean isGameOver = isGameOver();
		
	    if (isGameOver)
		{
			Const.logger.fine("calling clearLastKeyEvent()");
		    GUI.getInstance().clearLastKeyEvent();
			//AppEventStateManager.getInstance().setRepaintNecessary(true);
			AppEventStateManager.getInstance().setEventRan(AppEvents.GAME_OVER);
		}
		
		return isGameOver;
	}
    
    public PacDasher getPacDasher()
    {
        return this.pacDasher;
    }
    
    public void startNewGame()
    {
        level = 0;
        this.mazeAccessor.resetAllNumMazeAccesses();
        goToNewLevel();
        pacDasher.resetForGame();
    }
    
    public boolean moreLevelsToGo()
    {
        return (level < gameInfo.getNumLevels());
    }
    
    public void goToNewLevel()
    {
		level++;
        this.maze = mazeAccessor.getMaze(level);
    }
    
    public void addListeners(Maze maze)
    {
        Const.logger.fine("making the ghosts listen to PacDasher");
        GhostCollection ghostCollection = maze.getGhostCollection();
        if (ghostCollection == null)
        {
            Const.logger.severe("could not add null listener: ghostCollection");
        }
        else
        {
            pacDasher.addPacListener(maze.getGhostCollection());
        }
    }
    
    public void move()
    {
        this.maze.move();
        virtualTime++;
		//checkGameOver();
    }
    
    /**
     * Assume for speed: Only one thread will ever access.
     */
    public static UIModel getInstance()
    {
        if (UIModel.uiModel == null) 
        {
            Const.logger.fine("calling new UIModel()");
            UIModel.uiModel = new UIModel();
        }
        return uiModel;
    }
    
        
    public Maze getMaze()
    {
        return maze;
    }
    
    public static int getVirtualTime()
    {
        return virtualTime;
    }
    
    public static void resetVirtualTime()
    {
        virtualTime = 0;
    }
    
    public int getHighScore()
    {
        return this.pacDasher.getHighScore();
    }
        
    public void setHighScore(int highScore)
    {
        this.pacDasher.setHighScore(highScore);
    }
    
    public void setDirectionCode(DirectionCode directionCode)
    {
        pacDasher.setDirectionCode(directionCode);
    }
    
    public void setClipForGhostsFlight()
    {
        ResourceMgr rMgr = ResourceMgr.getInstance();
        boolean loop = true;     
        Const.logger.fine("Setting clip to sirenLow");
        rMgr.setCurrentClip(rMgr.sirenLow, loop);
    }
    
    public void setClipForGhostsNormal()
    {
        boolean loop = true;     
        this.rMgr.setCurrentClip(rMgr.siren, loop);
    }
    
    // pacDasher captured a Ghost
    public void setCaptureEventRan()
    {
        AppEventStateManager.getInstance().setEventRan(AppEvents.CAPTURE);
    }
    
    public void removeTempVisualObjects()
    {
        if (maze != null)
        {
            maze.removeTempVisualObjects();
        }
    }
    
    /**
     * @return whether or not the game is over.
     */
    public boolean loseLife()
    {                       
        pacDasher.loseLife();
        maze.resetMobiles();
        return checkGameOver();
    }
    
    public Image getCurrentFruitImg()
    {
        Image img = null;
        Fruit fruit = maze.getCurrentFruit();
        if (fruit != null)
        {
            img = fruit.getImage();
        }
        return img;
    }
    
    public void setPacDyingFrame(int frameNum)
    {
        pacDasher.setPacDyingFrame(frameNum);
    }
    
    public int getNumPacDyingFrames()
    {
        return pacDasher.getNumPacDyingFrames();
    }
    
    public boolean getGhostCaptureState()
    {
        if (maze.getGeneralGhostState() == GhostState.FLIGHT_GHOST_STATE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void fruitEaten(XY eatXY, int score)
    {        
        boolean loop = false;
        rMgr.setCurrentClip(rMgr.eatingFruit, loop);
        this.maze.createScoreVOFruit(eatXY, score);
    }
    
    public void pacDasherCaptured()
    {   
        // should be a new framerunner state
        AppEventStateManager.getInstance().setEventRan(AppEvents.PAC_CAPTURED);
    }
    
    public void pacDasherAteEnergizer()
    {
        getPacDasher().gainedPower();
    }

} 