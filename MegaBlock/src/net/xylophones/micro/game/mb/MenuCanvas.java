/**
 * MenuCanvas.java
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
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import net.xylophones.micro.db.SimpleStore;
import net.xylophones.micro.ui.selectslider.Option;
import net.xylophones.micro.ui.selectslider.SelectListener;
import net.xylophones.micro.ui.selectslider.SelectSlider;

/**
 * Canvas class for displaying the main menu with slider control
 * 
 * @author william@xylophones.net
 */
public class MenuCanvas extends Canvas
                        implements SelectListener {
    
    /**
     * For providing a slider UI for the user to choose the options
     * in the menu
     */
    SelectSlider slider = null;

    /**
     * Height of the select slider
     */
    private int SLIDER_HEIGHT = 40;
    
    /**
     * Leftmost pixel of the screen
     */
    int screenMinX = 0;
    
    /**
     * Rightmost pixel of the screen
     */
    int screenMaxX;
    
    /**
     * Topmost pixel of the screen
     */
    int screenMinY = 0;
    
    /**
     * Bottom pixel of the screen
     */
    int screenMaxY;
    
    /**
     * page title
     */
    private String title   = "MegaBlock";
    
    /**
     * Contact string
     */
    private String contact = "www.xylophones.net";
    
    /**
     * String describing who created this great app
     */
    private String by      = "by William Robertson";
    
    /**
     * Option for the text slider corresponding to viewing about screen
     */
    private Option newGameOption = new Option(1, "new game");
    
    /**
     * Option for the text slider corresponding to viewing about screen
     */
    private Option continueGameOption = new Option(2, "continue game");

    /**
     * Option for the text slider corresponding to turning vibrating on
     */
    private Option vibrateOnOption = new Option(3, "vibrate is off");

    /**
     * Option for the text slider corresponding to turning vibrating off
     */
    private Option vibrateOffOption = new Option(4, "vibrate is on");

    /**
     * Option for the text slider corresponding to viewing about screen
     */
    private Option aboutOption = new Option(5, "about");

    /**
     * Option for the text slider corresponding to viewing about screen
     */
    private Option exitOption = new Option(6, "exit");

    /**
     * SimpleStore object to store saved dataStore
     */
    private SimpleStore dataStore = null;
    
    /**
     * Font used when painting text on screen
     */
    private Font font;

    /**
     * Creates a new instance of MenuCanvas
     *
     * @param store
     */
    public MenuCanvas(SimpleStore store) {
        dataStore = store;
        setFullScreenMode(true);
        
        init();
    }

    /**
     * Paint the menu 
     *
     * @param g
     */
    public void paint(Graphics g) {
        font = g.getFont();
        
        // clear the screen
        
        g.setColor(0, 0, 0); // black
        g.fillRect(screenMinX, screenMinY, screenMaxX-screenMinX, screenMaxY-screenMinY);
        g.setColor(255, 255, 255); // white

        int yPos;
        int xPos;

        // paint our text in the appropriate places
        
        yPos = ((screenMaxY - screenMinY) - SLIDER_HEIGHT) / 2 - font.getHeight()*3;
        xPos = (screenMaxX - screenMinX - font.stringWidth(title)) / 2;
        g.drawString(title, xPos, yPos, 0);
        
        yPos = ((screenMaxY - screenMinY) - SLIDER_HEIGHT) / 2 - font.getHeight()*2;
        xPos = (screenMaxX - screenMinX - font.stringWidth(by))/2;
        g.drawString(by, xPos, yPos, 0);
        
        yPos = ((screenMaxY - screenMinY) + SLIDER_HEIGHT) / 2 + font.getHeight()*1;
        xPos = (screenMaxX - screenMinX - font.stringWidth(contact))/2;
        g.drawString(contact, xPos, yPos, 0);
        
        // draw the slider

        int sliderTop = (getHeight() - SLIDER_HEIGHT) / 2;
        
    	g.clipRect(0, 0, getWidth(), getHeight());
     	g.clipRect(0, sliderTop, getWidth(), SLIDER_HEIGHT);
    	
        slider.paint(g);
    }

    /**
     * Listen for keypresses and move the move the slider appropriately
     *
     * @param keyCode
     */
    public void keyPressed(int keyCode) {
        if (slider!=null) {
            if ((getGameAction(keyCode) == LEFT)) { 
                slider.moveLeft();
                repaint();
            } else if ((getGameAction(keyCode) == RIGHT)) {
                slider.moveRight();
                repaint();
            } else if ((getGameAction(keyCode) == FIRE)) {
                slider.select();
                repaint();
            }
        }
    }

    /**
     * SelectListener interface method 
     *
     * Listen for selection on the slider & turn on/off sound/vibrations 
     * according to the user's selections
     * 
     * @param o
     */
    public void optionSelected(Option o) {
        if (o == vibrateOnOption) {
            dataStore.put("vibrate", "Y");
            slider.replaceOption(vibrateOnOption, vibrateOffOption);
        } else if (o == vibrateOffOption) {
            dataStore.put("vibrate", "N");
            slider.replaceOption(vibrateOffOption, vibrateOnOption);
        }
    }

    /**
     * Get the select slider
     *
     * @return 
     */
    public SelectSlider getSelectSlider() {
        return slider;
    }

    /**
     * Set up the Text slider
     */
    private void init() {
        screenMinX = 0;
        screenMaxX = getWidth();
        screenMinY = 0;
        screenMaxY = getHeight();
        
        slider = new SelectSlider();
        
        slider.addOption(newGameOption); 
        
        if ( dataStore != null && dataStore.get("vibrate")!=null 
               && dataStore.get("vibrate").equals("N") ) {
            slider.addOption(vibrateOnOption);
        } else {
            slider.addOption(vibrateOffOption);
        }

        slider.addOption(aboutOption);
        slider.addOption(exitOption);
                
        slider.addSelectListener(this);

        slider.setCurrentOption(newGameOption);
    }
    
    /**
     * Configure the meunu to either be a pause menu or a main menu
     *
     * The differnce is the addition of a continue game option if paused
     *
     * @todo see code comments
     *
     * @param pause
     */
    public void setPaused(boolean pause) {
        if (pause) {
            // should check to see if exists already
            slider.addOption(continueGameOption);
            slider.setCurrentOption(continueGameOption);
        } else {
            slider.removeOption(continueGameOption);
            slider.setCurrentOption(newGameOption);
        }
    }
}
