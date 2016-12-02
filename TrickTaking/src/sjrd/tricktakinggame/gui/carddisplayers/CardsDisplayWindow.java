/*
 * TrickTakingGame - Trick-taking games platform on-line
 * Copyright (C) 2008  Sébastien Doeraene
 * All Rights Reserved
 *
 * This file is part of TrickTakingGame.
 *
 * TrickTakingGame is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * TrickTakingGame is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TrickTakingGame.  If not, see <http://www.gnu.org/licenses/>.
 */
package sjrd.tricktakinggame.gui.carddisplayers;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.gui.event.*;

/**
 * Fenêtre qui affiche une liste de cartes, sous forme graphique
 * @author sjrd
 */
public class CardsDisplayWindow extends JWindow
{
	/**
	 * ID de sérialisation
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Panel d'affichage des cartes
	 */
	private CardsDisplayPanel cardsDisplayPanel;
	
	/**
	 * Crée la fenêtre
	 * @param panel Panel d'affichage des cartes à utiliser
	 */
	public CardsDisplayWindow(CardsDisplayPanel panel)
	{
		super();
		
		cardsDisplayPanel = panel;
		add(cardsDisplayPanel, BorderLayout.CENTER);
	}
	
	/**
	 * Panel d'affichage des cartes
	 * @return Panel d'affichage des cartes
	 */
	public CardsDisplayPanel getCardsDisplayPanel()
	{
		return cardsDisplayPanel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize()
	{
		return cardsDisplayPanel.getPreferredSize();
	}
	
	/**
	 * @see CardsDisplayPanel#addCardSelectionListener(CardSelectionListener)
	 */
	public void addCardSelectionListener(CardSelectionListener listener)
	{
		cardsDisplayPanel.addCardSelectionListener(listener);
	}
	
	/**
	 * @see CardsDisplayPanel#removeCardSelectionListener(CardSelectionListener)
	 */
	public void removeCardSelectionListener(CardSelectionListener listener)
	{
		cardsDisplayPanel.removeCardSelectionListener(listener);
	}
	
	/**
	 * @see CardsDisplayPanel#getCardSelectionListeners()
	 */
	public CardSelectionListener[] getCardSelectionListeners()
	{
		return cardsDisplayPanel.getCardSelectionListeners();
	}
	
	/**
	 * @see CardsDisplayPanel#getPlaceCount()
	 */
	public int getPlaceCount()
	{
		return cardsDisplayPanel.getPlaceCount();
	}
	
	/**
	 * @see CardsDisplayPanel#setPlaceCount(int)
	 */
	public void setPlaceCount(int value)
	{
		cardsDisplayPanel.setPlaceCount(value);
	}
	
	/**
	 * @see CardsDisplayPanel#getAutoUpdatePlaceCount()
	 */
	public boolean getAutoUpdatePlaceCount()
	{
		return cardsDisplayPanel.getAutoUpdatePlaceCount();
	}
	
	/**
	 * @see CardsDisplayPanel#setAutoUpdatePlaceCount(boolean)
	 */
	public void setAutoUpdatePlaceCount(boolean value)
	{
		cardsDisplayPanel.setAutoUpdatePlaceCount(value);
	}
	
	/**
	 * @see CardsDisplayPanel#getCardCount()
	 */
	public int getCardCount()
	{
		return cardsDisplayPanel.getCardCount();
	}
	
	/**
	 * @see CardsDisplayPanel#getCards()
	 */
	public java.util.List<PosCard> getCards()
	{
		return cardsDisplayPanel.getCards();
	}
	
	/**
	 * @see CardsDisplayPanel#setCards(Collection)
	 */
	public void setCards(Collection<? extends PosCard> value)
	{
		cardsDisplayPanel.setCards(value);
	}
	
	/**
	 * @see CardsDisplayPanel#setCardsAutoPos(Collection)
	 */
	public void setCardsAutoPos(Collection<? extends Card> cards)
	{
		cardsDisplayPanel.setCardsAutoPos(cards);
	}
}
