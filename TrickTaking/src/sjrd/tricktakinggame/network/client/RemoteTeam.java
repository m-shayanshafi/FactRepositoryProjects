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
 * Equipe distante
 * @author sjrd
 */
public class RemoteTeam extends BaseRemotableTeam<RemoteGame, RemotePlayer>
{
	/**
	 * Crée une nouvelle équipe
	 * @param aGame Jeu propriétaire
	 */
	public RemoteTeam(RemoteGame aGame)
	{
		super(aGame);

		getGame().addTeam(this);
	}

	/**
	 * Ajoute un joueur
	 * @param player Joueur à ajouter
	 */
	protected void addPlayer(RemotePlayer player)
	{
		super.addPlayer(player);
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
	 * {@inheritDoc}
	 */
	@Override
	public int getScore()
	{
		getGame().ensureUpdated();
		return super.getScore();
	}
	
	/**
	 * Met à jour les données globales du jeu
	 * @param score Score
	 * @param collectedCards Cartes ramassées
	 */
	synchronized void updateData(int score, List<Card> collectedCards)
	{
		setScore(score);

		lockCollectedCardsWrite();
		try
		{
			this.collectedCards.clear();
			this.collectedCards.addAll(collectedCards);
		}
		finally
		{
			unlockCollectedCardsWrite();
		}
	}
}
