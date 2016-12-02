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
 
package com.oranda.pacdasher.uimodel;

import com.oranda.pacdasher.uimodel.util.*;
import com.oranda.pacdasher.uimodel.util.UIModelConsts.GhostState;
import com.oranda.pacdasher.uimodel.visualobjects.*;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;

public class MazeBackgroundStatic
{
    private MazeData staticMazeData;    
    private Class backgroundObjectClass;
    
    private int xCoarseBoundary;
    private int yCoarseBoundary;
    
    private VisualObjectFactory voFactory = new VisualObjectFactory();
    private VisualObject sampleBackground = new Background();
    
    // local to getNeighbors but hate gc
    private ArrayList<DirectionCode> neighbors 
            = new ArrayList<DirectionCode>(2);
        
    private Color wallColor;        
    private Image staticBgImage;
    
    MazeBackgroundStatic(int xCoarseBoundary, int yCoarseBoundary)
    {        
        this.xCoarseBoundary = xCoarseBoundary;
        this.yCoarseBoundary = yCoarseBoundary;
        this.staticMazeData = new MazeData(xCoarseBoundary, yCoarseBoundary);
    }
    
    void setDefaultBackgroundClass(Class visualObjectClass)
    {        
        this.backgroundObjectClass = visualObjectClass;
		sampleBackground = voFactory.construct(backgroundObjectClass);
		for (int xCoarse=0; xCoarse<=xCoarseBoundary; xCoarse++)
        {
            for (int yCoarse=0; yCoarse<=yCoarseBoundary; yCoarse++)
            {
                staticMazeData.put(xCoarse, yCoarse, sampleBackground);
            }
        }       
    }
    
        
    void setBackground(Set<XYCoarse> xycs, VisualObject visualObject)
    {            
        setWallColorInMaze();	
        for (XYCoarse xyc : xycs)
        {
            staticMazeData.put(xyc.getX(), xyc.getY(), visualObject); 
        }
    }
    
    void setWallColor(Color color)
    {
        this.wallColor = color;
    }
    
    void setWallColorInMaze()
    {
	    Wall.setColor(this.wallColor);
	    BoxWall.setColor(this.wallColor);
    }   
    
    boolean isFree(XYCoarse xyCoarse)
    {
        VisualObject voStatic = (VisualObject) 
                staticMazeData.get(xyCoarse.getX(), xyCoarse.getY());
        if (voStatic == null ||
            voStatic.getTypeID() == UIModelConsts.TYPE_ID_BACKGROUND)
        {
            return true;
        }
        return false;
    }
    
    boolean isBlocked(int xCoarse, int yCoarse)
    {
        VisualObject voStatic = (VisualObject) staticMazeData.get(xCoarse, yCoarse);
        
        if (voStatic != null)
        {
            if (voStatic.getTypeID() == UIModelConsts.TYPE_ID_WALL 
                    || voStatic.getTypeID() == UIModelConsts.TYPE_ID_BOX_WALL)
            {
                return true;
            }
        }
        return false;
    }
    
    /*
     * @return a List of DirectionCode's
     */
    public List getNeighbors(XYCoarse xyCoarse, VisualObject visualObject)
    {        
        this.neighbors.clear(); // use member var to avoid creating objects
        int xCoarse = xyCoarse.getX();
        int yCoarse = xyCoarse.getY();
        
        if (visualObject.equals(staticMazeData.get(xCoarse-1, yCoarse)))
        {
            this.neighbors.add(DirectionCode.LEFT);
        }
        if (visualObject.equals(staticMazeData.get(xCoarse+1, yCoarse)))
        {
            this.neighbors.add(DirectionCode.RIGHT);
        }
        if (visualObject.equals(staticMazeData.get(xCoarse, yCoarse-1)))
        {
            this.neighbors.add(DirectionCode.UP);
        }
        if (visualObject.equals(staticMazeData.get(xCoarse, yCoarse+1)))
        {
            this.neighbors.add(DirectionCode.DOWN);
        } 
        return this.neighbors;
    }
    
    void createStaticBgGraphics()
    {
        // don't like to access GUI but...
        Frame pacFrame 
                = com.oranda.pacdasher.ui.GUI.getInstance().getPacFrame();
        this.staticBgImage = pacFrame.createImage(Maze.WIDTH, Maze.HEIGHT);
        Graphics staticBgGraphics = this.staticBgImage.getGraphics();
        
        StringBuffer sbDebug = new StringBuffer();
        for (int xCoarse=0; xCoarse<=xCoarseBoundary; xCoarse++)
        {
            for (int yCoarse=0; yCoarse<=yCoarseBoundary; yCoarse++)
            {
                XYCoarse xyCoarse 
                        = XYCManager.getInstance().createXYC(xCoarse, yCoarse);
                VisualObject vo = (VisualObject) staticMazeData.get(xCoarse, yCoarse);
                if (vo.getClass().getName().indexOf(".Background") == -1) 
                {
                    sbDebug.append("" + xCoarse + "," + yCoarse + ": " + 
                            vo.getClass().getName() + "\n");
                }
                if (vo == null)
                {
                    //Const.logger.fine("vo null");
                }
                else if (vo.isNeighborly())
                {                    
                    List neighbors = getNeighbors(xyCoarse, vo);
                    vo.render(staticBgGraphics, xyCoarse, neighbors);
                }
                else
                {
                    //Const.logger.fine( vo.getClass().getName());
                    vo.render(staticBgGraphics, xyCoarse);
                }
            }   
        }
        //Const.logger.fine(sbDebug.toString());
    }
    
    int renderDirtyArea(Graphics g, 
            int xycLeft, int xycTop, int xycRight, int xycBottom)
    {  
        XYCManager xycm = XYCManager.getInstance();
        int numObjectsDrawn = 0;
        for (int xCoarse = xycLeft; xCoarse <= xycRight; xCoarse++)
        {
            for (int yCoarse = xycTop; yCoarse <= xycBottom; yCoarse++)
            {            
                VisualObject vo = staticMazeData.get(xCoarse, yCoarse);
                if (vo == null || vo.getTypeID() == UIModelConsts.TYPE_ID_BACKGROUND)
                {
                    XYCoarse xyCoarse = xycm.createXYC(xCoarse, yCoarse);
                    sampleBackground.render(g, xyCoarse);
                    numObjectsDrawn++;
                }
            }
        }
        return numObjectsDrawn;
    }
    
        

    
       
    // non-intersection based
    public void renderObjects(Graphics g)
    {        
        g.drawImage(this.staticBgImage, 0, 0, null);
    }
    
    
    public Object clone()
    {
        MazeBackgroundStatic clone = new MazeBackgroundStatic(
                this.xCoarseBoundary, this.yCoarseBoundary);
        clone.backgroundObjectClass = this.backgroundObjectClass;
        clone.sampleBackground 
                = voFactory.construct(this.backgroundObjectClass); 

        clone.staticBgImage = this.staticBgImage;      
        clone.wallColor = this.wallColor;
        clone.setWallColorInMaze(); 
        clone.staticMazeData = (MazeData) this.staticMazeData.clone();
        
        if (clone.sampleBackground == null)
        {
            Const.logger.fine("backgroundObjectClass: " 
                    + this.backgroundObjectClass);       
            throw new RuntimeException(
                    "Could not clone maze -- it was not initialized properly");            
        }
        
        clone.neighbors = new ArrayList<DirectionCode>(2);
        return clone;
    }        
}