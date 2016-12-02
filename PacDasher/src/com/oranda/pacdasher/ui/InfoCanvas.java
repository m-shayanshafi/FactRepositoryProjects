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
public class InfoCanvas extends AbstractCanvas implements IPacListener
{
    private final static String EXIT_STRING = "QUIT";
    private final static int FONT_HEIGHT = 12;  
    
    private int xExitBottomLeft, yExitBottomLeft;
    
    private int score;
    private int highScore;
    
    private Font font;
    private int fontWidth;
    private int fontHeight;

    private int width, height;  
    private boolean infoChanged = true;
    
    public InfoCanvas()
    {     
    }
    
    // main use is initialization
    public void initialize(BufferStrategy strategy, int xOffset, int yOffset)
    {
        super.initialize(strategy, xOffset, yOffset);
        
        this.fontHeight = UIModelConsts.FONT_SIZE_SCORE;
        // Monospaced maps to Courier                
        this.font = new Font("Monospaced", Font.BOLD, fontHeight);
        FontMetrics fontMetrics = strategy.getDrawGraphics().getFontMetrics(font);
        int [] widths = fontMetrics.getWidths();
        this.fontWidth = widths[0];
    }
    
    /* 
     * System-triggered and may be called from update
     */    
    protected void paintGraphics(Graphics drawGraphics)
    { 
        drawGraphics.setColor(UIModelConsts.MAZE_BG_COLOR);        
        //Const.logger.fine("paintInfo " + getWidth() + "," + getHeight());
        drawGraphics.fillRect(0, 0, getWidth(), getHeight());
        drawGraphics.setColor(Color.LIGHT_GRAY);
        drawGraphics.setFont(this.font);      
        
        int headingX = UIModelConsts.X_TILE_SIZE * 5;
        drawGraphics.drawString("1UP", headingX, fontHeight - 5);
        int numDigits = (Integer.toString(score)).length();
        int scoreX = headingX + fontWidth * 4 - fontWidth * numDigits;
        drawGraphics.drawString("" + this.score, scoreX, fontHeight * 2 - 10);       
        
        //Const.logger.fine("score " + this.score);
        
        headingX = UIModelConsts.X_TILE_SIZE * 11;
                
        drawGraphics.drawString("HIGH SCORE", headingX, fontHeight - 5); 
        numDigits = (Integer.toString(this.highScore)).length();
        int highScoreX = headingX + fontWidth * 8 - fontWidth * numDigits;
        drawGraphics.drawString("" + this.highScore, highScoreX, fontHeight * 2 - 10);   
         
        this.xExitBottomLeft = getWidth() - 28;
        this.yExitBottomLeft = FONT_HEIGHT * 3 / 2;        
        drawGraphics.setFont(new Font("Helvetica", Font.BOLD, FONT_HEIGHT)); 
        drawGraphics.setColor(UIModelConsts.EXIT_COLOR);
        drawGraphics.drawString(EXIT_STRING, xExitBottomLeft, yExitBottomLeft);
    }
 
    public boolean isClickOnExit(int x, int y)
    {
        y -= yOffset;
        if (x >= this.xExitBottomLeft && y >= this.yExitBottomLeft - FONT_HEIGHT
            && y <= this.yExitBottomLeft)
        {
            return true;            
        }
        return false;
    }
        
    public void pacScoreChanged(PacScoreEvent pse)
    {
        this.score = pse.getScore();
        this.highScore = pse.getHighScore();
    }
        
    public void pacDasherMoved(PacMoveEvent pme) {}
    public void pacDasherGainedPower() {}
    public void pacDasherLivesChanged(PacEvent pe) {}
}