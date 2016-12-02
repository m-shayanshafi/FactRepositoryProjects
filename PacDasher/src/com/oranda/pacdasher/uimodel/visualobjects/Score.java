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
 
package com.oranda.pacdasher.uimodel.visualobjects;

import com.oranda.pacdasher.uimodel.*;
import com.oranda.pacdasher.uimodel.util.*;

import java.awt.*;

/**
 * A temporary representation of a score in the maze, e.g. for when a ghost
 * gets captured.
 */
public class Score extends AnimatedVisualObject
{
    private final static int FONT_HEIGHT = 16;
    private final static Font font 
            = new Font("SansSerif", Font.BOLD, FONT_HEIGHT);
    protected Color color;
    protected int score;
    protected XY xy;
    
    private int birthTime;
    private int lifeTimeFrames = 20;
    
    public void initialize(XY xy, int score, Color color)
    {
        this.xy = xy;
        this.score = score;
        this.color = color;
        this.birthTime = UIModel.getInstance().getVirtualTime();
    }
    
    public boolean isExpired()
    {
        return UIModel.getInstance().getVirtualTime() > getDeathTime();
    }
    
    public int getDeathTime()
    {
        return this.birthTime + lifeTimeFrames;
    }
    
    public void render(Graphics g, XYCoarse xyc)
    {
        render(g);
    }

    public void render(Graphics g)
    {
        //Const.logger.fine("Rendering score " + score + "," + xy);
        g.setColor(color);
        g.setFont(Score.font);
        g.drawString("" + score, 
                this.xy.getX() - UIModelConsts.X_PAC_DASHER_SIZE / 2, 
                this.xy.getY() + FONT_HEIGHT / 2);
    }
    
    public int getTypeID()
    {
    	return UIModelConsts.TYPE_ID_SCORE;
    }
    
    public Object clone()
    {
        Score cloneScore = new Score();
        cloneScore.initialize(this.xyCoarse, this.score, this.color);
        return cloneScore;
    }
}
