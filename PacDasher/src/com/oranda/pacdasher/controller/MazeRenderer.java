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

import com.oranda.pacdasher.ui.GUI;
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
 * MazeRenderer - draws the current maze.
 * Effectively combines input from UIModel and State to draw
 * to the GUI.
 * MazeRenderer is the superclass for classes which implement behavior based
 * on state (State pattern).
 */ 
public class MazeRenderer
{
    protected final int fontHeight = UIModelConsts.FONT_SIZE_SCORE + 4;
	// Monospaced maps to Courier
    protected final Font font = new Font("Monospaced", Font.BOLD, fontHeight);    

	
    public MazeRenderer()
    {
    }
    
	// template method
	public void paint(Graphics g)
	{        
        Maze maze = UIModel.getInstance().getMaze();
        if (AppEventStateManager.getInstance().isRepaintNecessary())
        {
            //Const.logger.fine("paintMaze()");
            paintMaze(g, maze);
            //AppEventStateManager.getInstance().setRepaintNecessary(false);
        }
        else
        {
            //Const.logger.fine("paintMazeUpdate()");
            paintMazeUpdate(g, maze);
        }		
	}
	
    public void paintMazeUpdate(Graphics g, Maze maze)
    {
        if (g == null || maze == null)
        {
            throw new IllegalArgumentException();
        }
        // default: do nothing
    }
    
    public void paintMaze(Graphics g, Maze maze)
    {   
        if (g == null || maze == null)
        {
            throw new IllegalArgumentException();
        }
        //default: do nothing
    }
    
	/* 
	 * Utility method.
     * Draws "READY!"
     */    
    protected void renderReady(Graphics g)
    {
        Color saveColor = g.getColor();
        Font saveFont = g.getFont();
        g.setFont(font);  
        XYCManager xycm = XYCManager.getInstance();
        int xTileSize = UIModelConsts.X_TILE_SIZE;
        int yTileSize = UIModelConsts.Y_TILE_SIZE;
                
        g.setColor(Color.YELLOW);
        int xPos = 12 * xTileSize;
        int yPos = (37 * yTileSize)/2;
        g.drawString("READY!", xPos, yPos);
        XYCoarse xycTopLeft = xycm.createXYC(xPos/xTileSize, yPos/yTileSize - 2);
        XYCoarse xycBottomRight = xycm.createXYC(xPos/xTileSize + 6, yPos/yTileSize);
        
        g.setColor(saveColor);
        g.setFont(saveFont);        
    }
	
    
    public static void main(String[] args)
    {
    /*    final Maze maze = MazeFactory.create();
        final MazeRenderer renderer = new MazeRenderer(maze);
        
        GraphicsEnvironment graphicsEnvironment
            = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice 
            = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration
            = graphicsDevice.getDefaultConfiguration();
    
        Frame f = new Frame(graphicsConfiguration);
        int w=480, h=480;
        f.setSize(w, h);
        f.setVisible(true);
        Canvas c = new Canvas() {
          
            public void paint (Graphics g)
            {
                
                renderer.drawMaze(g);
            }
        };
        c.setSize(w, h);
        c.repaint();
        f.add(c);
        */
    }
    
}    