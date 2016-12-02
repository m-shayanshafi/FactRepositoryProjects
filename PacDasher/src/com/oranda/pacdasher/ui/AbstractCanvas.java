/**
 * 
 */
package com.oranda.pacdasher.ui;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

/**
 * Superclass for drawing areas inside the PacFrame.
 * Includes code to offset distance from the frame/screen's top left corner.
 */
public abstract class AbstractCanvas
{
    private int width, height;    
    protected int xOffset, yOffset;
    protected BufferStrategy strategy;
    

    // template method called by paint()
    protected abstract void paintGraphics(Graphics drawGraphics);
    
    public void initialize(BufferStrategy strategy, int xOffset, int yOffset)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        
        this.strategy = strategy;   
    }
    
    public int getWidth() { return this.width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return this.height; }
    public void setHeight(int height) { this.height = height; }
    public void setSize(int width, int height)
    {
        setWidth(width);
        setHeight(height);
    }
    
    public void paint()
    {
        if (this.strategy == null) 
        {
            Const.logger.info("strategy was null -- not painting");
            return;
        }
        Graphics drawGraphics = this.strategy.getDrawGraphics();
        drawGraphics.translate(xOffset, yOffset);
        
        // template method
        this.paintGraphics(drawGraphics);
        
        drawGraphics.translate(-xOffset, -yOffset);
    }
}
