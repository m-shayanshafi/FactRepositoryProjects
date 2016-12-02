/**
 * File:    SoundManager.java
 * Created: 28.12.2005
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


import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.soureforge.acousticMemory.gui.SoundFile;
import net.soureforge.acousticMemory.gui.SoundPlayer;
import net.soureforge.acousticMemory.model.Sound;




/**
 * Manages the sounds.
 * 
 * @version $Id: SoundManager.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class SoundManager
{
  /**
   * Property that notifies the start of a soundfile. The new value contains the
   * dataLine used to play back the sound.
   */
  public static final String PROPERTY_START_PLAYING = SoundManager.class
      .getName()
      + ".startPlaying";


  /**
   * Property that notifies the stop of a soundfile. The new value contains the
   * sound that stopped playing.
   */
  public static final String PROPERTY_STOP_PLAYING = SoundManager.class
      .getName()
      + ".stopPlaying";


  /**
   * Property for length of audio.
   */
  public static final String PROPERTY_LENGTH = SoundManager.class.getName()
      + ".length";


  /**
   * The sound files.
   */
  private ArrayList<SoundFile> soundFiles = new ArrayList<SoundFile>();


  /**
   * The SoundPlayer playing the sound.
   */
  private SoundPlayer soundPlayer = new SoundPlayer();


  /**
   * The last played or currently playing sound.
   */
  private SoundFile lastSound;




  /**
   * Constructor.
   */
  public SoundManager()
  {
    // create sound thread
    Thread player = new Thread(soundPlayer, "SoundPlayer");
    player.setDaemon(true);
    player.start();

    // load the initial sounds
    loadSounds();
  }



  /**
   * @return Returns the soundFilePath.
   */
  public File getSoundFilesDirectory()
  {
    return new File(PreferencesStore.getInstance().getSoundFilesDirectory());
  }



  /**
   * Sets a new soundFile Path and loads all the sound in there.
   * 
   * @param newSoundFilePath The soundFilePath to set.
   */
  public void setSoundFilesDirectory(String newSoundFilePath)
  {
    PreferencesStore.getInstance().setSoundFilesDirectory(newSoundFilePath);
    loadSounds();
  }



  /**
   * @return Returns the soundFiles.
   */
  public List<? extends Sound> getSounds()
  {
    return soundFiles;
  }



  /**
   * Loads the sound from the soundFilePath.
   */
  public void loadSounds()
  {
    soundFiles.clear();

    File[] files = getSoundFilesDirectory().listFiles();

    if (files == null)
    {
      return;
    }

    for (File file : files)
    {
      SoundFile soundFile = new SoundFile(file, this);
      if (soundFile.isValid())
      {
        soundFiles.add(soundFile);
      }
    }
  }



  /**
   * Resets the sound manager on a new game.
   */
  public synchronized void newGame()
  {
    stopSound();
    lastSound = null;
  }



  /**
   * Plays a sound.
   * 
   * @param sound The sound.
   */
  public synchronized void playSound(SoundFile sound)
  {
    soundPlayer.play(sound);
    lastSound = sound;
  }



  /**
   * Stops the currently playing sound.
   */
  public void stopSound()
  {
    soundPlayer.stop();
  }



  /**
   * Repeats playing of the last sound.
   */
  public synchronized void repeatLastSound()
  {
    playSound(lastSound);
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    soundPlayer.addPropertyChangeListener(listener);
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    soundPlayer.addPropertyChangeListener(propertyName, listener);
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    soundPlayer.removePropertyChangeListener(listener);
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    soundPlayer.removePropertyChangeListener(propertyName, listener);
  }
}
