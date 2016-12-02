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

import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.controller.AppEventStateManager;
import com.oranda.pacdasher.controller.FrameRunner;
import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.util.Str;

import java.awt.BufferCapabilities;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.Toolkit;

/*
 * Animation class contains main game loop: kicked off by PacDasherMain
 * Thread calls moveAndDraw repeatedly, with a delay, and modifies
 * the State.
 */
public class Animation 
{
    protected long curTime;

	protected static FrameRunner frameRunner;

    private int frameInterval;
    
    public Animation() 
    {            
        AnimationTask animationTask = new AnimationTask();
        Thread t = new Thread(animationTask);        
        //t.setPriority(Thread.MAX_PRIORITY - 1);
        t.start();     
        
        // input thread
        //Thread.currentThread().setPriority(Thread.MAX_PRIORITY);   
    }

	public static void repaint()
	{
		if (frameRunner != null)
		{
			frameRunner.repaint();
		}
	}
    
    class AnimationTask implements Runnable 
    {
        public void run() 
        {	
            try
			{
				PerformanceStats stats = new PerformanceStats(frameInterval);
                stats.initialize();
                                  
                while (true)
				{                
					runSetOfFrames(stats);
					debugPerformanceSummary(stats);
				}
			}
			catch (Throwable t)
			{                
                t.printStackTrace();
		        Const.logger.severe("Unexpected error only caught in Animation " 
                        + "thread" + com.oranda.util.Str.getStackTraceAsStr(t));
			}
			finally
		    {
			    PacDasherMain.exit();
		    }
        }
        
        private void runSetOfFrames(PerformanceStats stats)
        {
            stats.initializeForSet();
            
            while (stats.curFrameNum < PerformanceStats.NUM_FRAMES_PER_SET)
            {
                int frameInterval = UIModel.getInstance().getFrameInterval();
				if (!GUI.getInstance().isIconified())
			    {
					frameRunner
					    	= AppEventStateManager.getInstance().getFrameRunner();
				    frameRunner.run(stats, frameInterval);                                                       
				}    
				else
				{
				     stats.delay(frameInterval, false, true); 
				}
				
                stats.curFrameNum++;
            }	        	
        }
		
        protected void debugPerformanceSummary(PerformanceStats stats)
        {
			String performanceSummary = stats.getPerformanceSummary();
            Const.logger.fine(performanceSummary);
			Const.fileLogger.fine(performanceSummary);
    	}

	}
    	
	public static void logInitialPerfAnalysis()
	{
		Const.fileLogger.fine("\n\nINITIAL PERFORMANCE ANALYSIS\n\n");
        BufferCapabilities bufferCapabilities = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration().getBufferCapabilities();
        BufferCapabilities bufferCapabilitiesActive 
                = GUI.getInstance().getBufferCapabilities();
		boolean pageFlippingSupported = bufferCapabilities.isPageFlipping();
		Const.fileLogger.fine("Video card supports page flipping? "
		    + pageFlippingSupported + "\n");
        if (!pageFlippingSupported)
		{
			Const.logger.config("Video card does not support page flipping");
		}
		boolean isFullScreenRequired = bufferCapabilities.isFullScreenRequired();
		Const.fileLogger.fine("Full screen required for page flipping? "
		    + isFullScreenRequired + "\n");
        if (isFullScreenRequired)
		{
			Const.logger.config("Full screen required for pageFlipping");
		}
        boolean pageFlippingActive = bufferCapabilitiesActive.isPageFlipping();
        Const.fileLogger.fine("Page flipping active? "
                + pageFlippingActive + "\n");
        ImageCapabilities imageCapabilities = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration().getImageCapabilities();
        Const.fileLogger.fine("image Caps isAccelerated: " 
                + imageCapabilities.isAccelerated());
        Const.fileLogger.fine("image Caps isTrueVolatile: " 
                + imageCapabilities.isTrueVolatile());
		Const.fileLogger.fine(
		    "\nOS Arch: " +  System.getProperty("os.arch") + "\n" +
		    "OS Name: " +  System.getProperty("os.name") + "\n" +
			"OS Version: " + System.getProperty("os.version") + "\n" +
			"Java Version: " + System.getProperty("java.version") + "\n" +
			"Java Vendor: " + System.getProperty("java.vendor") + "\n");
	}
		
    /*
     * for testing
     */
    public static void main(String args[]) 
    {

    }

}
