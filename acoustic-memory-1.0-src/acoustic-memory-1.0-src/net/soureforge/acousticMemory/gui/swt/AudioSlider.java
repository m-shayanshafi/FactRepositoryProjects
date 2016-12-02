/**
 * File:    AudioSlider.java
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


package net.soureforge.acousticMemory.gui.swt;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.DataLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;

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
    extends Composite
    implements PropertyChangeListener, SelectionListener, DisposeListener
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
  private Scale slider;


  /**
   * Button for stopping the sound.
   */
  private Button stopButton;


  /**
   * Button for re-playing the sound.
   */
  private Button playButton;


  /**
   * Dataline that plays the audio.
   */
  private DataLine dataLine;


  /**
   * The timer for updating the slider.
   */
  private Timer timer;


  /**
   * Timertask that updates the slider.
   */
  private TimerTask timerTask;




  /**
   * @param parent A widget which will be the parent of the new instance (cannot
   *          be null).
   */
  public AudioSlider(Composite parent)
  {
    super(parent, SWT.NONE);
    createGui();
    Game.getInstance().getSoundManager().addPropertyChangeListener(this);

    timer = new Timer("audioSlider", true);

    addDisposeListener(this);
  }




  /**
   * Creations of components.
   */
  void createGui()
  {
    setLayout(new GridLayout(3, false));

    playButton = new Button(this, SWT.PUSH);
    Image playIcon = IconFactory.createPlayIcon(getDisplay());
    playButton.setImage(playIcon);
    playButton.setToolTipText(Gui.MESSAGES.getString("buttonPlay"));

    playButton.setSize(playIcon.getBounds().width + ICON_BORDER * 2, playIcon
        .getBounds().height
        + ICON_BORDER * 2);
    playButton.addSelectionListener(this);

    slider = new Scale(this, SWT.HORIZONTAL);
    slider.setEnabled(false);
    slider.setSelection(0);
    slider.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    slider.addDisposeListener(this);

    stopButton = new Button(this, SWT.PUSH);
    stopButton.setToolTipText(Gui.MESSAGES.getString("buttonStop"));
    Image stopIcon = IconFactory.createStopIcon(getDisplay());
    stopButton.setImage(stopIcon);
    stopButton.setSize(stopIcon.getBounds().width + ICON_BORDER * 2, stopIcon
        .getBounds().height
        + ICON_BORDER * 2);
    stopButton.addSelectionListener(this);
  }



  /**
   * Updates the slider.
   */
  synchronized void updateSlider()
  {
    if (isDisposed())
    {
      return;
    }
    
    Display display = getDisplay();
    if (display.getThread() != Thread.currentThread())
    {
      display.asyncExec(timerTask);
      return;
    }

    if (dataLine == null)
    {
      slider.setSelection(0);
    }
    else
    {
      slider.setSelection(dataLine.getFramePosition());
    }
  }



  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(final PropertyChangeEvent evt)
  {
    Display display = getDisplay();
    if (display.getThread() != Thread.currentThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          propertyChange(evt);
        }
      };
      display.asyncExec(action);
      return;
    }

    if (SoundManager.PROPERTY_LENGTH.equals(evt.getPropertyName()))
    {
      slider.setMaximum(((Long) evt.getNewValue()).intValue());
    }
    else if (SoundManager.PROPERTY_START_PLAYING.equals(evt.getPropertyName()))
    {
      dataLine = (DataLine) evt.getNewValue();
      slider.setSelection(0);

      if (dataLine != null)
      {
        synchronized (this)
        {
          timerTask = new TimerTask()
          {
            @Override
            public void run()
            {
              updateSlider();
            }
          };

          timer.schedule(timerTask, 0, UPDATE_DELAY);
        }
      }
    }
    else if (SoundManager.PROPERTY_STOP_PLAYING.equals(evt.getPropertyName()))
    {
      synchronized (this)
      {
        timerTask.cancel();
      }
      slider.setSelection(0);
    }
  }




  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == stopButton)
    {
      Game.getInstance().getSoundManager().stopSound();
    }
    else if (e.widget == playButton)
    {
      Game.getInstance().getSoundManager().repeatLastSound();
    }
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
   */
  public synchronized void widgetDisposed(DisposeEvent e)
  {
    if (e.widget == this || e.widget == slider)
    {
      timer.cancel();
    }
    else if (e.widget instanceof Button)
    {
      Button button = (Button) e.widget;
      if (button != null)
      {
        button.getImage().dispose();
      }
    }
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetDefaultSelected(SelectionEvent e)
  {
    // not used
  }
}
