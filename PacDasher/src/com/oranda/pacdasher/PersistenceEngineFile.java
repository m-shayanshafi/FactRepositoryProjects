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

import com.oranda.pacdasher.uimodel.UIModel;

import java.io.*;

/**
 * Persists data specified in PersistenceEngineBase. 
 * On a read, the UIModel is notified.
 * Assumes single thread access, i.e. no file locking.
 * Persistence file will be in the directory in which pacdasher is invoked.
 * Failure (esp. to access file system) will be logged, but will not disturb
 * anything else.
 */
public class PersistenceEngineFile extends PersistenceEngineBase
{
    private static PersistenceEngineFile filePersistenceEngine;
    private final static String FILE_PATH = "pac_data";
    
    private UIModel uiModel;
    
    private BufferedWriter out;
    private BufferedReader in;
    
    /**
     * Assume for speed: Only one thread will ever access.
     */
    static PersistenceEngineFile getInstance()
    {
        if (PersistenceEngineFile.filePersistenceEngine == null) 
        {
            Const.logger.fine("calling new FilePersistenceEngine()");
            PersistenceEngineFile.filePersistenceEngine 
                    = new PersistenceEngineFile();
        }
        return PersistenceEngineFile.filePersistenceEngine;
    }
    
    /** 
     * This is done in a thread so that other things like the GUI can load.
     */
    public void read(final UIModel uiModelAutoVar)
    {
        Runnable runnableRead = new Runnable() {
			public void run() {
                try
                {
                    uiModel = uiModelAutoVar;
                    in = new BufferedReader(new FileReader(FILE_PATH));
                    readHighScore();
                    in.close();
                }
                catch (Exception e)
                {
                    outputError("Problem reading " + FILE_PATH, e);
                }
			}
		};
        Thread t = new Thread(runnableRead);
        t.start();
    }
   
    /**
     * This is not done in a thread, to ensure data is saved before exit.
     */
    public void write()
    {
        if (uiModel == null)
        {
            throw new RuntimeException("Trying to persist data "
                    + "but uiModel is null");
        }        
        try
        {
            this.out = new BufferedWriter(new FileWriter(FILE_PATH));
            int highScore = this.uiModel.getHighScore();
            writeHighScore(highScore);
            this.out.close();
        }
        catch (Exception e)
        {
            outputError("Problem writing " + FILE_PATH, e);
        }
    }
    
    // public only to fulfill interface
    public int readHighScore()
    {  
        
        int highScore = 0;
        try
        {
            String line = this.in.readLine();
            try
            {
                highScore = Integer.parseInt(line);
            }
            catch (NumberFormatException nfe)
            {
                outputError("Could not parse high score in: " + line, nfe);
            }
            this.uiModel.setHighScore(highScore);
        }
        catch (IOException ioe)
        {
            outputError("reading highScore", ioe);
        }
        return highScore;
    }
    
    // public only to fulfill interface
    public void writeHighScore(int highScore)
    {  
        try
        {
            String str = "" + highScore + "\n";
            out.write(str, 0, str.length());
        }
        catch (IOException ioe)
        {
            outputError("writing highScore", ioe);
        }
    }
    
    private void outputError(String str, Throwable t)
    {
        Const.logger.severe("FilePersistence exception: " + str
                + "\n" + t.getMessage());
    }
}
