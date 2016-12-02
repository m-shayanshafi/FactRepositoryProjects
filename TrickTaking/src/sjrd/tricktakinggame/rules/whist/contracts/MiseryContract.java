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
 * Contrat de misère (petite ou grande)
 * @author sjrd
 */
public class MiseryContract extends WhistContract
{
	/**
	 * Indique si c'est une petite misère
	 */
	private boolean small;
	
	/**
	 * Indique si le contrat est sur table
	 */
	private boolean onTable;

	/**
	 * Crée le contrat
	 * @param aRules Règles associées
	 * @param aName Nom du contrat
	 * @param aSmall <tt>true</tt> si c'est une petite misère
	 * @param aOnTable <tt>true</tt> pour une annonce sur table
	 * @param aPlayer Joueur qui fait le contrat
	 */
	public MiseryContract(WhistRules aRules, String aName, boolean aSmall,
		boolean aOnTable, Player aPlayer)
	{
		super(aRules, aName, Suit.None, aPlayer);
		
		small = aSmall;
		onTable = aOnTable;
	}
	
	/**
	 * Indique si c'est une petite misère
	 * @return <tt>true</tt> si c'est une petite misère, <tt>false</tt> sinon
	 */
	public boolean isSmall()
	{
		return small;
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
	public void playTricks() throws CardGameException
	{
		Game game = getGame();
		
		// Si c'est une petite misère, chaque joueur doit éliminer une carte
		if (small)
			getRules().discardOneCard();
		
		/*
		 * Les autres plis se jouent normalement, mais on s'arrête dès que la
		 * misère est perdue.
		 */

		while ((game.getActivePlayer().getCardCount() > 0) &&
			(getPlayer().getCollectedTrickCount() == 0))
			getRules().playTrick();
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
		boolean successful = player.getCollectedTrickCount() == 0;
		
		int points = (small ? 25 : 50);
		
		if (successful)
		{
			rules.showMessageToAll(String.format(
				"%s a réussi son contrat et gagne %s points par personne",
				player.getName(), points));
		}
		else
		{
			rules.showMessageToAll(String.format(
				"%s a perdu son contrat et perd %s points par personne",
				player.getName(), points));
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
