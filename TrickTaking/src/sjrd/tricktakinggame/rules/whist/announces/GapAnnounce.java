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
package sjrd.tricktakinggame.rules.whist.announces;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.rules.whist.*;

import static sjrd.tricktakinggame.rules.whist.WhistSuitComparator.*;

/**
 * Annonce Trou (royal ou non)
 * @author sjrd
 */
public class GapAnnounce extends WhistAnnounce
{
	/**
	 * Indique si c'est un trou royal
	 */
	private boolean royal;
	
	/**
	 * Carte qui doit boucher le trou
	 */
	private Card stopgapCard;

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 * @param aRoyal <tt>true</tt> si c'est un trou royal
	 * @param aStopgapCard Carte qui doit boucher le trou
	 */
	public GapAnnounce(Player aPlayer, String aID, String aName, boolean aRoyal,
		Card aStopgapCard)
	{
		super(aPlayer, aID, aName);
		
		royal = aRoyal;
		stopgapCard = aStopgapCard;
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aStopgapCard Carte qui doit boucher le trou
	 */
	public GapAnnounce(Player aPlayer, Card aStopgapCard)
	{
		this(aPlayer, makeID(aStopgapCard), makeName(aStopgapCard),
			aStopgapCard.getForce() < 12, aStopgapCard);
	}
	
	/**
	 * Détermine la carte qui doit boucher le trou
	 * @param player Joueur qui fait l'annonce
	 * @return Carte qui doit boucher le trou (<tt>null</tt> si pas de trou)
	 */
	private static Card findStopgapCard(Player player)
	{
		Deck deck = player.getGame().getDeck();
		Suit[] orderedSuits = getOrderedSuits();

		List<Card> cards = player.getCardsSnapshot();
		
		Card missingAce = null;

		for (int i = 0; i < 4; i++)
		{
			Card ace = deck.findCardByProps(orderedSuits[i], 12);
			
			if (!cards.contains(ace))
			{
				if (missingAce != null)
					return null;
				missingAce = ace;
			}
		}
		
		if (missingAce != null)
			return missingAce;
		
		for (int force = 11; force >= 0; force--)
		{
			Card card = deck.findCardByProps(Suit.Heart, force);
			if (!cards.contains(card))
				return card;
		}
		
		assert false;
		return null;
	}
	
	/**
	 * Construit l'ID d'une annonce Trou
	 * @param stopgapCard Carte qui doit boucher le trou
	 * @return ID de l'annonce
	 */
	private static String makeID(Card stopgapCard)
	{
		if (stopgapCard.getForce() == 12)
			return "Gap";
		else
			return String.format("RoyalGap-%d", stopgapCard.getForce());
	}
	
	/**
	 * Construit le nom d'une annonce Trou
	 * @param stopgapCard Carte qui doit boucher le trou
	 * @return Nom de l'annonce
	 */
	private static String makeName(Card stopgapCard)
	{
		if (stopgapCard.getForce() == 12)
			return "Trou";
		else
			return String.format("Trou royal (bouché par %s)",
				stopgapCard.getLongName());
	}
	
	/**
	 * Teste s'il y a un trou
	 * @param auction Enchères
	 * @return Annonce de trou s'il y en a un, <tt>null</tt> sinon
	 */
	public static GapAnnounce checkForGap(Auction auction)
	{
		Game game = auction.getGame();
		
		for (Player player: game.playersIterable())
		{
			Card stopgapCard = findStopgapCard(player);
			if (stopgapCard != null)
				return new GapAnnounce(player, stopgapCard);
		}
		
		return null;
	}
	
	/**
	 * Indique si c'est un trou royal
	 * @return <tt>true</tt> si c'est un trou royal, <tt>false</tt> sinon
	 */
	public boolean isRoyal()
	{
		return royal;
	}
	
	/**
	 * Carte qui doit bouché le trou
	 * @return Carte qui doit bouché le trou
	 */
	public Card getStopgapCard()
	{
		return stopgapCard;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.Gap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
	    List<WhistAnnounce> announces)
	{
		/*
		 * On ne peut jamais arriver ici car cette annonce est directement
		 * remplacée par une StopgapAnnounce
		 */
		assert false;
	}
}
