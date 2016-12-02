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

import static sjrd.util.ArrayUtils.*;

/**
 * Contrat de petite misère à 2
 * @author sjrd
 */
public class DuoMiseryContract extends WhistContract
{
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aFirstPlayer Premier joueur
	 * @param aSecondPlayer Second joueur
	 */
	public DuoMiseryContract(WhistRules aRules, String aName,
		Player aFirstPlayer, Player aSecondPlayer)
	{
		super(aRules, aName, Suit.None, aFirstPlayer, aSecondPlayer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void playTricks() throws CardGameException
	{
		Game game = getGame();
		
		// Chaque joueur doit éliminer une carte
		getRules().discardOneCard();
		
		/*
		 * Les autres plis se jouent normalement, mais on s'arrête dès que les
		 * deux joueurs ont perdu.
		 */
		
		Player[] players = getPlayers();

		while (game.getActivePlayer().getCardCount() > 0)
		{
			if ((players[0].getCollectedTrickCount() != 0) &&
				(players[1].getCollectedTrickCount() != 0))
				break;
			
			getRules().playTrick();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeScores() throws CardGameException
	{
		WhistRules rules = getRules();
		Player[] players = getPlayers();
		boolean[] successful = new boolean[2];
		int[] points = new int[2];
		
		for (int i = 0; i < 2; i++)
			successful[i] = players[i].getCollectedTrickCount() == 0;
		
		if (successful[0] && successful[1])
		{
			points[0] = points[1] = 50;
			rules.showMessageToAll(String.format(
				"%s et %s ont réussi leur contrat et gagnent %d points chacun",
				players[0].getName(), players[1].getName(), points[0]));
		}
		else if (!successful[0] && !successful[1])
		{
			points[0] = points[1] = -50;
			rules.showMessageToAll(String.format(
				"%s et %s ont perdu leur contrat et perdent %d points chacun",
				players[0].getName(), players[1].getName(), -points[0]));
		}
		else
		{
			if (successful[1])
			{
				Player temp = players[0];
				players[0] = players[1];
				players[1] = temp;
			}
			
			points[0] = 100;
			points[1] = -100;
			
			rules.showMessageToAll(String.format(
				"%s a réussi son contrat mais %s pas, celui-ci lui donne %d " +
				"points", players[0].getName(), players[1].getName(),
				points[0]));
		}
		
		players[0].getTeam().addToScore(points[0]);
		players[1].getTeam().addToScore(points[1]);
		
		int otherPoints = - (points[0] + points[1]) / 2;
		
		for (Player player: getGame().playersIterable())
			if (!arrayContains(players, player))
				player.getTeam().addToScore(otherPoints);
	}
}
