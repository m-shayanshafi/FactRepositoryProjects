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

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;

/**
 * Enchères d'une partie de Rikiki
 * @author sjrd
 */
public class RikikiAuction extends Auction<RikikiAnnounce, RikikiContract>
{
	/**
	 * Atout
	 */
	private Suit trump;
	
	/**
	 * Nombre de plis à jouer dans cette donne
	 */
	private int trickCount;
	
	/**
	 * Crée les enchères
	 * @param aRules Règles associées
	 * @param aTrump Atout
	 * @param aTrickCount Nombre de plis à jouer dans cette donne
	 */
	public RikikiAuction(Rules aRules, Suit aTrump, int aTrickCount)
	{
		super(aRules);
		
		trump = aTrump;
		trickCount = aTrickCount;
	}
	
	/**
	 * Construit une liste des annonces disponibles pour un joueur
	 * @param player Joueur qui doit faire l'annonce
	 * @param exclude Nombre de plis à exclure (-1 si ne rien exclure)
	 * @return Liste des annonces disponibles pour le joueur
	 */
	protected RikikiAnnounce[] makeAnnounces(RemotablePlayer player,
		int exclude)
	{
		int count = trickCount+1;
		if (exclude >= 0)
			count--;

		RikikiAnnounce[] result = new RikikiAnnounce[count];
		
		int index = 0;
		for (int i = 0; i <= trickCount; i++)
			if (i != exclude)
				result[index++] = new RikikiAnnounce(player, i);
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RikikiContract doAuction() throws CardGameException
	{
		Game game = getGame();
		Player firstPlayer = game.getActivePlayer();
		
		int playerTrickCount[] = new int[game.getPlayerCount()];
		int totalTrickCount = 0;
		
		for (int i = 0; i < game.getPlayerCount(); i++)
		{
			Player player = firstPlayer.nextPlayer(i);

			int exclude = -1;
			if ((i == game.getPlayerCount() - 1) &&
				(totalTrickCount <= trickCount))
				exclude = trickCount - totalTrickCount;
			
			RikikiAnnounce[] announces = makeAnnounces(player, exclude);
			RikikiAnnounce announce = player.chooseAnnounce(announces);
			
			addAnnounce(announce);
			playerTrickCount[player.getPosition()] = announce.getTrickCount();
			totalTrickCount += announce.getTrickCount();
		}
		
		return new RikikiContract(trump, playerTrickCount);
	}
}
