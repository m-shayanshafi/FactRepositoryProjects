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
package sjrd.tricktakinggame.network.client;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Joueur distant
 * @author sjrd
 */
public class RemotePlayer extends BaseRemotablePlayer<RemoteGame, RemoteTeam>
{
	/**
	 * Crée un nouveau joueur
	 * @param aGame Jeu propriétaire
	 * @param aTeam Equipe du joueur
	 * @param aName Nom du joueur
	 */
	public RemotePlayer(RemoteGame aGame, RemoteTeam aTeam, String aName)
	{
		super(aGame, aTeam, aName);

		getGame().addPlayer(this);
		getTeam().addPlayer(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemotePlayer nextPlayer(int count)
	{
		return (RemotePlayer) super.nextPlayer(count);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemotePlayer nextPlayer()
	{
		return nextPlayer(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCardCount()
	{
		getGame().ensureUpdated();
		return super.getCardCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card getCards(int index)
	{
		getGame().ensureUpdated();
		return super.getCards(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Card> getCardsSnapshot()
	{
		getGame().ensureUpdated();
		return super.getCardsSnapshot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card getPlayedCard()
	{
		getGame().ensureUpdated();
		return super.getPlayedCard();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Card> getCollectedCardsSnapshot()
	{
		getGame().ensureUpdated();
		return super.getCollectedCardsSnapshot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCollectedTrickCount()
	{
		getGame().ensureUpdated();
		return super.getCollectedTrickCount();
	}
	
	/**
	 * Met à jour les données globales du jeu
	 * @param score Score
	 * @param collectedCards Cartes ramassées
	 */
	synchronized void updateData(List<Card> newCards, boolean hasPlayedCard,
		Card playedCard, boolean isPlayedCardHidden,
		List<Card> newCollectedCards)
	{
		lockCardsWrite();
		try
		{
			cards.clear();
			cards.addAll(newCards);
		}
		finally
		{
			unlockCardsWrite();
		}
		
		if (hasPlayedCard)
			setPlayedCard(playedCard, isPlayedCardHidden);
		else
			resetPlayedCard();

		lockCollectedCardsWrite();
		try
		{
			collectedCards.clear();
			collectedCards.addAll(newCollectedCards);
		}
		finally
		{
			unlockCollectedCardsWrite();
		}
	}
}
