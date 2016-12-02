/*
 * TetrisGameCanvas.java
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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import net.xylophones.micro.db.SimpleStore;
import net.xylophones.micro.game.mb.controller.GameController;
import net.xylophones.micro.game.mb.controller.GameListener;
import net.xylophones.micro.game.mb.mvc.board.BoardController;
import net.xylophones.micro.game.mb.mvc.board.BoardView;
import net.xylophones.micro.game.mb.mvc.status.StatusView;
import net.xylophones.micro.ui.canvas.LoopCanvas;
import net.xylophones.micro.ui.vibrator.Vibrator;

/**
 *
 * @author william@xylophones.net
 */
public class TetrisGameCanvas extends LoopCanvas implements GameListener {

    private BoardView boardView;

    private StatusView statusView;

    private String tileImage = "/tiles.png";

    private Image tiles;
    
    private GameController gameController;

    private boolean downPressed = false;
        
    private boolean leftPressed = false;
    
    private boolean rightPressed = false;
    
    private ScreenChooser screenChooser = null;

    private int lineVibrateDuration = 50;
    
    private int gameOverVibrateDuration = 100;
    
    private int tickCounter = 0;
    
    private int userInputWait = 1;
    
    /** 
     * Creates a new instance of TetrisGameCanvas 
     */
    public TetrisGameCanvas (ScreenChooser screenChooser, SimpleStore dataStore) {
        setFullScreenMode(true);
        
        this.screenChooser = screenChooser;
        
        try {
            tiles = Image.createImage(tileImage);
        } catch (Exception e) {
            System.err.println("Error creating image: " + tileImage);
        }
        
        gameController = new GameController(dataStore);
        gameController.addListener(this);
        
        boardView  = new BoardView(gameController.getBoardModel(), tiles);
        
        statusView = new StatusView(gameController.getStatusModel(), tiles);
        statusView.setPosition(boardView.getWidth(), 0);
    }
    
    /**
     * Paint the game to the screen
     */
    public void paint(Graphics g) {
        g.setColor(0, 0, 0); // black
        g.fillRect(0, 0, getWidth(), getHeight());

        //boardView.refresh();
        boardView.paint(g);

        statusView.paint(g);
    }
    
    /**
     * LoopCanvas implementation method
     * 
     * Accept user input & increment the game by one tick
     */
    public void tick() {
        tickCounter++;
        if (tickCounter % (userInputWait + 1) == 0) {
            userInput();
        }
        updateGame();
    }
    
    /**
     * Read user input and delegate it to the boardModel
     */
    private void userInput() {
        if ( leftPressed ) { 
            gameController.getBoardController().command(BoardController.COMMAND_LEFT);
        } else if ( rightPressed ) {
            gameController.getBoardController().command(BoardController.COMMAND_RIGHT);
        }

        if ( downPressed ) {
            gameController.getBoardController().command(BoardController.COMMAND_DOWN);
        } 
    }
                
    public void keyPressed(int keyCode) {
        int action = getGameAction(keyCode);
        
        switch(action) {
            case Canvas.UP:
                gameController.getBoardController().command(BoardController.COMMAND_ROTATE);
                break;
            case Canvas.DOWN:
                downPressed = true;
                break;
            case Canvas.LEFT:
                leftPressed = true;
                break;
            case Canvas.RIGHT:
                rightPressed = true;
                break;
        }
    }

    public void keyReleased(int keyCode) {
        int action = getGameAction(keyCode);
        
        switch(action) {
            case Canvas.DOWN:
                downPressed = false;
                break;
            case Canvas.LEFT:
                leftPressed = false;
                break;
            case Canvas.RIGHT:
                rightPressed = false;
                break;
        }
    }

    private void updateGame() {
        gameController.tick();
    }

    public void newGame() {
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        
        gameController.reset();
    }
    
    private void gameOver() {
        try {
            screenChooser.displayScreen(ScreenChooser.SCREEN_GAME_OVER);
            Vibrator.vibrate(gameOverVibrateDuration);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void gameEvent(int event) {
        switch (event) {
            case GameListener.EVENT_GAME_OVER:
                gameOver();
                break;
                
            case GameListener.EVENT_LINE_CLEARED:
                Vibrator.vibrate(lineVibrateDuration);
                break;
        }
    }
    
    /**
     * Get a snapshot of the game
     *
     * @return 
     */
    public Image getSnapshot() {
        Image snapshot = Image.createImage(getWidth(), getHeight());
        Graphics g = snapshot.getGraphics();
        
        paint(g);
        
        return snapshot;
    }

}
