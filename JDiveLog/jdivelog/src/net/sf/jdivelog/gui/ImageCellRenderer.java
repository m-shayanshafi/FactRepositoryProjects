/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: ImageCellRenderer.java
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

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Description: Cell containing picture
 * 
 * @author Pascal Pellmont <jdivelog@pellmont.dyndns.org>
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    
    private static final long serialVersionUID = 3258410638332998968L;

    public void setValue(Object o) {
        if (o != null) {
            setIcon(new ImageIcon((Image)o));
            setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            setVerticalAlignment(DefaultTableCellRenderer.CENTER);
        } else {
            setIcon(null);
            setText("");
        }
    }
}
