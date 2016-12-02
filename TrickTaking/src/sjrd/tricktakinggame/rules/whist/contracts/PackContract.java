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
 * Contrat d'un emballage
 * @author sjrd
 */
public class PackContract extends WhistContract
{
	/**
	 * Nombre de plis annoncés
	 */
	private int trickCount;
	
	/**
	 * Indique si c'est un trou
	 */
	private boolean gap;
	
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aFirstPlayer Premier joueur
	 * @param aSecondPlayer Second joueur
	 * @param aGap <tt>true</tt> si c'est un trou
	 */
	public PackContract(WhistRules aRules, String aName, Suit aTrump,
		int aTrickCount, Player aFirstPlayer, Player aSecondPlayer,
		boolean aGap)
	{
		super(aRules, aName, aTrump, aFirstPlayer, aSecondPlayer);
		
		trickCount = aTrickCount;
		gap = aGap;
	}
	
	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis annoncés
	 * @param aFirstPlayer Premier joueur
	 * @param aSecondPlayer Second joueur
	 */
	public PackContract(WhistRules aRules, String aName, Suit aTrump,
	    int aTrickCount, Player aFirstPlayer, Player aSecondPlayer)
	{
		this(aRules, aName, aTrump, aTrickCount, aFirstPlayer, aSecondPlayer,
			false);
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
	 * Indique si c'est un trou
	 * @return <tt>true</tt> si c'est un trou, <tt>false</tt> sinon
	 */
	public boolean isGap()
	{
		return gap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeScores() throws CardGameException
	{
		Game game = getGame();
		WhistRules rules = getRules();
		Player firstPlayer = getPlayers(0);
		Player secondPlayer = getPlayers(1);
		
		int doneTrickCount = firstPlayer.getCollectedTrickCount() +
			secondPlayer.getCollectedTrickCount();
		
		boolean successful = (doneTrickCount >= trickCount);
		int diffTrickCount = Math.abs(doneTrickCount - trickCount);
		int points = 0;
		String messageFormat;
		
		// Calculer les points et choisir le message à afficher
		
		points = 10 + 5 * diffTrickCount;
		if (isGap())
			points *= 2;

		if (successful)
			messageFormat = "%s et %s ont ramassé %d pli(s), soit %d de mieux";
		else
			messageFormat = "%s et %s ont ramassé %d pli(s), soit %d trop peu";
		
		// Afficher le message
		
		rules.showMessageToAll(String.format(messageFormat,
			firstPlayer.getName(), secondPlayer.getName(), doneTrickCount,
			diffTrickCount));
		
		// Modifier les scores
		
		if (successful)
		{
			rules.showMessageToAll(String.format(
				"Ils/elles gagnent donc %d points chacun", points));
		}
		else
		{
			rules.showMessageToAll(String.format(
				"Ils/elles perdent donc %d points chacun", points));
			points = -points;
		}
		
		firstPlayer.getTeam().addToScore(2*points);
		secondPlayer.getTeam().addToScore(2*points);
		for (Player otherPlayer: game.playersIterable())
			otherPlayer.getTeam().addToScore(-points);
	}
}
