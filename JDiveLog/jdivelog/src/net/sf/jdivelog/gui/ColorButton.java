/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ColorButton.java
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
package net.sf.jdivelog.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;

import com.bric.swing.ColorPicker;

public class ColorButton extends JButton implements ActionListener, Icon {
    
    private static final long serialVersionUID = -1992821268276381084L;

    private final Window window;
    private Color color;
    private boolean opacity;
    private int iconwidth;
    private int iconheight;

    private Set<ColorChangeListener> listeners;

    public ColorButton(Window window, boolean opacity) {
        iconwidth = 32;
        iconheight = 32;
        this.window = window;
        this.opacity = opacity;
        listeners = new HashSet<ColorChangeListener>();
        setFocusable(false);
        setRolloverEnabled(false);
        setIcon(this);
        addActionListener(this);
        setSize(iconwidth, iconheight);
        setPreferredSize(new Dimension(iconwidth, iconheight));
        setMargin(new Insets(0,0,0,0));
        setContentAreaFilled(false);
        setColor(Color.BLACK);
    }
    
    public synchronized void addColorChangeListener(ColorChangeListener l) {
        listeners.add(l);
    }
    
    public synchronized void removeColorChangeListener(ColorChangeListener l) {
        listeners.remove(l);
    }
    
    public ColorButton(Window window) {
        this(window, true);
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color c) {
        color = c;
        repaint();
        fireColorChanged();
    }
    
    public void actionPerformed(ActionEvent e) {
        Color newColor = ColorPicker.showDialog(window, getColor(), opacity);
        if (newColor != null) {
            setColor(newColor);
        }
    }
    
    protected synchronized void fireColorChanged() {
        ColorChangeEvent e = new ColorChangeEvent(this);
        for (ColorChangeListener l : listeners) {
            l.colorChanged(e);
            
        }
    }

    public int getIconHeight() {
        return iconwidth;
    }

    public int getIconWidth() {
        return iconheight;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color orig = g.getColor();
        g.setColor(Color.WHITE);
        g.fillRect(x, y, iconwidth, iconheight);
        g.setColor(getColor());
        g.fillRect(x, y, iconwidth, iconheight);
        g.setColor(orig);
    }
    

}
