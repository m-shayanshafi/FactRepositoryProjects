/**
 * File:    SoundFile.java
 * Created: 27.12.2005
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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import net.soureforge.acousticMemory.controller.SoundManager;
import net.soureforge.acousticMemory.model.Sound;




/**
 * A sound file which can be played.
 * 
 * @version $Id: SoundFile.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class SoundFile
    implements Sound
{
  /**
   * The file containing the audio.
   */
  private File file;


  /**
   * The SoundManager responsible for playing the sounds.
   */
  private SoundManager soundManager;




  /**
   * @param file The file containing the audio.
   * @param soundManager The SoundManager responsible for playing the sounds.
   */
  public SoundFile(File file, SoundManager soundManager)
  {
    this.file = file;
    this.soundManager = soundManager;
  }



  /**
   * Plays the sound and waits until the sound is finished or it has been
   * stopped explicitly. If an error occurs during playing the sound the method
   * simply returns without an error.
   */
  public synchronized void play()
  {
    soundManager.playSound(this);
  }



  /**
   * Opens the audioStream.
   * 
   * @return The audioStream. <code>null</code>, if an error occured.
   */
  public AudioInputStream open()
  {
    FileInputStream fis = null;
    AudioInputStream ais = null;

    try
    {
      fis = new FileInputStream(file);
    }
    catch (IOException e)
    {
      return null;
    }

    try
    {
      ais = AudioSystem.getAudioInputStream(fis);
    }
    catch (Exception e)
    {
      try
      {
        fis.close();
      }
      catch (IOException ioe)
      {
      }
      return null;
    }

    return ais;
  }



  /**
   * Test if the soundFile represents a valid audio file.
   * 
   * @return <code>true</code>, if the file is an audio, <code>false</code>
   *         otherwise.
   */
  public boolean isValid()
  {
    AudioInputStream ais = open();

    if (ais == null)
    {
      return false;
    }

    try
    {
      ais.close();
    }
    catch (IOException e)
    {
    }

    return true;
  }
}
