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
 * Contrat d'une partie de Rikiki
 * @author sjrd
 */
public class RikikiContract extends Contract
{
	/**
	 * Nombre de plis annoncés par chaque joueur
	 */
	private int[] playerTrickCount;
	
	/**
	 * Crée le contrat
	 * @param aTrump Atout
	 * @param aPlayerTrickCount Nombres de plis annoncés par les joueurs
	 */
	public RikikiContract(Suit aTrump, int[] aPlayerTrickCount)
	{
		super(makeName(aTrump, aPlayerTrickCount));
		playerTrickCount = aPlayerTrickCount;
	}
	
	/**
	 * Construit le nom du contrat
	 * @param playerTrickCount Nombres de plis annoncés par les joueurs
	 * @return Nom du contrat
	 */
	protected static String makeName(Suit trump, int[] playerTrickCount)
	{
		if (trump == Suit.None)
			return "Sans-atout";
		else
			return "Atout " + trump.getName();
	}
	
	/**
	 * Nombre de plis qu'a annoncé un joueur
	 * @param player Joueur demandé
	 * @return Nombre de plis qu'a annoncé ce joueur
	 */
	public int getPlayerTrickCount(RemotablePlayer player)
	{
		return playerTrickCount[player.getPosition()];
	}
}
