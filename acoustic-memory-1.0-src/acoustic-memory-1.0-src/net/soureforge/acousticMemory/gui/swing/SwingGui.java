/**
 * File:    SwingGui.java
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


package net.soureforge.acousticMemory.gui.swing;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import net.soureforge.acousticMemory.Main;
import net.soureforge.acousticMemory.controller.Game;
import net.soureforge.acousticMemory.gui.Common;
import net.soureforge.acousticMemory.gui.Gui;
import net.soureforge.acousticMemory.gui.GuiCard;




/**
 * Swing implementation of the gui.
 * 
 * @version $Id: SwingGui.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class SwingGui
    implements Gui, ContainerListener, ActionListener
{
  /**
   * Border for grouping.
   */
  public static final Border DEFAULT_BORDER = BorderFactory
      .createEtchedBorder(EtchedBorder.LOWERED);


  /**
   * The main frame.
   */
  private JFrame mainFrame = new JFrame();


  /**
   * The field Panel to draw the fields.
   */
  private FieldPanel fieldPanel = new FieldPanel();


  /**
   * Start a new game.
   */
  private JMenuItem newGameItem;


  /**
   * Stop the current sound.
   */
  private JMenuItem stopSoundItem;


  /**
   * Play the last sound again.
   */
  private JMenuItem replaySoundItem;


  /**
   * Change preferences of the game.
   */
  private JMenuItem preferencesItem;


  /**
   * Exit the game.
   */
  private JMenuItem exitItem;


  /**
   * About the game.
   */
  private JMenuItem aboutItem;


  /**
   * License of the game.
   */
  private JMenuItem licenseItem;




  /**
   * Initialises and builds the swing gui.
   */
  public SwingGui()
  {
    JFrame.setDefaultLookAndFeelDecorated(true);

    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        initGui();
      }
    });
  }



  /**
   * Creations of components.
   */
  void initGui()
  {
    mainFrame.setTitle(Main.APPLICATION_PROPERTIES.getString("name"));
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setPreferredSize(new Dimension(400, 300));
    mainFrame.setLocationByPlatform(true);

    mainFrame.setLayout(new BorderLayout());

    mainFrame.getContentPane().add(fieldPanel.getPanel(), BorderLayout.CENTER);
    fieldPanel.getPanel().addContainerListener(this);


    // build menu
    JMenuBar menuBar = new JMenuBar();
    mainFrame.setJMenuBar(menuBar);

    // game menu
    JMenu gameMenu = new JMenu(Gui.MESSAGES.getString("gameMenu"));
    menuBar.add(gameMenu);

    newGameItem = new JMenuItem(Gui.MESSAGES.getString("gameNew"));
    stopSoundItem = new JMenuItem(Gui.MESSAGES.getString("gameStopSound"));
    stopSoundItem.setIcon(IconFactory.createStopIcon());
    replaySoundItem = new JMenuItem(Gui.MESSAGES.getString("gameRepeatSound"));
    replaySoundItem.setIcon(IconFactory.createPlayIcon());
    preferencesItem = new JMenuItem(Gui.MESSAGES.getString("gamePreferences"));
    exitItem = new JMenuItem(Gui.MESSAGES.getString("gameExit"));

    newGameItem.addActionListener(this);
    stopSoundItem.addActionListener(this);
    replaySoundItem.addActionListener(this);
    preferencesItem.addActionListener(this);
    exitItem.addActionListener(this);

    gameMenu.add(newGameItem);
    gameMenu.addSeparator();
    gameMenu.add(stopSoundItem);
    gameMenu.add(replaySoundItem);
    gameMenu.addSeparator();
    gameMenu.add(preferencesItem);
    gameMenu.addSeparator();
    gameMenu.add(exitItem);


    // about menu
    JMenu aboutMenu = new JMenu(Gui.MESSAGES.getString("aboutMenu"));
    menuBar.add(Box.createHorizontalGlue());
    menuBar.add(aboutMenu);

    aboutItem = new JMenuItem(Gui.MESSAGES.getString("aboutAbout"));
    licenseItem = new JMenuItem(Gui.MESSAGES.getString("aboutLicense"));

    aboutItem.addActionListener(this);
    licenseItem.addActionListener(this);

    aboutMenu.add(aboutItem);
    aboutMenu.add(licenseItem);


    // audio slider
    AudioSlider audioSlider = new AudioSlider();
    audioSlider.setBorder(DEFAULT_BORDER);
    mainFrame.getContentPane().add(audioSlider, BorderLayout.SOUTH);

    mainFrame.pack();
  }



  /* (non-Javadoc)
   * @see akustikMemory.gui.Gui#show()
   */
  public void show()
  {
    if (!SwingUtilities.isEventDispatchThread())
    {
      Runnable action = new Runnable()
      {
        public void run()
        {
          show();
        }
      };
      SwingUtilities.invokeLater(action);
    }
    else
    {
      mainFrame.setVisible(true);
    }
  }




  /* (non-Javadoc)
   * @see akustikMemory.gui.Gui#createField(java.awt.Dimension, int)
   */
  public GuiCard[] createField(Dimension size, int numCards)
  {
    fieldPanel.createField(size);

    GuiCard[] cards = new GuiCard[numCards];
    for (int i = 0; i < numCards; i++)
    {
      cards[i] = fieldPanel.addCard();
    }

    return cards;
  }



  /**
   * @return Returns the fieldPanel.
   */
  public FieldPanel getFieldPanel()
  {
    return fieldPanel;
  }



  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == newGameItem)
    {
      Game.getInstance().start();
    }
    else if (e.getSource() == stopSoundItem)
    {
      Game.getInstance().getSoundManager().stopSound();
    }
    else if (e.getSource() == replaySoundItem)
    {
      Game.getInstance().getSoundManager().repeatLastSound();
    }
    else if (e.getSource() == preferencesItem)
    {
      PreferencesDialog dialog = new PreferencesDialog(mainFrame);
      dialog.setVisible(true);
    }
    else if (e.getSource() == exitItem)
    {
      System.exit(0);
    }
    else if (e.getSource() == aboutItem)
    {
      showAboutDialog();
    }
    else if (e.getSource() == licenseItem)
    {
      JDialog dialog = new LicenseDialog(mainFrame);
      dialog.setVisible(true);
    }
  }



  /**
   * Shows an about dialog.
   */
  private void showAboutDialog()
  {
    String title = aboutItem.getText() + " "
        + Main.APPLICATION_PROPERTIES.getString("name");
    String message = "<html>"
        + Common.getInstance().getAboutMessage().replace("<", "&lt;")
            .replace(">", "&gt;").replace("\n", "<br>") + "</html>";

    JOptionPane.showMessageDialog(mainFrame, message, title,
                                  JOptionPane.INFORMATION_MESSAGE);
  }



  /* (non-Javadoc)
   * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
   */
  public void componentAdded(ContainerEvent e)
  {
    Dimension preferredSize = mainFrame.getPreferredSize();
    Dimension size = mainFrame.getSize();

    if (size.width < preferredSize.width)
    {
      size.width = preferredSize.width;
    }
    if (size.height < preferredSize.height)
    {
      size.height = preferredSize.height;
    }

    mainFrame.setSize(size);
    mainFrame.validate();
  }



  /* (non-Javadoc)
   * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
   */
  public void componentRemoved(ContainerEvent e)
  {
  }

}
