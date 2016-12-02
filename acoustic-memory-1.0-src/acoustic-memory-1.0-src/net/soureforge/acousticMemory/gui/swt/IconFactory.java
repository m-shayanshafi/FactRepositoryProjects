/**
 * File:    IconFactory.java
 * Created: 29.12.2005
 *
 *
 * Copyright (c) 2005  Markus Bauer <markusbauer@users.sourceforge.net>
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
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */


package net.soureforge.acousticMemory.gui.swt;


import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;




/**
 * Creates some icons for the gui.
 * 
 * @version $Id: IconFactory.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class IconFactory
{
  /**
   * The size of the created icons.
   */
  public static final Dimension ICON_SIZE = new Dimension(16, 16);




  /**
   * @param device The device on which to create the image.
   * @return An icon for starting playing a sound.
   */
  public static Image createPlayIcon(Device device)
  {
    Image image = new Image(device, ICON_SIZE.width, ICON_SIZE.height);
    GC gc = new GC(image);
    
    gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
    gc.setBackground(device.getSystemColor(SWT.COLOR_GREEN));
    
    int[] points = new int[6];

    points[0] = 0;
    points[1] = points[0];

    points[2] = ICON_SIZE.width - points[0];
    points[3] = ICON_SIZE.height / 2;

    points[4] = points[0];
    points[5] = ICON_SIZE.height - points[1];

    gc.fillPolygon(points);
    gc.drawPolygon(points);
    
    gc.dispose();

    return image;
  }



  /**
   * @param device The device on which to create the image.
   * @return An icon for stopping a sound.
   */
  public static Image createStopIcon(Device device)
  {
    Image image = new Image(device, ICON_SIZE.width, ICON_SIZE.height);
    GC gc = new GC(image);

    int border = 2;
    Rectangle bounds = new Rectangle(border, border, ICON_SIZE.width - 2 * border,
                                     ICON_SIZE.height - 2 * border);
    gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
    gc.drawRectangle(bounds);
    gc.setBackground(device.getSystemColor(SWT.COLOR_RED));
    gc.fillRectangle(bounds);
    gc.dispose();

    return image;
  }



  /**
   * Factory can not be instanciated
   */
  private IconFactory()
  {
  }

}
