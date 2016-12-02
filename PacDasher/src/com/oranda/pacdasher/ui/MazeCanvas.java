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

import com.oranda.pacdasher.controller.Animation;
import com.oranda.pacdasher.controller.MazeRenderer;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
 
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;


/**
 * Main canvas for PacDasher - not currently used
 */
public class MazeCanvas extends AbstractCanvas
{        
    private int width, height;    
    private int xOffset, yOffset;
    
    private BufferStrategy strategy;
        
    private MazeRenderer mazeRenderer;
    
    public MazeCanvas()
    {           
    }

    public void paintGraphics(Graphics drawGraphics)
    {
        drawGraphics.fillRect(50, 50, 100, 100);
        drawGraphics.translate(this.xOffset, this.yOffset);
        this.mazeRenderer.paint(drawGraphics);
        drawGraphics.translate(-this.xOffset, -this.yOffset);
    }
    
    // adapter method -- may want to get refactor this away
    public void paint(MazeRenderer mazeRenderer)
    {
        this.mazeRenderer = mazeRenderer;
        paint();
    }
}
