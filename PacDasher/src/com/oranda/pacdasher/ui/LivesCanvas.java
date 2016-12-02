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
import com.oranda.pacdasher.ResourceMgr;
import com.oranda.util.ResourceUtils;
import com.oranda.util.Str;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Canvas showing lives, fruit, etc.
 */
public class LivesCanvas extends AbstractCanvas implements IPacListener
{
    private final static int FONT_HEIGHT = 12;    
    private final static String EXIT_STRING = "EXIT";
    
    // after this, this variable is depended of pacDasher.getNumLives()
    private int numLives = UIModelConsts.NUM_LIVES_INIT;

    private int width, height;
        
    private List<Image> fruitImages = new ArrayList<Image>();
    
        
    public LivesCanvas()
    {        
    }

       
    public void addFruitImage(Image img)
    {
        this.fruitImages.add(img);
        Const.logger.fine("" + this.fruitImages);
    }
    
    public void resetFruitImages()
    {
        fruitImages = new ArrayList<Image>();
    }
    
    protected void paintGraphics(Graphics drawGraphics)
    {
        drawGraphics.setColor(UIModelConsts.MAZE_BG_COLOR);
        drawGraphics.fillRect(0, 0, getWidth(), getHeight() + 10);
        
        /*
         * draw the lives
         */
        int pacImgWidth = (UIModelConsts.X_PAC_DASHER_SIZE*2)/3;
        int pacImgHeight = (UIModelConsts.Y_PAC_DASHER_SIZE*2)/3;
        for (int i=0; i<this.numLives-1; i++)
        {
            int x = (i + 2) * (pacImgWidth * 4)/3;
            Image pac = ResourceMgr.getInstance().pacLeft;
            drawGraphics.drawImage(pac, x, 5, pacImgWidth, pacImgHeight, null);  
        }   
        
        /*
         * draw the fruit
         */
        
        int fruitImgWidth = UIModelConsts.DEFAULT_SPRITE_SIZE;
        int fruitImgHeight = UIModelConsts.DEFAULT_SPRITE_SIZE;
        int count = 0;
        for (Image img : fruitImages)
        {                 
            count++;
            int x = getWidth() - (count * fruitImgWidth);
            drawGraphics.drawImage(img, x, 5, fruitImgWidth, fruitImgHeight, 
                    null);  
        }
    }
    
    public void pacScoreChanged(PacScoreEvent pse)
    {
    }
    
    public void pacDasherLivesChanged(PacEvent pe)
    {        
        numLives = ((PacDasher) pe.getSource()).getNumLives();
        //paintInfo();
    }
        
    public void pacDasherMoved(PacMoveEvent pme) {}
    public void pacDasherGainedPower() {}
    
}