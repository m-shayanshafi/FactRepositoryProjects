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

import com.oranda.pacdasher.uimodel.AnimatedVisualObject;
import com.oranda.pacdasher.uimodel.Maze;
import com.oranda.pacdasher.uimodel.PacDasher;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.VisualObject;
import com.oranda.pacdasher.uimodel.util.XY;
import com.oranda.pacdasher.uimodel.util.XYCoarse;
import com.oranda.pacdasher.uimodel.util.XYCManager;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

 

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;


/**
 * MazeRendererGameReady - draws the current maze.
 * Effectively combines input from UIModel and State to draw
 * to the GUI.
 */ 
public class MazeRendererGameReady extends MazeRenderer
{

    public MazeRendererGameReady()
    {
        super();
		Const.logger.fine("");
    }

    public void paintMazeUpdate(Graphics g, Maze maze)
    {
        if (g == null || maze == null)
        {
            throw new IllegalArgumentException();
        }
		// do nothing
    }
    
    public void paintMaze(Graphics g, Maze maze)
    {    
        if (g == null || maze == null)
        {
            throw new IllegalArgumentException();
        }
        Const.logger.fine("MazeRendererGameReady.paintMaze() ********");
                
        g.setColor(UIModelConsts.MAZE_BG_COLOR);
        g.fillRect(0, 0, Maze.DRAWABLE_WIDTH, Maze.DRAWABLE_HEIGHT);
        maze.renderObjectsAll(g);

        Const.logger.fine("Calling renderGamePrompt()");
        renderGamePrompt(g);
    }

 
    /* 
     * Draws "PLAYER ONE" and "READY!"
     */
    private void renderGamePrompt(Graphics g)
    {
        Color saveColor = g.getColor();
        Font saveFont = g.getFont();
        g.setFont(font);  
        XYCManager xycm = XYCManager.getInstance();
        
        int xTileSize = UIModelConsts.X_TILE_SIZE;
        int yTileSize = UIModelConsts.Y_TILE_SIZE;
        
        g.setColor(Color.CYAN);
        int xPos = 10 * xTileSize;
        int yPos = (25 * yTileSize)/2;
        g.drawString("PLAYER ONE", xPos, yPos);
        XYCoarse xycTopLeft = xycm.createXYC(xPos/xTileSize, yPos/yTileSize - 2);
        XYCoarse xycBottomRight 
            = xycm.createXYC(xPos/xTileSize + 10,  yPos/yTileSize);
        renderReady(g);
        
        g.setColor(saveColor);
        g.setFont(saveFont);
    }

        
    public static void main(String[] args)
    {

    }
    
}    