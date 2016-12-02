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
 * MazeRendererPacCaptured - draws the current maze without ghosts.
 */
public class MazeRendererPacCaptured extends MazeRenderer
{
    
    public MazeRendererPacCaptured()
    {
        super();
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
        //Const.logger.fine("");
                
        g.setColor(UIModelConsts.MAZE_BG_COLOR);
        g.fillRect(0, 0, Maze.DRAWABLE_WIDTH, Maze.DRAWABLE_HEIGHT);
        g.setClip(0, 0, Maze.DRAWABLE_WIDTH, Maze.DRAWABLE_HEIGHT);
        maze.renderObjectsAll(g); 
        PacDasher pacDasher = UIModel.getInstance().getPacDasher();
        pacDasher.render(g);
    }
    
    public static void main(String[] args)
    {

    }
    
}    