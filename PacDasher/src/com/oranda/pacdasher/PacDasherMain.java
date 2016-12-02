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

package com.oranda.pacdasher;

import com.oranda.pacdasher.ui.GUI;
import com.oranda.pacdasher.ui.InfoCanvas;
import com.oranda.pacdasher.ui.LivesCanvas;
import com.oranda.pacdasher.controller.Animation;
import com.oranda.pacdasher.controller.AppEvents;
import com.oranda.pacdasher.controller.AppEventStateManager;
import com.oranda.pacdasher.uimodel.PacDasher;
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;

import com.oranda.util.Str;

import java.io.*;
import javax.sound.sampled.*;
import java.util.Arrays;

/**
 * Entry point: Initialize the PacDasher game, and set off the Animation
 */
public class PacDasherMain
{

    public PacDasherMain(String [] args)
    {    
        try
        {
            Const.logger.info("Beginning PacDasherMain");

            if (args.length > 0)
            {
                Const.logger.info("Arg(s) are: " + Arrays.asList(args));
                GlobVars.relPathXML = args[0];
                Const.logger.fine("relPathXML: " + GlobVars.relPathXML);
            }

            AppEventStateManager.getInstance().setEventRan(
                    AppEvents.STARTING_PACDASHER);           

			runPacDasher();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            Const.logger.severe("Unexpected error only caught in PacDasherMain" 
                    + Str.getStackTraceAsStr(t));
        }
    }
       
    protected void runPacDasher()
    {
		UIModel uiModel = UIModel.getInstance(); // initializes
        GUI gui = GUI.getInstance(); // initializes
        PersistenceEngineFile.getInstance().read(uiModel);
        
        InfoCanvas infoCanvas = GUI.getInstance().getInfoCanvas();
        LivesCanvas livesCanvas = GUI.getInstance().getLivesCanvas();
        
        PacDasher pacDasher = UIModel.getInstance().getPacDasher();
        pacDasher.addPacListener(infoCanvas);
        pacDasher.addPacListener(livesCanvas);

        //Animation.repaint();
    	Const.logger.fine("calling exerciseKeyInput()");
        GUI.getInstance().exerciseKeyInput(); 
		
        new Animation();   
    }
	
	/*
	 * Ensure any cleanup is done at end
	 */
	public static void exit()
	{
        GUI.getInstance().dispose(); // avoids exception
        PersistenceEngineFile.getInstance().write();
	    System.exit(0);
	}
	
    public static void main(String [] args) 
    {
        new PacDasherMain(args);
    }

}
