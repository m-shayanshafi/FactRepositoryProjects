/**
 * File:    Game.java
 * Created: 24.10.2005
 *
 *
 * Copyright (C) 2005  Markus Bauer <markusbauer@users.sourceforge.net>
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA  02111-1307, USA.
 */


package net.soureforge.acousticMemory.controller;


import java.beans.PropertyChangeSupport;

import net.soureforge.acousticMemory.gui.Gui;
import net.soureforge.acousticMemory.gui.GuiCard;
import net.soureforge.acousticMemory.gui.GuiFactory;
import net.soureforge.acousticMemory.model.Card;
import net.soureforge.acousticMemory.model.Field;




/**
 * Controls the game.
 * 
 * Instanciates all needed Object for the game.
 * 
 * @version $Id: Game.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class Game
{
  /**
   * The singleton instance.
   */
  private static Game instance;


  /**
   * The field for the cards.
   */
  private final Field field = new Field();


  /**
   * The manager for the sound files.
   */
  private final SoundManager soundManager = new SoundManager();


  /**
   * The gui of the game.
   */
  private final Gui gui;


  /**
   * Property change listener for sound.
   */
  private final PropertyChangeSupport soundPropertyListener = new PropertyChangeSupport(
                                                                                        this);




  /**
   * Constructor.
   * @throws Exception If the gui could not be instanciated.
   */
  public Game() throws Exception
  {
    instance = this;

    soundManager.addPropertyChangeListener(SoundManager.PROPERTY_STOP_PLAYING,
                                           field);
    gui = GuiFactory.createGui();
    gui.show();
  }



  /**
   * @return Returns the singleton instance.
   */
  public static Game getInstance()
  {
    return instance;
  }



  /**
   * @return Returns the soundManager.
   */
  public SoundManager getSoundManager()
  {
    return soundManager;
  }



  /**
   * @return Returns the field.
   */
  public Field getField()
  {
    return field;
  }



  /**
   * @return Returns the gui.
   */
  public Gui getGui()
  {
    return gui;
  }



  /**
   * Starts the game.
   */
  public void start()
  {
    soundManager.newGame();

    field.create();
    Card[] cards = field.getCards();
    GuiCard[] guiCards = gui.createField(field.getSize(), cards.length);
    
    if (guiCards == null)
    {
      return;
    }

    // make model and view cards know each other
    for (int i = 0; i < cards.length; i++)
    {
      cards[i].addPropertyChangeListener(guiCards[i]);
      guiCards[i].addPropertyChangeListener(cards[i]);
    }
  }

}
