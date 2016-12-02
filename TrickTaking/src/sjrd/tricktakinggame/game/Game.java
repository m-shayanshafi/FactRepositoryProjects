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

import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.cards.*;

/**
 * Jeu de cartes
 * @author sjrd
 */
public class Game extends BaseRemotableGame<Team, Player>
{
	/**
	 * Règles de ce jeu de cartes
	 */
	private Rules rules;

	/**
	 * Paquet de cartes
	 */
	private Deck deck;

	/**
	 * Verrou pour l'appel à <tt>playGame()</tt>
	 * @see Game#playGame()
	 */
	private final Object playGameLock = new Object();
	
	/**
	 * Enchère en cours
	 */
	private Auction<?, ?> currentAuction = null;
	
	/**
	 * Contrat en cours
	 */
	private Contract currentContract = null;

	/**
	 * Crée un nouveau jeu
	 * <p>
	 * <tt>playerNames</tt> doit avoir un nombre d'éléments égal au nombre de
	 * joueurs qu'indiquent les règles du jeu.
	 * </p>
	 * @param aRules Règles du jeu
	 * @param playerNames Noms des joueurs
	 */
	public Game(Rules aRules, PlayerControllerCreator controllerCreator,
	    String[] playerNames)
	{
		super();

		rules = aRules;
		rules.setGame(this);

		deck = rules.makeDeck();

		assert rules.getPlayerCount() == playerNames.length;

		for (int i = 0; i < rules.getTeamCount(); i++)
			new Team(this);

		for (int i = 0; i < rules.getPlayerCount(); i++)
			new Player(this, getTeams(rules.teamOfPlayer(i)), playerNames[i],
			    controllerCreator);

		super.setDealer(getPlayers((new Random()).nextInt(getPlayerCount())));
		super.setActivePlayer(getDealer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int addTeam(Team team)
	{
		return super.addTeam(team);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int addPlayer(Player player)
	{
		return super.addPlayer(player);
	}
	
	/**
	 * Règles du jeu
	 * @return Règles du jeu
	 */
	public Rules getRules()
	{
		return rules;
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
	 * Teste si le jeu de cartes a été interrompu
	 * @throws CardGameInterruptedException Le jeu de cartes a été iterrompu
	 */
	public void checkInterrupted() throws CardGameInterruptedException
	{
		if (Thread.interrupted())
			throw new CardGameInterruptedException();
	}
	
	/**
	 * Enchère courante
	 * @return Enchère courante (peut être <tt>null</tt>)
	 */
	synchronized public Auction<?, ?> getCurrentAuction()
	{
		return currentAuction;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAuctioning()
	{
		return getCurrentAuction() != null;
	}
	
	/**
	 * Modifie l'enchère courante
	 * @param value Nouvelle enchère (peut être <tt>null</tt>)
	 */
	protected void setCurrentAuction(Auction<?, ?> value)
	{
		synchronized (this)
		{
			currentAuction = value;
		}
		
		notifyUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Announce> getAnnounces()
	{
		Auction<?, ?> auction = getCurrentAuction();
		
		if (auction == null)
			return null;
		else
			return new ArrayList<Announce>(auction.getAnnouncesSnapshot());
	}
	
	/**
	 * Contrat courant
	 * <p>
	 * Un résultat valant <tt>null</tt> signifie qu'aucun contrat n'est en
	 * cours. Trois situations exemples dans lesquelles ceci peut arriver sont :
	 * </p>
	 * <ul>
	 *   <li>Aucun jeu n'est en cours ;</li>
	 *   <li>L'enchère est en cours, et il n'y a donc pas encore de
	 *   contrat ;</li>
	 *   <li>Ces règles n'ont tout simplement jamais de contrat.</li>
	 * </ul>
	 * @return Contrat courant (peut être <tt>null</tt>)
	 */
	synchronized public Contract getCurrentContract()
	{
		return currentContract;
	}
	
	/**
	 * Modifie le contrat courant
	 * @param value Nouveau contrat (peut être <tt>null</tt>)
	 */
	protected void setCurrentContract(Contract value)
	{
		synchronized (this)
		{
			currentContract = value;
		}
		
		notifyUpdate();
	}
	
	/**
	 * Nom du contrat courant
	 * @return Nom du contrat courant (peut être <tt>null</tt>)
	 * @see #getCurrentContract()
	 */
	@Override
	public String getCurrentContractName()
	{
		Contract contract = getCurrentContract();
		
		if (contract == null)
			return null;
		else
			return contract.getName();
	}

	/**
	 * Modifie le serveur
	 * @param value Nouveau serveur
	 */
	@Override
	public void setDealer(Player value)
	{
		if (value == getDealer())
			return;
		
		super.setDealer(value);
		notifyUpdate();
	}

	/**
	 * Modifie le joueur actif
	 * @param value Nouveau joueur actif
	 */
	@Override
	public void setActivePlayer(Player value)
	{
		if (value == getActivePlayer())
			return;

		super.setActivePlayer(value);
		notifyUpdate();
	}

	/**
	 * Distribue les cartes aux joueurs
	 * <p>
	 * La distribution commence au joueur suivant le serveur (<tt>dealer</tt>),
	 * et se poursuit dans l'ordre des positions croissantes.
	 * </p>
	 * <p>
	 * La liste de paramètres <tt>blockCounts</tt> permet de suivre les règles
	 * de distribution d'un jeu.
	 * </p>
	 * <p>
	 * En exemple, à la Manille, on donne 3 cartes, puis 2 cartes, et enfin 3
	 * cartes. Ainsi, l'appel sera <tt>deal(3, 2, 3);</tt>.
	 * </p>
	 * <p>
	 * S'il n'y a pas suffisamment de cartes dans le paquet, il n'y a pas
	 * d'erreur : la distribution s'arrête à ce moment.
	 * </p>
	 * <p>
	 * Si la façon de distribuer les cartes n'est pas importante, donnez un seul
	 * argument : le nombre de cartes à donner à chaque joueur.
	 * </p>
	 * @param blockCounts Nombres de cartes à distribuer, par bloc
	 */
	public void deal(int ... blockCounts)
	{
		for (Player player: playersIterable())
			player.lockCardsWrite();
		try
		{
			int playerCount = getPlayerCount();
			int dealerPos = getDealer().getPosition();

			for (int i = 0; (i < blockCounts.length) && (!deck.isEmpty()); i++)
			{
				int blockCount = blockCounts[i];

				for (int j = 1; (j <= playerCount) && (!deck.isEmpty()); j++)
				{
					Player player = getPlayers((dealerPos + j) % playerCount);

					for (int k = 0; (k < blockCount) && (!deck.isEmpty()); k++)
						player.cards.add(deck.pop());
				}
			}
		}
		finally
		{
			for (Player player: playersIterable())
				player.unlockCardsWrite();
		}
		
		notifyUpdate();
	}

	/**
	 * Joue le jeu
	 * <p>
	 * Il est impossible de lancer deux fois le jeu en même temps, que ce soit
	 * <i>via</i> des threads ou non.
	 * </p>
	 * @throws CardGameException Erreur fatale durant la partie
	 */
	public void playGame() throws CardGameException
	{
		synchronized (playGameLock)
		{
			try
			{
				rules.playGame();
				incPlayCount();
			}
			catch (RuntimeException error)
			{
				// Ne pas faire confiance à Rules.playGame()) !
				throw new CardGameException(error);
			}
		}
	}

	/**
	 * Affiche un message à tous les joueurs
	 * @param message Message
	 */
	public void showMessageToAll(Message message)
	{
		for (Player player: playersIterable())
			player.showMessage(message);
	}

	/**
	 * Affiche un message à tous les joueurs
	 * @param source Source du message
	 * @param message Contenu du message
	 */
	public void showMessageToAll(MessageSource source, String message)
	{
		showMessageToAll(new Message(source, message));
	}

	/**
	 * Fait faire une pause à tous les joueurs, simultanément
	 */
	public void pauseAll() throws CardGameException
	{
		int playerCount = getPlayerCount();
		Thread[] threads = new Thread[playerCount];
		
		for (int i = 0; i < playerCount; i++)
			threads[i] = getPlayers(i).asynchPause(null);
		
		try
		{
			for (int i = 0; i < playerCount; i++)
				threads[i].join();
		}
		catch (InterruptedException error)
		{
			throw new CardGameInterruptedException(error);
		}
	}

	/**
	 * Notifie un changement dans le jeu
	 */
	public void notifyUpdate()
	{
		if (Thread.interrupted())
			return;
		
		for (Player player: playersIterable())
			player.notifyUpdate();
	}
}
