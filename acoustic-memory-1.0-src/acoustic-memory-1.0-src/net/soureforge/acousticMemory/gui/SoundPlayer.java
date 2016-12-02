/**
 * File:    SoundPlayer.java
 * Created: 01.01.2006
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


import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import net.soureforge.acousticMemory.controller.AbstractPropertyChangeListenerSupport;
import net.soureforge.acousticMemory.controller.SoundManager;




/**
 * Thread that plays sounds.
 * 
 * @version $Id: SoundPlayer.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class SoundPlayer
    extends AbstractPropertyChangeListenerSupport
    implements Runnable
{
  /**
   * If <code>true</code>, the sound should stop.
   */
  private boolean shouldStop = false;


  /**
   * The next sound to play.
   */
  private SoundFile nextSound;


  /**
   * Data line that plays the audio.
   */
  private SourceDataLine dataLine;





  /**
   * Constructor
   */
  public SoundPlayer()
  {
  }



  /**
   * Plays a new sound.
   * 
   * @param nextSound The new sound file.
   */
  public synchronized void play(final SoundFile nextSound)
  {
    this.nextSound = nextSound;
    this.notifyAll();
  }



  /**
   * Stops playing the sound.
   */
  public synchronized void stop()
  {
    nextSound = null;

    if (dataLine != null)
    {
      dataLine.stop();
      dataLine.flush();
      dataLine.close();
    }

    shouldStop = true;
  }



  /**
   * @return <code>true</code>, if the sound should stop.
   */
  private synchronized boolean shouldStop()
  {
    return shouldStop || nextSound != null;
  }



  /**
   * Waits in an endless loops for new soundFiles and plays them.
   */
  public void run()
  {
    while (true)
    {
      AudioInputStream audioStream;
      SoundFile currentSound = null;

      synchronized (this)
      {
        // wait for the next sound
        while (nextSound == null)
        {
          try
          {
            this.wait();
          }
          catch (InterruptedException e)
          {
          }
        }

        shouldStop = false;
        
        currentSound = nextSound;
        nextSound = null;

        // create audiostream
        audioStream = currentSound.open();

        if (audioStream == null)
        {
          // cannot play this, continue with next audio
          continue;
        }
      }

      getPropertyChangeSupport()
          .firePropertyChange(SoundManager.PROPERTY_LENGTH, null,
                              audioStream.getFrameLength());

      try
      {
        // create SourceDataLine with correct format
        synchronized (this)
        {
          dataLine = AudioSystem.getSourceDataLine(audioStream.getFormat());
          dataLine.open(audioStream.getFormat());

          getPropertyChangeSupport()
              .firePropertyChange(SoundManager.PROPERTY_START_PLAYING, null,
                                  dataLine);

          dataLine.start();
        }

        byte[] buffer = new byte[dataLine.getBufferSize()];
        int read = 0;


        // read data from the stream and play them
        while (!shouldStop())
        {
          // if there are still data, play them
          read = audioStream.read(buffer, 0, buffer.length);

          if (!shouldStop())
          {
            if (read > 0)
            {
              dataLine.write(buffer, 0, read);
            }
            else
            {
              dataLine.drain();
              break;
            }
          }
        }
      }
      catch (IOException e)
      {
      }
      catch (LineUnavailableException e)
      {
      }
      finally
      {
        synchronized (this)
        {
          if (dataLine != null)
          {
            getPropertyChangeSupport()
                .firePropertyChange(SoundManager.PROPERTY_STOP_PLAYING, null,
                                    currentSound);

            dataLine.close();
            dataLine = null;
          }
        }

        // close streams
        if (audioStream != null)
        {
          try
          {
            audioStream.close();
          }
          catch (IOException e)
          {
          }
        }
      } // finally
    } // while true
  } // run()
}
