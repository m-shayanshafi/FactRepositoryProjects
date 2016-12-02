/**
 * File:    Card.java
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


package net.soureforge.acousticMemory.model;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.soureforge.acousticMemory.controller.AbstractPropertyChangeListenerSupport;
import net.soureforge.acousticMemory.controller.Game;
import net.soureforge.acousticMemory.gui.GuiCard;




/**
 * A card in the game.
 * 
 * @version $Id: Card.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class Card
    extends AbstractPropertyChangeListenerSupport
    implements PropertyChangeListener
{
  /**
   * If <code>true</code>, a selected card will show the number of its
   * partner. This option is for debugging.
   */
  private static final boolean SHOW_SOLUTION = false;


  /**
   * Property for selected attribute.
   */
  public static final String PROPERTY_SELECTED = Card.class.getName()
      + ".selected";


  /**
   * Property for guessed attribute.
   */
  public static final String PROPERTY_GUESSED = Card.class.getName()
      + ".guessed";


  /**
   * Property for text attribute.
   */
  public static final String PROPERTY_TEXT = Card.class.getName() + ".text";


  /**
   * The unique id of this card.
   */
  private int id;


  /**
   * A field is only visible, if it is not already guessed.
   */
  private boolean guessed = false;


  /**
   * Indicates, that the field is selected.
   */
  private boolean selected = false;


  /**
   * The partner card.
   */
  private Card partner;


  /**
   * The sound to play for this card.
   */
  private Sound sound;




  /**
   * Constructor.
   * 
   * @param id The id of this card.
   */
  public Card(int id)
  {
    this.id = id;
  }



  /**
   * @return Returns the id.
   */
  public int getId()
  {
    return id;
  }



  /**
   * A field is only visible, if it is not already guessed.
   * 
   * @return <code>true</code>, if the field is guessed, otherwise
   *         <code>false</code>.
   */
  public boolean isGuessed()
  {
    return guessed;
  }



  /**
   * @param guessed The guessed to set.
   */
  public void setGuessed(boolean guessed)
  {
    boolean oldGuessed = this.guessed;

    if (oldGuessed == guessed)
    {
      return;
    }

    this.guessed = guessed;
    getPropertyChangeSupport().firePropertyChange(PROPERTY_GUESSED, oldGuessed,
                                                  guessed);

    partner.setGuessed(guessed);
  }



  /**
   * @return Returns the partner.
   */
  public Card getPartner()
  {
    return partner;
  }



  /**
   * Sets the partner of this card and also itself as partner of the other card.
   * 
   * @param partner The partner to set.
   */
  public void setPartner(Card partner)
  {
    if (this.partner == partner)
    {
      return;
    }

    this.partner = partner;
    partner.setPartner(this);
  }



  /**
   * @return Returns the sound.
   */
  public Sound getSound()
  {
    return sound;
  }



  /**
   * @param newSound The sound to set.
   */
  public void setSound(Sound newSound)
  {
    if (sound == newSound)
    {
      return;
    }

    sound = newSound;
    partner.setSound(newSound);
  }



  /**
   * @return Returns the selected.
   */
  public boolean isSelected()
  {
    return selected;
  }



  /**
   * Selects the card or reverts the selection. If the card is selected it
   * checks if it is guessed.
   * 
   * @param newSelected The selected to set.
   */
  public void setSelected(boolean newSelected)
  {
    if (selected == newSelected)
    {
      return;
    }

    boolean oldSelected = selected;
    String oldText = getText();

    selected = newSelected;

    getPropertyChangeSupport().firePropertyChange(PROPERTY_SELECTED,
                                                  oldSelected, newSelected);

    if (SHOW_SOLUTION)
    {
      getPropertyChangeSupport().firePropertyChange(PROPERTY_TEXT, oldText,
                                                    getText());
    }


    if (selected)
    {
      // play the sound
      if (sound != null)
      {
        sound.play();
      }

      // check for guessed
      if (partner.isSelected())
      {
        setGuessed(true);
      }
    }
  }




  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public void propertyChange(PropertyChangeEvent evt)
  {
    if (GuiCard.PROPERTY_SELECTED.equals(evt.getPropertyName()))
    {
      Game.getInstance().getField().selectCard(this);
    }
  }



  /**
   * Returns a text representation of the button.
   * 
   * @return The text.
   */
  public String getText()
  {
    if (SHOW_SOLUTION && isSelected())
    {
      return String.valueOf(getPartner().getId());
    }

    return String.valueOf(id);
  }



  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return getText();
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    super.addPropertyChangeListener(listener);
    getPropertyChangeSupport().firePropertyChange(Card.PROPERTY_TEXT, null,
                                                  getText());
  }



  /* (non-Javadoc)
   * @see akustikMemory.controller.AbstractPropertyChangeListenerSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  @Override
  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    super.addPropertyChangeListener(propertyName, listener);
    getPropertyChangeSupport().firePropertyChange(Card.PROPERTY_TEXT, null,
                                                  getText());
  }
}
