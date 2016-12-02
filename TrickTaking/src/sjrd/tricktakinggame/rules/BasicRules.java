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

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Règles de base pour la majorité des jeux à plis, sans gestion d'annonces
 * <p>
 * Cette classe peut être utilisée comme classe de base pour la majorité des
 * règles de jeux de cartes à plis. Elle fournit de nombreuses méthodes de
 * facilité à l'implémentation de la gestion de règles.
 * </p>
 * <p>
 * En particulier, elle propose une implémentation complète du déroulement du
 * jeu des plis.
 * </p>
 * <p>
 * Le déroulement du jeu est découpé en de nombreuses sous-méthodes, que vous
 * pouvez surcharger indépendamment pour les adapter à vos règles.
 * </p>
 * <p>
 * Si vous avez besoin de gérer des enchères en plus, utilisez plutôt la classe
 * <tt>AuctionRules</tt>, qui hérite elle-même de <tt>BasicRules</tt>, et
 * propose des facilités de gestion des annonces.
 * </p>
 * @author sjrd
 * @see AuctionRules
 */
public abstract class BasicRules extends Rules
{
	/**
	 * Si <tt>true</tt>, les cartes sont ramassées par l'équipe ; si
	 * <tt>false</tt>, elles sont ramassées par joueur.
	 */
	private boolean collectByTeam;
	
	/**
	 * Atout
	 */
	private Suit trump = null;

	/**
	 * Crée les règles
	 * @param aPlayerCount Nombre de joueurs
	 * @param aTeamCount Nombre d'équipes
	 * @param aCollectByTeam <tt>true</tt> pour ramasser les cartes par équipe
	 */
	public BasicRules(int aPlayerCount, int aTeamCount, boolean aCollectByTeam)
	{
		super(aPlayerCount, aTeamCount);

		collectByTeam = aCollectByTeam;
	}
	
	/**
	 * Atout
	 * @return Atout
	 */
	synchronized protected Suit getTrump()
	{
		return trump;
	}
	
	/**
	 * Modifie l'atout
	 * @param value Nouvel atout
	 */
	synchronized protected void setTrump(Suit value)
	{
		trump = value;
	}
	
	/**
	 * <p>
	 * Joue le jeu selon l'ordre habituel du déroulement d'un jeu à plis. Cette
	 * méthode appelle successivement les méthodes suivantes :
	 * </p>
	 * <ol>
	 *   <li><tt>prepareGame()</tt> - prépare le jeu (distribution, etc.) ;</li>
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
	 * @see #prepareGame()
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
	 * Affiche un message à tous les joueurs
	 * @param message Contenu du message
	 */
	protected void showMessageToAll(String message)
	{
		getGame().showMessageToAll(MessageSource.Rules, message);
	}
	
	/**
	 * Affiche un message d'erreur à tous les joueurs
	 * @param message Contenu du message
	 */
	protected void showErrorMessageToAll(String message)
	{
		getGame().showMessageToAll(MessageSource.RulesError, message);
	}
	
	/**
	 * Prépare une partie
	 * <p>
	 * L'implémentation dans <tt>BasicRules</tt> ne fait rien.
	 * </p>
	 * <p>
	 * Cette méthode devrait être surchargée pour préparer un jeu, entre autres
	 * pour distribuer les cartes, et renseigner le joueur actif.
	 * </p>
	 */
	protected void prepareGame() throws CardGameException
	{
	}
	
	/**
	 * Calcule les scores
	 * <p>
	 * L'implémentation dans <tt>BasicRules</tt> ne fait rien.
	 * </p>
	 * <p>
	 * Cette méthode devrait être surchargée pour calculer les scores, du moins
	 * si ces règles calculent bien les scores en fin de partie.
	 * </p>
	 */
	protected void makeScores() throws CardGameException
	{
	}
	
	/**
	 * Termine la partie
	 * <p>
	 * L'implémentation dans <tt>BasicRules</tt> supprime le contrat actuel, et
	 * remplace le serveur par le joueur qui le suit, dans l'ordre des
	 * positions.
	 * </p>
	 * <p>
	 * Vous pouvez surcharger cette méthode pour terminer différemment le jeu,
	 * ou pour annuler ce comportement par défaut.
	 * </p>
	 */
	protected void finishGame() throws CardGameException
	{
		setCurrentContract(null);
		getGame().setDealer(getGame().getDealer().nextPlayer());
	}
	
	/**
	 * Remet les cartes ramassées par les joueurs et les équipes dans le paquet
	 * <p>
	 * Cette méthode est appelée en dernier dans une partie classique.
	 * </p>
	 * <p>
	 * L'implémentation dans <tt>BasicRules</tt> reprend les cartes des équipes,
	 * dans l'ordre de leurs index, puis celles des joueurs, dans l'ordre de
	 * leurs positions.
	 * </p>
	 * <p>
	 * Enfin, elle ramasse les cartes que les joueurs auraient encore en main.
	 * </p>
	 * <p>
	 * Vous pouvez surcharger cette méthode pour ramasser différemment les
	 * cartes, ou pour annuler ce comportement par défaut.
	 * </p>
	 */
	protected void recollectCardsToDeck() throws CardGameException
	{
		Game game = getGame();
		Deck deck = game.getDeck();
		
		// Ramasser les cartes des équipes
		
		for (Team team: game.teamsIteratable())
		{
			team.lockCollectedCardsWrite();
			try
			{
				deck.addAll(team.collectedCards);
				team.collectedCards.clear();
			}
			finally
			{
				team.unlockCollectedCardsWrite();
			}
		}
		
		// Ramasser les cartes des joueurs
		
		for (Player player: game.playersIterable())
		{
			player.lockCollectedCardsWrite();
			try
			{
				deck.addAll(player.collectedCards);
				player.collectedCards.clear();
			}
			finally
			{
				player.unlockCollectedCardsWrite();
			}
		}
		
		// Ramasser les cartes dans les mains des joueurs
		
		for (Player player: game.playersIterable())
		{
			player.lockCardsWrite();
			try
			{
				deck.addAll(player.cards);
				player.cards.clear();
			}
			finally
			{
				player.unlockCardsWrite();
			}
		}
	}

	/**
	 * Teste si une nouvelle carte est meilleure qu'une ancienne
	 * @param newCard Nouvelle carte
	 * @param oldCard Ancienne carte
	 * @param leadSuit Couleur de tête
	 * @return <tt>true</tt> si <tt>newCard</tt> est meilleure que
	 *         <tt>oldCard</tt>, <tt>false</tt> sinon
	 */
	protected boolean isBestCard(Card newCard, Card oldCard, Suit leadSuit)
	{
		Suit trump = getTrump();

		if (newCard.getSuit() == trump)
		{
			return (oldCard.getSuit() != trump)
			    || (oldCard.getForce() < newCard.getForce());
		}
		else if (newCard.getSuit() == leadSuit)
		{
			return (oldCard.getSuit() != trump)
			    && (oldCard.getForce() < newCard.getForce());
		}
		else
			return false;
	}
	
	/**
	 * Filtre des cartes valides selon la règle "Doit suivre la couleur"
	 * <p>
	 * Si les cartes de la liste <tt>cards</tt> contiennent au moins une carte
	 * dont la couleur est <tt>suit</tt>, toutes celles qui n'ont pas cette
	 * propriété sont supprimées. 
	 * </p>
	 * @param cards Liste des cartes à filtrer
	 * @param suit Couleur à suivre
	 */
	protected void filterMustFollowSuit(List<Card> cards, Suit suit)
	{
		boolean canFollowSuit = false;

		for (Card card: cards)
		{
			if (card.getSuit() == suit)
			{
				canFollowSuit = true;
				break;
			}
		}

		if (canFollowSuit)
		{
			for (int i = cards.size() - 1; i >= 0; i--)
				if (cards.get(i).getSuit() != suit)
					cards.remove(i);
		}
	}
	
	/**
	 * Filtre des cartes valides selon la règle "Doit prendre le pli"
	 * <p>
	 * Si les cartes de la liste <tt>cards</tt> contiennent au moins une carte
	 * qui peut prendre le pli, toutes celles qui n'ont pas cette propriété sont
	 * supprimées. 
	 * </p>
	 * <p>
	 * Pour déterminer si une carte peut prendre le pli, la méthode
	 * <tt>isBestCard(Card, Card, Suit)</tt> est utilisée.
	 * </p>
	 * @param cards Liste des cartes à filtrer
	 * @param winningCard Carte qui gagne jusque là
	 * @param leadSuit Couleur de tête
	 * @see #isBestCard(Card, Card, Suit)
	 */
	protected void filterMustWin(List<Card> cards, Card winningCard,
		Suit leadSuit)
	{
		boolean canWin = false;

		for (Card card: cards)
		{
			if (isBestCard(card, winningCard, leadSuit))
			{
				canWin = true;
				break;
			}
		}

		if (canWin)
		{
			for (int i = cards.size() - 1; i >= 0; i--)
				if (!isBestCard(cards.get(i), winningCard, leadSuit))
					cards.remove(i);
		}
	}

	/**
	 * Construit un ensemble des cartes valides pour un joueur en tête
	 * <p>
	 * Cette implémentation définit que le joueur peut jouer n'importe quelle
	 * carte qu'il a en main.
	 * </p>
	 * @param player Joueur qui joue
	 * @return Ensemble des cartes valides
	 */
	protected Collection<Card> makeValidLeadCards(Player player)
	{
		return player.getCardsSnapshot();
	}

	/**
	 * Construit un ensemble des cartes valides pour un joueur
	 * <p>
	 * Cette implémentation définit que le joueur peut jouer n'importe quelle
	 * carte qu'il a en main.
	 * </p>
	 * @param player Joueur qui joue
	 * @param leadSuit Couleur de tête
	 * @param winningCard Carte qui gagne jusqu'ici
	 * @param winningPlayer Joueur qui gagne jusqu'ici
	 * @return Ensemble des cartes valides
	 */
	protected Collection<Card> makeValidCards(Player player, Suit leadSuit,
	    Card winningCard, Player winningPlayer)
	{
		return player.getCardsSnapshot();
	}

	/**
	 * Fait jouer une carte à un joueur
	 * @param player Joueur
	 * @param validCards Ensemble des cartes valides à jouer
	 * @param hidden <tt>true</tt> pour jouer la carte cachée
	 * @return Carte jouée
	 */
	protected Card playCard(Player player, Collection<Card> validCards,
		boolean hidden) throws CardGameException
	{
		final String invalidCard = "Carte invalide";

		Card result = player.playTurn();

		while (!validCards.contains(result))
		{
			player.showMessage(MessageSource.RulesError, invalidCard);
			result = player.playTurn();
		}

		player.discard(result);
		player.setPlayedCard(result, hidden);
		return result;
	}

	/**
	 * Fait jouer une carte visible à un joueur
	 * @param player Joueur
	 * @param validCards Ensemble des cartes valides à jouer
	 * @return Carte jouée
	 */
	protected Card playCard(Player player, Collection<Card> validCards)
		throws CardGameException
	{
		return playCard(player, validCards, false);
	}
	
	/**
	 * Ramasse les cartes jouées
	 * <p>
	 * Selon que la propriété <tt>collectByTeam</tt> (renseignée <i>via</i> le
	 * constructeur) vaut <tt>true</tt> ou <tt>false</tt>, les cartes jouées par
	 * les joueurs seront ajoutées à la collection <tt>collectedCards</tt> de
	 * l'équipe du joueur gagnant ou du joueur gagnant directement,
	 * respectivement. 
	 * </p>
	 * <p>
	 * Dans tous les cas, les cartes jouées par les joueurs seront
	 * réinitialisées (supprimées) ensuite.
	 * </p>
	 * @param winningPlayer Joueur qui a gagné le pli
	 */
	protected void collectCards(Player winningPlayer)
	{
		Game game = getGame();
		List<Card> collectedCards;
		Team winningTeam = winningPlayer.getTeam();

		if (collectByTeam)
		{
			collectedCards = winningTeam.collectedCards;
			winningTeam.lockCollectedCardsWrite();
		}
		else
		{
			collectedCards = winningPlayer.collectedCards;
			winningPlayer.lockCollectedCardsWrite();
		}

		try
		{
			for (Player player: game.playersIterable())
			{
				collectedCards.add(player.getPlayedCard());
				player.resetPlayedCard();
			}
		}
		finally
		{
			if (collectByTeam)
				winningTeam.unlockCollectedCardsWrite();
			else
				winningPlayer.unlockCollectedCardsWrite();
		}
	}

	/**
	 * Achève le pli
	 * <p>
	 * Cette implémentation affiche un message indiquant qui a remporté le pli.
	 * Ensuite, elle indique à tous les joueurs de faire une pause, avant de
	 * ramasser les cartes, <i>via</i> la méthode <tt>collectCards(Player)</tt>.
	 * </p>
	 * @param winningPlayer Joueur qui a gagné le pli
	 * @see #collectCards(Player)
	 */
	protected void terminateTrick(Player winningPlayer)
		throws CardGameException
	{
		Game game = getGame();

		showMessageToAll(winningPlayer.getName() + " remporte le pli");
		game.pauseAll();

		collectCards(winningPlayer);
	}
	
	/**
	 * Joue un pli
	 * <p>
	 * Dans un pli, le joueur actif commence à jouer. Les cartes qu'il peut
	 * jouer sont déterminées par la méthode <tt>makeValidLeadCards</tt>.
	 * </p>
	 * <p>
	 * Ensuite, les autres joueurs jouent à leur tour, dans un sens croissant
	 * des positions (cycliques). Les cartes qu'ils peuvent jouer sont
	 * déterminées par la méthode <tt>makeValidCards</tt>.
	 * </p>
	 * <p>
	 * Lorsque tous les joueurs ont joué, la méthode <tt>terminateTrick</tt>
	 * est appelée, avant de préparer la situation pour le tour suivant.
	 * </p>
	 */
	protected void playTrick() throws CardGameException
	{
		Game game = getGame();
		Player player = game.getActivePlayer();

		game.checkInterrupted();

		// Carte de tête
		Collection<Card> validCards = makeValidLeadCards(player);
		Card leadCard = playCard(player, validCards);

		// Carte gagnante et joueur gagnant
		Card winningCard = leadCard;
		Player winningPlayer = player;

		// Les autres joueurs jouent
		for (int i = 1; i < game.getPlayerCount(); i++)
		{
			player = player.nextPlayer();
			validCards =
				makeValidCards(player, leadCard.getSuit(), winningCard,
					winningPlayer);
			Card card = playCard(player, validCards);

			game.checkInterrupted();

			// Mettre à jour la carte gagnante et le joueur gagnant
			if (isBestCard(card, winningCard, leadCard.getSuit()))
			{
				winningCard = card;
				winningPlayer = player;
			}
		}

		// Terminer le pli
		terminateTrick(winningPlayer);
		game.setActivePlayer(winningPlayer);
	}

	/**
	 * Joue les plis selon la forme habituelle des jeux à plis
	 * <p>
	 * Les plis sont joués avec la méthode <tt>playTrick()</tt>, jusqu'à ce que
	 * le joueur actif n'ait plus de cartes en main.
	 * </p>
	 */
	protected void playTricks() throws CardGameException
	{
		Game game = getGame();

		while (game.getActivePlayer().getCardCount() > 0)
			playTrick();
	}
}
