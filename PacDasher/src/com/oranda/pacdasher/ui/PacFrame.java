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
 
package com.oranda.pacdasher.ui;

 
import java.awt.*;
import java.awt.event.*;

public class PacFrame extends Frame
{
    private KeyEvent lastKeyEvent;
	
    public PacFrame()
    {
	    super();
    	enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
    
    public PacFrame(String s)
    {
	    super(s);
    	enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
    
    public PacFrame(String s, GraphicsConfiguration gConf)
    {
    	super(s, gConf);
    	enableEvents(AWTEvent.KEY_EVENT_MASK);
    }
    
    
    protected void processKeyEvent(KeyEvent e)
    {
        // Const.logger.fine("received event " + e + "; setting lastKeyEvent");
        // filter out KEY_TYPED events because they have "keyCode Unknown"
        if (e.getKeyCode() != KeyEvent.VK_UNDEFINED)
        {
            this.lastKeyEvent = e;
        }
    }

    public KeyEvent getLastKeyEvent()
    {
	    return this.lastKeyEvent;
    }
	
    public void clearLastKeyEvent()
    {
        //Const.logger.fine("lastKeyEvent set to null");
	    this.lastKeyEvent = null;
    }
	
    /**
     * This is useful for converting mouse clicks to key events,
     * which the FrameRunner's are responsive to.
     */
    public void simulateKeyEvent(int keyCode)
    {
        this.lastKeyEvent = new KeyEvent(this, KeyEvent.KEY_PRESSED, 
                System.currentTimeMillis(), 0, keyCode, 
                KeyEvent.CHAR_UNDEFINED);
    }
    
    public void paint(Graphics g)
    {
        Const.logger.fine("PacFrame.paint() called");
        getBufferStrategy().show();
    }
    
    public void update(Graphics g)
    {
        Const.logger.fine("PacFrame.update() called");
    }
    
}
