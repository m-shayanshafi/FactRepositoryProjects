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
import com.oranda.pacdasher.uimodel.UIModel;
import com.oranda.pacdasher.uimodel.util.UIModelConsts;
import com.oranda.util.ResourceUtils;
import com.oranda.util.Str;

import java.awt.Image;
import java.io.*;
import javax.sound.sampled.*;

/*
 * Singleton.
 * Currently just for loading and accessing resources.
 */
public class ResourceMgr
{
    private static final String imagesDir = UIModelConsts.DIR_IMAGES;
    private static final String soundDir = UIModelConsts.DIR_SOUNDS;
    
    private static boolean isSoundActive = true;
    
    static
    {
        String versionStr = System.getProperty("java.specification.version");
        // not supporting sound in Java 1.4, so deactivate there
        try
        {
            float version = Float.parseFloat(versionStr);
            if (version < 1.5f)
            {
                isSoundActive = false;
            }
            Const.logger.fine("version " + version + "; isSoundActive " + isSoundActive);
        
        }
        catch (NumberFormatException nfe)
        {
            Const.logger.severe("Could not parse JVM version: " + versionStr);
            isSoundActive = false;            
        }
        
    }
    
    // initialize r before the addImageResource calls
    private ResourceUtils r = new ResourceUtils();    
    private AudioRunner audioRunner = new AudioRunner();
    private Thread audioRunnerThread = new Thread(audioRunner);
    
    // keep the addResource calls here, ensuring no resource is left undefined
    // It is ok for these to be referenced from other packages as, e.g.
    // Resource.gClydeRight
    public Image gSplashScreen = addImageResource("gSplashScreen.png");
    
    public Image gClydeRight = addImageResource("gClydeRight.gif");
    public Image gClydeLeft = addImageResource("gClydeLeft.gif");
    public Image gClydeUp = addImageResource("gClydeUp.gif");
    public Image gClydeDown = addImageResource("gClydeDown.gif");
    public Image gBlinkyRight = addImageResource("gBlinkyRight.gif");
    public Image gBlinkyLeft = addImageResource("gBlinkyLeft.gif");
    public Image gBlinkyUp = addImageResource("gBlinkyUp.gif");
    public Image gBlinkyDown = addImageResource("gBlinkyDown.gif");
    public Image gPinkyRight = addImageResource("gPinkyRight.gif");
    public Image gPinkyLeft = addImageResource("gPinkyLeft.gif");
    public Image gPinkyUp = addImageResource("gPinkyUp.gif");
    public Image gPinkyDown = addImageResource("gPinkyDown.gif");
    public Image gInkyRight = addImageResource("gInkyRight.gif");
    public Image gInkyLeft = addImageResource("gInkyLeft.gif");
    public Image gInkyUp = addImageResource("gInkyUp.gif");
    public Image gInkyDown = addImageResource("gInkyDown.gif");
    public Image gFlight = addImageResource("gFlight.gif");
    public Image gFlightWhite = addImageResource("gFlightWhite.gif");
    public Image gEyesLeft = addImageResource("gEyesLeft.gif");
    public Image gEyesRight = addImageResource("gEyesRight.gif");
    
    public Image pacDasher = addImageResource("pacDasher.gif");
    public Image pacRight = addImageResource("pacRight.gif");
    public Image pacLeft = addImageResource("pacLeft.gif");
    public Image pacUp = addImageResource("pacUp.gif");
    public Image pacDown = addImageResource("pacDown.gif");
    public Image pacDying0 = addImageResource("pacDying0.gif");
    public Image pacDying1 = addImageResource("pacDying1.gif");
    public Image pacDying2 = addImageResource("pacDying2.gif");
    public Image pacDying3 = addImageResource("pacDying3.gif");
    public Image pacDying4 = addImageResource("pacDying4.gif");
    public Image pacDying5 = addImageResource("pacDying5.gif");
    
    public Image fApple = addImageResource("fApple.gif");
    public Image fCherry = addImageResource("fCherry.gif");
    public Image fKiwi = addImageResource("fKiwi.gif");
    public Image fPeach = addImageResource("fPeach.gif");
    public Image fStrawberry = addImageResource("fStrawberry.gif");
    
    public Clip openingSong = addSoundResource("openingSong.wav");
    public Clip eatingShort = addSoundResource("eatingShort.wav");
    public Clip eatingGhost = addSoundResource("eatingGhost.wav");
    public Clip eatingFruit = addSoundResource("eatingFruit.wav");
    public Clip pacmanDies = addSoundResource("pacmanDies.wav");
    public Clip siren = addSoundResource("siren.wav");
    public Clip sirenLow = addSoundResource("sirenLow.wav");
        
    // Only one clip can play at a time
    private Clip currentClip;
    private Clip newClip; 
    private boolean newClipShouldLoop = false;
    
    
    // Singleton code follows
    private static ResourceMgr resourceMgr;    
    
    private ResourceMgr() {};
    
    public static ResourceMgr getInstance()
    {
        if (resourceMgr == null)
        {
            resourceMgr = new ResourceMgr();
        }

        return resourceMgr;
    }
    
    private Image addImageResource(String filename) 
    {
        String path = imagesDir + filename;
        Image img = null;
        try
        {
            img = r.loadImageFromJar(path);
        }
        catch (IOException ioe)
        {
            // fail fast: refuse to start without every resource
            Const.logger.severe("Could not load image at " + path
                    + ioe.getMessage() + Str.getStackTraceAsStr(ioe));
            System.exit(-1);
        }
        return img;
    }
    
    /**
     * @return an opened clip or null if there was a failure
     */
    private Clip addSoundResource(String filename)
    {
        if (!isSoundActive) { return null; }
        Clip clip = null;
        AudioInputStream soundStream = null;
        String path = soundDir + filename;
        try
        {
            //File f = new File(path);
            soundStream = //AudioSystem.getAudioInputStream(f);
                    r.getSoundInputStreamFromJar(path);
            AudioFormat format = soundStream.getFormat();
            Const.logger.fine("Audio format: " + format);
            //DataLine line = AudioSystem.getSourceDataLine(format);
        	DataLine.Info info = new DataLine.Info(Clip.class, format);
            boolean lineSupported = AudioSystem.isLineSupported(info);
			if (lineSupported)
			{
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(soundStream);
            }            
        }
        catch (IOException ioe)
        {
            // fail fast: refuse to start without every resource
            Const.logger.severe("Could not load sound at " + path
                    + ioe.getMessage() + Str.getStackTraceAsStr(ioe));
            System.exit(-1);
        }
        catch (LineUnavailableException lue)
        {
            Const.logger.severe("Too many audio lines? " + path
                    + lue.getMessage() + Str.getStackTraceAsStr(lue));
            // do not exit
        }
        catch (UnsupportedAudioFileException uafe)
        {
            Const.logger.severe("Could not load strange audio file " + path
                    + uafe.getMessage() + Str.getStackTraceAsStr(uafe));
            // do not exit
        }
        return clip;
        // return new SoundClip(clip, soundStream); // may have a null clip
    }
    
    public void setPaused()
    {
        //Const.logger.fine("setting to pause");
        this.audioRunner.setPaused();
    }
    
    public void setRestarted()
    {
        //Const.logger.fine("setting to restart");
        this.audioRunner.setRestarted();
    }
    
    public void setCurrentClip(Clip clip, boolean clipShouldLoop)
    {
        if (!isSoundActive) { return; }
        this.newClip = clip;
        this.newClipShouldLoop = clipShouldLoop;
        this.audioRunner.setClipChanged();
        if (!audioRunnerThread.isAlive())
        {
            audioRunnerThread.start();
        }
    }
    
    /**
     * Set the clip if none other is playing
     */
    public void setCurrentClipIfFree(Clip clip, boolean clipShouldLoop)
    {
        if (!isSoundActive) { return; }         
        if (this.newClip != clip && !this.audioRunner.currentClipActive()) 
        {
            setCurrentClip(clip, clipShouldLoop);
        }
    }
    
    public long getMillisecondLength(Clip clip)
    {
        if (clip == null)
        {
            return 0;
        }
        return clip.getMicrosecondLength() / 1000;
    }
    
    /*
     * Note: the sound stuff needs to be in a separate thread,
     * else it interferes with input.
     */
    class AudioRunner implements Runnable
    {      
        private boolean clipChanged, paused, restarted;
        private void setClipChanged() { clipChanged = true; }
        private void setPaused() { paused = true; }
        private void setRestarted() { restarted = true; }
        
        public void run()
        {
            while (true)
            {
                if (paused)
                {
                    paused = false;
                    if (currentClip != null)
                    {
                        currentClip.stop();
                    }
                }
                else if (restarted)
                {
                    restarted = false;
                    Const.logger.fine("Restarting... " + currentClip);
                    if (currentClip != null)
                    {
                        boolean shouldLoop = true;
                        playClip(currentClip, shouldLoop);
                    }
                }
                else if (clipChanged)
                {
                    Const.logger.fine("clip changed");   
                    clipChanged = false;
                    if (currentClip != null)
                    {
                        currentClip.stop();
                    }
                    playClip(newClip, newClipShouldLoop);
                    currentClip = newClip;
                }
                try 
                { 
                    Thread.currentThread().sleep(
                            UIModel.getInstance().getFrameInterval()); 
                } 
                catch (InterruptedException ie) {};
            }
        }
        
        private void playClip(Clip clip, boolean clipShouldLoop)
        {            
            if (clip != null)
            {
                if (newClipShouldLoop)
                {
                    Const.logger.fine("starting clip... loop");
                    newClip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                else
                {
                    Const.logger.fine("starting clip...");
                    // start() does not work after another clip in 5.0 >=B2
                    //newClip.setFramePosition(0);
                    //newClip.start();
                    RunSoundHack player = new RunSoundHack(newClip);
                	Thread t = new Thread(player);
                	t.start();
                }
            }
        }
     
        // Clip.loop() works where Clip.start() doesn't
    	class RunSoundHack implements Runnable
		{
			private Clip soundClip;
			
		    public RunSoundHack(Clip soundClip) 
		    {
	            this.soundClip = soundClip;	    	
		    }	
		    
		    public void run()
		    {
		    	soundClip.setFramePosition(0);
		    	long millis = soundClip.getMicrosecondLength() / 1000;
		    	millis -= 30; // make certain the 2nd loop doesn't start
		    	soundClip.loop(10);
		    	try {	Thread.sleep(millis); } catch (InterruptedException ie)
		    	{ ie.printStackTrace(); }
		    	soundClip.stop(); // kill after one iteration
		    }
	    }
    	
        boolean currentClipActive()
        {
            if (currentClip == null)
            {
                return false;
            }
            else
            {
                return currentClip.isActive();
            }
        }
    }
}