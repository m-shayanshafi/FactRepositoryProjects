/**
 * File:    Field.java
 * Created: 05.11.2005
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


import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.soureforge.acousticMemory.controller.Game;
import net.soureforge.acousticMemory.controller.PreferencesStore;




/**
 * The field containing the cards.
 * 
 * @version $Id: Field.java 29 2006-01-12 23:43:52Z markus $
 * @author Markus Bauer <markusbauer@users.sourceforge.net>
 */
public class Field
    implements PropertyChangeListener
{
  /**
   * The cards in the game.
   */
  private Card[] cards = null;


  /**
   * The card that is currently selected.
   */
  private Card currentSelectedCard = null;


  /**
   * The last card, that was selected.
   */
  private Card lastSelectedCard = null;




  /**
   * Constructor.
   */
  public Field()
  {
  }



  /**
   * @return Returns the size.
   */
  public Dimension getSize()
  {
    return PreferencesStore.getInstance().getFieldSize();
  }



  /**
   * @param size The size to set.
   */
  public void setSize(Dimension size)
  {
    PreferencesStore.getInstance().setFieldSize(size);
  }



  /**
   * @return Returns the cards.
   */
  public Card[] getCards()
  {
    return cards;
  }



  /**
   * @return The number of cards in the game.
   */
  public int getNumCards()
  {
    return cards == null ? 0 : cards.length;
  }



  /**
   * Returns a specific card in the game.
   * 
   * @param number The number of the card.
   * @return The selected card or <code>null</code>, if it does not exist.
   */
  public Card getCard(int number)
  {
    if (number <= 0 || cards == null || number > cards.length)
    {
      return null;
    }

    return cards[number - 1];
  }



  /**
   * Creates a new game field.
   */
  public void create()
  {
    synchronized (this)
    {
      lastSelectedCard = null;
      currentSelectedCard = null;
    }
    
    // determine number of cards
    // must be dividable by 2 and there must not be more cards than available
    // sounds.
    int numCards = getSize().height * getSize().width;
    if (numCards % 2 != 0)
    {
      numCards--;
    }

    // get sounds
    List<? extends Sound> sounds = Game.getInstance().getSoundManager()
        .getSounds();
    Collections.shuffle(sounds);
    if (numCards > sounds.size() * 2)
    {
      numCards = sounds.size() * 2;
    }

    // create cards
    cards = new Card[numCards];
    ArrayList<Integer> numbers = new ArrayList<Integer>(numCards);

    for (int i = 0; i < numCards; i++)
    {
      numbers.add(i);
      cards[i] = new Card(i + 1);
    }

    // shuffle cards and set partners
    Collections.shuffle(numbers);
    for (int i = 0; i < numbers.size(); i += 2)
    {
      int card1 = numbers.get(i);
      int card2 = numbers.get(i + 1);

      cards[card1].setPartner(cards[card2]);
      cards[card1].setSound(sounds.get(i / 2));
    }
  }



  /**
   * If two cards are selected, the cards are deselected.
   */
  private synchronized void resetCards()
  {
    if (lastSelectedCard != null)
    {
      lastSelectedCard.setSelected(false);
      currentSelectedCard.setSelected(false);
      lastSelectedCard = null;
      currentSelectedCard = null;
    }
  }



  /**
   * Select a specific card.
   * 
   * @param card The card to select.
   */
  public void selectCard(Card card)
  {
    if (card == lastSelectedCard || card == currentSelectedCard)
    {
      return;
    }

    resetCards();

    card.setSelected(true);

    synchronized (this)
    {
      lastSelectedCard = currentSelectedCard;
      currentSelectedCard = card;
    }
  }



  /* (non-Javadoc)
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
  public synchronized void propertyChange(PropertyChangeEvent evt)
  {
    if (currentSelectedCard != null
        && evt.getNewValue() == currentSelectedCard.getSound())
    {
      resetCards();
    }
  }
}
