/**
 * File:    Common.java
 * Created: 03.01.2006
 *
 *
 * Copyright (c) 2006  Markus Bauer <markusbauer@users.sourceforge.net>
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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.soureforge.acousticMemory.Main;
import net.soureforge.acousticMemory.controller.Game;




/**
 * Common functions for GUI implementations.
 * 
 * @version $Id: Common.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class Common
{
  /**
   * The singleton instance.
   */
  private static Common instance;


  /**
   * Cached about message.
   */
  private String aboutMessage;




  /**
   * @return The singleton instance.
   */
  public static Common getInstance()
  {
    if (instance == null)
    {
      instance = new Common();

    }

    return instance;
  }



  /**
   * Loads the license from the COPYING file. If this fails it loads the license
   * from the application properties.
   * 
   * @return The license.
   */
  public String getLicense()
  {
    StringBuffer buffer = new StringBuffer();

    try
    {
      InputStream is = Main.class.getResourceAsStream("/COPYING");
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      while (reader.ready())
      {
        buffer.append(reader.readLine()).append("\n");
      }
    }
    catch (Exception e)
    {
      buffer = new StringBuffer();
      buffer.append(Main.APPLICATION_PROPERTIES.getString("license"));
    }

    return buffer.toString();
  }



  /**
   * @return An about message.
   */
  public String getAboutMessage()
  {
    if (aboutMessage == null)
    {
      StringBuffer message = new StringBuffer();
      message.append(Main.APPLICATION_PROPERTIES.getString("name"));
      message.append("\n");
      message.append(Gui.MESSAGES.getString("aboutVersion")).append(": ");
      message.append(Main.APPLICATION_PROPERTIES.getString("version"));
      message.append("\n\n");
      message.append(Gui.MESSAGES.getString("aboutCopyright")).append(" (c) ");
      message.append(Main.APPLICATION_PROPERTIES.getString("year"));
      message.append("  ");
      message.append(Main.APPLICATION_PROPERTIES.getString("author"));
      message.append("\n\n");
      message.append(Main.APPLICATION_PROPERTIES.getString("homepage"));

      aboutMessage = message.toString();
    }

    return aboutMessage;
  }



  /**
   * @return The current size of the field.
   */
  public Dimension getFieldSize()
  {
    return Game.getInstance().getField().getSize();
  }



  /**
   * @return The directory for the audio files.
   */
  public String getAudioDirectory()
  {
    return Game.getInstance().getSoundManager().getSoundFilesDirectory()
        .getPath();
  }



  /**
   * Sets new Preferences.
   * 
   * @param newFieldWidth The new width of the field.
   * @param newFieldHeight The new height of the field.
   * @param newAudioDirectory The new audio directory.
   */
  public void setPreferences(int newFieldWidth, int newFieldHeight,
      String newAudioDirectory)
  {
    Game.getInstance().getField().setSize(
                                          new Dimension(newFieldWidth,
                                                        newFieldHeight));

    Game.getInstance().getSoundManager()
        .setSoundFilesDirectory(newAudioDirectory);
  }



  /**
   * Singleton.
   */
  private Common()
  {
  }

}
