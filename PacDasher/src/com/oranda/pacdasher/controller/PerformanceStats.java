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

/*
 * Performance statistics collected during animation and used to calculate
 * delays, etc.
 */
public class PerformanceStats
{
    static final int NUM_FRAMES_PER_SET = 500;
		
	long frameInterval;
			
    //long beginTime;  
    int numTooLongs;
    long bytesUsedForSet;
    long gcFreed;
    long idealTime;
    long freeMemory;
    long lastFreeMemory;
    long gcCount;
    int curFrameNum;
        
    PerformanceStats(long frameInterval)
    {
		this.frameInterval = frameInterval;
	}
		
    public void resetTime()
    {
        	idealTime = System.currentTimeMillis();
    } 
        
    public void initialize()
    {
        freeMemory = 0;
        lastFreeMemory = freeMemory;
        gcCount = 0;            
    }
        
    public void initializeForSet()
    {
        curFrameNum = 0;
        numTooLongs = 0;
        bytesUsedForSet = 0;
        gcFreed = 0;
    }
    
    public void delay(long milliseconds,  boolean force, boolean animationFrame)
    {              
        idealTime += milliseconds;
                    	
        long curTime = System.currentTimeMillis();
        long correctDelay = 0;
                    
        if (force == true)
        {
         	correctDelay = milliseconds;
        }
        else
        {
            // frame rate control!
            correctDelay = idealTime - curTime;
        }

        if (correctDelay > 0)
        {           	        	 	    
            if (correctDelay >= 500)
            {
                System.gc();
            }
            try
            {
                //Const.logger.fine("sleeping " + correctDelay);  
                Thread.currentThread().sleep(correctDelay);
            }
            catch (InterruptedException e)
            {
                Const.logger.severe("Interrupted while sleeping " + milliseconds);
            } 
        }
        else
        {
            // Give input thread a chance anytway
            Thread.currentThread().yield();
        }       
        if (animationFrame)
        {
            debugPerformance(-correctDelay);
        }
        if (correctDelay <= -60)
        {
            // unrecoverable overrun, e.g. from a full paint
            resetTime();
            correctDelay = milliseconds;
        }    
    }
    
    protected void debugPerformance(long overrun)
    {        	
        //if (Output.isDebugging())
    	
    	//long curTime = System.currentTimeMillis();
    	//long delay = curTime - prevTime;
    	    	  
        if (overrun > 0) 
        {
            numTooLongs++;
            if (overrun > frameInterval * 2)
            Const.logger.fine("****************** "
                    + " exceptional overrun! frame " 
                    + curFrameNum + " overrun " + overrun
                    + " idealTime " + idealTime);            
        }
        debugMemory();
    }  
    
    private void debugMemory()
    {
        freeMemory = Runtime.getRuntime().freeMemory();
        //Const.logger.fine("freeMemory: " + freeMemory);  
        if (lastFreeMemory > 0)
        {                  
            long memoryUsed = lastFreeMemory - freeMemory;
            if (memoryUsed < 0)
            {
                Const.logger.fine("memoryFreed, frame " 
                   	    + curFrameNum + " freed " + (-memoryUsed));
                gcCount++;
                gcFreed += (-memoryUsed);
            }
            else 
            {
                bytesUsedForSet = bytesUsedForSet + memoryUsed;
            }
        }        
        lastFreeMemory = freeMemory;        
    }
    
    public String getPerformanceSummary()
    {
        long totalMemory = Runtime.getRuntime().totalMemory();
        return  "\n\nPERFORMANCE SUMMARY "
			    + PerformanceStats.NUM_FRAMES_PER_SET + " frames \n\n" 
				+ "numTooLongs = " + numTooLongs + "\n"
			    + "totalMemory = " + totalMemory + "\n"
                + "freeMemory = " + freeMemory + "\n"
                + "bytes used per animation frame = "
                + bytesUsedForSet/PerformanceStats.NUM_FRAMES_PER_SET + "\n"
				+ "gcCount (animation) = " + gcCount + "\n"
                + "gcFreed (animation) = " + gcFreed + "\n\n";
    }
}
