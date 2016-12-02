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

import static sjrd.util.ArrayUtils.*;

/**
 * Joueur
 * @author sjrd
 */
public class Player extends BaseRemotablePlayer<Game, Team>
{
	/**
	 * Contrôleur
	 */
	private PlayerController controller;

	/**
	 * Crée un nouveau joueur
	 * @param aGame Jeu propriétaire
	 * @param aTeam Equipe du joueur
	 * @param aName Nom du joueur
	 */
	public Player(Game aGame, Team aTeam, String aName,
	    PlayerControllerCreator controllerCreator)
	{
		super(aGame, aTeam, aName);

		getGame().addPlayer(this);
		getTeam().addPlayer(this);

		controller = controllerCreator.createPlayerController(this);
	}

	/**
	 * Xème joueur suivant
	 * @param count Nombre de joueurs à compter (0 = soi-même)
	 * @return Xème joueur suivant
	 */
	@Override
	public Player nextPlayer(int count)
	{
		return (Player) super.nextPlayer(count);
	}

	/**
	 * Joueur suivant
	 * @return Joueur suivant
	 */
	@Override
	public Player nextPlayer()
	{
		return nextPlayer(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unlockCardsWrite()
	{
		super.unlockCardsWrite();
		getGame().notifyUpdate();
	}

	/**
	 * Ajoute une carte dans la main du joueur
	 * @param card Carte à ajouter
	 */
	public void addCard(Card card)
	{
		lockCardsWrite();
		try
		{
			cards.add(card);
		}
		finally
		{
			unlockCardsWrite();
		}
	}

	/**
	 * Se défausse d'une carte
	 * @param card Carte à défausser
	 */
	public void discard(Card card)
	{
		lockCardsWrite();
		try
		{
			cards.remove(card);
		}
		finally
		{
			unlockCardsWrite();
		}
	}

	/**
	 * Trie les cartes selon les couleurs, et ensuite selon les forces
	 */
	public void sortCards()
	{
		lockCardsWrite();
		try
		{
			Collections.sort(cards, new Card.VisualHelpingComparator());
		}
		finally
		{
			unlockCardsWrite();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetPlayedCard()
	{
		super.resetPlayedCard();
		getGame().notifyUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayedCard(Card value, boolean hidden)
	{
		super.setPlayedCard(value, hidden);
		getGame().notifyUpdate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlayedCard(Card value)
	{
		super.setPlayedCard(value);
		getGame().notifyUpdate();
	}

	/**
	 * Choisit une annonce parmi celles disponibles
	 * <p>
	 * Il doit y avoir au moins une annonce disponible.
	 * </p>
	 * @param <A> Type d'annonce
	 * @param availableAnnounces Annonces disponibles
	 * @return Annonce choisie
	 * @see PlayerController#chooseAnnounce(Announce[])
	 */
	public <A extends Announce> A chooseAnnounce(A ... availableAnnounces)
		throws CardGameException
	{
		assert availableAnnounces.length > 0;
		A announce = controller.chooseAnnounce(availableAnnounces);
		assert arrayContains(availableAnnounces, announce);
		return announce;
	}
	
	/**
	 * Joue un tour
	 * @return Carte jouée
	 * @see PlayerController#playTurn()
	 */
	public Card playTurn() throws CardGameException
	{
		return controller.playTurn();
	}

	/**
	 * Affiche un message au joueur, sans bouton
	 * @param message Message
	 * @see PlayerController#showMessage(String)
	 */
	public void showMessage(Message message)
	{
		controller.showMessage(message);
	}
	
	/**
	 * Fait faire une pause à ce joueur
	 * @see PlayerController#pause()
	 */
	public void pause() throws CardGameException
	{
		controller.pause();
	}

	/**
	 * Affiche un message au joueur, sans bouton
	 * @param source Source du message
	 * @param message Contenu du message
	 * @see PlayerController#showMessage(String)
	 */
	public void showMessage(MessageSource source, String message)
	{
		showMessage(new Message(source, message));
	}

	/**
	 * Notifie un changement dans le jeu
	 * @see PlayerController#notifyUpdate()
	 */
	public void notifyUpdate()
	{
		controller.notifyUpdate();
	}
	
	/**
	 * Fait faire une pause à ce joueur, de manière asynchrone
	 * <p>
	 * Lorsque la pause sera terminée, l'objet <tt>notifyObject</tt> sera
	 * notifié <i>via</i> sa méthode <tt>notify()</tt>, à moins qu'il ne soit
	 * égal à <tt>null</tt>.
	 * </p>
	 * @param notifyObject Objet à notifier à la fin (peut être <tt>null</tt>)
	 * @return Thread dans lequel s'effectue la pause
	 */
	public Thread asynchPause(final Object notifyObject)
	{
		Thread thread = new Thread()
		{
			@Override
		    public void run()
		    {
			    try
			    {
			    	pause();
			    }
			    catch (CardGameException ignore)
			    {
			    }
			    
			    if (notifyObject != null)
			    {
			    	synchronized (notifyObject)
			    	{
			    		notifyObject.notify();
			    	}
			    }
		    }
		};
		
		thread.start();
		return thread;
	}
}
