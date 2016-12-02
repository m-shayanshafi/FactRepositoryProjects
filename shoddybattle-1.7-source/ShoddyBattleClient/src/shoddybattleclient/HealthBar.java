/*
 * HealthBar.java
 *
 * Created on March 11, 2007, 4:36 PM
 *
 * This file is a part of Shoddy Battle.
 * Copyright (C) 2006  Colin Fitzpatrick
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package shoddybattleclient;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author Colin
 */
public class HealthBar extends JPanel {
    
    private static final Image m_image;
    private double m_ratio = 1.0;
    
    static {
        m_image = Toolkit
                .getDefaultToolkit()
                .createImage(
                    GameVisualisation.class.getResource("resources/healthbar.jpg"));
    }
    
    /** Creates a new instance of HealthBar */
    public HealthBar() {
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    public void setRatio(double ratio) {
        if ((ratio > 0) && ((int)(ratio * 100.0) == 0)) {
            ratio = 0.01;
        }
        m_ratio = ratio;
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(m_image, 0, (int)((double)getWidth() * m_ratio), getHeight());
        try {
            tracker.waitForAll();
        } catch (InterruptedException e) {
            
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int width = getWidth();
        int height = getHeight();
        int x = (int)((double)width * m_ratio);
        g.drawImage(m_image, 0, 0, x, height, this);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, x - 1, height - 1);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setFont(new Font(null, Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics();
        String str = (int)(m_ratio * 100) + "%";
        g.drawString(str,
                (width - metrics.stringWidth(str)) / 2,
                (height - metrics.getDescent() + metrics.getAscent()) / 2);
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Testing HealthBar");
        frame.setSize(230, 70);
        HealthBar health = new HealthBar();
        health.setRatio(1.0 / 300);
        frame.getContentPane().add(health);
        health.setSize(frame.getSize());
        health.setVisible(true);
        health.setLocation(0, 0);
        frame.setVisible(true);
    }
    
}
