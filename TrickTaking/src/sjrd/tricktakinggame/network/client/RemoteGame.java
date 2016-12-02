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

import java.io.*;
import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Jeu distant
 * @author sjrd
 */
public class RemoteGame extends BaseRemotableGame<RemoteTeam, RemotePlayer>
{
	/**
	 * Gestionnaire de mise à jour
	 */
	private Updater updater;
	
	/**
	 * Paquet de cartes
	 */
	private Deck deck;
	
	/**
	 * Verrou pour les champs <tt>mustUpdate</tt> et <tt>updating</tt>
	 */
	private final Object updateLock = new Object();
	
	/**
	 * Indique si le jeu doit être remis à jour
	 */
	private boolean mustUpdate;
	
	/**
	 * Indique si le jeu est en train d'être remis à jour
	 */
	private boolean updating;
	
	/**
	 * Crée un nouveau jeu
	 */
	public RemoteGame(Updater aUpdater, Deck aDeck,
		int teamCount, String[] playerNames, int[] playerTeams)
	{
		super();
		
		assert playerNames.length == playerTeams.length;
		
		updater = aUpdater;
		deck = aDeck;

		for (int i = 0; i < teamCount; i++)
			new RemoteTeam(this);

		for (int i = 0; i < playerNames.length; i++)
			new RemotePlayer(this, getTeams(playerTeams[i]), playerNames[i]);
		
		mustUpdate = true;
		updating = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int addTeam(RemoteTeam team)
	{
		return super.addTeam(team);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int addPlayer(RemotePlayer player)
	{
		return super.addPlayer(player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAuctioning()
	{
		ensureUpdated();
		return super.isAuctioning();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Announce> getAnnounces()
	{
		ensureUpdated();
		return super.getAnnounces();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCurrentContractName()
	{
		ensureUpdated();
		return super.getCurrentContractName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemotePlayer getDealer()
	{
		ensureUpdated();
		return super.getDealer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RemotePlayer getActivePlayer()
	{
		ensureUpdated();
		return super.getActivePlayer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPlayCount()
	{
		ensureUpdated();
		return super.getPlayCount();
	}
	
	/**
	 * Paquet de cartes
	 * @return Paquet de cartes
	 */
	public Deck getDeck()
	{
		return deck;
	}
	
	/**
	 * Indique au jeu qu'il doit se mettre à jour
	 */
	protected void update()
	{
		synchronized (updateLock)
		{
			mustUpdate = true;
		}
	}
	
	/**
	 * S'assure que le jeu est à jour
	 */
	public void ensureUpdated()
	{
		synchronized (updateLock)
		{
			if (updating)
			{
				try
				{
					updateLock.wait();
				}
				catch (InterruptedException ignore)
				{
				}

				return;
			}
			
			if (!mustUpdate)
				return;

			mustUpdate = false;
			updating = true;
		}

		try
		{
			boolean again;
			
			do
			{
				updater.updateGame(this);

				synchronized (updateLock)
				{
					again = mustUpdate;
					mustUpdate = false;
				}
			} while (again);
		}
		catch (IOException ignore)
		{
		}
		finally
		{
			synchronized (updateLock)
			{
				updating = false;
				updateLock.notifyAll();
			}
		}
	}
	
	/**
	 * Met à jour les données globales du jeu
	 * @param trump Atout
	 * @param dealer Serveur
	 * @param activePlayer Joueur actif
	 * @param playCount Nombre de parties jouées
	 */
	synchronized void updateData(List<? extends Announce> announces,
		String currentContractName, RemotePlayer dealer,
		RemotePlayer activePlayer, int playCount)
	{
		setAnnounces(announces);
		setCurrentContractName(currentContractName);
		setDealer(dealer);
		setActivePlayer(activePlayer);
		setPlayCount(playCount);
	}
	
	/**
	 * Gestionnaire de mise à jour d'un jeu à distance
	 * @author sjrd
	 */
	public interface Updater
	{
		/**
		 * Met à jour un jeu à distance
		 * @param game Jeu à mettre à jour
		 */
		public void updateGame(RemoteGame game) throws IOException;
	}
}
