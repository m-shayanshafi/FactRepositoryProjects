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

public class GameInfo
{
    private int initFramesPerSecond, framesLevelSpeedup;
    private int numLevels = 1; // minimum
    
	public int getInitFramesPerSecond()
    {
        return this.initFramesPerSecond;        
    }
    
    public void setInitFramesPerSecond(int initFramesPerSecond)
    {
        this.initFramesPerSecond = initFramesPerSecond;
    }
    
    public int getFramesLevelSpeedup()
    {
        return this.framesLevelSpeedup;
    }
    
    public void setFramesLevelSpeedup(int framesLevelSpeedup)
    {
        this.framesLevelSpeedup = framesLevelSpeedup;
    }   
    
    public int getNumLevels()
    {
        return this.numLevels;
    }
    
    public void setNumLevels(int numLevels)
    {
        this.numLevels = numLevels;
    }       
    
    /************************************************************************
     * Utility methods
     ************************************************************************/
    public int getFramesPerSecond(int level)
    {        
        int framesPerSecond = getInitFramesPerSecond()
        + (getFramesLevelSpeedup() * (level - 1)); 
        if (framesPerSecond == 0)
        {
            Const.logger.severe("Could not get framesPerSecond");
            framesPerSecond = 40;
        }
  
        return framesPerSecond;
    }

}