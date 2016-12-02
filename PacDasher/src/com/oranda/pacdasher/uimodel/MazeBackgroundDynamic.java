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
import com.oranda.pacdasher.uimodel.visualobjects.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;


/**
 * Anything that can change in the maze except the mobile visual objects.
 * Examples: Energizer's (because they blink), Dot's (because they can be 
 * eaten), and the Gate (because unlike the Wall, it can be obscured by Ghosts).
 */
public class MazeBackgroundDynamic
{
    private MazeData dynamicMazeData;
    
    private int xCoarseBoundary;
    private int yCoarseBoundary;
    
    private Class defaultForegroundClass;
    
    private int[][] clearArea = new int[2][2];
    private Set<XYCoarse> clearAreaExtra;
    
    private int numDotsEaten, numEnergizersEaten; 
    
    private Dot sampleDot = new Dot();
    private Energizer sampleEnergizer = new Energizer();
    private Fruit sampleFruit = new Cherry();
    private Score sampleScore = new Score();
    
    private VisualObjectFactory voFactory = new VisualObjectFactory();
    
    /**
     * Variable local to adjustModel is made a member to avoid gc.
     **/
    private HashSet<XYCoarse> coordsAVOsToRemove;
    
    MazeBackgroundDynamic(int xCoarseBoundary, int yCoarseBoundary)
    {        
        this.xCoarseBoundary = xCoarseBoundary;
        this.yCoarseBoundary = yCoarseBoundary;
        this.coordsAVOsToRemove = new HashSet<XYCoarse>();
        this.dynamicMazeData = new MazeData(xCoarseBoundary, yCoarseBoundary);
        this.numDotsEaten = 0;
        this.numEnergizersEaten = 0;
    }
    
    boolean isBlocked(MobileVisualObject mvo, int xCoarse, int yCoarse)
    {
        VisualObject voDynamic 
                = (VisualObject) dynamicMazeData.get(xCoarse, yCoarse);
        if (voDynamic != null)
        {
            if (voDynamic.getTypeID() == UIModelConsts.TYPE_ID_GATE 
                    && mvo.getTypeID() == UIModelConsts.TYPE_ID_PAC_DASHER)
            {
                return true;
            }
        }
        return false;
    }
        
    void setGateColor(Color color)
	{
        Gate.setColor(color);
	}	
    
    	
	void setClearAreaBounds(int left, int top, int right, int bottom)
	{
		clearArea[0][0] = left;
		clearArea[0][1] = top;
		clearArea[1][0] = right;
		clearArea[1][1] = bottom;
	}	
	
	void setClearAreaExtra(Set<XYCoarse> xycs)
	{
        clearAreaExtra = xycs;
        //clearAreaExtra = new int[0][0];
	}
    
    
    void setDefaultForegroundClass(Class visualObjectClass)
	{
		defaultForegroundClass = visualObjectClass;
	}
    
    int getNumEnergizersEaten()
    {
        return numEnergizersEaten;
    }
    
    void setEnergizersEaten(int numEnergizersEaten)
    {
        this.numEnergizersEaten = numEnergizersEaten;
    }
    
    int getNumDotsEaten()
    {
        return numDotsEaten;
    }
    
    void setNumDotsEaten(int numDotsEaten)
    {
        this.numDotsEaten = numDotsEaten;
    }
    
    int getNumDotsEnergizersEaten()
    {
        return getNumDotsEaten() + getNumEnergizersEaten();
    }
    
    int renderDirtyArea(
            Graphics g, int xycLeft, int xycTop, int xycRight, int xycBottom)
    {
        int numObjectsDrawn = 0;
        XYCManager xycm = XYCManager.getInstance();
        for (int xCoarse = xycLeft; xCoarse <= xycRight  + 1; xCoarse++)
        {
            for (int yCoarse = xycTop; yCoarse <= xycBottom + 1; yCoarse++)
            {            	
                VisualObject vo = dynamicMazeData.get(xCoarse, yCoarse);
                if (vo != null)
                {
                    XYCoarse xyCoarse = xycm.createXYC(xCoarse, yCoarse);
                    vo.render(g, xyCoarse);
                    numObjectsDrawn++;
                }
                
            }
        }
        return numObjectsDrawn;
    }
    
    void setBackground(Set<XYCoarse> xycs, VisualObject visualObject)
    {            
        for (XYCoarse xyc : xycs)
        {
            this.dynamicMazeData.put(xyc.getX(), xyc.getY(), visualObject); 
        }
    }
    
    void setBackgroundRemainder(MazeBackgroundStatic mazeBgStatic)
    {
        setBackgroundRemainder(mazeBgStatic, clearArea);                
        setBackground(clearAreaExtra, null); // overwrite dots
    }
    
    /*
     * Fill blank parts of maze with specified visualObject (dots)
     */
    private void setBackgroundRemainder(
            MazeBackgroundStatic mazeBgStatic, int[][] clearArea)
    {             
        VisualObject visualObject = voFactory.construct(defaultForegroundClass);
        
        for (int xCoarse=1; xCoarse<=xCoarseBoundary - 2; xCoarse++)
        {
            for (int yCoarse=1; yCoarse<=yCoarseBoundary; yCoarse++)
            {
                if (xCoarse >= clearArea[0][0] && xCoarse <= clearArea[1][0]
                    && yCoarse >= clearArea[0][1] && yCoarse <= clearArea[1][1])
                {
                    continue;
                } 
                
                XYCoarse xyCoarse0 
                     = XYCManager.getInstance().createXYC(xCoarse, yCoarse);
                XYCoarse xyCoarse1 
                     = XYCManager.getInstance().createXYC(xCoarse - 1, yCoarse);
                XYCoarse xyCoarse2 
                     = XYCManager.getInstance().createXYC(xCoarse, yCoarse - 1);
                XYCoarse xyCoarse3 
                     = XYCManager.getInstance().createXYC(xCoarse - 1, yCoarse - 1);

                if (isFree(xyCoarse0) 
                    && mazeBgStatic.isFree(xyCoarse0) 
                    && mazeBgStatic.isFree(xyCoarse1)
                    && mazeBgStatic.isFree(xyCoarse2) 
                    && mazeBgStatic.isFree(xyCoarse3))
                {
                    dynamicMazeData.put(xCoarse, yCoarse, visualObject);
                }
            }
        } 
        
    }    
    
    boolean isFree(XYCoarse xyCoarse)
    {

        VisualObject voDynamic = (VisualObject) 
            dynamicMazeData.get(xyCoarse.getX(), xyCoarse.getY());
        if (voDynamic == null || 
           (!(voDynamic.getTypeID() == UIModelConsts.TYPE_ID_GATE) 
            && !(voDynamic.getTypeID() == UIModelConsts.TYPE_ID_ENERGIZER) 
            && !(voDynamic.getTypeID() == UIModelConsts.TYPE_ID_FRUIT)))
        {
            return true;
        }
        return false;
    }
    
    boolean checkMazeFinished()
    {
        boolean isMazeFinished = true;
        for (int xCoarse=0; xCoarse<=xCoarseBoundary; xCoarse++)
        {
            for (int yCoarse=0; yCoarse<=yCoarseBoundary; yCoarse++)
            {
                VisualObject vo = (VisualObject) 
                        dynamicMazeData.get(xCoarse, yCoarse);
                if (vo != null && (vo.getTypeID() == UIModelConsts.TYPE_ID_DOT
                     || vo.getTypeID() == UIModelConsts.TYPE_ID_ENERGIZER))
                {
                    isMazeFinished = false;
                    return isMazeFinished;
                }
            }
        }        
        Const.logger.fine("maze finished!");
        return isMazeFinished;
    }
    
    /**
     * @return the coarse xy coordinates of all animated visual objects removed
     * or null if there were none
     **/
    HashSet<XYCoarse> adjustModel(PacDasher pacDasher)
    {
        coordsAVOsToRemove.clear(); // re-use object to avoid garbage collection
        
        /* 
         * Check to see if PacDasher has eaten anything
         * we can restrict search to the dirty area
         */         
        DirtyArea dirtyArea = pacDasher.getDirtyArea();

        if (dirtyArea == null || dirtyArea.getXYCLeftTop() == null)
        {
            return coordsAVOsToRemove;
        }

        int xPacLeft = dirtyArea.getXYCLeftTop().getX();
        int yPacTop = dirtyArea.getXYCLeftTop().getY();
        int xPacRight = dirtyArea.getXYCRightBottom().getX();
        int yPacBottom = dirtyArea.getXYCRightBottom().getY();

        int xLeft, yTop, xRight, yBottom, diameter;
                
        for (int xCoarse=xPacLeft; xCoarse<=xPacRight; xCoarse++)
        {
            for (int yCoarse=yPacTop; yCoarse<=yPacBottom; yCoarse++)
            {    
                XYCManager xycm = XYCManager.getInstance();
                XYCoarse xyCoarse = xycm.createXYC(xCoarse, yCoarse);
                VisualObject vo 
                        = (VisualObject) dynamicMazeData.get(xCoarse, yCoarse);    
                if (vo != null)
                {
                    diameter = vo.getDiameter();
                    xLeft = vo.getXTopLeft(xyCoarse, diameter); 
                    yTop = vo.getYTopLeft(xyCoarse, diameter);                
                    xRight = xLeft + diameter;
                    yBottom = yTop + diameter;
                
                    if (pacDasher.envelopes(xLeft, xRight, yTop, yBottom))
                    {
                        eatObject(pacDasher, vo, xCoarse, yCoarse);
                        if (vo.getTypeID() == UIModelConsts.TYPE_ID_ENERGIZER)
                        {  
                            Const.logger.fine("envelopes energizer");
                            this.numEnergizersEaten++;
                            coordsAVOsToRemove.add(xyCoarse);
                            UIModel.getInstance().pacDasherAteEnergizer();
                        }
                        else if (vo.getTypeID() == UIModelConsts.TYPE_ID_DOT)
                        {
                            this.numDotsEaten++;
                            //Const.logger.fine("numDots And Energizers Eaten: " 
                            //        + getNumDotsEnergizersEaten());
                        }
                        else if (vo.getTypeID() == UIModelConsts.TYPE_ID_FRUIT)
                        {    
                            Const.logger.fine("envelopes fruit");
                            coordsAVOsToRemove.add(xyCoarse);
                            XY eatXY = new XY(vo.getX(xyCoarse), 
                                    vo.getY(xyCoarse));
                            UIModel.getInstance().fruitEaten(
                                    eatXY, vo.getScoreValue());
                        }
                    }
                }
            }
        }
        if (coordsAVOsToRemove.isEmpty())
        {
            return null;
        }
        else
        {
            return (HashSet) coordsAVOsToRemove.clone();
        }
    }

    /**
     * Does not apply to Ghosts, etc.
     */
    void eatObject(
            PacDasher pacDasher, VisualObject vo, int xCoarse, int yCoarse)
    {
        vo.giveCredit(pacDasher);
        //Const.logger.fine("eatObject at " + xCoarse + "," + yCoarse);
        dynamicMazeData.put(xCoarse, yCoarse, null);
    } 
        
    void addFruit(Fruit fruit)
    {
        if (fruit == null)
        {
            return;
        }
        XYCoarse xyCoarse = fruit.getXYCoarse();
        if (dynamicMazeData.get(xyCoarse.getX(), xyCoarse.getY()) == null)
        {
            dynamicMazeData.put(xyCoarse.getX(), xyCoarse.getY(), fruit);
        }
    }
    
    void removeFruit(Fruit fruit)
    {
        if (fruit == null)
        {
            return;
        }
        XYCoarse xyCoarse = fruit.getXYCoarse();
        dynamicMazeData.put(xyCoarse.getX(), xyCoarse.getY(), null);
    }
    
    void renderObjects(Graphics g)
    {        
        // intersection-based like Dot
        for (int xCoarse=0; xCoarse<=xCoarseBoundary; xCoarse++)
        {
            for (int yCoarse=0; yCoarse<=yCoarseBoundary; yCoarse++)
            {
                XYCoarse xyCoarse 
                     = XYCManager.getInstance().createXYC(xCoarse, yCoarse);
                VisualObject vo = (VisualObject) 
                        dynamicMazeData.get(xCoarse, yCoarse);
                if (vo != null 
                        && vo.getTypeID() != sampleEnergizer.getTypeID() 
                        && vo.getTypeID() != sampleFruit.getTypeID())
                {
                    vo.render(g, xyCoarse);
                }
            }
        }        
    }    
    
    public Object clone()
    {        
        MazeBackgroundDynamic clone = new MazeBackgroundDynamic(
                this.xCoarseBoundary, this.yCoarseBoundary);
        clone.sampleDot = this.sampleDot;
        clone.sampleEnergizer = this.sampleEnergizer;
        clone.clearArea = (int[][]) this.clearArea.clone();
        if (this.clearAreaExtra != null)
        {
            clone.clearAreaExtra =  new HashSet<XYCoarse>(this.clearAreaExtra);
        }
        else
        {
            clone.clearAreaExtra = null;
        }                
        clone.dynamicMazeData = (MazeData) this.dynamicMazeData.clone();
        clone.defaultForegroundClass = this.defaultForegroundClass;
        return clone;
    }
}