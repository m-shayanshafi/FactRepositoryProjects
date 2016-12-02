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
package sjrd.tricktakinggame.rules.rikiki;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Règles du Rikiki
 * @author sjrd
 */
public class RikikiRules extends AuctionRules<RikikiAuction, RikikiContract>
{
	/**
	 * Noms par défaut des cartes
	 */
	protected static final String[] defaultCardNames =
	    {"2", "3", "4", "5", "6", "7", "8", "9", "Valet", "Dame", "Roi", "As"};

	/**
	 * Noms de dessin des cartes
	 */
	protected static final String[] cardDrawIDs =
	    {"2", "3", "4", "5", "6", "7", "8", "9", "Jack", "Queen", "King",
		"Ace"};
	
	/**
	 * Ordre d'utilisation des atouts
	 */
	protected static final Suit[] trumpOrder =
		{Suit.Heart, Suit.Diamond, Suit.Club, Suit.Spade, Suit.None};

	/**
	 * Nombre de plis courant
	 */
	private int trickCount;
	
	/**
	 * Indique si le nombre de plis va croissant
	 */
	private boolean trickCountIncreasing;
	
	/**
	 * Crée les règles
	 * @param aPlayerCount Nombre de joueurs (doit être > 2)
	 */
	public RikikiRules(int aPlayerCount)
	{
		super(aPlayerCount, aPlayerCount, false);
		
		trickCount = 0;
		trickCountIncreasing = true;
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
	protected RikikiAuction createAuction()
	{
		return new RikikiAuction(this, getTrump(), trickCount);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void prepareGame() throws CardGameException
	{
		Game game = getGame();
		Player dealer = game.getDealer();
		Deck deck = game.getDeck();
		
		// Change le nombre de plis
		if (trickCountIncreasing)
		{
			trickCount++;
			if (trickCount * game.getPlayerCount() > deck.size())
			{
				trickCount -= 2;
				trickCountIncreasing = false;
			}
		}
		else
		{
			trickCount--;
			if (trickCount <= 0)
			{
				trickCount = 1;
				trickCountIncreasing = true;
			}
		}
		
		// Déterminer l'atout pour cette partie
		setTrump(trumpOrder[game.getPlayCount() % 5]);

		// Distribuer les cartes
		game.getDeck().shuffle();
		game.deal(trickCount);
		for (Player player: game.playersIterable())
			player.sortCards();

		// Le jeu commence avec le joueur suivant le serveur
		game.setActivePlayer(dealer.nextPlayer());
		
		// Afficher l'atout et le premier joueur
		String message;
		if (getTrump() == Suit.None)
			message = "Sans-atout";
		else
			message = "Atout : " + getTrump().getName();

		showMessageToAll(String.format("%s ; %s parle et joue en premier",
			message, game.getActivePlayer()));
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
	protected List<Card> makeValidCards(Player player, Suit leadSuit,
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
	 * Calcule les points
	 */
	@Override
	protected void makeScores() throws CardGameException
	{
		Game game = getGame();
		RikikiContract contract = getContract();
		
		for (Player player: game.playersIterable())
		{
			int playerTrickCount = player.getCollectedTrickCount();
			int contractTrickCount = contract.getPlayerTrickCount(player);
			boolean successful = (playerTrickCount == contractTrickCount);
			
			int points = playerTrickCount;
			if (successful)
				points += 10;
			
			player.getTeam().addToScore(points);
			
			String message = String.format(
				"%s avait annoncé %d pli(s) et en a fait %d, donc gagne %d " +
				"points", player.getName(), contractTrickCount,
				playerTrickCount, points);
			showMessageToAll(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void filterTeamCollectedCards(Player destPlayer, Team sourceTeam,
		List<Card> collectedCards)
	{
		filterAllCards(collectedCards);
	}

	/**
	 * {@inheritDoc}
	 */
	public void filterPlayerCards(Player destPlayer, Player sourcePlayer,
		List<Card> cards)
	{
		if (destPlayer != sourcePlayer)
			filterAllCards(cards);
	}

	/**
	 * {@inheritDoc}
	 */
	public void filterPlayerCollectedCards(Player destPlayer,
		Player sourcePlayer, List<Card> collectedCards)
	{
		filterAllCards(collectedCards);
	}
}
