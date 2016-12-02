/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: LedCanvas.java
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sf.jdivelog.gui.statusbar;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Canvas simulating an LED.
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class LedCanvas extends Canvas {

    private static final long serialVersionUID = 1477599835253938817L;
    
    public static enum LedShape { ROUND, RECTANGLE }; 

    /** shape of the led */
    private LedShape shape;
    
    /** Color of LED when on */
    private Color colorOn;
    
    /** Color for LED when off */
    private Color colorOff;
    
    /** duration of flash in milliseconds */
    private long flashDuration;
    
    /** Thread for turning off LED after flashDuration */
    private FlashThread flashThread;
    
    /** whether the LED is burning or not */
    private boolean burning;
    
    public LedCanvas(LedShape shape) {
        this.shape = shape;
        this.flashDuration = 200;
        this.colorOn = new Color(255, 50, 50);
        this.colorOff = new Color(100, 10, 10);
        this.burning = false;
        this.flashThread = new FlashThread();
        this.flashThread.start();
    }
    
    /**
     * sets the duration of a flash.
     * @param milliseconds
     */
    public void setFlashDuration(long milliseconds) {
        this.flashDuration = milliseconds;
    }
    
    /**
     * turns on the LED for the duration of a flash
     * @see #setFlashDuration(long)
     */
    public void flash() {
        setBurning(true);
        flashThread.interrupt();
    }
    
    @Override
    public void paint(Graphics g) {
        Dimension d = getPreferredSize();
        if (d.getWidth() > 0 && d.getHeight() > 0) {
            int w = new Double(d.getWidth()).intValue();
            int h = new Double(d.getHeight()).intValue();
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            if (shape == LedShape.RECTANGLE) {
                drawRectangularLed(g2d, w ,h);
            } else if (shape == LedShape.ROUND) {
                drawRoundLed(g2d, w, h);
            }
            g.drawImage(img, 0, 0, new Double(d.getWidth()).intValue(), new Double(d.getHeight()).intValue(), Color.WHITE, this);
        }

    }
    
    private void drawRectangularLed(Graphics2D g, int width, int height) {
        if (burning) {
            g.setBackground(colorOn);
        } else {
            g.setBackground(colorOff);            
        }
        g.clearRect(0, 0, width, height);
        Color colorTopLeft = new Color(200, 200, 200);
        Color colorBottomRight = new Color(50, 50, 50);
        g.setColor(colorTopLeft);
        g.drawLine(0, 0, width, 0);
        g.drawLine(0, 0, 0, height);
        g.setColor(colorBottomRight);
        g.drawLine(width-1, 1, width-1, height);
        g.drawLine(1, height-1, width, height-1);
    }
    
    private void drawRoundLed(Graphics2D g, int width, int height) {
        throw new NoSuchMethodError("Round LED not yet supported");
    }
    
    private void setBurning(boolean burning) {
        this.burning = burning;
        invalidate();
        repaint();
    }
    
    //
    // inner classes
    //
    
    private class FlashThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(flashDuration);
                    if (burning) {
                        setBurning(false);
                    }
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    // just fine... we're working with interrupts to restart the timer
                }
            }
        }
    }
    
}
