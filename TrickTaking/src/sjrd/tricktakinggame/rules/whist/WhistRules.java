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

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Règles du Whist (Whist à la couleur, ou Whist belge)
 * @author sjrd
 */
public class WhistRules extends AuctionRules<WhistAuction, WhistContract>
{
	/**
	 * Noms par défaut des cartes
	 */
	protected static final String[] defaultCardNames =
	    {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Valet", "Dame", "Roi",
		"As"};

	/**
	 * Noms de dessin des cartes
	 */
	protected static final String[] cardDrawIDs =
	    {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King",
		"Ace"};

	/**
	 * Crée les règles
	 */
	public WhistRules()
	{
		super(4, 4, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Deck makeDeck()
	{
		return new Deck(defaultCardNames, cardDrawIDs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected WhistAuction createAuction()
	{
		return new WhistAuction(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showMessageToAll(String message)
	{
		super.showMessageToAll(message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showErrorMessageToAll(String message)
	{
		super.showErrorMessageToAll(message);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepareGame() throws CardGameException
	{
		Game game = getGame();
		Player dealer = game.getDealer();

		// Distribuer les cartes
		game.getDeck().shuffle();
		game.deal(4, 5, 4);
		for (Player player: game.playersIterable())
			player.sortCards();

		// Le jeu commence avec le joueur suivant le serveur
		game.setActivePlayer(dealer.nextPlayer());
		
		// Afficher le serveur et celui qui parle en premier
		showMessageToAll(String.format("%s donne ; %s parle en premier",
			dealer.getName(), game.getActivePlayer().getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void makeScores() throws CardGameException
	{
		getContract().makeScores();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBestCard(Card newCard, Card oldCard, Suit leadSuit)
	{
		return super.isBestCard(newCard, oldCard, leadSuit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void filterMustFollowSuit(List<Card> cards, Suit suit)
	{
		super.filterMustFollowSuit(cards, suit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Card> makeValidLeadCards(Player player)
	{
		return super.makeValidLeadCards(player);
	}

	/**
	 * Construit une liste des cartes valides pour un joueur
	 * <p>
	 * Il y a deux critères successifs de validité :
	 * </p>
	 * <ol>
	 * <li>Le joueur a cette carte en main ;</li>
	 * <li>La carte suit la couleur, si le joueur a cette couleur en main.</li>
	 * </ol>
	 */
	@Override
	public List<Card> makeValidCards(Player player, Suit leadSuit,
	    Card winningCard, Player winningPlayer)
	{
		// Liste des cartes valides, d'abord celles que le joueur a en main
		List<Card> validCards = player.getCardsSnapshot();

		// Doit suivre la couleur de tête
		filterMustFollowSuit(validCards, leadSuit);

		// Les deux tests sont passés ; il reste toujours au moins une carte
		assert !validCards.isEmpty();

		return validCards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card playCard(Player player, Collection<Card> validCards,
		boolean hidden) throws CardGameException
	{
		return super.playCard(player, validCards, hidden);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Card playCard(Player player, Collection<Card> validCards)
		throws CardGameException
	{
		return super.playCard(player, validCards);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectCards(Player winningPlayer)
	{
		super.collectCards(winningPlayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminateTrick(Player winningPlayer)
		throws CardGameException
	{
		super.terminateTrick(winningPlayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playTrick() throws CardGameException
	{
		super.playTrick();
	}
	
	/**
	 * Demande à chaque joueur de se débarrasser d'une carte
	 * <p>
	 * Les cartes choisies ne sont pas visibles par les autres joueurs, et sont
	 * remises directement dans le paquet.
	 * </p>
	 */
	public void discardOneCard() throws CardGameException
	{
		Game game = getGame();
		Player player = game.getActivePlayer();
		
		// Choisir les cartes
		do
		{
			player.showMessage(MessageSource.Rules,
				"Sélectionnez la carte à retirer du jeu");
			playCard(player, player.getCardsSnapshot(), true);
			
			player = player.nextPlayer();
		} while (player != game.getActivePlayer());
		
		game.pauseAll();
		
		// Mettre les cartes dans le paquet
		do
		{
			game.getDeck().push(player.getPlayedCard());
			player.resetPlayedCard();
			
			player = player.nextPlayer();
		} while (player != game.getActivePlayer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void filterTeamCollectedCards(Player destPlayer, Team sourceTeam,
		List<Card> collectedCards)
	{
		filterAllCards(collectedCards);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void filterPlayerCards(Player destPlayer, Player sourcePlayer,
		List<Card> cards)
	{
		if (destPlayer == sourcePlayer)
			return;
		
		WhistContract contract = getContract();
		if ((contract != null) && contract.isPlayerHandVisible(sourcePlayer))
			return;
		
		filterAllCards(cards);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void filterPlayerCollectedCards(Player destPlayer,
		Player sourcePlayer, List<Card> collectedCards)
	{
		filterAllCards(collectedCards);
	}
	
	/**
	 * Joue tous plis restant de manière standard
	 * @see BasicRules#playTricks()
	 */
	public void playAllTricks() throws CardGameException
	{
		super.playTricks();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void playTricks() throws CardGameException
	{
		getContract().playTricks();
	}
}
