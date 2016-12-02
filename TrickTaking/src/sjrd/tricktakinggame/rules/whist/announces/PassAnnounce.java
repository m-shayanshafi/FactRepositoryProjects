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
package sjrd.tricktakinggame.rules.whist.announces;

import java.util.*;

import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.rules.whist.*;

/**
 * Annonce de passer
 * @author sjrd
 */
public class PassAnnounce extends WhistAnnounce
{
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public PassAnnounce(Player aPlayer, String aID, String aName)
	{
		super(aPlayer, aID, aName);
	}

	/**
	 * Crée l'annonce avec les ID et nom par défaut
	 * @param aPlayer Joueur qui fait l'annonce
	 */
	public PassAnnounce(Player aPlayer)
	{
		this(aPlayer, "Pass", "Passe");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.Pass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canOverride(ContractAnnounce bestAnnounce)
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		// Aucune annonce possible quand on a déjà passé
	}
}
