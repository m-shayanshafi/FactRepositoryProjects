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
package sjrd.tricktakinggame.game;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Règles abstraites
 * @author sjrd
 */
abstract public class Rules
{
	/**
	 * Nombre de joueurs
	 */
	private int playerCount;

	/**
	 * Nombre d'équipes
	 */
	private int teamCount;

	/**
	 * Jeu maître
	 */
	private Game game;

	/**
	 * Crée de nouvelles règles
	 * @param aPlayerCount Nombre de joueurs
	 * @param aTeamCount Nombre d'équipes
	 */
	public Rules(int aPlayerCount, int aTeamCount)
	{
		super();

		playerCount = aPlayerCount;
		teamCount = aTeamCount;
	}

	/**
	 * Construit le paquet de cartes
	 * <p>
	 * Les sous-classes de <tt>Rules</tt> doivent implémenter cette méthode
	 * pour indiquer comment le paquet de cartes doit être constitué.
	 * </p>
	 */
	public abstract Deck makeDeck();

	/**
	 * @see sjrd.cardgame.game.Rules#getPlayerCount()
	 */
	public int getPlayerCount()
	{
		return playerCount;
	}

	/**
	 * @see sjrd.cardgame.game.Rules#getTeamCount()
	 */
	public int getTeamCount()
	{
		return teamCount;
	}

	/**
	 * Répartit les joueurs dans les équipes en "tournant", avec un modulo
	 */
	public int teamOfPlayer(int playerPosition)
	{
		return playerPosition % teamCount;
	}

	/**
	 * Jeu maître
	 * @return Jeu maître
	 */
	public Game getGame()
	{
		return game;
	}

	/**
	 * Modifie le jeu maître
	 * @param newGame Nouveau jeu maître
	 */
	void setGame(Game newGame)
	{
		game = newGame;
	}
	
	/**
	 * Modifie l'enchère courante
	 * @param value Nouvelle enchère (peut être <tt>null</tt>)
	 * @see Game#setCurrentAuction(Auction)
	 */
	protected void setCurrentAuction(Auction<?, ?> value)
	{
		game.setCurrentAuction(value);
	}
	
	/**
	 * Modifie le contrat courant
	 * @param value Nouveau contrat (peut être <tt>null</tt>)
	 * @see Game#setCurrentContract(Contract)
	 */
	protected void setCurrentContract(Contract value)
	{
		game.setCurrentContract(value);
	}

	/**
	 * Joue une partie
	 */
	abstract public void playGame() throws CardGameException;
	
	/**
	 * Filtre toutes les cartes d'une liste en les remplaçant par <tt>null</tt>.
	 * @param cards Liste à filtrer
	 */
	protected static void filterAllCards(List<Card> cards)
	{
		for (int i = 0; i < cards.size(); i++)
			cards.set(i, null);
	}

	/**
	 * Filtre les cartes ramassées par une équipe
	 * <p>
	 * Avant d'envoyer à un joueur les informations sur les cartes ramassées par
	 * une équipe, cette liste passe par un appel à
	 * <tt>filterTeamCollectedCards</tt>.
	 * </p>
	 * <p>
	 * En entrée, le paramètre <tt>collectedCards</tt> est une copie de la liste
	 * des cartes ramassées par l'équipe <tt>sourceTeam</tt>. Cette méthode peut
	 * modifier cette liste comme elle l'entend.
	 * </p>
	 * <p>
	 * L'implémentation par défaut dans <tt>Rules</tt> ne fait rien, et ne
	 * filtre donc rien.
	 * </p>
	 * @param destPlayer Joueur qui recevra les informations
	 * @param sourceTeam Equipe dont traiter les cartes ramassées
	 * @param collectedCards Cartes ramassées par l'équipe
	 */
	public void filterTeamCollectedCards(Player destPlayer, Team sourceTeam,
		List<Card> collectedCards)
	{
	}

	/**
	 * Filtre les cartes dans la main d'un joueur
	 * <p>
	 * Avant d'envoyer à un joueur les informations sur les cartes dans la main
	 * d'un joueur, cette liste passe par un appel à <tt>filterPlayerCards</tt>.
	 * Ceci est aussi valable pour les cartes la main du joueur à qui seront
	 * envoyées les données.
	 * </p>
	 * <p>
	 * En entrée, le paramètre <tt>cards</tt> est une copie de la liste des
	 * cartes dans la main du joueur <tt>sourcePlayer</tt>. Cette méthode peut
	 * modifier cette liste comme elle l'entend.
	 * </p>
	 * <p>
	 * L'implémentation par défaut dans <tt>Rules</tt> ne fait rien, et ne
	 * filtre donc rien.
	 * </p>
	 * @param destPlayer Joueur qui recevra les informations
	 * @param sourcePlayer Joueur dont traiter les cartes en main
	 * @param cards Cartes dans la main du joueur
	 */
	public void filterPlayerCards(Player destPlayer, Player sourcePlayer,
		List<Card> cards)
	{
	}

	/**
	 * Filtre les cartes ramassées par un joueur
	 * <p>
	 * Avant d'envoyer à un joueur les informations sur les cartes ramassées par
	 * un joueur, cette liste passe par un appel à
	 * <tt>filterPlayerCollectedCards</tt>. Ceci est aussi valable pour les
	 * cartes ramassées par le joueur à qui seront envoyées les données.
	 * </p>
	 * <p>
	 * En entrée, le paramètre <tt>collectedCards</tt> est une copie de la liste
	 * des cartes ramassées par le joueur <tt>sourcePlayer</tt>. Cette méthode
	 * peut modifier cette liste comme elle l'entend.
	 * </p>
	 * <p>
	 * L'implémentation par défaut dans <tt>Rules</tt> ne fait rien, et ne
	 * filtre donc rien.
	 * </p>
	 * @param destPlayer Joueur qui recevra les informations
	 * @param sourcePlayer Joueur dont traiter les cartes ramassées
	 * @param collectedCards Cartes ramassées par le joueur
	 */
	public void filterPlayerCollectedCards(Player destPlayer,
		Player sourcePlayer, List<Card> collectedCards)
	{
	}
}
