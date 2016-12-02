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
 
package com.oranda.pacdasher.uimodel.util;

import java.awt.Color;

/**
 * UIModelConsts 
 */
 
public class UIModelConsts
{
    public static final int MAZE_WIDTH = 31; // including 2 offscreen
    public static final int MAZE_HEIGHT = 32;
    
    // canvas offsets
    public static final int FRAME_BAR_SIZE = 24;
    public static final int FRAME_EDGE_SIZE = 4;
   
    public static final int X_TILE_SIZE = 15; 
    public static final int Y_TILE_SIZE = 15;

    public static final int INFO_HEIGHT = (5 * UIModelConsts.Y_TILE_SIZE)/2; 
    public static final int LIVES_HEIGHT = (3 * UIModelConsts.Y_TILE_SIZE)/2;

    public static final int DEFAULT_SPRITE_SIZE = 28;
    
    public static final int WRAP_ADJUST_X = MAZE_WIDTH * X_TILE_SIZE;
    public static final int WRAP_ADJUST_Y = MAZE_HEIGHT * Y_TILE_SIZE;
    
    public static final int X_PAC_DASHER_SIZE = DEFAULT_SPRITE_SIZE;
    public static final int Y_PAC_DASHER_SIZE = DEFAULT_SPRITE_SIZE;
    
    public static final int GHOST_DIAMETER = DEFAULT_SPRITE_SIZE;
    public static final int FONT_SIZE_SCORE = 18;
    public static final int FONT_SIZE_SPLASH = 16;
    
    public static final Color MAZE_BG_COLOR = Color.BLACK;
    public static final Color EXIT_COLOR = Color.RED;
    
    public static final int NUM_LIVES_INIT = 3;
    
    public static final int HIGH_SCORE_INIT = 1000; // not used
    
    // pixels per move
    public static final int MOVE_SIZE = 2;
    
    public static final int DELAY_GAME_READY = 2000;
    public static final int DELAY_READY = 2000;
    public static final int DELAY_BEFORE_LEVEL = 2000;
    public static final int DELAY_CAPTURE = 500;
    public static final int DELAY_PAC_CAPTURE = 1000;
    public static final int DELAY_GAME_OVER = 750;
    public static final int DELAY_BETWEEN_GAMES_INPUT = 40;
    
    public static final String DIR_IMAGES = "images/";
    public static final String DIR_SOUNDS = "sounds/";
    public static final String DIR_XML = "";
    public static final String FILENAME_XML = "pacdasher.xml";
    public static final String FILENAME_XML_SCHEMA = "pacdasher.xsd";
    
    public static final  String STR_START_GAME = "START GAME (1)";
    public static final  String STR_MINIMIZE = "MINIMIZE (M)";
    public static final  String STR_EXIT = "EXIT (Esc)";
    
    // points gained for consuming each type of thing
    public static final int SCORE_ENERGIZER = 50;    
    public static final int SCORE_CHERRY = 100;    
    public static final int SCORE_STRAWBERRY = 300;    
    public static final int SCORE_PEACH = 500;    
    public static final int SCORE_APPLE = 700;    
    public static final int SCORE_KIWI = 1000;    
    
    // type IDs used for efficiency (e.g. as opposed to instanceof)
    public static final int TYPE_ID_DEFAULT = 0;
    public static final int TYPE_ID_BACKGROUND = 10;
    public static final int TYPE_ID_WALL = 20;
    public static final int TYPE_ID_BOX_WALL = 30;
    public static final int TYPE_ID_GATE = 40;
    public static final int TYPE_ID_DOT = 50;
    public static final int TYPE_ID_ENERGIZER = 60;
    public static final int TYPE_ID_GHOST = 70;
    public static final int TYPE_ID_PAC_DASHER = 80;
    public static final int TYPE_ID_SCORE = 90;
    public static final int TYPE_ID_FRUIT = 100;
    
    public enum GhostState 
    { 
        NORMAL_GHOST_STATE, 
        SCATTER_GHOST_STATE, 
        FLIGHT_GHOST_STATE, 
        RETURNING_GHOST_STATE
    };
}