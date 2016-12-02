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

/**
 * Implémentation de base par défaut de <tt>RemotableGame</tt>
 * @author sjrd
 */
public class BaseRemotableGame<T extends RemotableTeam,
	P extends RemotablePlayer> implements RemotableGame
{
	/**
	 * Equipes
	 */
	private final List<T> teams = new ArrayList<T>();

	/**
	 * Itérable sur les équipes remotables
	 */
	private final Iterable<RemotableTeam> teamsIterable =
	    new RemotableGame.TeamsIterable(this);

	/**
	 * Joueurs
	 */
	private final List<P> players = new ArrayList<P>();

	/**
	 * Itérable sur les joueurs remotables
	 */
	private final Iterable<RemotablePlayer> playersIterable =
	    new RemotableGame.PlayersIterable(this);
	
	/**
	 * Liste des annonces (<tt>null</tt> si pas d'enchère en cours)
	 */
	private List<Announce> announces = null;

	/**
	 * Nom du contrat courant
	 */
	private String currentContractName = null;

	/**
	 * Serveur
	 */
	private P dealer = null;

	/**
	 * Joueur actif, qui a la main
	 */
	private P activePlayer = null;

	/**
	 * Nombre de parties jouées
	 */
	private int playCount = 0;

	/**
	 * Ajoute une équipe
	 * @param team Equipe à ajouter
	 * @return Index de l'équipe
	 */
	protected int addTeam(T team)
	{
		teams.add(team);
		return teams.size() - 1;
	}

	/**
	 * Ajoute un joueur
	 * @param player Joueur à ajouter
	 * @return Position du joueur
	 */
	protected int addPlayer(P player)
	{
		players.add(player);
		return players.size() - 1;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getTeamCount()
	{
		return teams.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public T getTeams(int index)
	{
		return teams.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	public T getCyclicTeams(int index)
	{
		return getTeams(index % teams.size());
	}

	/**
	 * Itérateur sur les équipes
	 * @return Itérateur sur les équipes
	 */
	public Iterator<T> teamsIterator()
	{
		return teams.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator<RemotableTeam> getTeamsIterator()
	{
		return new RemotableGame.TeamsIterator(this);
	}

	/**
	 * Itérable sur les équipes
	 * @return Itérable sur les équipes
	 */
	public Iterable<T> teamsIteratable()
	{
		return teams;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterable<RemotableTeam> getTeamsIterable()
	{
		return teamsIterable;
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
	 * {@inheritDoc}
	 */
	public P getCyclicPlayers(int index)
	{
		return getPlayers(index % players.size());
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
		return new RemotableGame.PlayersIterator(this);
	}

	/**
	 * Itérable sur les joueurs
	 * @return Itérable sur les joueurs
	 */
	public Iterable<P> playersIterable()
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
	synchronized public boolean isAuctioning()
	{
		return announces != null;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public List<Announce> getAnnounces()
	{
		if (announces == null)
			return null;
		else
			return new ArrayList<Announce>(announces);
	}
	
	/**
	 * Modifie les annonces faites
	 * @param value Annonces faites (<tt>null</tt> pour pas d'enchère)
	 */
	synchronized protected void setAnnounces(List<? extends Announce> value)
	{
		if (value == null)
			announces = null;
		else
			announces = new ArrayList<Announce>(value);
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public String getCurrentContractName()
	{
		return currentContractName;
	}
	
	/**
	 * Modifie le nom du contrat courant
	 * @param value Nom du nouveau contrat (<tt>null</tt> pour pas de contrat)
	 */
	synchronized protected void setCurrentContractName(String value)
	{
		currentContractName = value;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public P getDealer()
	{
		return dealer;
	}

	/**
	 * Modifie le serveur
	 * @param value Nouveau serveur
	 */
	synchronized protected void setDealer(P value)
	{
		dealer = value;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public P getActivePlayer()
	{
		return activePlayer;
	}

	/**
	 * Modifie le joueur actif
	 * @param newActivePlayer Nouveau joueur actif
	 */
	synchronized protected void setActivePlayer(P value)
	{
		activePlayer = value;
	}

	/**
	 * {@inheritDoc}
	 */
	synchronized public int getPlayCount()
	{
		return playCount;
	}
	
	/**
	 * Modifie le nombre de parties jouées
	 * @param value Nouveau nombre de parties jouées
	 */
	synchronized protected void setPlayCount(int value)
	{
		playCount = value;
	}
	
	/**
	 * Incrémente le nombre de parties jouées
	 */
	synchronized protected void incPlayCount()
	{
		playCount++;
	}
}
