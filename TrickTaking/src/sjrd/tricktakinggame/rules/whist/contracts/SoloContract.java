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
package sjrd.tricktakinggame.rules.whist.contracts;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.*;

/**
 * Contrat de prise de plis, fait par un seul joueur
 * @author sjrd
 */
public class SoloContract extends WhistContract
{
	/**
	 * Nombre de plis annoncés
	 */
	private int trickCount;
	
	/**
	 * Indique si le contrat est sur table
	 */
	private boolean onTable;
	
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aOnTable <tt>true</tt> pour un contrat sur table
	 * @param aPlayer Joueur qui fait le contrat
	 */
	public SoloContract(WhistRules aRules, String aName, Suit aTrump,
	    int aTrickCount, boolean aOnTable, Player aPlayer)
	{
		super(aRules, aName, aTrump, aPlayer);
		
		trickCount = aTrickCount;
		onTable = aOnTable;
	}
	
	/**
	 * Nombre de plis annoncés
	 * @return Nombre de plis annoncés
	 */
	public int getTrickCount()
	{
		return trickCount;
	}
	
	/**
	 * Indique si le contrat est sur table
	 * @return <tt>true</tt> si le contrat est sur table, <tt>false</tt> sinon
	 */
	public boolean isOnTable()
	{
		return onTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeScores() throws CardGameException
	{
		Game game = getGame();
		WhistRules rules = getRules();
		Player player = getPlayer();
		int doneTrickCount = player.getCollectedTrickCount();
		boolean successful = (doneTrickCount >= trickCount);
		int diffTrickCount = Math.abs(doneTrickCount - trickCount);
		int points = 0;
		String messageFormat;
		
		// Calculer les points et choisir le message à afficher
		
		if (trickCount < 9)
		{
			// Contrats à points variables

			points = 10 + 5 * diffTrickCount;
			
			if (successful)
				messageFormat = "%s a ramassé %d pli(s), soit %d de mieux";
			else
				messageFormat = "%s a ramassé %d pli(s), soit %d trop peu";
		}
		else
		{
			// Contrats à points fixes
			
			// Points
			if (trickCount == 9)
				points = 40;
			else if (trickCount == 11)
				points = 50;
			else if (trickCount == 12)
				points = 200;
			else if (trickCount == 13)
				points = 500;
			else
				assert false;
			
			// Message
			if (successful)
				messageFormat =
					"%s a ramassé %d plis, donc réussit son contrat";
			else
				messageFormat =
					"%s a ramassé %d plis, donc rate son contrat";
		}
		
		// Afficher le message
		
		rules.showMessageToAll(String.format(messageFormat,
			player.getName(), doneTrickCount, diffTrickCount));
		
		// Modifier les scores
		
		if (onTable)
			points += points / 2;
		
		if (successful)
		{
			rules.showMessageToAll(String.format(
				"Il/elle gagne donc %d points par personne", points));
		}
		else
		{
			rules.showMessageToAll(String.format(
				"Il/elle perd donc %d points par personne", points));
			points = -points;
		}
		
		player.getTeam().addToScore(4*points);
		for (Player otherPlayer: game.playersIterable())
			otherPlayer.getTeam().addToScore(-points);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlayerHandVisible(Player player)
	{
		return onTable && (player == getPlayer());
	}
}
