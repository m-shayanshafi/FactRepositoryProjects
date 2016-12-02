/**
 * File:    AudioSlider.java
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


package net.soureforge.acousticMemory.gui.swing;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.sampled.DataLine;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import net.soureforge.acousticMemory.controller.Game;
import net.soureforge.acousticMemory.controller.SoundManager;
import net.soureforge.acousticMemory.gui.Gui;




/**
 * Slider showing current audio position.
 * 
 * @version $Id: AudioSlider.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class AudioSlider
    extends JPanel
    implements PropertyChangeListener, ActionListener
{
  /**
   * Update delay of the slider in milliseconds.
   */
  private static final int UPDATE_DELAY = 100;


  /**
   * Border around the icons.
   */
  private static final int ICON_BORDER = 5;


  /**
   * The slider.
   */
  private JSlider slider = new JSlider();


  /**
   * Button for stopping the sound.
   */
  private JButton stopButton = new JButton();


  /**
   * Button for re-playing the sound.
   */
  private JButton playButton = new JButton();


  /**
   * Dataline that plays the audio.
   */
  private DataLine dataLine;


  /**
   * Timer that update the slider.
   */
  private Timer timer;




  /**
   * 
   */
  public AudioSlider()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        initGui();
      }
    });

    Game.getInstance().getSoundManager().addPropertyChangeListener(this);
    timer = new Timer(UPDATE_DELAY, this);
  }



  /**
   * Creations of components.
   */
  void initGui()
  {
    setLayout(new BorderLayout());

    slider.setEnabled(false);
    slider.setValue(0);

    Icon playIcon = IconFactory.createPlayIcon();
    Icon stopIcon = IconFactory.createStopIcon();

    playButton.setIcon(playIcon);
    stopButton.setIcon(stopIcon);
    playButton.setToolTipText(Gui.MESSAGES.getString("buttonPlay"));
    stopButton.setToolTipText(Gui.MESSAGES.getString("buttonStop"));

    playButton.setPreferredSize(new Dimension(playIcon.getIconWidth()
        + ICON_BORDER * 2, playIcon.getIconHeight() + ICON_BORDER * 2));
    stopButton.setPreferredSize(new Dimension(stopIcon.getIconWidth()
        + ICON_BORDER * 2, stopIcon.getIconHeight() + ICON_BORDER * 2));

    playButton.addActionListener(this);
    stopButton.addActionListener(this);

    add(playButton, BorderLayout.WEST);
    add(slider, BorderLayout.CENTER);
    add(stopButton, BorderLayout.EAST);
  }



  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt)
  {
    if (!SwingUtilities.isEventDispatchThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          propertyChange(evt);
        }
      };
      SwingUtilities.invokeLater(action);
      return;
    }

    if (SoundManager.PROPERTY_LENGTH.equals(evt.getPropertyName()))
    {
      slider.setMaximum(((Long) evt.getNewValue()).intValue());
    }
    else if (SoundManager.PROPERTY_START_PLAYING.equals(evt.getPropertyName()))
    {
      dataLine = (DataLine) evt.getNewValue();
      slider.setValue(0);

      if (dataLine != null)
      {
        timer.start();
      }
    }
    else if (SoundManager.PROPERTY_STOP_PLAYING.equals(evt.getPropertyName()))
    {
      timer.stop();
      slider.setValue(0);
    }
  }



  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == timer)
    {
      if (dataLine != null)
      {
        slider.setValue(dataLine.getFramePosition());
      }
    }
    else if (e.getSource() == stopButton)
    {
      Game.getInstance().getSoundManager().stopSound();
    }
    else if (e.getSource() == playButton)
    {
      Game.getInstance().getSoundManager().repeatLastSound();
    }
  }
}
