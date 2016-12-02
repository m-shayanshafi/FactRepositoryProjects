/**
 * File:    PreferencesStore.java
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


package net.soureforge.acousticMemory.controller;


import java.awt.Dimension;
import java.util.prefs.Preferences;

import net.soureforge.acousticMemory.Main;




/**
 * This class stores and retrieves the preferences for the game.
 * 
 * @version $Id: PreferencesStore.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class PreferencesStore
{
  /**
   * Initial size of the field.
   */
  private static final Dimension INITIAL_FIELD_SIZE = new Dimension(5, 4);


  /**
   * Initial directory for the sound files.
   */
  private static final String INITIAL_SOUND_FILES_DIRECTORY = "sounds";


  /**
   * Key to store width of field size.
   */
  private static final String KEY_FIELD_SIZE_WIDTH = "fieldSize.width";


  /**
   * Key to store height of field size.
   */
  private static final String KEY_FIELD_SIZE_HEIGHT = "fieldSize.height";


  /**
   * Key to store the directory for sound files.
   */
  private static final String KEY_SOUND_FILES_DIRECTORY = "soundFilesDirectory";


  /**
   * The singleton instance.
   */
  private static PreferencesStore instance;


  /**
   * The preferences.
   */
  private Preferences preferences = Preferences.userNodeForPackage(Main.class);


  /**
   * Internal stored field size.
   */
  private Dimension fieldSize;


  /**
   * Internal stored directory for sound files.
   */
  private String soundFilesDirectory;




  /**
   * Sigleton class cannot be instaciated.
   */
  private PreferencesStore()
  {
  }



  /**
   * @return Returns the sigleton instance.
   */
  public static PreferencesStore getInstance()
  {
    if (instance == null)
    {
      instance = new PreferencesStore();
    }

    return instance;
  }



  /**
   * @return The size of the field.
   */
  public Dimension getFieldSize()
  {
    if (fieldSize == null)
    {
      int width = preferences.getInt(KEY_FIELD_SIZE_WIDTH,
                                     INITIAL_FIELD_SIZE.width);
      int height = preferences.getInt(KEY_FIELD_SIZE_HEIGHT,
                                      INITIAL_FIELD_SIZE.height);

      fieldSize = new Dimension(width, height);
    }

    return fieldSize;
  }



  /**
   * Stores the field size.
   * 
   * @param size The field size. If the size is <code>null</code>, the stores
   *          values are removed and the size is resetted to the default.
   */
  public void setFieldSize(Dimension size)
  {
    if (size == null)
    {
      preferences.remove(KEY_FIELD_SIZE_WIDTH);
      preferences.remove(KEY_FIELD_SIZE_HEIGHT);
    }
    else if (size.equals(fieldSize))
    {
      return;
    }
    else
    {
      preferences.putInt(KEY_FIELD_SIZE_WIDTH, size.width);
      preferences.putInt(KEY_FIELD_SIZE_HEIGHT, size.height);
    }

    fieldSize = size;
  }



  /**
   * @return Returns the soundFilesDirectory.
   */
  public String getSoundFilesDirectory()
  {
    if (soundFilesDirectory == null)
    {
      soundFilesDirectory = preferences.get(KEY_SOUND_FILES_DIRECTORY,
                                            INITIAL_SOUND_FILES_DIRECTORY);
    }

    return soundFilesDirectory;
  }



  /**
   * @param directory The soundFilesDirectory to set.
   */
  public void setSoundFilesDirectory(String directory)
  {
    if (soundFilesDirectory == null)
    {
      preferences.remove(KEY_SOUND_FILES_DIRECTORY);
    }
    else if (directory.equals(soundFilesDirectory))
    {
      return;
    }
    else
    {
      preferences.put(KEY_SOUND_FILES_DIRECTORY, directory);
    }

    soundFilesDirectory = directory;
  }
}
