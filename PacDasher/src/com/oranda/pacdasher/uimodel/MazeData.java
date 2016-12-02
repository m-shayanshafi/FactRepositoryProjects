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

/**
 * MazeData
 */
 
public class MazeData implements Cloneable
{    
    VisualObject[][] data;
    int width, height;
    
    public MazeData(int width, int height)
    {
        data = new VisualObject[width+1][height+1];
        this.width = width;
        this.height = height;
    }
    
    public void put(int xCoarse, int yCoarse, VisualObject visualObject)
    {
        data[xCoarse][yCoarse] = visualObject;
    }
    
    public VisualObject get(int xCoarse, int yCoarse)
    {
        if (xCoarse < 0 || yCoarse < 0 ||
            xCoarse > width || yCoarse > height)
        {
            return null;
        }
        return data[xCoarse][yCoarse];
    }
    
    public Object clone()
    {
        MazeData cloneMazeData = new MazeData(this.width, this.height);
        cloneMazeData.data = new VisualObject[width+1][height+1];;
        for (int xc = 0; xc <= width; xc++)
        {
            for (int yc = 0; yc <= height; yc++)
            {
                Object o = data[xc][yc];
                if (o != null)
                {
                    VisualObject vo = (VisualObject) o;
                    VisualObject cloneVO = (VisualObject) vo.clone();
                    cloneMazeData.data[xc][yc] = cloneVO;
                }
            }
        }
        //cloneMazeData.data = new VisualObject[width+1][height+1];
        return cloneMazeData;
    }
}