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

import sjrd.tricktakinggame.remotable.*;

/**
 * Annonce dans une partie de Rikiki
 * @author sjrd
 */
public class RikikiAnnounce extends Announce
{
	/**
	 * Nombre de plis
	 */
	private int trickCount;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aTrickCount Nombre de plis
	 */
	public RikikiAnnounce(RemotablePlayer aPlayer, int aTrickCount)
	{
		super(aPlayer, String.valueOf(aTrickCount), makeName(aTrickCount));
		
		trickCount = aTrickCount;
	}
	
	/**
	 * Construit le nom d'une annonce de Rikiki
	 * @param trickCount Nombre de plis
	 * @return Nom de l'annonces correspondante
	 */
	private static String makeName(int trickCount)
	{
		if (trickCount == 0)
			return "Aucun pli";
		else if (trickCount == 1)
			return "1 pli";
		else
			return String.format("%d plis", trickCount);
	}
	
	/**
	 * Nombre de plis
	 * @return Nombre de plis
	 */
	public int getTrickCount()
	{
		return trickCount;
	}
}
