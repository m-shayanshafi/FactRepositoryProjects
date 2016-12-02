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

import java.util.*;

import sjrd.tricktakinggame.cards.*;

/**
 * Association d'une carte et d'une position entière
 * @author sjrd
 */
public class PosCard
{
	/**
	 * Position
	 */
	private int position;
	
	/**
	 * Carte
	 */
	private Card card;
	
	/**
	 * Crée l'association carte/position
	 * @param aPosition Position
	 * @param aCard Carte
	 */
	public PosCard(int aPosition, Card aCard)
	{
		super();
		
		position = aPosition;
		card = aCard;
	}
	
	/**
	 * Position
	 * @return Position
	 */
	public int getPosition()
	{
		return position;
	}
	
	/**
	 * Carte
	 * @return Carte
	 */
	public Card getCard()
	{
		return card;
	}
	
	/**
	 * Construit une collection de cartes en cartes avec positions
	 * <p>
	 * Les positions associées aux cartes sont les index de celles-ci dans la
	 * collection, dans l'ordre renvoyé par l'itérateur associé.
	 * </p>
	 * @param cards Collection de cartes source
	 * @return Collection des cartes associées à des positions
	 */
	public static Collection<PosCard> cardCollectionToPosCardCollection(
		Collection<? extends Card> cards)
	{
		Collection<PosCard> result = new ArrayList<PosCard>(cards.size());
		
		int position = 0;
		for (Card card: cards)
			result.add(new PosCard(position++, card));
		
		return result;
	}
}
