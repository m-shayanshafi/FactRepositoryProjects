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
 
package com.oranda.pacdasher.ui;


import com.oranda.pacdasher.uimodel.PacDasher;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.pacdasher.uimodel.event.IPacListener;
import com.oranda.pacdasher.uimodel.event.PacEvent;
import com.oranda.pacdasher.uimodel.event.PacScoreEvent;
import com.oranda.pacdasher.uimodel.event.PacMoveEvent;
 

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 * Canvas showing score, etc.
 */
public class MenuCanvas extends AbstractCanvas
{
    private final static String EXIT_STRING = "QUIT";
    private final static int FONT_HEIGHT = 10;  
    private int textOffset = 10;
    
    private int score;
    private int highScore;
    
    private Font font;
    private int fontWidth;
    private int fontHeight;

    private int width, height;  
    private int xOffset, yOffset;

    private int /*yStartGameBottomLeft,*/ yMinimizeBottomLeft, yExitBottomLeft;
    
    private BufferStrategy strategy;
    
    public MenuCanvas()
    {     
    }
    
    // main use is initialization
    public void initialize(BufferStrategy strategy, int xOffset, int yOffset)
    {
        super.initialize(strategy, xOffset, yOffset);
        this.textOffset = getWidth() / 20;
        this.fontHeight = UIModelConsts.FONT_SIZE_SCORE;
        
        //this.fontHeight = FONT_HEIGHT;
        // Monospaced maps to Courier                
        this.font = new Font("Monospaced", Font.BOLD, fontHeight);
    }
    
    /* 
     * System-triggered and may be called from update
     */    
    protected void paintGraphics(Graphics drawGraphics)
    { 
        //drawGraphics.fillRect(0, 0, getWidth(), getHeight());
        drawGraphics.setColor(Color.LIGHT_GRAY);
        drawGraphics.setFont(this.font);      
        
        FontMetrics fontMetrics = drawGraphics.getFontMetrics(font);
        //this.yStartGameBottomLeft = (getHeight() - this.fontHeight * 5) / 2;
        this.yMinimizeBottomLeft = (getHeight() - this.fontHeight * 4) / 2;
        this.yExitBottomLeft = (getHeight() + this.fontHeight) / 2;
        
        //drawGraphics.drawString(UIModelConsts.STR_START_GAME, 
        //        this.textOffset, this.yStartGameBottomLeft);
        drawGraphics.drawString(UIModelConsts.STR_MINIMIZE, 
                this.textOffset, this.yMinimizeBottomLeft);
        drawGraphics.drawString(UIModelConsts.STR_EXIT, 
                this.textOffset, this.yExitBottomLeft);    
    }  
    
    /*
    public boolean isClickOnStartGame(int x, int y)
    {
        //y -= yOffset;
        if (x >= this.textOffset
                && y >= this.yStartGameBottomLeft - this.fontHeight
                && y <= this.yStartGameBottomLeft)
        {
            return true;            
        }
        return false;
    }
    */
    public boolean isClickOnMinimize(int x, int y)
    {
        //y -= yOffset;
        if (x >= this.textOffset
                && y >= this.yMinimizeBottomLeft - this.fontHeight
                && y <= this.yMinimizeBottomLeft)
        {
            return true;            
        }
        return false;
    }
    
    public boolean isClickOnExit(int x, int y)
    {
        //y -= yOffset;
        if (x >= this.textOffset 
                && y >= this.yExitBottomLeft - this.fontHeight
                && y <= this.yExitBottomLeft)
        {
            return true;            
        }
        return false;
    }
}