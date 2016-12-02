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

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Enchères de la Manille
 * @author sjrd
 */
public class ManilleAuction extends Auction<Announce, ManilleContract>
{
	/**
	 * Crée les enchères
	 * @param aRules Règles
	 */
	public ManilleAuction(Rules aRules)
	{
		super(aRules);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ManilleContract doAuction() throws CardGameException
	{
		Game game = getGame();
		Player dealer = game.getDealer();
		
		// Le serveur choisit l'atout
		Suit trump = trumpAnnounce(dealer);

		// Contre
		boolean countered = counterAnnounce(dealer.nextPlayer()) ||
			counterAnnounce(dealer.nextPlayer(3));
		
		// Surcontre
		boolean reCountered = false;
		if (countered)
			reCountered = reCounterAnnounce(dealer) ||
				reCounterAnnounce(dealer.nextPlayer(2));

		// Créer le contrat
		return new ManilleContract(trump, countered, reCountered);
	}
	
	/**
	 * Exécute l'annonce de choix de l'atout
	 * @param player Joueur qui doit faire l'annonce
	 * @return Atout choisi
	 */
	protected Suit trumpAnnounce(Player player) throws CardGameException
	{
		Announce noTrump = new Announce(player, "NoTrump", "Sans-atout");
		Announce heartTrump = new Announce(player, "HeartTrump", "Atout Coeur");
		Announce diamondTrump = new Announce(player, "DiamondTrump",
			"Atout Carreau");
		Announce clubTrump = new Announce(player, "ClubTrump", "Atout Trèfle");
		Announce spadeTrump = new Announce(player, "SpadeTrump", "Atout Pique");
		
		Announce chosen = player.chooseAnnounce(noTrump, heartTrump,
			diamondTrump, clubTrump, spadeTrump);
		addAnnounce(chosen);
		
		if (chosen == heartTrump)
			return Suit.Heart;
		else if (chosen == diamondTrump)
			return Suit.Diamond;
		else if (chosen == clubTrump)
			return Suit.Club;
		else if (chosen == spadeTrump)
			return Suit.Spade;
		else
			return Suit.None;
	}
	
	/**
	 * Exécute une annonce de contre
	 * @param player Joueur qui doit faire l'annonce
	 * @return <tt>true</tt> s'il a contré, <tt>false</tt> sinon
	 */
	protected boolean counterAnnounce(Player player) throws CardGameException
	{
		Announce noCounter = new Announce(player, "NoCounter",
			"Pas contré");
		Announce counter = new Announce(player, "Counter", "Contré");
		
		Announce chosen = player.chooseAnnounce(noCounter, counter);
		addAnnounce(chosen);
		
		return chosen == counter;
	}
	
	/**
	 * Exécute une annonce de surcontre
	 * @param player Joueur qui doit faire l'annonce
	 * @return <tt>true</tt> s'il a surcontré, <tt>false</tt> sinon
	 */
	protected boolean reCounterAnnounce(Player player) throws CardGameException
	{
		Announce noReCounter = new Announce(player, "NoReCounter",
			"Pas surcontré");
		Announce reCounter = new Announce(player, "ReCounter", "Surcontré");

		Announce chosen = player.chooseAnnounce(noReCounter, reCounter);
		addAnnounce(chosen);

		return chosen == reCounter;
	}
}
