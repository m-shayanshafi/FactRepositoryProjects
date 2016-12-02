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

import com.oranda.pacdasher.controller.StateHolder.State;
import com.oranda.util.AppEventDetails;
import com.oranda.util.AppEventManager;
 

import java.util.HashMap;
import java.util.Map;

/*
 * AppEventStateManager: facade for state and event management.
 */
public class AppEventStateManager
{
	// the one true instance
	private static AppEventStateManager mgr;

    // map from events to states
	private final static Map<String, State> eventsToStateMap 
            = new HashMap<String, State>();
	private final static Map stateToFrameRunnerMap 
            = new HashMap<State, FrameRunner>();
	
	private boolean isRepaintNecessary = true;
	
	static
	{
	    eventsToStateMap.put(AppEvents.STARTING_PACDASHER, State.START_STATE);
	    //eventsToStateMap.put(AppEvents.STARTED_LOADING_XML, State.START_STATE);
	    //eventsToStateMap.put(AppEvents.ENDED_LOADING_XML, State.START_STATE);
	    eventsToStateMap.put(AppEvents.SPLASH_SCREEN, State.SPLASH_SCREEN_STATE);
	    eventsToStateMap.put(AppEvents.GAME_READY, State.GAME_READY_STATE);
		eventsToStateMap.put(AppEvents.PLAY, State.NORMAL_STATE);
		eventsToStateMap.put(AppEvents.BEFORE_LEVEL, State.BEFORE_LEVEL_STATE);
		eventsToStateMap.put(AppEvents.READY, State.READY_STATE);
		eventsToStateMap.put(AppEvents.CAPTURE, State.CAPTURE_STATE);
        eventsToStateMap.put(AppEvents.PAC_CAPTURED, State.PAC_CAPTURED_STATE);
		eventsToStateMap.put(AppEvents.GAME_OVER, State.GAME_OVER_STATE);
        eventsToStateMap.put(AppEvents.BETWEEN_GAMES, 
                State.BETWEEN_GAMES_STATE);
	    //eventsToStateMap.put(AppEvents.AFTER_1ST_500_FRAMES, 
		//    new Integer(State.START_STATE));
	}
		
	
	private AppEventStateManager()
	{
		AppEvents.initializeEvents();
		
        stateToFrameRunnerMap.put(State.START_STATE, 
    		    new FrameRunnerStart());
        stateToFrameRunnerMap.put(State.SPLASH_SCREEN_STATE, 
    		    new FrameRunnerSplashScreen());
        stateToFrameRunnerMap.put(State.GAME_READY_STATE, 
                new FrameRunnerGameReady());
        stateToFrameRunnerMap.put(State.NORMAL_STATE, 
                new FrameRunnerNormal());
        stateToFrameRunnerMap.put(State.BEFORE_LEVEL_STATE, 
                new FrameRunnerBeforeLevel());
        stateToFrameRunnerMap.put(State.READY_STATE, 
                new FrameRunnerReady());
        stateToFrameRunnerMap.put(State.GAME_OVER_STATE, 
                new FrameRunnerGameOver());
        stateToFrameRunnerMap.put(State.CAPTURE_STATE, 
                new FrameRunnerCapture());
        stateToFrameRunnerMap.put(State.PAC_CAPTURED_STATE, 
                new FrameRunnerPacCaptured());
        stateToFrameRunnerMap.put(State.BETWEEN_GAMES_STATE, 
                new FrameRunnerBetweenGames());
                
	}
	
	
	public synchronized static AppEventStateManager getInstance()
	{
		if (mgr == null)
		{
			mgr = new AppEventStateManager();
			return mgr;
		}
		else
		{
			return mgr;
		}
	}
	
    private static State getCurrentState()
	{
		return StateHolder.getState();
    }
	 
	private static void setCurrentState(State state)
	{
		if (state != getCurrentState())
		{
			getInstance().setRepaintNecessary(true);
		}
		StateHolder.setState(state);
    }

		
	private void setStateBasedOnEvent(String eventStr)
	{
		State newState = eventsToStateMap.get(eventStr);
		if (newState != null)
		{
			setCurrentState(newState);
		}
	}

    public FrameRunner getFrameRunner()
    {
		State currentState = getCurrentState();
		//Const.logger.fine("state=" 
		//    + currentState);
		FrameRunner frameRunner = (FrameRunner) stateToFrameRunnerMap.get(currentState);
		//Const.logger.fine(" " 
		//    + frameRunner.getClass().getName());
		return frameRunner;
	}
		
	/*
	 * AppEventManager wrappers
	 */
    public void addEvent(int order, String eventStr)
    {
		AppEventManager.getInstance().addEvent(order, eventStr);
    }  
	
	public AppEventDetails setEventRan(String eventStr)
	{
		setStateBasedOnEvent(eventStr);
		return AppEventManager.getInstance().setEventRan(eventStr);
    }
	
	public void setEventRan(String eventStr, long freeMemory)
	{
		setStateBasedOnEvent(eventStr);
		AppEventManager.getInstance().setEventRan(eventStr, freeMemory);
	}
	
	// return milliseconds
	public String getTimeBetweenEvents(String firstEventStr, 
	    String secondEventStr)
	{
		return AppEventManager.getInstance().getTimeBetweenEvents(firstEventStr,
		    secondEventStr);
	}	

	public boolean isRepaintNecessary()
	{
		return this.isRepaintNecessary;
	}

    public void setRepaintNecessary(boolean isRepaintNecessary)
	{
		this.isRepaintNecessary = isRepaintNecessary;
	}
}
