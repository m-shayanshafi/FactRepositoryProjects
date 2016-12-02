/**
 * ScreenChooser.java
 * 
 * Copyright 2007 William Robertson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package net.xylophones.micro.game.mb;

/**
 * Simple class for choosing which screen to display. Rather than passing
 * around a midlet instance, pass this around instead. Safer.
 *
 * @author william@xylophones.net
 */
public class ScreenChooser {
    
    /**
     * The FroggerMIDLet instance
     */
    Midlet midlet;
    
    /**
     * The main menu
     */
    public static final int SCREEN_MAIN_MENU = 1;
    
    /**
     * The game board
     */
    public static final int SCREEN_BOARD = 2;
    
    /**
     * The pause screen (main menu with extra option)
     */
    public static final int SCREEN_PAUSE = 3;
    
    /**
     * The about screen
     */
    public static final int SCREEN_ABOUT = 4;   
    
    /**
     * The game over screen
     */
    public static final int SCREEN_GAME_OVER = 5;   

    /**
     * Creates a new instance of ScreenChooser 
     *
     * Takes TetrisMIDlet as a parameter - used for actually
     * displaying the appropriate Displayables
     *
     * @param midlet
     */
    public ScreenChooser (Midlet midlet) {
        this.midlet = midlet;
    }

    /**
     * <p>
     * Display a particular screen must be one of the SCREEN_XXX variables
     * </p>
     * 
     * <p>
     * Quietly fails if given an invalid screen
     * </p>
     *
     * @param screen  Identifier of screen to display
     */
    public void displayScreen(int screen) {
        switch (screen) {
            case SCREEN_MAIN_MENU:
                midlet.displayMainMenu();
                break;
                
            case SCREEN_BOARD:
                midlet.displayBoard();
                break;
                
            case SCREEN_PAUSE:
                midlet.displayPause();
                break;
                                
            case SCREEN_ABOUT:
                midlet.displayAbout();
                break;
                
            case SCREEN_GAME_OVER:
                midlet.displayGameOver();
                break;
        }
    }    
}
