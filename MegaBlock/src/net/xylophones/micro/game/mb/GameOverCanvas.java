/**
 * GameOverCanvas.java
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

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

/**
 * A simple class to display a page saying congrats for winning the game
 *
 * @author william@xylophones.net
 */
public class GameOverCanvas extends Canvas {

    /**
     * title text
     */
    private String title = "Game over";

    /**
     * Left hand side of a bit of text telling the user to press a key to continue
     * in the middle of this and continueTextRight will be the name of the button 
     * they have to press to continue
     */
    private String continueTextLeft = "press ";
    
    /**
     * Right hand side of a bit of text telling the user to press a key to continue
     * in the middle of this and continueTextLeft will be the name of the button 
     * they have to press to continue
     */
    private String continueTextRight = " to continue";

    /**
     * Colour of the text
     */
    private int textColor = 0x0000FF;

    /**
     * Background colour of the screen
     */
    int backgroundColor = 0x000000;
    
    /**
     * The background image
     */
    private Image background = null;
    
    /**
     * Padding around the text
     */
    private int textPadding = 10;
    
    /**
     * Background colour of the text part 
     */
    private int textBackgroundColor = 0x000000;
    
    /**
     * Width of the border around text block
     */
    private int textBorder = 2;
    
    /**
     * Colour of the text
     */
    private int textBorderColor = 0xAA0000;
    
    /**
     * Holds the Font object returned from the Graphics object - needed
     * so that we can determine its height for spacing when painting on the 
     * screen
     */
    private Font font;
    
    /**
     * Used to switch between different screens - needed to display the 
     * main menu when the user continues from this screen
     */
    private ScreenChooser screenChooser;

    /**
     * Creates a new instance of WonCanvas
     */
    public GameOverCanvas(ScreenChooser screenChooser) {
        setFullScreenMode(true);
        this.screenChooser = screenChooser;
    }

    /**
     * <p>
     * Paint the congrats to the screen. 
     * </p>
     *
     * @param g
     */
    public void paint(Graphics g) {
                
        // needed for dimentions
        font = g.getFont();
        
        // background
        if (background != null) {
            g.drawImage(background, 0, 0, 0);
        } else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // text background
        int textBackgroundHeight = 2 * (textPadding + font.getHeight());
        int textBackgroundYPos = getHeight() / 2 - font.getHeight() - textPadding;
        
        g.setColor(textBackgroundColor);
        g.fillRect(0, textBackgroundYPos, getWidth(), textBackgroundHeight);
        
        g.setColor(textBorderColor);
        g.fillRect(0, textBackgroundYPos-textBorder, getWidth(), textBorder);
        g.fillRect(0, textBackgroundHeight+textBackgroundYPos, getWidth(), textBorder);

        // text
        g.setColor(textColor);

        int yPos = getHeight() / 2 - font.getHeight();
        int xPos = getWidth() / 2;
        g.drawString(title, xPos, yPos, Graphics.TOP|Graphics.HCENTER);

        String continueText = continueTextLeft
                              + getKeyName( getKeyCode(Canvas.FIRE) ).toUpperCase()
                              + continueTextRight;
        
        yPos = getHeight() / 2;
        xPos = getWidth() / 2;
        g.drawString(continueText, xPos, yPos, Graphics.TOP|Graphics.HCENTER);
    }
    
    /**
     * Listen for the continue keypress - the {@code Canvas.FIRE} keypress -
     * and when it is received, display the main menu screen
     *
     * @param keyCode
     */
    public void keyPressed(int keyCode) {
        if (getGameAction(keyCode) == Canvas.FIRE) {
            try {
                screenChooser.displayScreen(ScreenChooser.SCREEN_MAIN_MENU);
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Set the background image
     * 
     * @param background
     */
    public void setBackground(Image background) {
        this.background = background;
    }
}
