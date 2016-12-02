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

/*
 * State: contains superficial state of the game. Modified
 * by the UIModel and user events.
 */
public class StateHolder
{
    
    public enum State 
    { 
        START_STATE, 
        SPLASH_SCREEN_STATE,
        GAME_READY_STATE, 
        NORMAL_STATE, 
        CAPTURE_STATE,
        PAC_CAPTURED_STATE,
        READY_STATE,
        BEFORE_LEVEL_STATE,
        GAME_OVER_STATE,
        BETWEEN_GAMES_STATE
    };
    
    protected static State state = State.START_STATE;
        
    static State getState()
    {
        return state;
    }
    
    static void setState(State newState)
    {
        state = newState;
        Const.logger.fine("State set to " + state);
    }

}