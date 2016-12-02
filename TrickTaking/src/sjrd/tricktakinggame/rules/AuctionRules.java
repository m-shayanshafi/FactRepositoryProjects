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
package sjrd.tricktakinggame.rules;

import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Règles de base pour la majorité des jeux à plis avec enchères
 * <p>
 * En plus des fonctionnalités proposées par <tt>BasicRules</tt>, cette classe
 * prend en charge la gestion "administrative" des enchères. La partie active
 * reste implémentée <i>via</i> la méthode <tt>Auction.doAuction()</tt>.
 * </p>
 * <p>
 * Si vous n'avez pas besoin de la gestion des enchères, utilisez plutôt la
 * classe <tt>BasicRules</tt> comme classe de base.
 * </p>
 * @param <A> Type des enchères utilisées
 * @param <C> Type de contrat produit par ces enchères
 * @author sjrd
 * @see Auction
 * @see BasicRules
 */
public abstract class AuctionRules<A extends Auction<?, C>, C extends Contract>
	extends BasicRules
{
	/**
	 * Enchère en cours (<tt>null</tt> si pas d'enchère en cours)
	 */
	private A auction = null;
	
	/**
	 * Contrat en cours (<tt>null</tt> si pas de contrat en cours)
	 */
	private C contract = null;
	
	/**
	 * Crée les règles
	 * @param aPlayerCount Nombre de joueurs
	 * @param aTeamCount Nombre d'équipes
	 * @param aCollectByTeam <tt>true</tt> pour ramasser les cartes par équipe
	 */
	public AuctionRules(int aPlayerCount, int aTeamCount,
		boolean aCollectByTeam)
	{
		super(aPlayerCount, aTeamCount, aCollectByTeam);
	}
	
	/**
	 * Enchère en cours
	 * @return Enchère en cours (<tt>null</tt> si pas d'enchère en cours)
	 */
	synchronized public A getAuction()
	{
		return auction;
	}
	
	/**
	 * Modifie l'enchère en cours
	 * @param value Nouvelle enchère (peut être <tt>null</tt>)
	 */
	protected void setAuction(A value)
	{
		synchronized (this)
		{
			auction = value;
		}

		setCurrentAuction(value);
	}
	
	/**
	 * Contrat en cours
	 * @return Contrat en cours (<tt>null</tt> si pas de contrat en cours)
	 */
	synchronized public C getContract()
	{
		return contract;
	}
	
	/**
	 * Modifie le contrat en cours
	 * <p>
	 * Si le contrat est un contrat avec atout (c-à-d s'il est une instance de
	 * la classe <tt>ContractWithTrump</tt>, ceci modifie également l'atout des
	 * règles, <i>via</i> la méthode <tt>setTrump()</tt>.
	 * </p>
	 * @param value Nouveau contrat (peut être <tt>null</tt>)
	 * @see ContractWithTrump
	 * @see #setTrump(sjrd.tricktakinggame.cards.Suit)
	 */
	protected void setContract(C value)
	{
		synchronized (this)
		{
			contract = value;
		}

		setCurrentContract(value);
		if (value instanceof ContractWithTrump)
			setTrump(((ContractWithTrump) value).getTrump());
	}
	
	/**
	 * <p>
	 * Joue le jeu selon l'ordre habituel du déroulement d'un jeu à plis avec
	 * enchères. Ce qui, par rapport à un jeu sans enchères, ajoute la phase
	 * d'enchères entre la préparation du jeu et le jeu des plis.
	 * </p>
	 * <ol>
	 *   <li><tt>prepareGame()</tt> - prépare le jeu (distribution, etc.) ;</li>
	 *   <li><tt>doAuction()</tt> - réalise les enchères ;</li>
	 *   <li><tt>playTricks()</tt> - joue les plis ;</li>
	 *   <li><tt>makeScores()</tt> - calcule les scores ;</li>
	 *   <li><tt>finishGame()</tt> - termine le jeu (supprime le contract,
	 *   etc.) ;</li>
	 *   <li><tt>recollectCardsToDeck()</tt> - ramasse les cartes et les remet
	 *   dans le paquet.</li>
	 * </ol>
	 * <p>
	 * Après chaque appel, la méthode <tt>checkInterrupted()</tt> du jeu est
	 * appelée.
	 * </p>
	 * <p>
	 * Vous pouvez toujours surcharger <tt>playGame()</tt> si ce comportement
	 * usuel ne convient pas aux règles.
	 * </p>
	 * @see BasicRules#playGame()
	 * @see #prepareGame()
	 * @see #doAuction()
	 * @see #playTricks()
	 * @see #makeScores()
	 * @see #finishGame()
	 * @see #recollectCardsToDeck()
	 * @see Game#checkInterrupted()
	 */
	@Override
	public void playGame() throws CardGameException
	{
		Game game = getGame();

		prepareGame();
		game.checkInterrupted();

		doAuction();
		game.checkInterrupted();

		playTricks();
		game.checkInterrupted();

		makeScores();
		game.checkInterrupted();

		finishGame();
		game.checkInterrupted();

		recollectCardsToDeck();
		game.checkInterrupted();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void finishGame() throws CardGameException
	{
		setContract(null);
		getGame().setDealer(getGame().getDealer().nextPlayer());
	}
	
	/**
	 * Crée l'enchère
	 * @return Enchère créée
	 */
	protected abstract A createAuction();
	
	/**
	 * Réalise l'enchère
	 * <p>
	 * Cette opération passe par les étapes suivantes :
	 * </p>
	 * <ol>
	 *   <li>L'enchère est créée avec <tt>createAuction()</tt>, est alors mise
	 *   à jour l'enchère courante ;</li>
	 *   <li>La méthode <tt>doAuction()</tt> de l'enchère est appelée ;</li>
	 *   <li>Une pause est envoyée à tous les joueurs ;</li>
	 *   <li>L'enchère en cours est remise à <tt>null</tt> ;
	 *   <li>Le contrat renvoyé par <tt>doAuction()</tt> est enregistré comme
	 *   contrat en cours ;</li>
	 *   <li>Le même contrat est renvoyé en résultat de cette méthode.</li>
	 * </ol>
	 * @return Contrat déterminé par l'enchère
	 */
	protected C doAuction() throws CardGameException
	{
		A auction = createAuction();
		setAuction(auction);
		C contract;
		
		try
		{
			contract = auction.doAuction();
			getGame().pauseAll();
		}
		finally
		{
			setAuction(null);
		}
		
		setContract(contract);
		return contract;
	}
}
