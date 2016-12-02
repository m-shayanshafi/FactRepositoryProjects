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


package net.soureforge.acousticMemory.gui.swing;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;




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
   * @return An icon for starting playing a sound.
   */
  public static Icon createPlayIcon()
  {
    GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getDefaultScreenDevice()
        .getDefaultConfiguration();

    BufferedImage image = gc.createCompatibleImage(ICON_SIZE.width,
                                                   ICON_SIZE.height,
                                                   Transparency.TRANSLUCENT);
    Graphics2D graphics = image.createGraphics();

    int[] xpoints = new int[3];
    int[] ypoints = new int[3];

    xpoints[0] = 0;
    ypoints[0] = xpoints[0];

    xpoints[1] = ICON_SIZE.width - xpoints[0];
    ypoints[1] = ICON_SIZE.height / 2;

    xpoints[2] = xpoints[0];
    ypoints[2] = ICON_SIZE.height - ypoints[0];

    Polygon triangle = new Polygon(xpoints, ypoints, xpoints.length);

    graphics.setPaint(Color.GREEN);
    graphics.fill(triangle);
    graphics.setPaint(Color.BLACK);
    graphics.draw(triangle);

    return new ImageIcon(image);
  }



  /**
   * @return An icon for stopping a sound.
   */
  public static Icon createStopIcon()
  {
    GraphicsConfiguration gc = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getDefaultScreenDevice()
        .getDefaultConfiguration();

    BufferedImage image = gc.createCompatibleImage(ICON_SIZE.width,
                                                   ICON_SIZE.height,
                                                   Transparency.TRANSLUCENT);
    Graphics2D graphics = image.createGraphics();


    int border = 2;
    Shape quader = new Rectangle2D.Float(border, border, ICON_SIZE.width - 2
        * border, ICON_SIZE.height - 2 * border);

    graphics.setPaint(Color.RED);
    graphics.fill(quader);
    graphics.setPaint(Color.BLACK);
    graphics.draw(quader);

    return new ImageIcon(image);
  }



  /**
   * Factory can not be instanciated
   */
  private IconFactory()
  {
  }

}
