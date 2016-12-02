/**
 * File:    Gui.java
 * Created: 31.12.2005
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


package net.soureforge.acousticMemory.gui;


import java.awt.Dimension;
import java.util.ResourceBundle;




/**
 * Interface to the GUI.
 * 
 * @version $Id: Gui.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public interface Gui
{
  /**
   * The resource bundle for the messages.
   */
  public static final ResourceBundle MESSAGES = ResourceBundle
      .getBundle(Gui.class.getPackage().getName() + ".messages");




  /**
   * Displays the gui.
   */
  public void show();



  /**
   * Creates a new (empty) field for a new game.
   * 
   * @param size The size of the field.
   * @param numCards The number of cards that are added.
   * @return The created cards.
   */
  public GuiCard[] createField(Dimension size, int numCards);
}
