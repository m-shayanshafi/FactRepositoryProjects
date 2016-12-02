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
package sjrd.tricktakinggame.remotable;

import java.util.*;
import java.util.concurrent.locks.*;

import sjrd.tricktakinggame.cards.*;

/**
 * Implémentation de base par défaut de <tt>RemotableTeam</tt>
 * @author sjrd
 */
public class BaseRemotableTeam<G extends RemotableGame,
	P extends RemotablePlayer> implements RemotableTeam
{
	/**
	 * Jeu propriétaire
	 */
	private G game = null;

	/**
	 * Index
	 */
	private int index;

	/**
	 * Joueurs dans cette équipe
	 */
	private final List<P> players = new ArrayList<P>();

	/**
	 * Itérable sur les joueurs remotables
	 */
	private final Iterable<RemotablePlayer> playersIterable =
	    new RemotableTeam.PlayersIterable(this);

	/**
	 * Cartes ramassées
	 */
	public final List<Card> collectedCards = new ArrayList<Card>();

	/**
	 * Verrou de type multi-read-exclusive-write sur les cartes ramassées
	 */
	private final ReadWriteLock collectedCardsLock =
	    new ReentrantReadWriteLock();

	/**
	 * Score de l'équipe
	 */
	private int score = 0;

	/**
	 * Crée une nouvelle équipe
	 * <p>
	 * Il appartient aux classes descendantes de référencer l'équipe auprès du
	 * jeu propriétaire.
	 * </p>
	 * @param aGame Jeu propriétaire
	 */
	public BaseRemotableTeam(G aGame)
	{
		super();

		game = aGame;
		index = game.getTeamCount();
	}

	/**
	 * Ajoute un joueur
	 * @param player Joueur à ajouter
	 */
	protected void addPlayer(P player)
	{
		players.add(player);
	}

	/**
	 * {@inheritDoc}
	 */
	public G getGame()
	{
		return game;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		int playerCount = getPlayerCount();
		
		String result;

		if (playerCount == 1)
			result = getPlayers(0).getName();
		else
		{
			result = getPlayers(playerCount-2).getName() +
				" et " + getPlayers(playerCount-1).getName();

			for (int i = playerCount-3; i >= 0; i--)
				result = getPlayers(i).getName() + ", " + result;
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isNamePlural()
	{
		return getPlayerCount() > 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPlayerCount()
	{
		return players.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public P getPlayers(int index)
	{
		return players.get(index);
	}

	/**
	 * Itérateur sur les joueurs
	 * @return Itérateur sur les joueurs
	 */
	public Iterator<P> playersIterator()
	{
		return players.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<RemotablePlayer> getPlayersIterator()
	{
		return new RemotableTeam.PlayersIterator(this);
	}

	/**
	 * Itérable sur les joueurs
	 * @return Itérable sur les joueurs
	 */
	public Iterable<P> playersIteratable()
	{
		return players;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<RemotablePlayer> getPlayersIterable()
	{
		return playersIterable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return getName();
	}
	
	/**
	 * Verrouille les cartes ramassées en lecture
	 */
	public void lockCollectedCardsRead()
	{
		collectedCardsLock.readLock().lock();
	}
	
	/**
	 * Déverrouille les cartes ramassées en lecture
	 */
	public void unlockCollectedCardsRead()
	{
		collectedCardsLock.readLock().unlock();
	}
	
	/**
	 * Verrouille les cartes ramassées en écriture
	 */
	public void lockCollectedCardsWrite()
	{
		collectedCardsLock.writeLock().lock();
	}
	
	/**
	 * Déverrouille les cartes ramassées en écriture
	 */
	public void unlockCollectedCardsWrite()
	{
		collectedCardsLock.writeLock().unlock();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Card> getCollectedCardsSnapshot()
	{
		lockCollectedCardsRead();
		try
		{
			return new ArrayList<Card>(collectedCards);
		}
		finally
		{
			unlockCollectedCardsRead();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int getCollectedTrickCount()
	{
		lockCollectedCardsRead();
		try
		{
			return collectedCards.size() / game.getPlayerCount();
		}
		finally
		{
			unlockCollectedCardsRead();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public int getScore()
	{
		return score;
	}

	/**
	 * Modifie le nombre de points de l'équipe
	 * @param value Nombre de points
	 */
	synchronized protected void setScore(int value)
	{
		score = value;
	}

	/**
	 * Ajoute des points à l'équipe
	 * @param value Nombre de points à ajouter (peut être négatif)
	 */
	synchronized protected void addToScore(int value)
	{
		score += value;
	}
}
