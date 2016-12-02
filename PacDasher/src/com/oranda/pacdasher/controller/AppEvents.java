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

 

import com.oranda.util.AppEventManager;

/*
 * Enumeration of application events in PacDasher.
 */
public class AppEvents
{
	public final static String STARTING_PACDASHER = "Starting PacDasher";
	public final static String SPLASH_SCREEN = "Showing Splash Screen";
	public final static String STARTED_LOADING_XML = "Started loading XML";
	public final static String ENDED_LOADING_XML = "Ended loading XML";
	public final static String PLAY = "Play";
	public final static String GAME_READY = "Game Ready";
	public final static String AFTER_1ST_500_FRAMES = "After 1st 500 frames";
	public final static String CAPTURE = "Capture";
	public final static String PAC_CAPTURED = "PacDasher captured"; 
    public final static String READY = "Ready";
    public final static String BEFORE_LEVEL = "Before level";
    public final static String GAME_OVER = "Game Over";
    public final static String BETWEEN_GAMES = "Between Games";
    
    public static void initializeEvents() 
    {
        AppEventManager appEventMgr = AppEventManager.getInstance();

	    appEventMgr.addEvent(0, STARTING_PACDASHER);
	    appEventMgr.addEvent(50, SPLASH_SCREEN);
	    appEventMgr.addEvent(100, STARTED_LOADING_XML);
	    appEventMgr.addEvent(200, ENDED_LOADING_XML);
	    appEventMgr.addEvent(300, GAME_READY);
	    appEventMgr.addEvent(400, PLAY);
	    appEventMgr.addEvent(500, AFTER_1ST_500_FRAMES);
		appEventMgr.addEvent(600, CAPTURE);
        appEventMgr.addEvent(650, PAC_CAPTURED);
		appEventMgr.addEvent(700, BEFORE_LEVEL);
		appEventMgr.addEvent(800, READY);
		appEventMgr.addEvent(2000, GAME_OVER);
		appEventMgr.addEvent(3000, BETWEEN_GAMES);

    }    
}	
