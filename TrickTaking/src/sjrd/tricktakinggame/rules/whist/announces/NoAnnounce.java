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
import sjrd.tricktakinggame.rules.whist.contracts.*;

/**
 * Pas d'annonce
 * @author sjrd
 */
public class NoAnnounce extends ContractAnnounce
{
	/**
	 * Indique s'il faut autoriser les propositions
	 */
	private boolean allowPropose;
	
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur
	 * @param aAllowPropose <tt>true</tt> autorise les propositions
	 */
	public NoAnnounce(Player aPlayer, boolean aAllowPropose)
	{
		super(aPlayer, "None", "Pas d'annonce");
		
		allowPropose = aAllowPropose;
	}
	
	/**
	 * Crée l'annonce
	 * @param aPlayer
	 */
	public NoAnnounce(Player aPlayer)
	{
		this(aPlayer, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.None;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
		List<WhistAnnounce> announces)
	{
		if (player.getGame().getActivePlayer() == player)
			announces.add(new WaitAnnounce(player));
		if (allowPropose)
			ProposeAnnounce.makeProposeAnnounces(auction, player, announces);
		PackAnnounce.makePackAnnounces(auction, player, announces);
		VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces);
		MiseryAnnounce.makeMiseryAnnounces(auction, player, announces);
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	{
		return new NoContract(rules, getPlayer());
	}
}
