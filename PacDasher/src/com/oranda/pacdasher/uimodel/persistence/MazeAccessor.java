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
 
package com.oranda.pacdasher.uimodel.persistence;

 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.oranda.pacdasher.PacDasherMain;
import com.oranda.pacdasher.controller.AppEventStateManager;
import com.oranda.pacdasher.controller.AppEvents;
import com.oranda.pacdasher.uimodel.Const;
import com.oranda.pacdasher.uimodel.GameInfo;
import com.oranda.pacdasher.uimodel.Maze;
import com.oranda.util.Str;

/**
 * Facade for accessing Maze objects. They are taken from the cache
 * or, if not there, created by reading the XML file.
 * Mazes will generally be added in numeric sequence, 
 * i.e. in order 1, 2, 3, etc..
 */
public class MazeAccessor implements IMazeAccessor
{
    private IUIModelReader uiModelReader;
    
    // Mazes may be repeated. E.g. the maze for level 3 might be
    // the same as for level 1.
    private Map<Integer, Maze> mazeCache = new HashMap<Integer, Maze>(); 

    public MazeAccessor(IUIModelReader uiModelReader)
    {
        this.uiModelReader = uiModelReader;
    }
    
    public Maze getMaze(int level)
    {  
        //return MazeFactoryXML.create(level);

        Maze maze = null;
        Maze mazeClone = null;
        
        if (level == 0)
        {
            Const.logger.severe("Tried to get Maze 0: invalid number");
        }
        
        maze = (Maze) this.mazeCache.get(level);
        if (maze == null)
        {
            readAndCacheMaze(level);
            maze = (Maze) this.mazeCache.get(level);
        }
        
        maze.incNumMazeAccesses();
        mazeClone = (Maze) maze.clone();
        Const.logger.fine("Cloned maze for level " + level);
        mazeClone.reset();

        return mazeClone;
    }
    
    public void resetAllNumMazeAccesses()
    {
        for (Maze maze : this.mazeCache.values())
        {
            maze.resetNumMazeAccesses();
        }
    }
    
    private void readAndCacheMaze(int level)
    {
        //Maze maze = this.mazeFactoryXML.create(level);
        Maze maze = readAndCreate(level);

        Const.logger.fine("Created maze for level " + level);
        
        // This maze may apply to other levels too, so cache it where 
        // appropriate
        Const.logger.fine("The maze is also used for levels " + maze.getLevels());
        for (Integer l : maze.getLevels())
        {
            if (this.mazeCache.get(l) != null)
            {
                Const.logger.severe("Tried to get maze for level " + l
                        + " but maze is already cached.");
            }
            else
            {
                Const.logger.fine("Caching maze for level " + l);
                this.mazeCache.put(l, maze);
            }
        }        
    }
    
    public Maze readAndCreate(int level)
    {
        /*
         * Read from XML to a MazeRaw object, then convert that to a real Maze
         */
        MazeRaw mazeRaw = null;
        try 
        {            
    		mazeRaw = this.uiModelReader.parseForMaze(level);
            
            if (level == 1)
            {
                Const.logger.config( "Finished reading XML for maze, level 1");
                AppEventStateManager.getInstance().setEventRan(
                        AppEvents.ENDED_LOADING_XML);
                String time 
                		= AppEventStateManager.getInstance().getTimeBetweenEvents(
                		        AppEvents.STARTED_LOADING_XML, 
                		        AppEvents.ENDED_LOADING_XML);
                Const.fileLogger.fine("Millis from " + 
                        AppEvents.STARTED_LOADING_XML + " to " 
                        + AppEvents.ENDED_LOADING_XML + " was " + time);
            }
        }
        catch (ParseException pe)
        {
            Const.logger.severe("Could not read maze: " 
                    + Str.getStackTraceAsStr(pe));
            PacDasherMain.exit();
        }
        return Maze.create(mazeRaw);
    }
}
