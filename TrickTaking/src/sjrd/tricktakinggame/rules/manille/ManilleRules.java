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
package sjrd.tricktakinggame.rules.manille;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.*;

/**
 * Règles de la Manille
 * @author sjrd
 */
public class ManilleRules extends AuctionRules<ManilleAuction, ManilleContract>
{
	/**
	 * Noms par défaut des cartes
	 */
	protected static final String[] defaultCardNames =
	    {"7", "8", "9", "Valet", "Dame", "Roi", "As", "Manille"};

	/**
	 * Noms de dessin des cartes
	 */
	protected static final String[] cardDrawIDs =
	    {"7", "8", "9", "Jack", "Queen", "King", "Ace", "10"};

	/**
	 * Points des cartes selon leur force
	 */
	protected static final int[] cardPoints = {0, 0, 0, 1, 2, 3, 4, 5};

	/**
	 * Crée un objet règles de Manille
	 */
	public ManilleRules()
	{
		super(4, 2, true);
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
	protected ManilleAuction createAuction()
	{
		return new ManilleAuction(this);
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
		game.deal(3, 2, 3);
		for (Player player: game.playersIterable())
			player.sortCards();

		// Le jeu commence avec le joueur suivant le serveur
		game.setActivePlayer(dealer.nextPlayer());
		
		// Afficher les serveur et premier joueur
		showMessageToAll(String.format("%s donne ; %s joue en premier",
			dealer.getName(), game.getActivePlayer().getName()));
	}

	/**
	 * Construit une liste des cartes valides pour un joueur
	 * <p>
	 * Il y a trois critères successifs de validité :
	 * </p>
	 * <ol>
	 * <li>Le joueur a cette carte en main ;</li>
	 * <li>La carte suit la couleur, si le joueur a cette couleur en main ;</li>
	 * <li>La carte gagne, si le joueur a au moins une carte qui gagne.</li>
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

		// Doit prendre le pli, si possible
		if (winningPlayer.getTeam() != player.getTeam())
			filterMustWin(validCards, winningCard, leadSuit);

		// Les trois tests sont passés ; il reste toujours au moins une carte
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
		assert game.getTeamCount() == 2;

		// Comptage des points
		int points[] = new int[2];
		for (int i = 0; i < 2; i++)
		{
			Team team = game.getTeams(i);

			points[i] = 0;
			for (Card card: team.collectedCards)
				points[i] += cardPoints[card.getForce()];

			String messageFormat;
			if (team.isNamePlural())
				messageFormat = "%s ont rammassé %d points.";
			else
				messageFormat = "%s a ramassé %d points.";
			showMessageToAll(String.format(messageFormat,
				team.getName(), points[i]));
		}

		assert points[0] + points[1] == 60;

		// Test d'égalité
		if (points[0] == points[1])
		{
			// Egalité
			showMessageToAll("Egalité");
			return;
		}

		// Equipe gagnante et points de base
		Team winningTeam =
		    (points[0] > points[1] ? game.getTeams(0) : game.getTeams(1));
		int baseScore = points[winningTeam.getIndex()] - 30;

		// Un capot fait 60
		if (baseScore == 30)
			baseScore = 60;

		// Afficher les points de base
		String messageFormat;
		if (winningTeam.isNamePlural())
			messageFormat = "%s gagnent %d";
		else
			messageFormat = "%s gagne %d";
		String message = String.format(messageFormat,
			winningTeam.getName(), baseScore);

		int score = baseScore;

		// Un jeu en sans-atout double la mise
		if (getTrump() == Suit.None)
		{
			message += " * 2 (sans-atout)";
			score *= 2;
		}

		// Un contre double la mise, et un surcontre aussi
		if (getContract().isCountered())
		{
			message += " * 2 (contré)";
			score *= 2;
		}
		if (getContract().isReCountered())
		{
			message += " * 2 (surcontré)";
			score *= 2;
		}

		// Donner les points à l'équipe
		winningTeam.addToScore(score);

		// Afficher le message
		if (score != baseScore)
			message += " = " + score;
		message += " points.";
		showMessageToAll(message);
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
