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

import static sjrd.tricktakinggame.rules.whist.WhistSuitComparator.*;

import java.util.*;

import sjrd.tricktakinggame.cards.*;
import sjrd.tricktakinggame.game.*;
import sjrd.tricktakinggame.remotable.*;
import sjrd.tricktakinggame.rules.whist.*;
import sjrd.tricktakinggame.rules.whist.contracts.*;

/**
 * Annonce Petite misère à 2
 * @author sjrd
 */
public class DuoMiseryAnnounce extends DuoContractAnnounce
{
	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aPartner Partenaire
	 * @param aID ID de l'annonce
	 * @param aName Nom de l'annonce
	 */
	public DuoMiseryAnnounce(Player aPlayer, Player aPartner, String aID,
	    String aName)
	{
		super(aPlayer, aPartner, aID, aName);
	}

	/**
	 * Crée l'annonce
	 * @param aPlayer Joueur qui fait l'annonce
	 * @param aPartner Partenaire
	 */
	public DuoMiseryAnnounce(Player aPlayer, Player aPartner)
	{
		this(aPlayer, aPartner, "DuoMisery", "Emballe la petite misère");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getOrderValue()
	{
		return AnnounceValues.DuoSmallMisery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void selected(WhistAuction auction)
	{
		super.selected(auction);
		auction.setLastAnnounce(getPartner(), this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeAvailableAnnounces(WhistAuction auction, Player player,
	    List<WhistAnnounce> announces)
	{
		MiseryAnnounce.makeMiseryAnnounces(auction, player, announces);
		VarSoloAnnounce.makeVarSoloAnnounces(auction, player, announces,
			8, null);
		ContractSoloAnnounce.makeContractSoloAnnounces(auction, player,
			announces);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSpecialCounters(WhistAuction auction,
		Player player, List<WhistAnnounce> announces)
	{
		super.makeSpecialCounters(auction, player, announces);
		
		Suit[] orderedSuits = getOrderedSuits();
		
		for (int i = 0; i < 4; i++)
			announces.add(new VarSoloAnnounce(player, orderedSuits[i], 8,
				true));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getContractName()
	{
		return "Petite misère";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WhistContract makeContract(WhistAuction auction, WhistRules rules)
	    throws CardGameException
	{
		return new DuoMiseryContract(rules, makeContractName(), getPlayer(),
			getPartner());
	}
}
