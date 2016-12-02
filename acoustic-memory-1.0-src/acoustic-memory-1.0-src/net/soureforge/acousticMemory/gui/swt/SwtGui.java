/**
 * File:    SwtGui.java
 * Created: 02.01.2006
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


import java.awt.Dimension;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import net.soureforge.acousticMemory.Main;
import net.soureforge.acousticMemory.controller.Game;
import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;
import net.soureforge.acousticMemory.gui.GuiCard;




/**
 * SWT implementation of the gui.
 * 
 * @version $Id: SwtGui.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class SwtGui
    implements Gui, Runnable, SelectionListener, DisposeListener
{
  /**
   * The SWT display.
   */
  private Display display;


  /**
   * The SWT shell.
   */
  Shell shell;


  /**
   * Start a new game.
   */
  private MenuItem newGameItem;


  /**
   * Stop the current sound.
   */
  private MenuItem stopSoundItem;


  /**
   * Play the last sound again.
   */
  private MenuItem replaySoundItem;


  /**
   * Change preferences of the game.
   */
  private MenuItem preferencesItem;


  /**
   * Exit the game.
   */
  private MenuItem exitItem;


  /**
   * About the game.
   */
  private MenuItem aboutItem;


  /**
   * License of the game.
   */
  private MenuItem licenseItem;


  /**
   * The field containing the buttons.
   */
  private FieldComposite fieldComposite;




  /**
   * Constructor.
   */
  public SwtGui()
  {
    Thread swtDispatcher = new Thread(this);
    swtDispatcher.start();
  }



  /**
   * Creates the GUI.
   */
  private synchronized void createGui()
  {
    display = new Display();
    shell = new Shell(display);

    shell.setText(Main.APPLICATION_PROPERTIES.getString("name"));
    shell.setMinimumSize(300, 200);
    shell.setLayout(new GridLayout(1, true));


    // build menu
    Menu menu = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menu);

    // game menu
    Menu gameMenu = new Menu(menu);

    MenuItem gameMenuItem = new MenuItem(menu, SWT.CASCADE);
    gameMenuItem.setText(Gui.MESSAGES.getString("gameMenu"));
    gameMenuItem.setMenu(gameMenu);

    newGameItem = new MenuItem(gameMenu, SWT.PUSH);
    newGameItem.setText(Gui.MESSAGES.getString("gameNew"));
    newGameItem.addSelectionListener(this);

    new MenuItem(gameMenu, SWT.SEPARATOR);

    stopSoundItem = new MenuItem(gameMenu, SWT.PUSH);
    stopSoundItem.setText(Gui.MESSAGES.getString("gameStopSound"));
    stopSoundItem.setImage(IconFactory.createStopIcon(display));
    stopSoundItem.addSelectionListener(this);
    stopSoundItem.addDisposeListener(this);

    replaySoundItem = new MenuItem(gameMenu, SWT.PUSH);
    replaySoundItem.setText(Gui.MESSAGES.getString("gameRepeatSound"));
    replaySoundItem.setImage(IconFactory.createPlayIcon(display));
    replaySoundItem.addSelectionListener(this);
    replaySoundItem.addDisposeListener(this);

    new MenuItem(gameMenu, SWT.SEPARATOR);

    preferencesItem = new MenuItem(gameMenu, SWT.PUSH);
    preferencesItem.setText(Gui.MESSAGES.getString("gamePreferences"));
    preferencesItem.addSelectionListener(this);

    new MenuItem(gameMenu, SWT.SEPARATOR);

    exitItem = new MenuItem(gameMenu, SWT.PUSH);
    exitItem.setText(Gui.MESSAGES.getString("gameExit"));
    exitItem.addSelectionListener(this);


    // about menu
    // TODO move menu to the right
    Menu aboutMenu = new Menu(menu);

    MenuItem aboutMenuItem = new MenuItem(menu, SWT.CASCADE);
    aboutMenuItem.setText(Gui.MESSAGES.getString("aboutMenu"));
    aboutMenuItem.setMenu(aboutMenu);

    aboutItem = new MenuItem(aboutMenu, SWT.PUSH);
    aboutItem.setText(Gui.MESSAGES.getString("aboutAbout"));
    aboutItem.addSelectionListener(this);

    licenseItem = new MenuItem(aboutMenu, SWT.PUSH);
    licenseItem.setText(Gui.MESSAGES.getString("aboutLicense"));
    licenseItem.addSelectionListener(this);


    // add field
    fieldComposite = new FieldComposite(shell);
    fieldComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    // audioSlider
    Group audioSliderGroup = new Group(shell, SWT.SHADOW_ETCHED_IN);
    audioSliderGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    audioSliderGroup.setLayout(new FillLayout());
    new AudioSlider(audioSliderGroup);


    shell.pack();

    this.notifyAll();
  }



  /* (non-Javadoc)
   * @see akustikMemory.gui.Gui#show()
   */
  public void show()
  {
    Runnable action = new Runnable()
    {
      public void run()
      {
        shell.open();
      }
    };

    synchronized (this)
    {
      while (display == null || shell == null)
      {
        try
        {
          this.wait();
        }
        catch (InterruptedException e)
        {
        }
      }
    }
    display.asyncExec(action);
  }



  /* (non-Javadoc)
   * @see akustikMemory.gui.Gui#createField(java.awt.Dimension, int)
   */
  public GuiCard[] createField(final Dimension size, final int numCards)
  {
    // internal field for performance in usage in inner class.
    final FieldComposite internalField = fieldComposite;

    final GuiCard[] cards = new GuiCard[numCards];

    Runnable action = new Runnable()
    {
      public void run()
      {
        internalField.createField(size);

        for (int i = 0; i < numCards; i++)
        {
          cards[i] = internalField.addCard();
        }


        Point size = internalField.getParent().getSize();
        Point preferredSize = internalField.getParent()
            .computeSize(SWT.DEFAULT, SWT.DEFAULT);

        if (size.x < preferredSize.x)
        {
          size.x = preferredSize.x;
        }
        if (size.y < preferredSize.y)
        {
          size.y = preferredSize.y;
        }

        internalField.getParent().setSize(size);

        // TODO Bug: Buttons not shown when not resized
      }
    };

    display.syncExec(action);

    return cards;
  }



  /* (non-Javadoc)
   * @see java.lang.Runnable#run()
   */
  public void run()
  {
    createGui();

    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }

    display.dispose();
  }



  /* (non-Javadoc)
   * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
   */
  public void widgetSelected(SelectionEvent e)
  {
    if (e.widget == newGameItem)
    {
      Game.getInstance().start();
    }
    else if (e.widget == stopSoundItem)
    {
      Game.getInstance().getSoundManager().stopSound();
    }
    else if (e.widget == replaySoundItem)
    {
      Game.getInstance().getSoundManager().repeatLastSound();
    }
    else if (e.widget == preferencesItem)
    {
      PreferencesDialog dialog = new PreferencesDialog(shell);
      dialog.open();
    }
    else if (e.widget == exitItem)
    {
      shell.close();
    }
    else if (e.widget == aboutItem)
    {
      showAboutDialog();
    }
    else if (e.getSource() == licenseItem)
    {
      LicenseDialog dialog = new LicenseDialog(shell);
      dialog.open();
    }
  }



  /**
   * Shows an about dialog.
   */
  private void showAboutDialog()
  {
    String appName = Main.APPLICATION_PROPERTIES.getString("name");

    String title = aboutItem.getText() + " " + appName;

    MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION);
    dialog.setText(title);
    dialog.setMessage(Common.getInstance().getAboutMessage());
    dialog.open();
  }





  /* (non-Javadoc)
   * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
   */
  public void widgetDisposed(DisposeEvent e)
  {
    // dispose images
    if (e.widget instanceof Item)
    {
      Item item = (Item) e.widget;
      if (item.getImage() != null)
      {
        item.getImage().dispose();
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
