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
package sjrd.tricktakinggame.rules.whist;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Contrat d'une partie de Whist
 * @author sjrd
 */
public abstract class WhistContract extends ContractWithTrump
{
	/**
	 * Jeu maître
	 */
	private Game game;
	
	/**
	 * Règles associées
	 */
	private WhistRules rules;
	
	/**
	 * Joueurs qui font le contrat
	 */
	private Player[] players;
	
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aTrump Atout
	 * @param aPlayers Joueurs qui font le contrat (1 ou 2)
	 */
	public WhistContract(WhistRules aRules, String aName, Suit aTrump,
		Player ... aPlayers)
	{
		super(aName, aTrump);
		
		assert (aPlayers.length == 1) || (aPlayers.length == 2);
		
		game = aRules.getGame();
		rules = aRules;
		players = aPlayers;
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
	 * Règles associées
	 * @return Règles associées
	 */
	public WhistRules getRules()
	{
		return rules;
	}
	
	/**
	 * Joueurs qui font le contrat
	 * @return Joueurs qui font le contrat
	 */
	public Player[] getPlayers()
	{
		return players.clone();
	}
	
	/**
	 * Joueurs qui font le contrat
	 * @param index Index d'un joueur
	 * @return Joueur qui fait le contrat à l'index spécifié
	 */
	public Player getPlayers(int index)
	{
		return players[index];
	}
	
	/**
	 * Joueur qui fait le contrat (il doit être seul)
	 * @return Joueur qui fait le contrat
	 */
	public Player getPlayer()
	{
		assert players.length == 1;
		return players[0];
	}
	
	/**
	 * Joue les plis
	 * <p>
	 * L'implémentation par défaut joue tous les plis de manière standard, au
	 * moyen de la méthode <tt>WhistRules.playAllTricks()</tt>.
	 * </p>
	 * @see WhistRules#playAllTricks()
	 */
	public void playTricks() throws CardGameException
	{
		getRules().playAllTricks();
	}
	
	/**
	 * Calcule les scores de cette donne
	 */
	public abstract void makeScores() throws CardGameException;
	
	/**
	 * Teste si la main d'un joueur donné doit être visible (sur table)
	 * @param player Joueur
	 * @return <tt>true</tt> si visible par tous, <tt>false</tt> sinon
	 */
	public boolean isPlayerHandVisible(Player player)
	{
		return false;
	}
}
