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

import com.oranda.pacdasher.ResourceMgr;
import com.oranda.pacdasher.controller.Animation;
import com.oranda.pacdasher.controller.MazeRenderer;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
 
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;


/**
 * Canvas for display of graphics between games.
 */
public class SplashCanvas extends AbstractCanvas
{        
    final static int GAP_UNDER_SPLASH_IMAGE = 0;
    
    private int width, height;    
    private int splashWidth, splashHeight;
    private int fontWidth;
    private Font font;
    private int fontHeight;
    private int xStartGameBottomLeft, xMinimizeBottomLeft, xExitBottomLeft;
    private int yStartGameBottomLeft, yMinimizeBottomLeft, yExitBottomLeft;
    
    private Image gSplashScreen;
    
    public SplashCanvas()
    {           
    }
    
    public void initialize(BufferStrategy strategy, int xOffset, int yOffset)
    {
        super.initialize(strategy, xOffset, yOffset);
        Const.logger.fine("initialize() " + xOffset + "," + yOffset);
        this.gSplashScreen = ResourceMgr.getInstance().gSplashScreen;
        
        fontHeight = UIModelConsts.FONT_SIZE_SPLASH;
        // Monospaced maps to Courier                
        this.font = new Font("Monospaced", Font.BOLD, fontHeight);
    }
        
    
    public void paintGraphics(Graphics drawGraphics)
    {       
        //Const.logger.fine("about to paint gSplashScreen");

        drawGraphics.drawImage(this.gSplashScreen, 0, 0, null);
        
        drawGraphics.setColor(Color.WHITE);
        drawGraphics.setFont(this.font);    
        
        FontMetrics fontMetrics = drawGraphics.getFontMetrics(font);
        this.xStartGameBottomLeft = (getWidth() 
                - fontMetrics.stringWidth(UIModelConsts.STR_START_GAME)) / 2;
        this.yStartGameBottomLeft = this.gSplashScreen.getHeight(null) 
        		+ GAP_UNDER_SPLASH_IMAGE + (int) (this.fontHeight * 1.5);
        this.xMinimizeBottomLeft = (getWidth() 
                - fontMetrics.stringWidth(UIModelConsts.STR_MINIMIZE)) / 2;
        this.yMinimizeBottomLeft = this.gSplashScreen.getHeight(null) 
        		+ GAP_UNDER_SPLASH_IMAGE + this.fontHeight * 3;
        this.xExitBottomLeft = (getWidth() 
                - fontMetrics.stringWidth(UIModelConsts.STR_EXIT)) / 2;
        this.yExitBottomLeft = this.gSplashScreen.getHeight(null) 
        		+ GAP_UNDER_SPLASH_IMAGE + (int) (this.fontHeight * 4.5);
        
        drawGraphics.drawString(UIModelConsts.STR_START_GAME, 
                this.xStartGameBottomLeft, this.yStartGameBottomLeft);
        drawGraphics.drawString(UIModelConsts.STR_MINIMIZE, 
                this.xMinimizeBottomLeft, this.yMinimizeBottomLeft);
        drawGraphics.drawString(UIModelConsts.STR_EXIT, 
                this.xExitBottomLeft, this.yExitBottomLeft);
    }
    
    public boolean isClickOnStartGame(int x, int y)
    {
        //y -= yOffset;
        if (x >= this.xStartGameBottomLeft 
                && y >= this.yStartGameBottomLeft - this.fontHeight
                && y <= this.yStartGameBottomLeft)
        {
            return true;            
        }
        return false;
    }
    
    public boolean isClickOnMinimize(int x, int y)
    {
        //y -= yOffset;
        if (x >= this.xMinimizeBottomLeft 
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
        if (x >= this.xExitBottomLeft 
                && y >= this.yExitBottomLeft - this.fontHeight
                && y <= this.yExitBottomLeft)
        {
            return true;            
        }
        return false;
    }
}
